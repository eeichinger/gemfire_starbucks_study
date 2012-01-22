package support.logging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

/**
 * @author: Erich Eichinger
 * @date: 21/01/12
 */
public class JulOverGemfireInstaller {

    public JulOverGemfireInstaller() throws IOException {
        LogManager logManager = LogManager.getLogManager();
        byte[] props = String.format("handlers=%s", JulOverGemfireBridgeHandler.class.getName()).getBytes();
        logManager.readConfiguration(new ByteArrayInputStream(props));
    }

}
