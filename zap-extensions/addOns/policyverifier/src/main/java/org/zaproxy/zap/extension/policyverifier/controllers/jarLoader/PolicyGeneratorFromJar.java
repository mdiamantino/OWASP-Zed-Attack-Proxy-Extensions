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
import org.zaproxy.zap.extension.policyverifier.controllers.PolicyGeneratorFactory;
import org.zaproxy.zap.extension.policyverifier.models.Policy;
import org.zaproxy.zap.extension.policyverifier.models.Rule;

/** The class groups all behaviours needed in order to extract a policy from a Jar File */
public class PolicyGeneratorFromJar extends PolicyGeneratorFactory {
    private static final Logger logger = Logger.getLogger(PolicyGeneratorFromJar.class);

    public PolicyGeneratorFromJar() {}

    /**
     * Instantiate all Rules defined in the jar and creates a policy from them.
     *
     * @return The Policy containing all rules defined in the given Jar
     * @throws IOException if could not read .classes in the jar file
     * @throws IllegalArgumentException if jar is empty
     */
    @Override
    public Policy generatePolicy() throws Exception {
        logger.info("Generating Policy for JAR");
        Set<Rule> rules = getRules();
        logger.info("Rules generated for JAR :" + rules.toString());
        if (rules.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String policyName =
                getFile()
                        .getName()
                        .substring(
                                0, getFile().getName().indexOf(".")); // Policy name without ".jar"
        return new Policy(rules, policyName);
    }

    /**
     * Load on runtime the Rules in the jar file and instantiate them
     *
     * @return Set of instantiated rules given the Jar they have been compiled into
     * @throws Exception if could not load or instantiate the classes properly
     */
    protected Set<Rule> getRules() throws Exception {
        logger.warn("In GetRules() ");
        Set<Rule> instances = new HashSet<>();
        Set<String> classesNames = getRulesNamesInJarFile();
        ClassLoader loader = getClassLoader(getFile());
        for (String className : classesNames) {
            Class<?> clazz = Class.forName(className, false, loader);
            // Force the fact that they implement the Rule interface
            Class<? extends Rule> newClass = clazz.asSubclass(Rule.class);
            Constructor<? extends Rule> constructor = newClass.getConstructor();
            instances.add(constructor.newInstance());
        }
        return instances;
    }

    /**
     * Find the name of all .class files in a Jar file
     *
     * @return A set of names of all the .class files in the given Jar
     * @throws IOException if cannot read the input stream of the file
     */
    private Set<String> getRulesNamesInJarFile() throws IOException {
        logger.info("Getting Names in jar file " + getFileName());
        Set<String> classNames = new HashSet<>();
        logger.info("File: " + getFileName());
        ZipInputStream zip = new ZipInputStream(new FileInputStream(getFile()));
        logger.info("Got ZIP");
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
        logger.info(classNames.toString());
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
    private ClassLoader getClassLoader(File jar) throws MalformedURLException {
        ClassLoader loader;
        loader =
                URLClassLoader.newInstance(
                        new URL[] {jar.toURI().toURL()},
                        PolicyGeneratorFromJar.class.getClassLoader());
        return loader;
    }
}
