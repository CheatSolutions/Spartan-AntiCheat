package kireiko.dev.millennium.ml.logic;

import ai.idealistic.spartan.Register;

public class Logger {

    private static final java.util.logging.Logger logger = Register.plugin.getLogger();
    public static void info(String msg) {
        logger.info("[MILLENNIUM] " + msg);
    }

    public static void warn(String msg) {
        logger.warning("[MILLENNIUM] " + msg);
    }

    public static void error(String msg) {
        logger.severe("[MILLENNIUM] " + msg);
    }
}
