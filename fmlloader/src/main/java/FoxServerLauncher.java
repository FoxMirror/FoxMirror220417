public class FoxServerLauncher {
    public static void main(String[] args) throws Throwable {
        Class.forName("net.minecraftforge.fml.server.ServerMain", true, ClassLoader.getSystemClassLoader()).getMethod("main", String[].class).invoke(null, args);
    }

    public static String getVersion() {
        return (FoxServerLauncher.class.getPackage().getImplementationVersion() != null) ? FoxServerLauncher.class.getPackage().getImplementationVersion() : "unknown";
    }
}
