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

package org.parallax3d.parallax.platforms.gwt;


import org.parallax3d.parallax.Logger;

import java.util.logging.Level;

public class GwtLogger implements Logger {
    public final static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("");

    @Override
    public void info(String message) {
        logger.log(Level.INFO, message);
        System.out.println( message );
    }

    @Override
    public void debug(String message) {
        logger.log(Level.FINE, message);
        System.out.println( message );
    }

    @Override
    public void warn(String message) {
        logger.log(Level.WARNING, message);
        System.err.println( message );
    }

    @Override
    public void error(String message) {
        logger.log(Level.SEVERE, message);
        System.err.println( message );
    }

    @Override
    public void error(String message, Throwable exception) {
        logger.log(Level.SEVERE, message, exception);
        System.err.println(message);
    }

    @Override
    public void setLogLevel(Level logLevel) {
        logger.setLevel(logLevel);
    }

    @Override
    public Level getLogLevel() {
        return logger.getLevel();
    }
}
