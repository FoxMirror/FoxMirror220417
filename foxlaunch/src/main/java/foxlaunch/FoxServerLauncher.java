package foxlaunch;

public class FoxServerLauncher {
    public static void main(String[] args) throws Throwable {
        System.out.println("Loading libraries, please wait...");

        DataManager.setupLibrariesMap();
        DataManager.unpackData();

        DataManager.downloadLibraries();

        DataManager.launch(args);
    }
}
