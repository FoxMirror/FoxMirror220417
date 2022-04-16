package foxlaunch;

import java.net.URLClassLoader;

public class FoxServerLauncher {
    public static void main(String[] args) throws Throwable {
        checkJavaVersion();

        System.out.println("Loading libraries, please wait...");

        DataManager.setupLibrariesMap();
        DataManager.unpackData();

        DataManager.downloadLibraries();

        DataManager.generateLaunchScript(args);
    }

    private static void checkJavaVersion() throws Exception {
        String javaVersion = System.getProperty("java.version", "");

        if (FoxServerLauncher.class.getClassLoader() instanceof URLClassLoader) {
            System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), javaVersion));
            System.exit(1);
        }

        String[] javaVersionSplit = javaVersion.split("\\.");
        try {
            if (javaVersionSplit.length >= 3 && Integer.parseInt(javaVersionSplit[0]) >= 17) {
                return;
            }
        } catch (Exception ignored) {}

        System.out.println(String.format(LanguageUtils.I18nToString("launch.java_wrong"), javaVersion));
        Thread.sleep(5000);
    }
}
