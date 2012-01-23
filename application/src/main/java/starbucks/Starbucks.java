package starbucks;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.InterestResultPolicy;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.query.CqAttributesFactory;
import com.gemstone.gemfire.cache.query.CqQuery;
import starbucks.gemfire.BaristaStatisticsCollector;
import starbucks.gemfire.BaseCacheListenerAdapter;
import starbucks.gemfire.BaseCqListenerAdapter;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class Starbucks {

    public interface CoffeeReadyCallback {
        void onCoffeeReady(CoffeeRequest request, PreparedCoffee coffee);
    }

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ClientCache cache;
    private final Region<CoffeeRequestKey,CoffeeRequest> coffeeRequests;
    private final Region<CoffeeRequestKey,PreparedCoffee> preparedCoffees;

    public Starbucks(String name, Properties props) {

        this.cache = new ClientCacheFactory(props)
                .set("name", name)
//                .set("durable-client-id", "StarbucksClient")
                .set("cache-xml-file", "clientCache.xml")
                .create();

        coffeeRequests = cache.<CoffeeRequestKey,CoffeeRequest>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                .addCacheListener(new CoffeeRequestListener())
                .create("CoffeeRequests");
        preparedCoffees = cache.<CoffeeRequestKey,PreparedCoffee>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                .addCacheListener(new PreparedCoffeeListener())
                .create("PreparedCoffees");

        CqAttributesFactory attFactory = new CqAttributesFactory();
        attFactory.addCqListener(new CQPreparedCoffeeListener());
        try {
            CqQuery query = preparedCoffees.getRegionService().getQueryService().newCq("SELECT * FROM /PreparedCoffees", attFactory.create(), true);
            query.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        cache.close(true);
    }

    public void submitOrder(CoffeeRequest request, CoffeeReadyCallback readyCallback) {
        coffeeRequests.put(request.getRequestKey(), request, null);
        coffeeRequests.registerInterest(request.getRequestKey(), InterestResultPolicy.KEYS, false, true);
        preparedCoffees.registerInterest(request.getRequestKey(), InterestResultPolicy.KEYS, false, true);
    }

    @SuppressWarnings("unchecked")
    public BaristaStatistics getBaristaStatistics() {
        Execution func = FunctionService
                .onServers(coffeeRequests.getRegionService())
//                .onRegion(coffeeRequests)
                .withCollector(new BaristaStatisticsCollector() {
            @Override
            public void endResults() {
                logger.info(String.format("Got baristaStats from all servers"));
            }
        });

        Serializable res = func.execute("baristaStats").getResult();
        return (BaristaStatistics) res;
    }

    class CoffeeRequestListener extends BaseCacheListenerAdapter<CoffeeRequestKey,CoffeeRequest> {
        @Override
        public void afterUpdate(EntryEvent<CoffeeRequestKey, CoffeeRequest> entryEvent) {
            logger.info(String.format("Barista picked up my order [%s]: %s", this, entryEvent.getNewValue()));
        }
    }

    class PreparedCoffeeListener extends BaseCacheListenerAdapter<CoffeeRequestKey, PreparedCoffee> {

//        @Override
//        public void afterCreate(EntryEvent<CoffeeRequestKey, PreparedCoffee> entryEvent) {
//            super.afterCreate(entryEvent);
////            coffeeRequests.remove(entryEvent.getKey());
//        }
    }

    class CQPreparedCoffeeListener extends BaseCqListenerAdapter<CoffeeRequestKey, PreparedCoffee> {
//        @Override
//        public void onEvent(CqEvent aCqEvent) {
//            super.onEvent(aCqEvent);
//        }
        //        @Override
//        public void afterCreate(EntryEvent<CoffeeRequestKey, PreparedCoffee> entryEvent) {
//            super.afterCreate(entryEvent);
////            coffeeRequests.remove(entryEvent.getKey());
//        }
    }
}
