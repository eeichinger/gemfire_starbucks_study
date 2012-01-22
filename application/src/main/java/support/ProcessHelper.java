package support;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author: Erich Eichinger
 * @date: 21/01/12
 */
public class ProcessHelper {

    private final static String currentProcessId;

    static {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        currentProcessId = runtimeMXBean.getName();
    }

    public static String getCurrentProcessId() {
        return currentProcessId;
    }
}
