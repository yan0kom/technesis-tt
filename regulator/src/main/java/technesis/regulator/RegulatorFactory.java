package technesis.regulator;

import technesis.regulator.internal.RegulatorImpl;

public class RegulatorFactory {
    private static Regulator instance;

    public static synchronized Regulator createInstance() {
        if (instance == null) {
            instance = new RegulatorImpl();
        }
        return instance;
    }

    public static synchronized void releaseInstance() {
        if (instance != null) {
            instance = null;
        }
    }
}
