package starbucks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import support.logging.LogWriterLogbackBridge;

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
        System.setProperty("java.util.logging.config.class", LogWriterLogbackBridge.class.getName());
        LogManager.getLogManager().reset();

        Logger rootLogger = Logger.getLogger(this.getClass().getName());

        rootLogger.info("Simulating Customers");

        StarbucksClient starbucksClient = new StarbucksClient("starbuckstestclient");

        for(int i=0;i<10;i++) {
            CoffeeRequest request = CoffeeRequest.newCoffeeRequest(CoffeeType.AMERICANO);
            starbucksClient.submitOrder(request);
            pause(500);
        }

        starbucksClient.close();
    }
}
