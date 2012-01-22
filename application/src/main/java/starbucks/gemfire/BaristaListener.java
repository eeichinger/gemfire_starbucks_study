package starbucks.gemfire;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import starbucks.Barista;
import starbucks.CoffeeRequest;
import starbucks.CoffeeRequestKey;
import starbucks.PreparedCoffee;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class BaristaListener extends BaseCacheListenerAdapter<CoffeeRequestKey, CoffeeRequest> {

    private Barista baristas;

    public BaristaListener() {
        int numberOfBaristas = 1;
        baristas = new Barista(numberOfBaristas);
        logger.info(String.format("Instantiated [%s]", this));
    }

    @Override
    public void init(Properties properties) {
        logger.info(String.format("Init [%s]", this));
        int numberOfBaristas = Integer.parseInt(properties.getProperty("numberOfBaristas", "1"));
        baristas = new Barista(numberOfBaristas);
        logger.info(String.format("Initialized [%s] with %s Baristas", this, numberOfBaristas));
    }

    @Override
    public void afterCreate(EntryEvent<CoffeeRequestKey, CoffeeRequest> entryEvent) {
        CoffeeRequest coffeeRequest = entryEvent.getNewValue();
        logger.info(String.format("afterCreate [%s]:%s on %s", this, coffeeRequest, entryEvent.getDistributedMember().getProcessId()));

        final Region<CoffeeRequestKey, PreparedCoffee> preparedCoffeeRegion = entryEvent
                .getRegion()
                .getRegionService()
                .getRegion("PreparedCoffees");
        final Region<CoffeeRequestKey, CoffeeRequest> requestRegion = entryEvent.getRegion();

        baristas.prepareCoffee(coffeeRequest, new Barista.CoffeeReadyCallback() {
            @Override
            public void notifyCoffeeReady(PreparedCoffee coffee) {
                preparedCoffeeRegion.put(coffee.getRequestKey(), coffee);
                requestRegion.remove(coffee.getRequestKey());
            }
        });
    }

    @Override
    public void afterDestroy(EntryEvent<CoffeeRequestKey, CoffeeRequest> entryEvent) {
        CoffeeRequest coffeeRequest = entryEvent.getNewValue();
        logger.info(String.format("afterDestroy [%s]:%s on %s", this, coffeeRequest, entryEvent.getDistributedMember().getProcessId()));
    }
}
