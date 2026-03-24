package HolloMon_Log;

import java.io.*;

public class HolloLog {

    public enum Level {
        INFO,
        WARNING,
        CRITICAL,
    }

    public static <T> void Console(HolloLog.Level level, T... message) {
        System.out.print("| " + checkLevel(level));
        for (T msg : message) System.out.print(" " + msg.toString());
        System.out.println();
    }

    public static <T> void Console(T... message) {
        for (T msg : message) System.out.print(msg.toString() + " ");
        System.out.println();
    }

    public static String checkLevel(Level lvl) {
        switch (lvl) {
            case INFO:
                return "[INFO] -> ";
            case WARNING:
                return "[WARNING] -> ";
            case CRITICAL:
                return "[CRITICAL] -> ";
            default:
                return "";
        }
    }
}
