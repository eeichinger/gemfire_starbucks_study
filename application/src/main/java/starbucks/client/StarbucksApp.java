package starbucks.client;

import org.slf4j.bridge.SLF4JBridgeHandler;
import starbucks.*;
import support.logging.LogWriterLogbackBridge;

import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static support.Assert.notNull;

/**
 * @author: Erich Eichinger
 * @date: 23/01/12
 */
public class StarbucksApp {

    public static void main(String[] args) {
        // redirect JUL to SLF4J
        LogManager.getLogManager().reset();
        Logger.getLogger("").addHandler(new SLF4JBridgeHandler());

        // redirect Gemfire LogWriter to SLF4J
        Properties props = new Properties();
        props.put("log-writer", new LogWriterLogbackBridge());

        StarbucksApp app = new StarbucksApp(props);
        try {
            app.simulateCustomer();
            app.getBaristaStats();
        } finally {
            app.close();
        }
    }

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Starbucks starbucksClient;

    public StarbucksApp(Properties props) {
        notNull(props, "props");
        starbucksClient = new Starbucks("starbuckstestclient", props);
    }

    public void close() {
        starbucksClient.close();
    }

    public void simulateCustomer() {
        logger.info("Simulating Customers");

        for(int i=0;i<100;i++) {
            CoffeeRequest request = CoffeeRequest.newCoffeeRequest(CoffeeType.AMERICANO);
            starbucksClient.submitOrder(request, new Starbucks.CoffeeReadyCallback() {
                @Override
                public void onCoffeeReady(CoffeeRequest request, PreparedCoffee coffee) {
                    logger.info("got my coffee");
                }
            });
//            pause(500);
        }

    }

    public void getBaristaStats() {
        BaristaStatistics stats = starbucksClient.getBaristaStatistics();
        logger.info("Got stats " + stats);
    }
}
