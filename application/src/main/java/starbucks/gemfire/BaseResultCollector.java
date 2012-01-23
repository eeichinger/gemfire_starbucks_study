package starbucks.gemfire;

import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;

import java.util.concurrent.TimeUnit;

/**
 * @author: Erich Eichinger
 * @date: 22/01/12
 */
public class BaseResultCollector<Result extends java.io.Serializable, PartialResult extends java.io.Serializable> implements ResultCollector<Result, PartialResult> {
    @Override
    public PartialResult getResult() throws FunctionException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PartialResult getResult(long l, TimeUnit timeUnit) throws FunctionException, InterruptedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addResult(DistributedMember distributedMember, Result result) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endResults() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearResults() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
