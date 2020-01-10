package kmclauncher;

public final class Logger{
    public static void warn(final String msg) {
        System.out.println("[WARN] " + msg);
    }

    public static void info(final String msg) {
        System.out.println("[INFO] " + msg);
    }

    public static void error(final String msg) {
        System.out.println("[ERRO] " + msg);
    }

    public static void addDir(final String dir)
    {
        System.out.println("[DIR ] Creating '" + dir + "' directory");
    }
}