package support;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
public class TimerHelpers {

    public static void pause(long timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
