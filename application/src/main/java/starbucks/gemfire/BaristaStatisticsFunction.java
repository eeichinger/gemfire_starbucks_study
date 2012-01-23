package starbucks.gemfire;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import starbucks.BaristaStatistics;
import starbucks.CoffeeRequest;
import starbucks.CoffeeRequestKey;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author: Erich Eichinger
 * @date: 22/01/12
 */
public class BaristaStatisticsFunction extends FunctionAdapter implements Declarable {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private String id;

    public BaristaStatisticsFunction() {
        logger.info("Instantiated " + this);
        id = getClass().getSimpleName();
    }

    @Override
    public void init(Properties properties) {
        id = properties.getProperty("id", getClass().getSimpleName());
        logger.info(String.format("initialized %s ",this));
    }

    public void execute(FunctionContext fc) {

        BaristaStatistics stats = BaristaStatistics.EMPTY;
        CacheFactory.getAnyInstance();
        Region<CoffeeRequestKey,CoffeeRequest> region = CacheFactory.getAnyInstance().getRegion("CoffeeRequests");

        for(CacheListener cacheListener: region.getAttributes().getCacheListeners()) {
            if (cacheListener instanceof BaristaListener) {
                BaristaStatistics statistics = ((BaristaListener) cacheListener).getStatistics();
                stats = stats.add(statistics);
            }
        }

        logger.info("executed getStatistics " + stats);
        fc.getResultSender().lastResult(stats);
    }

    public String getId() {
        return id;
    }
    
    public String toString() {
        return String.format("BaristaStatisticsFunction{id=%s,hashcode=%s}", getId(), hashCode());
    }

}