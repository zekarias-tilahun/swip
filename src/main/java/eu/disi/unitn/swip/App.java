package eu.disi.unitn.swip;


import eu.disi.unitn.swip.start.DiscoverOrderDependency;
import eu.disi.unitn.swip.util.Utility;

import java.io.IOException;

/**
 * The main program
 */
public class App {
    public static void main(String[] args) {
        try {
            Utility.initProperties();
            Utility.verifyAndSetupDatabaseInstallation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long start  = System.currentTimeMillis();
        DiscoverOrderDependency discoverOrderDependency = new DiscoverOrderDependency();
        discoverOrderDependency.execute();
        long end = System.currentTimeMillis();
        System.out.println("[Info]: Execution time: " + (end-start) + "ms");
    }
}
