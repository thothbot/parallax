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

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class JsFileVisitor extends SimpleFileVisitor<Path> {

    private PathMatcher matcher;

    JsFileVisitor(){

        matcher = FileSystems.getDefault().getPathMatcher("glob:*.js");

    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {

        Path fileName = file.getFileName();

        if ( matcher.matches(fileName))
        {
            System.out.println("Found: "+ file);

            new JsFile( file );
        }

        //Continue to search for other txt files
        return FileVisitResult.CONTINUE;
    }

}
