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
import java.nio.file.Path;

public class TaskCompareTests {

    public static void main(String[] args) throws Exception
    {
        File threeTestDir = new File(System.getProperty("threejs.test.dir"));
        if(!threeTestDir.exists() || !threeTestDir.isDirectory())
            throw new Exception("Threejs TESTS are not found in Path: " + threeTestDir.getPath());

        for (Path p : FileLoader.getJSFiles(threeTestDir.toPath())) {
            System.out.println(p.toString());
        }

        File parallaxTestDir = new File(System.getProperty("parallax.test.dir"));
        if(!parallaxTestDir.exists() || !parallaxTestDir.isDirectory())
            throw new Exception("Parallax TESTS are not found in Path: " + parallaxTestDir.getPath());

        for (Class c : JavaReflections.processDirectory( parallaxTestDir )) {
            System.out.println(c.getName());
        }

    }
}