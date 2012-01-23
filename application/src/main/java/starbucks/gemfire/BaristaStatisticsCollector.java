package starbucks.gemfire;

import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;
import starbucks.BaristaStatistics;

import java.util.concurrent.TimeUnit;

/**
* @author: Erich Eichinger
* @date: 22/01/12
*/
public class BaristaStatisticsCollector implements ResultCollector<BaristaStatistics,BaristaStatistics> {

    private BaristaStatistics stats = BaristaStatistics.EMPTY;

    @Override
    public BaristaStatistics getResult() throws FunctionException {
        return stats;
    }

    @Override
    public BaristaStatistics getResult(long l, TimeUnit timeUnit) throws FunctionException, InterruptedException {
        return stats;
    }

    @Override
    public void addResult(DistributedMember distributedMember, BaristaStatistics baristaStatistics) {
        stats = stats.add(baristaStatistics);
    }

    @Override
    public void endResults() {
    }

    @Override
    public void clearResults() {
        stats = BaristaStatistics.EMPTY;
    }
}
