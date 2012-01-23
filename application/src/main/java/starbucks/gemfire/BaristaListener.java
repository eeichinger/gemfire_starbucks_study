package starbucks.gemfire;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Region;
import starbucks.*;

import java.util.Properties;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class BaristaListener extends BaseCacheListenerAdapter<CoffeeRequestKey, CoffeeRequest> {

    private Barista baristas;

    public BaristaStatistics getStatistics() {
        return baristas.getStatistics();
    }

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
    public void afterCreate(final EntryEvent<CoffeeRequestKey, CoffeeRequest> entryEvent) {
        final CoffeeRequest coffeeRequest = entryEvent.getNewValue();
        logger.info(String.format("afterCreate [%s]:%s on %s", this, coffeeRequest, entryEvent.getDistributedMember().getProcessId()));

        final Region<CoffeeRequestKey, PreparedCoffee> preparedCoffeeRegion = entryEvent
                .getRegion()
                .getRegionService()
                .getRegion("PreparedCoffees");
        final Region<CoffeeRequestKey, CoffeeRequest> requestRegion = entryEvent.getRegion();

        baristas.scheduleMakingCoffee(coffeeRequest, new Barista.CoffeeProgressCallback() {
            @Override
            public void notifyMakingCoffee(CoffeeRequest request) {
                requestRegion.put(request.getRequestKey(), request);
                logger.info(String.format("Barista starting making coffee [%s]:%s on %s", this, coffeeRequest, entryEvent.getDistributedMember().getProcessId()));
            }

            @Override
            public void notifyCoffeeReady(PreparedCoffee coffee) {
                preparedCoffeeRegion.put(coffee.getRequestKey(), coffee);
                logger.info(String.format("Barista finished making coffee [%s]:%s on %s", this, coffeeRequest, entryEvent.getDistributedMember().getProcessId()));
            }
        });
    }
}
