package support.logging;

import ch.qos.logback.classic.Logger;
import com.gemstone.gemfire.i18n.StringId;
import com.gemstone.gemfire.internal.LogWriterImpl;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * @author: Erich Eichinger
 * @date: 21/01/12
 */
public class LogWriterLogbackBridge extends LogWriterImpl {

    private Logger logger = (Logger) LoggerFactory.getLogger("gemfire");

    @Override
    public int getLevel() {
//        Level level = logger.getEffectiveLevel();
/*
    public static final int FINEST_LEVEL = 300;
    public static final int FINER_LEVEL = 400;
    public static final int FINE_LEVEL = 500;
    public static final int CONFIG_LEVEL = 700;
    public static final int INFO_LEVEL = 800;
    public static final int WARNING_LEVEL = 900;
    public static final int ERROR_LEVEL = 950;
    public static final int SEVERE_LEVEL = 1000;
         */
        /*
  final public int TRACE_INT = 00;
  final public int DEBUG_INT = 10;
  final public int INFO_INT = 20;
  final public int WARN_INT = 30;
  final public int ERROR_INT = 40;
         */
//        return level.toInt() / 25;
        return ALL_LEVEL;
    }

    @Override
    protected void put(int i, String s, Throwable throwable) {
        int level = convertLevel(i);

        logger.log(null, "gemfireLogger", level, s, null, throwable);
    }

    @Override
    protected void put(int i, StringId stringId, Object[] objects, Throwable throwable) {
        int level = convertLevel(i);

        logger.log(null, "gemfireLogger", level, stringId.toLocalizedString(), objects, throwable);
    }

    private int convertLevel(int i) {
        int level;
        switch (i) {
            case FINEST_LEVEL:
            case FINER_LEVEL:
                level = LocationAwareLogger.TRACE_INT;
                break;
            case FINE_LEVEL:
            case CONFIG_LEVEL:
                level = LocationAwareLogger.DEBUG_INT;
                break;
            case INFO_LEVEL:
                level = LocationAwareLogger.INFO_INT;
                break;
            case WARNING_LEVEL:
                level = LocationAwareLogger.WARN_INT;
                break;
            case ERROR_LEVEL:
            case SEVERE_LEVEL:
                level = LocationAwareLogger.ERROR_INT;
                break;
            default:
                throw new IllegalArgumentException(i + " not a valid level value");
        }
        return level;
    }
}
