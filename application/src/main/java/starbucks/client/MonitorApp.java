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
 * Simulates customers placing coffee orders. After pressing a key to exit the client, 
 *
 * @author: Erich Eichinger
 * @date: 23/01/12
 */
public class MonitorApp {

    public static void main(String[] args) throws Exception {
        // redirect JUL to SLF4J
        LogManager.getLogManager().reset();
        Logger.getLogger("").addHandler(new SLF4JBridgeHandler());

        // redirect Gemfire LogWriter to SLF4J
        Properties props = new Properties();
        props.put("log-writer", new LogWriterLogbackBridge());

        final MonitorApp app = new MonitorApp(props);
        try {
            Thread simulationThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    app.monitor();
                }
            }, "Monitoring Thread");
            simulationThread.setDaemon(true);
            simulationThread.start();

            System.out.println("Started Monitoring - press enter to exit");
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

    public MonitorApp(Properties props) {
        notNull(props, "props");
        starbucksClient = new Starbucks("monitoringclient", props);
    }

    public void close() {
        isClosed = true;
        pause(2000);
        starbucksClient.close();
    }

    public void monitor() {
        while(!isClosed) {
            BaristaStatistics stats = starbucksClient.getBaristaStatistics();
            logger.info("Got monitor stats " + stats);
            pause(2000);
        }
    }
}
