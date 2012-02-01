package starbucks.gemfire;

import com.gemstone.gemfire.SystemFailure;
import com.gemstone.gemfire.internal.cache.CacheServerLauncher;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;
import support.logging.LogWriterLogbackBridge;

import java.util.Map;
import java.util.Properties;

/**
 * This server launcher replaces the standard server launcher in order to redirect Gemfire's LogWriter implementation
 * to slf4j/logback
 *
 * @author: Erich Eichinger
 * @date: 23/01/12
 */
public class StarbucksServerLauncher extends CacheServerLauncher {

    public static void main(String[] args)
    {
        StarbucksServerLauncher launcher = new StarbucksServerLauncher("CacheServer");
        boolean inServer = false;
        try
        {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("start")) {
                    launcher.start(args);
                }
                else if (args[0].equalsIgnoreCase("server")) {
                    inServer = true;
                    launcher.server(args);
                }
                else if (args[0].equalsIgnoreCase("stop")) {
                    launcher.stop(args);
                }
                else if (args[0].equalsIgnoreCase("status")) {
                    launcher.status(args);
                }
                else {
                    launcher.usage();
                    System.exit(1);
                }
            }
            else {
                launcher.usage();
                System.exit(1);
            }

            throw new Exception(LocalizedStrings.CacheServerLauncher_INTERNAL_ERROR_SHOULDNT_REACH_HERE.toLocalizedString());
        }
        catch (VirtualMachineError err) {
            SystemFailure.initiateFailure(err);

            throw err;
        }
        catch (Throwable t)
        {
            SystemFailure.checkFailure();
            t.printStackTrace();
            if (inServer) {
                launcher.setServerError(LocalizedStrings.CacheServerLauncher_ERROR_STARTING_SERVER_PROCESS.toLocalizedString(), t);
            }

            launcher.restoreStdOut();
            if (launcher.logger != null) {
                launcher.logger.severe(LocalizedStrings.CacheServerLauncher_CACHE_SERVER_ERROR, t);
            }
            else
            {
                System.out.println(LocalizedStrings.CacheServerLauncher_ERROR_0.toLocalizedString(new Object[] { t.getMessage() }));
            }
            System.exit(1);
        }
    }

    public StarbucksServerLauncher(String baseName) {
        super(baseName);
    }

    @Override
    protected Map<String, Object> getServerOptions(String[] args) throws Exception {
        Map<String,Object> opts = super.getServerOptions(args);
        Properties props = (Properties) opts.get("properties");
        props.put("log-writer", new LogWriterLogbackBridge());
        return opts;
    }
}
