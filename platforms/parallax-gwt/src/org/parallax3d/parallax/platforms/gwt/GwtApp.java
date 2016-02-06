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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import org.parallax3d.parallax.*;
import org.parallax3d.parallax.platforms.gwt.preloader.Preloader;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GwtApp extends App implements EntryPoint {

	public final static Logger logger = Logger.getLogger("");

	GwtAppConfiguration config;

	GwtRenderingContext rendering;

	Preloader preloader;

	private static AgentInfo agentInfo;

	public void setLogLevel (Level logLevel) {
		logger.setLevel(logLevel);
	}

	public Level getLogLevel (){
		return logger.getLevel();
	}

	// Default configuration
	public GwtAppConfiguration getConfig () {

		return new GwtAppConfiguration();

	};

	public abstract void onInit();

	@Override
	public void onModuleLoad () {

		App.app = GwtApp.this;
		GwtApp.agentInfo = computeAgentInfo();
		this.config = getConfig();

		addEventListeners();

		final Preloader.PreloaderCallback callback = getPreloaderCallback();
		preloader = createPreloader();
		App.files = new GwtFiles(preloader);

		onInit();

		preloader.preload("assets.txt", new Preloader.PreloaderCallback() {
			@Override
			public void error(String file) {
				callback.error(file);
			}

			@Override
			public void update(Preloader.PreloaderState state) {
				callback.update(state);
				if (state.hasEnded()) {

				}
			}
		});
	}

	public String getPreloaderBaseURL()
	{
		return GWT.getHostPageBaseURL() + "assets/";
	}

	public Preloader createPreloader() {
		return new Preloader(getPreloaderBaseURL());
	}

	public Preloader.PreloaderCallback getPreloaderCallback () {

		return new Preloader.PreloaderCallback() {

			@Override
			public void error (String file) {
				Log.error("Preloader: error: " + file);
			}

			@Override
			public void update (Preloader.PreloaderState state) {

			}

		};
	}

	public void setRendering(GwtRenderingContext rendering) {

		this.rendering = rendering;

	}

	@Override
	public RenderingContext getRendering () {
		return rendering;
	}

	@Override
	public Files getFiles() {
		return App.files;
	}

	@Override
	public void info (String message) {
		GwtApp.logger.log(Level.INFO, message);
		System.out.println( message );
	}

	@Override
	public void debug(String message) {
		GwtApp.logger.log(Level.FINE, message);
		System.out.println( message );
	}

	@Override
	public void warn(String message) {
		GwtApp.logger.log(Level.WARNING, message);
		System.err.println( message );
	}

	@Override
	public void error(String message) {
		GwtApp.logger.log(Level.SEVERE, message);
		System.err.println( message );
	}

	@Override
	public void error(String message, Throwable exception) {
		GwtApp.logger.log(Level.SEVERE, message, exception);
		System.err.println(message);
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
}
