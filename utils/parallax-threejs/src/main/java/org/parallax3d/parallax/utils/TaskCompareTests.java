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

import org.parallax3d.parallax.system.ThreejsTest;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskCompareTests {

    static List<JavaTestClass> javaTests = new ArrayList<>();

    static List<JsTestFile> jsTests = new ArrayList<>();
    static List<JsTestFile> jsTestsFound = new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
        File threeTestDir = new File(System.getProperty("threejs.test.dir"));
        if(!threeTestDir.exists() || !threeTestDir.isDirectory())
            throw new Exception("Threejs TESTS are not found in Path: " + threeTestDir.getPath());

        for (Path p : FileLoader.getJSFiles(threeTestDir.toPath()))
        {
            try
            {
                JsTestFile test = new JsTestFile(p);
                if(test.isTest())
                    jsTests.add( test );
            }
            catch (Exception e) {
                // just ignore
            }
        }

        File parallaxTestDir = new File(System.getProperty("parallax.test.dir"));
        if(!parallaxTestDir.exists() || !parallaxTestDir.isDirectory())
            throw new Exception("Parallax TESTS are not found in Path: " + parallaxTestDir.getPath());

        for (Class c : JavaReflections.processDirectory( parallaxTestDir )) {

            if(c.getAnnotation(ThreejsTest.class) != null)
                javaTests.add( new JavaTestClass(c) );

        }

        log("TESTS Loaded:");
        log("\tThreejs tests:\t" + jsTests.size());
        log("\tParallax tests:\t" + javaTests.size());

        compareParallax2Three();
    }

    static void compareParallax2Three()
    {
        log("-- Compare Parallax to Threejs tests:");

        for(JavaTestClass java: javaTests)
        {
            for(JsTestFile js: jsTests)
            {
                if(js.getTestId().equals(java.getThreeTestId()))
                {
                    jsTestsFound.add(js);
                    log("\tFound test for Parallax " + java.getName());
                }
            }
        }
    }

    static void log(String msg) {
        System.out.println( msg );
    }
}