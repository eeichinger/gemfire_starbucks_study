package starbucks.client;

import org.slf4j.bridge.SLF4JBridgeHandler;
import starbucks.*;
import support.logging.LogWriterLogbackBridge;

import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static support.Assert.notNull;
import static support.TimerHelpers.pause;

/**
 * @author: Erich Eichinger
 * @date: 23/01/12
 */
public class StarbucksApp {

    public static void main(String[] args) throws Exception {
        // redirect JUL to SLF4J
        LogManager.getLogManager().reset();
        Logger.getLogger("").addHandler(new SLF4JBridgeHandler());

        // redirect Gemfire LogWriter to SLF4J
        Properties props = new Properties();
        props.put("log-writer", new LogWriterLogbackBridge());

        final StarbucksApp app = new StarbucksApp(props);
        try {
            Thread simulationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    app.simulateCustomers();
                }
            }, "Customer Simulation Thread");
            simulationThread.setDaemon(true);
            simulationThread.start();

            System.out.println("Started Customer Simulation - press enter to exit");
            System.in.read();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            app.close();
        }
    }

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Starbucks starbucksClient;
    private boolean isClosed = false;
    
    public StarbucksApp(Properties props) {
        notNull(props, "props");
        starbucksClient = new Starbucks("starbuckstestclient", props);
    }

    public void close() {
        isClosed = true;
        pause(750);
        starbucksClient.close();
    }

    public void simulateCustomers() {
        logger.info("Simulating Customers");

        while(!isClosed) {
            CoffeeRequest request = CoffeeRequest.newCoffeeRequest(CoffeeType.AMERICANO);
            starbucksClient.submitOrder(request, new Starbucks.CoffeeReadyCallback() {
                @Override
                public void onCoffeeReady(CoffeeRequest request, PreparedCoffee coffee) {
                    logger.info("got my coffee");
                }
            });
            pause(Math.round(Math.random() * 500.0)+250);
        }
    }
}
