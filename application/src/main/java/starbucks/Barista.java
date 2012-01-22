package starbucks;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static support.TimerHelpers.pause;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class Barista {

    public interface CoffeeReadyCallback {
        void notifyCoffeeReady(PreparedCoffee coffee);
    }

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final int numberOfBaristas;

    public Barista(int numberOfBaristas) {
        this.numberOfBaristas = numberOfBaristas;
    }

    class MakeCoffeeTask implements Runnable {

        private final CoffeeReadyCallback notifyTarget;
        private final CoffeeRequest coffeeRequest;

        MakeCoffeeTask(CoffeeRequest coffeeRequest, CoffeeReadyCallback notifyTarget) {
            this.coffeeRequest = coffeeRequest;
            this.notifyTarget = notifyTarget;
        }

        @Override
        public void run() {
            long prepTimeMillis = Math.round(Math.random()*5000);
            pause(prepTimeMillis);
            notifyTarget.notifyCoffeeReady(new PreparedCoffee(coffeeRequest.getRequestKey(), prepTimeMillis));
        }
    }

    public void prepareCoffee(CoffeeRequest request,CoffeeReadyCallback notifyTarget) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(numberOfBaristas);
        exec.schedule(new MakeCoffeeTask(request,notifyTarget), 0, TimeUnit.SECONDS);
    }
}
