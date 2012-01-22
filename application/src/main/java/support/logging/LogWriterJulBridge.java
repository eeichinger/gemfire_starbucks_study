package support.logging;

import com.gemstone.gemfire.i18n.StringId;
import com.gemstone.gemfire.internal.LogWriterImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
* @author: Erich Eichinger
* @date: 21/01/12
*/
public class LogWriterJulBridge extends LogWriterImpl {

    private Logger logger = Logger.getLogger("");

    static private class MyLevel extends Level {
        protected MyLevel(int value) {
            super("custom", value);
        }
    }

    @Override
    public int getLevel() {
        return logger.getLevel().intValue();
    }

    @Override
    protected void put(int i, String s, Throwable throwable) {
        logger.log(new MyLevel(i), s, throwable);
    }

    @Override
    protected void put(int i, StringId stringId, Object[] objects, Throwable throwable) {
        logger.log(new MyLevel(i), stringId.toString(objects), throwable);
    }
}
