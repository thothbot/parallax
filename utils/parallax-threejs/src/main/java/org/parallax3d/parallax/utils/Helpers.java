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
import java.util.Scanner;

public class Helpers {

    public static String getCopyHeader() {

        try {
            File copyheader = new File(System.getProperty("copyheader.file"));
            if(!copyheader.exists())
                throw new Exception("Parallax copyright header file does not exist in " + copyheader.getPath());

            return new Scanner( copyheader ).useDelimiter("\\Z").next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }
}
