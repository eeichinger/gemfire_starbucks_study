package starbucks.gemfire;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import support.ProcessHelper;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author: Erich Eichinger
 * @date: 21/01/12
 */
public abstract class BaseCacheListenerAdapter<K,V> extends CacheListenerAdapter<K,V> implements Declarable {

    protected final String formattedClassname = String.format("%s[%s]:%s", this.getClass().getSimpleName(), this.hashCode(), ProcessHelper.getCurrentProcessId());
    protected final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public String toString() {
        return formattedClassname;
    }

    @Override
    public void init(Properties properties) {
        logger.info(String.format("init [%s]", this));
    }

    @Override
    public void afterCreate(EntryEvent<K,V> entryEvent) {
        logger.info(String.format("afterCreate [%s]: %s", this, entryEvent));
    }

    @Override
    public void afterDestroy(EntryEvent<K,V> entryEvent) {
        logger.info(String.format("afterDestroy [%s]: %s", this, entryEvent));
    }

    @Override
    public void afterInvalidate(EntryEvent<K,V> entryEvent) {
        logger.info(String.format("afterInvalidate [%s]: %s", this, entryEvent));
    }

    @Override
    public void afterUpdate(EntryEvent<K,V> entryEvent) {
        logger.info(String.format("afterUpdate [%s]: %s", this, entryEvent));
    }
}
