package org.zaproxy.zap.extension.policyverifier.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarLoader<C> {

    public Set<C> loadClassesInJar(File jar, Class<C> parentClass) throws IOException {
        Set<C> instances = new HashSet<>();
        Set<String> classesNames = getClassesNamesIn(jar);
        ClassLoader loader = getClassLoader(jar);
        try {
            for (String className : classesNames) {
                String classPath = "policyExample." + className;
                Class<?> clazz = Class.forName(classPath, true, loader);
                Class<? extends C> newClass = clazz.asSubclass(parentClass);
                Constructor<? extends C> constructor = newClass.getConstructor();
                instances.add(constructor.newInstance());
            }
        } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return instances;
    }

    private Set<String> getClassesNamesIn(File file) throws IOException {
        Set<String> classNames = new HashSet<>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".java")) {
                String[] res = entry.getName().split("/", 0);
                String fileName = res[res.length - 1];
                classNames.add(fileName.substring(0, fileName.indexOf(".")));
            }
        }
        return classNames;
    }

    private ClassLoader getClassLoader(File jar) {
        ClassLoader loader = null;
        try {
            loader = URLClassLoader.newInstance(
                    new URL[]{jar.toURI().toURL()},
                    getClass().getClassLoader()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return loader;
    }
}
