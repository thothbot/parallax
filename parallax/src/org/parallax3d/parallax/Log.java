/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax;

public class Log {

    public static void info(String msg)
    {
        Parallax.app().getLogger().info( msg);
    }

    public static void debug(String msg)
    {
        Parallax.app().getLogger().debug( msg);
    }

    public static void warn(String msg)
    {
        Parallax.app().getLogger().warn( msg);
    }

    public static void error(String msg)
    {
        Parallax.app().getLogger().error( msg);
    }

    public static void error(Object ... all)
    {
        StringBuffer result = new StringBuffer();
        for ( Object mods : all )
        {
            result.append(mods + " ");
        }

        Parallax.app().getLogger().error( result.toString());
    }

    public static void error(String msg, Throwable thrown)
    {
        Parallax.app().getLogger().error(msg, thrown);
    }
}
