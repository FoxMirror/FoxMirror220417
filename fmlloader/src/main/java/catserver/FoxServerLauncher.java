package catserver;

public class FoxServerLauncher {
    public static String getVersion() {
        return (FoxServerLauncher.class.getPackage().getImplementationVersion() != null) ? FoxServerLauncher.class.getPackage().getImplementationVersion() : "unknown";
    }
}
