package starbucks;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.InterestResultPolicy;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.query.CqAttributesFactory;
import com.gemstone.gemfire.cache.query.CqQuery;
import starbucks.gemfire.BaseCacheListenerAdapter;
import starbucks.gemfire.BaseCqListenerAdapter;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class StarbucksClient {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ClientCache cache;
    private final Region<CoffeeRequestKey,CoffeeRequest> coffeeRequests;
    private final Region<CoffeeRequestKey,PreparedCoffee> preparedCoffees;

    public StarbucksClient(String name, Properties props) {

        this.cache = new ClientCacheFactory(props)
                .set("name", name)
//                .set("durable-client-id", "StarbucksClient")
                .set("cache-xml-file", "clientCache.xml")
                .create();

        coffeeRequests = cache.<CoffeeRequestKey,CoffeeRequest>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                .addCacheListener(new CoffeeRequestListener())
                .setPoolName("client")
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

    public void submitOrder(CoffeeRequest request) {
        coffeeRequests.put(request.getRequestKey(), request);
        coffeeRequests.registerInterest(request.getRequestKey(), InterestResultPolicy.KEYS, false, true);
        preparedCoffees.registerInterest(request.getRequestKey(), InterestResultPolicy.KEYS, false, true);
    }

    class CoffeeRequestListener extends BaseCacheListenerAdapter<CoffeeRequestKey,CoffeeRequest> {
    }

    class PreparedCoffeeListener extends BaseCacheListenerAdapter<CoffeeRequestKey, PreparedCoffee> {

        @Override
        public void afterCreate(EntryEvent<CoffeeRequestKey, PreparedCoffee> entryEvent) {
            super.afterCreate(entryEvent);
            coffeeRequests.remove(entryEvent.getKey());
        }
    }

    class CQPreparedCoffeeListener extends BaseCqListenerAdapter {
    }
}
