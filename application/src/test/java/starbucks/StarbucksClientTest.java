package starbucks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.bridge.SLF4JBridgeHandler;
import support.logging.LogWriterLogbackBridge;

import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static support.TimerHelpers.pause;

/**
 * @author: Erich Eichinger
 * @date: 20/01/12
 */
@RunWith(JUnit4.class)
public class StarbucksClientTest {
    
    @Test
    public void simulateCustomer() {
        // redirect JUL to SLF4J
        LogManager.getLogManager().reset();
        Logger.getLogger("").addHandler(new SLF4JBridgeHandler());

        Logger rootLogger = Logger.getLogger(this.getClass().getName());

        // redirect Gemfire LogWriter to SLF4J
        Properties props = new Properties();
        props.put("log-writer", new LogWriterLogbackBridge());

        StarbucksClient starbucksClient = new StarbucksClient("starbuckstestclient", props);

        rootLogger.info("Simulating Customers");

        for(int i=0;i<10;i++) {
            CoffeeRequest request = CoffeeRequest.newCoffeeRequest(CoffeeType.AMERICANO);
            starbucksClient.submitOrder(request);
            pause(500);
        }

        starbucksClient.close();
    }
}
