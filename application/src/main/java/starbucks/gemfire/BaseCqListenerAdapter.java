package starbucks.gemfire;

import com.gemstone.gemfire.cache.query.CqEvent;
import com.gemstone.gemfire.cache.util.CqListenerAdapter;
import support.ProcessHelper;

import java.util.logging.Logger;

/**
 * @author: Erich Eichinger
 * @date: 21/01/12
 */
public class BaseCqListenerAdapter extends CqListenerAdapter {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected final String formattedClassname = String.format("[%s@%s@%s]", this.getClass().getSimpleName(), this.hashCode(), ProcessHelper.getCurrentProcessId());

    @Override
    public String toString() {
        return formattedClassname;
    }

    @Override
    public void onError(CqEvent aCqEvent) {
        logger.info(String.format("onError [%s] %s", this, aCqEvent));
    }

    @Override
    public void onEvent(CqEvent aCqEvent) {
        logger.info(String.format("onEvent [%s] %s", this, aCqEvent));
    }
}
