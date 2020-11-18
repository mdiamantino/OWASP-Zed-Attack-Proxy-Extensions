/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2020 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.policyverifier.controllers.jarLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Logger;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

/** The class groups all behaviours needed in order to extact a policy from a Jar File */
public class PolicyGeneratorFromJar {
    private static final Logger logger = Logger.getLogger(PolicyGeneratorFromJar.class);

    /**
     * Instantiate all Rules defined in the jar and creates a policy from them.
     *
     * @param file Jar file
     * @return The Policy containing all rules defined in the given Jar
     * @throws IOException if could not read .classes in the jar file
     * @throws IllegalArgumentException if jar is empty
     */
    public static Policy generatePolicy(File file) throws Exception {
        Set<Rule> rules = getInstantiatedRulesFromJar(file);
        if (rules.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String policyName =
                file.getName()
                        .substring(0, file.getName().indexOf(".")); // Policy name without ".jar"
        return new Policy(rules, policyName);
    }

    /**
     * Find the name of all .class files in a Jar file
     *
     * @param jar Jar file
     * @return A set of names of all the .class files in the given Jar
     * @throws IOException if cannot read the input stream of the file
     */
    private static Set<String> getRulesNamesInJarFile(File jar) throws IOException {
        Set<String> classNames = new HashSet<>();
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jar));
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String filename = entry.getName();
                String fullClassName =
                        filename.substring(0, filename.indexOf(".")) // Without .class
                                .replace('/', '.'); // Replace / with .
                if (!fullClassName.equals("Rule")) {
                    logger.info(String.format("Found in jar class name : %s", fullClassName));
                    classNames.add(fullClassName);
                }
            }
        }
        return classNames;
    }

    /**
     * Method used to build a loader useful to convert classes of the jar in actual usable class in
     * code
     *
     * @param jar Jar file
     * @return A class loader of the jar
     * @throws MalformedURLException if cannot create the loader
     */
    private static ClassLoader getClassLoader(File jar) throws MalformedURLException {
        ClassLoader loader;
        loader =
                URLClassLoader.newInstance(
                        new URL[] {jar.toURI().toURL()},
                        PolicyGeneratorFromJar.class.getClassLoader());
        return loader;
    }

    /**
     * Load on runtime the Rules in the jar file and instantiate them
     *
     * @param jar Jar File
     * @return Set of instantiated rules given the Jar they have been compiled into
     * @throws Exception if could not load or instantiate the classes properly
     */
    private static Set<Rule> getInstantiatedRulesFromJar(File jar) throws Exception {
        Set<Rule> instances = new HashSet<>();
        Set<String> classesNames = getRulesNamesInJarFile(jar);
        ClassLoader loader = getClassLoader(jar);
        for (String className : classesNames) {
            Class<?> clazz = Class.forName(className, false, loader);
            // Force the fact that they implement the Rule interface
            Class<? extends Rule> newClass = clazz.asSubclass(Rule.class);
            Constructor<? extends Rule> constructor = newClass.getConstructor();
            instances.add(constructor.newInstance());
        }
        return instances;
    }
}
