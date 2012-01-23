package starbucks;

import support.ProcessHelper;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static support.TimerHelpers.pause;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class Barista {

    public interface CoffeeProgressCallback {
        void notifyMakingCoffee(CoffeeRequest request);
        void notifyCoffeeReady(PreparedCoffee coffee);
    }

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final ScheduledThreadPoolExecutor exec;

    public Barista(int numberOfBaristas) {
        exec = new ScheduledThreadPoolExecutor(numberOfBaristas);
        exec.setMaximumPoolSize(numberOfBaristas);
    }

    public BaristaStatistics getStatistics() {
        return new BaristaStatistics(exec.getCorePoolSize(), exec.getQueue().size());
    }

    public void scheduleMakingCoffee(final CoffeeRequest request, final CoffeeProgressCallback notifyTarget) {
        exec.schedule(new Runnable() {
            @Override
            public void run() {
                Barista.this.makeCoffee(request, notifyTarget);
            }
        }, 0, TimeUnit.SECONDS);
    }

    public void makeCoffee(CoffeeRequest coffeeRequest, CoffeeProgressCallback notifyTarget) {
        String baristaId = String.format("%s:%s",ProcessHelper.getCurrentProcessId(), Thread.currentThread().getId());
        coffeeRequest.markHandledBy(baristaId);
        notifyTarget.notifyMakingCoffee(coffeeRequest);

        long prepTimeMillis = Math.round(Math.random()*5000);
        pause(prepTimeMillis);
        notifyTarget.notifyCoffeeReady(new PreparedCoffee(""+Thread.currentThread().getId(), coffeeRequest.getRequestKey(), prepTimeMillis));
    }
}
