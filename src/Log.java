import java.io.*;

public class Log {

  public enum Level {
    INFO,
    WARNING,
    CRITICAL
  }

  public static <T> void Console(Log.Level level, T... message) {

    System.out.print("| " + checkLevel(level));
    for (T msg : message)
      System.out.print(" " + msg.toString());
    System.out.println();
  }

  public static <T> void Console(T... message) {
    for (T msg : message)
      System.out.print(msg.toString() + " ");
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

};
