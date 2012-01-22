package support.logging;

import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;

import java.util.logging.*;

/**
* @author: Erich Eichinger
* @date: 21/01/12
*/
public class JulOverGemfireBridgeHandler extends ConsoleHandler {

    public JulOverGemfireBridgeHandler() {
    }

    @Override
    public void publish(LogRecord record) {
        GemFireCacheImpl gemFireCache = GemFireCacheImpl.getInstance();
        if (gemFireCache != null) {
            Logger rootLogger = Logger.getLogger("");
            rootLogger.removeHandler(this);
            Handler gemfireHandler = gemFireCache.getLogger().getHandler();
            rootLogger.addHandler(gemfireHandler);
            gemfireHandler.publish(record);
        } else {
            super.publish(record);
        }
    }
}
