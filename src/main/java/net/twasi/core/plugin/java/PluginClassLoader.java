package net.twasi.core.plugin.java;

import com.google.common.io.ByteStreams;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.TwasiPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
public final class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final PluginConfig description;
    private final File file;
    private final File dataFolder;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    public final TwasiPlugin plugin;
    private TwasiPlugin pluginInit;
    private IllegalStateException pluginState;

    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginConfig description, final File dataFolder, final File file) throws Exception, IOException, MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        if (loader == null) {
            throw new Exception("Loader can't be null");
        }

        this.loader = loader;
        this.description = description;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();
        this.dataFolder = dataFolder;

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new Exception("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends TwasiPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(TwasiPlugin.class);
            } catch (ClassCastException ex) {
                throw new Exception("main class `" + description.getMain() + "' does not extend TwasiPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new Exception("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new Exception("Abnormal plugin type", ex);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("net.twasi.core.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);
            }

            if (result == null) {
                String path = name.replace('.', '/').concat(".class");
                JarEntry entry = jar.getJarEntry(path);

                if (entry != null) {
                    byte[] classBytes;

                    try (InputStream is = jar.getInputStream(entry)) {
                        classBytes = ByteStreams.toByteArray(is);
                    } catch (IOException ex) {
                        throw new ClassNotFoundException(name, ex);
                    }

                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ex) {
                                if (getPackage(pkgName) == null) {
                                    throw new IllegalStateException("Cannot find package " + pkgName);
                                }
                            }
                        }
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    result = defineClass(name, classBytes, 0, classBytes.length, source);
                }

                if (result == null) {
                    result = super.findClass(name);
                }

                if (result != null) {
                    loader.setClass(name, result);
                }
            }

            classes.put(name, result);
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    Set<String> getClasses() {
        return classes.keySet();
    }

    public synchronized void initialize(TwasiPlugin twasiPlugin) {
        if (twasiPlugin == null) {
            throw new IllegalStateException("Initializing plugin cannot be null");
        }
        ;
        if (twasiPlugin.getClass().getClassLoader() != this)
            throw new IllegalStateException("Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = twasiPlugin;

        twasiPlugin.init(loader, description, dataFolder, new File(""), this);
    }
}