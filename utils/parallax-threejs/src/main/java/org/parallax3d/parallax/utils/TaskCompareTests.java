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

import org.parallax3d.parallax.Parallax;

import java.io.File;
import java.nio.file.Path;

public class TaskCompareTests {

    private static final String THREE_TEST_DIR = "test/unit";

    public static void main(String[] args) throws Exception
    {
        File main = new File(System.getProperty("threejs.dir"));
        if(!main.exists())
            throw new Exception("Main threejs path is not found: " + main.getPath());

        File src = new File(main, THREE_TEST_DIR);
        if(!src.exists())
            throw new Exception("TEST threejs path is not found: " + src.getPath());

        for (Path p : FileLoader.getJSFiles(src.toPath())) {
            System.out.println(p.toString());
        }

        for (Class c : JavaReflections.getClassesForPackage(Parallax.class.getPackage())) {
            System.out.println(c.toString());
        }

    }

}