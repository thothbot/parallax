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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.storage.client.Storage;
import org.parallax3d.parallax.*;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.platforms.gwt.system.assets.Assets;

public class GwtParallax extends Parallax {

	public static final Assets assets = GWT.create(Assets.class);
	public static final Storage LocalStorage = Storage.getLocalStorageIfSupported();

	private static final AgentInfo agentInfo = computeAgentInfo();

	protected GwtParallax() {

	}

	public static void init(ParallaxListener listener) {
		if(Parallax.instance == null)
			Parallax.instance = new GwtParallax();

		listener.onParallaxApplicationReady( Parallax.instance );
	}

	@Override
	public GwtFileHandle getAsset(String path) {
		return getAsset(path, null);
	}

	@Override
	public GwtFileHandle getAsset(String path, FileListener<? extends FileHandle> listener) {
		return ( new GwtFileHandle( path )).load((FileListener<GwtFileHandle>) listener);
	}

	@Override
	public Logger getLogger() {
		return new GwtLogger();
	}

	@Override
	public Parallax.Platform getType() {
		return Parallax.Platform.WebGL;
	}

	// Default configuration

	/** Contains precomputed information on the user-agent. Useful for dealing with browser and OS behavioral differences. Kindly
	 * borrowed from PlayN */
	public static AgentInfo agentInfo() {
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
}
