package foxlaunch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class DataManager {
    private static final Map<String, File> librariesMap = new TreeMap<>();
    private static final Map<String, File> foxLaunchLibsMap = new TreeMap<>();
    private static final List<String> launchArgs = new ArrayList<>();

    public static void setupLibrariesMap() {
        try (JarFile serverJar = new JarFile(System.getProperty("java.class.path"))) {
            String classPath = (String) serverJar.getManifest().getMainAttributes().get(Attributes.Name.CLASS_PATH);
            if (classPath != null) {
                String[] libraries = classPath.split(" ");
                for (String library : libraries) {
                    File file = new File(library);
                    librariesMap.put(file.getName(), file.getParentFile());
                }
            } else {
                throw new RuntimeException("Missing MANIFEST.MF?");
            }

            if (librariesMap.size() == 0) {
                throw new RuntimeException("Class-Path is empty!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load libraries!", e);
        }
    }

    public static void unpackData() {
        try (JarFile serverJar = new JarFile(System.getProperty("java.class.path"))) {
            Enumeration<JarEntry> entry = serverJar.entries();
            while (entry.hasMoreElements()) {
                JarEntry jarEntry = entry.nextElement();
                if (jarEntry != null) {
                    String[] name = jarEntry.getName().split("/");
                    if (name.length == 2) {
                        if (Objects.equals(name[0], "data")) {
                            if (name[1].endsWith(".jar")) {
                                File path = librariesMap.get(name[1]);

                                if (path == null) {
                                    path = new File("foxlaunch-libs");
                                    foxLaunchLibsMap.put(name[1], path);
                                }

                                if (!path.exists()) {
                                    path.mkdirs();
                                }

                                try (OutputStream out = new FileOutputStream(new File(path, name[1]))) {
                                    try (InputStream in = serverJar.getInputStream(jarEntry)) {
                                        byte[] bytes = new byte[4096];
                                        int readSize;
                                        while ((readSize = in.read(bytes)) > 0) {
                                            out.write(bytes, 0, readSize);
                                        }
                                    }
                                    out.flush();
                                }
                            } else if (Objects.equals(name[1], "unix_args.txt")) {
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(serverJar.getInputStream(jarEntry)))) {
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        launchArgs.add(line);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not unpack data!", e);
        }
    }

    public static void launch(String[] args) throws Throwable {
        List<String> launchArgs = DataManager.launchArgs.stream().filter(s -> s.startsWith("-D") || s.startsWith("--launchTarget") || s.startsWith("--fml")).collect(Collectors.toList());
        launchArgs.addAll(Arrays.asList(args));
        Class.forName("cpw.mods.bootstraplauncher.BootstrapLauncher", true, ClassLoader.getSystemClassLoader()).getMethod("main", String[].class).invoke(null, new Object[]{ launchArgs.toArray(new String[0]) });
    }
}
