package foxlaunch;

public class FoxServerLauncher {
    public static void main(String[] args) throws Throwable {
        Class.forName("net.minecraftforge.fml.server.ServerMain", true, ClassLoader.getSystemClassLoader()).getMethod("main", String[].class).invoke(null, new Object[]{ args });
    }
}
