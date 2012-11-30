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

package thothbot.parallax.core.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Logger object is used to log messages for a specific
 * system or application component. 
 * <p>
 * Logger should be configured in GWT configuration file. For example:
 * 
 * <pre>{@code
 * <inherits name="com.google.gwt.logging.Logging"/>
 * <set-property name="gwt.logging.enabled" value="TRUE"/> 
 * <set-property name="gwt.logging.logLevel" value="ALL"/>
 * 
 * <set-property name="gwt.logging.consoleHandler" value="DISABLED" />
 * <set-property name="gwt.logging.popupHandler" value="DISABLED" />
 * }</pre>
 * 
 * 
 * @author thothbot
 *
 */
public class Log
{
	public final static Logger logger = Logger.getLogger("");
	
	public static void debug(String msg)
	{
		Log.logger.log(Level.FINE, msg);
	}

	public static void info(String msg)
	{
		Log.logger.log(Level.INFO, msg);
	}
	
	public static void warn(String msg)
	{
		Log.logger.log(Level.WARNING, msg);
	}
	
	public static void error(String msg)
	{
		Log.logger.log(Level.SEVERE, msg);
	}
	
	public static void error(Object ... all)
	{
		StringBuffer result = new StringBuffer();
		for ( Object mods : all )
		{
			result.append(mods + " ");
		}

		Log.logger.log(Level.SEVERE, result.toString());
	}
	
	public static void error(String msg, Throwable thrown)
	{
		Log.logger.log(Level.SEVERE, msg, thrown);
	}
}
