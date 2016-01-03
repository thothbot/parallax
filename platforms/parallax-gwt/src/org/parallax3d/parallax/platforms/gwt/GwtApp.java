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

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.*;
import org.parallax3d.parallax.Animation;
import org.parallax3d.parallax.App;
import org.parallax3d.parallax.Files;
import org.parallax3d.parallax.Rendering;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GwtApp extends App implements EntryPoint {

	public final static Logger logger = Logger.getLogger("");

	private int logLevel = LOG_ERROR;

	GwtAppConfiguration config;
	GwtRendering rendering;

	private static AgentInfo agentInfo;

	LoadingListener loadingListener;

	// Default configuration
	public GwtAppConfiguration getConfig () {

		return new GwtAppConfiguration();

	};

	public abstract void onInit();

	@Override
	public void onModuleLoad () {

		GwtApp.agentInfo = computeAgentInfo();
		this.config = getConfig();

		addEventListeners();

		App.app = this;
		App.files = new GwtFiles();

		onInit();
	}

	public void setRendering(Panel root, GwtAppConfiguration config) {

		root.clear();

		// setup modules
		try {
			rendering = new GwtRendering(root, config);
		} catch (Throwable e) {
			root.clear();
			String msg = "Sorry, your browser doesn't seem to support WebGL";
			root.add(new Label(msg));
			App.app.error("setRendering", msg, e);
			return;
		}
	}

	@Override
	public Rendering getRendering () {
		return rendering;
	}

	@Override
	public Files getFiles() {
		return App.files;
	}

	@Override
	public void log (String tag, String message) {
		if (logLevel >= LOG_INFO) {
			String msg = tag + ": " + message;
			GwtApp.logger.log(Level.INFO, msg);
			System.out.println(msg);
		}
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) {
			String msg = tag + ": " + message;
			GwtApp.logger.log(Level.INFO, msg, exception);

			System.out.println(msg + "\n" + exception.getMessage());
			System.out.println(getStackTrace(exception));
		}
	}

	@Override
	public void error(String tag, String message) {
		if (logLevel >= LOG_ERROR) {
			String msg = tag + ": " + message;
			GwtApp.logger.log(Level.SEVERE, msg);

			System.err.println(msg);
		}
	}

	@Override
	public void error(String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) {
			String msg = tag + ": " + message;
			GwtApp.logger.log(Level.SEVERE, msg, exception);

			System.err.println(msg + "\n" + exception.getMessage() + "\n");
			System.out.println(getStackTrace(exception));
		}
	}

	@Override
	public void debug(String tag, String message) {
		if (logLevel >= LOG_DEBUG) {
			String msg = tag + ": " + message;
			GwtApp.logger.log(Level.FINE, msg);
			System.out.println( msg + "\n");
		}
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) {
			String msg = tag + ": " + message;
			GwtApp.logger.log(Level.FINE, msg, exception);

			System.out.println(msg + "\n" + exception.getMessage());
			System.out.println(getStackTrace(exception));
		}
	}
	
	private String getMessages (Throwable e) {
		StringBuffer buffer = new StringBuffer();
		while (e != null) {
			buffer.append(e.getMessage() + "\n");
			e = e.getCause();
		}
		return buffer.toString();
	}
	
	private String getStackTrace (Throwable e) {
		StringBuffer buffer = new StringBuffer();
		for (StackTraceElement trace : e.getStackTrace()) {
			buffer.append(trace.toString() + "\n");
		}
		return buffer.toString();
	}

	@Override
	public void setLogLevel (int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public int getLogLevel() {
		return logLevel;
	}

	@Override
	public ApplicationType getType () {
		return ApplicationType.WebGL;
	}

	@Override
	public void exit () {
	}

	/** Contains precomputed information on the user-agent. Useful for dealing with browser and OS behavioral differences. Kindly
	 * borrowed from PlayN */
	public static AgentInfo agentInfo () {
		return agentInfo;
	}

	/** kindly borrowed from PlayN **/
	private static native AgentInfo computeAgentInfo () /*-{
		var userAgent = navigator.userAgent.toLowerCase();
		return {
		// browser type flags
		isFirefox : userAgent.indexOf("firefox") != -1,
		isChrome : userAgent.indexOf("chrome") != -1,
		isSafari : userAgent.indexOf("safari") != -1,
		isOpera : userAgent.indexOf("opera") != -1,
		isIE : userAgent.indexOf("msie") != -1,
		// OS type flags
		isMacOS : userAgent.indexOf("mac") != -1,
		isLinux : userAgent.indexOf("linux") != -1,
		isWindows : userAgent.indexOf("win") != -1
		};
	}-*/;

	/** Returned by {@link #agentInfo}. Kindly borrowed from PlayN. */
	public static class AgentInfo extends JavaScriptObject {
		public final native boolean isFirefox () /*-{
			return this.isFirefox;
		}-*/;

		public final native boolean isChrome () /*-{
			return this.isChrome;
		}-*/;

		public final native boolean isSafari () /*-{
			return this.isSafari;
		}-*/;

		public final native boolean isOpera () /*-{
			return this.isOpera;
		}-*/;

		public final native boolean isIE () /*-{
			return this.isIE;
		}-*/;

		public final native boolean isMacOS () /*-{
			return this.isMacOS;
		}-*/;

		public final native boolean isLinux () /*-{
			return this.isLinux;
		}-*/;

		public final native boolean isWindows () /*-{
			return this.isWindows;
		}-*/;

		protected AgentInfo () {
		}
	}

//	public String getBaseUrl () {
//		return preloader.baseUrl;
//	}
//
//	public CanvasElement getCanvasElement(){
//		return graphics.canvas;
//	}

	public LoadingListener getLoadingListener () {
		return loadingListener;
	}

	public void setLoadingListener (LoadingListener loadingListener) {
		this.loadingListener = loadingListener;
	}

	native static public void consoleLog(String message) /*-{
		console.log( "GWT: " + message );
	}-*/;
	
	private native void addEventListeners () /*-{
		var self = this;
		$doc.addEventListener('visibilitychange', function (e) {
			self.@org.parallax3d.parallax.platforms.gwt.GwtApp::onVisibilityChange(Z)($doc['hidden'] !== true);
		});
	}-*/;

	private void onVisibilityChange (boolean visible) {
	}
	
	/**
	 * LoadingListener interface main purpose is to do some things before or after {@link GwtApp#setupLoop()}
	 */
	public interface LoadingListener{
		/**
		 * Method called before the setup
		 */
		public void beforeSetup();

		/**
		 * Method called after the setup
		 */
		public void afterSetup();
	}
}
