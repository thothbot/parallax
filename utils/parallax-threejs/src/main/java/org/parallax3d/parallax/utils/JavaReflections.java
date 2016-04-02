/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
 *
 * This file is part of Parallax project.
 *
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 *
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 *
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */
package org.parallax3d.parallax.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class JavaReflections {

    private static void log(String msg) {
//        System.out.println("ClassDiscovery: " + msg);
    }

    private static Class<?> loadClass(String className) throws Exception {
        return Class.forName(className);
    }

    public static List<Class<?>> processDirectory(File directory)
    {
        return processDirectory(directory, "");
    }

    /**
     * Given a package name and a directory returns all classes within that directory
     *
     * @param directory
     * @param pkgname
     * @return Classes within Directory with package name
     */
    public static List<Class<?>> processDirectory(File directory, String pkgname) {

        ArrayList<Class<?>> classes = new ArrayList<>();

        log("Reading Directory '" + directory + "'");

        if(pkgname.length() > 0) pkgname += '.';

        // Get the list of the files contained in the package
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            String className = null;

            // we are only interested in .class files
            if (fileName.endsWith(".class")) {
                // removes the .class extension
                className = pkgname + fileName.substring(0, fileName.length() - 6);
            }

            log("FileName '" + fileName + "'  =>  class '" + className + "'");

            if (className != null) {
                try {
                    classes.add(loadClass(className));
                } catch (Exception e) {
                    System.out.println( e );
                }
            }

            //If the file is a directory recursively class this method.
            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                classes.addAll(processDirectory(subdir, pkgname + fileName));
            }
        }
        return classes;
    }

    /**
     * Given a jar file's URL and a package name returns all classes within jar file.
     *
     * @param resource
     * @param pkgname
     */
    public static List<Class<?>> processJarfile(URL resource, String pkgname) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        //Turn package name to relative path to jar file
        String relPath = pkgname.replace('.', '/');
        String resPath = resource.getPath();
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        log("Reading JAR file: '" + jarPath + "'");
        JarFile jarFile;

        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }

        //get contents of jar file and iterate through them
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            //Get content name from jar file
            String entryName = entry.getName();
            String className = null;

            //If content is a class save class name.
            if (entryName.endsWith(".class") && entryName.startsWith(relPath)
                    && entryName.length() > (relPath.length() + "/".length())) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }

            log("JarEntry '" + entryName + "'  =>  class '" + className + "'");

            //If content is a class add class to List
            if (className != null) {
                try {
                    classes.add(loadClass(className));
                } catch (Exception e) {
                    System.out.println( e );
                }
            }
        }
        return classes;
    }

    /**
     * Give a package this method returns all classes contained in that package
     *
     * @param pkgname
     */
    public static List<Class<?>> getClassesForPackage( String pkgname ) {
        ArrayList<Class<?>> classes = new ArrayList<>();

        //Get name of package and turn it to a relative path
        String relPath = pkgname.replace('.', '/');

        // Get a File object for the package
        Enumeration<URL> resources = null;
        try {
            resources = ClassLoader.getSystemClassLoader().getResources(relPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If we can't find the resource we throw an exception
        if (resources == null) {
            throw new RuntimeException("Unexpected problem: No resource for " + relPath);
        }

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            log("Package: '" + pkgname + "' becomes Resource: '" + resource.toString() + "'");

            //If the resource is a jar get all classes from jar
            if (resource.toString().startsWith("jar:")) {
                classes.addAll(processJarfile(resource, pkgname));
            } else {
                classes.addAll(processDirectory(new File(resource.getPath()), pkgname));
            }
        }

        return classes;
    }
}
