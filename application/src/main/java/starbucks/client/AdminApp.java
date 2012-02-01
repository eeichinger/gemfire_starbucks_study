package starbucks.client;

import com.gemstone.gemfire.admin.*;
import com.gemstone.gemfire.distributed.DistributedSystem;

import java.util.Properties;

/**
 * @author: Erich Eichinger
 * @date: 23/01/12
 */
public class AdminApp {
    
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
//        props.setProperty("mcast-address", "224.0.0.250");
//        props.setProperty("mcast-port", "10333");
        props.setProperty("locators", "localhost[40404]");
        props.setProperty("mcast-port", "0");
// Before connecting to either AdminDistributedSystem or DistributedSystem,
// call setEnableAdministrationOnly.
        AdminDistributedSystemFactory.setEnableAdministrationOnly(false);
        DistributedSystem connection = DistributedSystem.connect(props);
        DistributedSystemConfig config =
                AdminDistributedSystemFactory.defineDistributedSystem(connection, null);
        AdminDistributedSystem admin =
                AdminDistributedSystemFactory.getDistributedSystem(config);
        admin.connect();
// Wait for the connection to be made
        long timeout = 30 * 1000;
        try {
            if (!admin.waitToBeConnected(timeout)) {
                String s = "Could not connect after " + timeout + "ms";
                throw new Exception(s);
            }
        }
        catch (InterruptedException ex) {
            String s = "Interrupted while waiting to be connected";
            throw new Exception(s, ex);
        }

        admin.addCacheListener(new SystemMemberCacheListener() {
            @Override
            public void afterRegionCreate(SystemMemberRegionEvent systemMemberRegionEvent) {
                System.out.println("afterRegionCreate " + systemMemberRegionEvent);
            }

            @Override
            public void afterRegionLoss(SystemMemberRegionEvent systemMemberRegionEvent) {
                System.out.println("afterRegionLoss " + systemMemberRegionEvent);
            }

            @Override
            public void afterCacheCreate(SystemMemberCacheEvent systemMemberCacheEvent) {
                System.out.println("afterCacheCreate " + systemMemberCacheEvent);
            }

            @Override
            public void afterCacheClose(SystemMemberCacheEvent systemMemberCacheEvent) {
                System.out.println("afterCacheCreate " + systemMemberCacheEvent);
            }
        });
        
        System.out.println("Listening for Cache Events - press enter to stop");
        System.in.read();
    }
}
