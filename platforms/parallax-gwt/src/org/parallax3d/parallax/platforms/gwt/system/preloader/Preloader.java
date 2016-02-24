/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * Based on libgdx Preloader.java file
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

package org.parallax3d.parallax.platforms.gwt.system.preloader;

import com.google.gwt.core.client.GWT;
import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.platforms.gwt.GwtFileHandle;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Preloader {
	
	public FastMap<Asset> directories = new FastMap<>();
	public FastMap<Asset> images = new FastMap<>();
	public FastMap<Asset> audio = new FastMap<>();
	public FastMap<Asset> texts = new FastMap<>();
	public FastMap<Asset> binaries = new FastMap<>();

	public static class Asset {
		public Asset (String url, AssetFilter.AssetType type, long size, String mimeType) {
			this.url = url;
			this.type = type;
			this.size = size;
			this.mimeType = mimeType;
		}

		public final String url;
		public final AssetFilter.AssetType type;
		public final long size;
		public final String mimeType;

		// Async loading states
		public long loaded;
		public boolean isLoaded;
		public boolean isFailed;
		public Object data;
	}

	public final String baseUrl;

	public interface PreloaderCallback {

		void ready(boolean success);

	}

	public Preloader (String newBaseURL, final String assetFileUrl, final PreloaderCallback callback) {
		
		baseUrl = newBaseURL;
	
		// trigger copying of assets and creation of assets.txt
		GWT.create(PreloaderBundle.class);

		final AssetDownloader loader = new AssetDownloader();
		loader.loadText(baseUrl + assetFileUrl, new FileListener<String>() {
			@Override
			public void onProgress (double amount) {
			}
			@Override
			public void onFailure () {
				Log.warn("Can't load asset file: " + baseUrl + assetFileUrl);
				callback.ready(false);
			}
			@Override
			public void onSuccess (String result) {
				String[] lines = result.split("\n");
				for (String line : lines) {
					String[] tokens = line.split(":");
					if (tokens.length != 4) {
						throw new ParallaxRuntimeException("Invalid assets description file.");
					}

					String url = tokens[1].trim();
					long size = Long.parseLong(tokens[2]);
					String mime = tokens[3];

					if (tokens[0].equals("t"))
						texts.put(url, new Asset(url, AssetFilter.AssetType.Text, size, mime));
					if (tokens[0].equals("i"))
						images.put(url, new Asset(url, AssetFilter.AssetType.Image, size, mime));
					if (tokens[0].equals("b"))
						binaries.put(url, new Asset(url, AssetFilter.AssetType.Binary, size, mime));
					if (tokens[0].equals("a"))
						audio.put(url, new Asset(url, AssetFilter.AssetType.Audio, size, mime));
					if (tokens[0].equals("d"))
						directories.put(url, new Asset(url, AssetFilter.AssetType.Directory, size, mime));
				}

				Log.info("Loaded information about assets: [text files (" +texts.size() + ")], " +
						"[images (" + images.size() + ")], [binaries (" +binaries.size()+ ")], " +
						"[audio (" +audio.size()+ ")], [directories (" + directories.size() +")]" );

				callback.ready(true);
			}
		});
	}

	public InputStream read (String url) {
		throw new ParallaxRuntimeException("Not supported in GWT");
//		if (texts.containsKey(url)) {
//			try {
//				return new ByteArrayInputStream(texts.get(url).getBytes("UTF-8"));
//			} catch (UnsupportedEncodingException e) {
//				return null;
//			}
//		}
//		if (images.containsKey(url)) {
//			return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
//		}
//		if (binaries.containsKey(url)) {
//			return binaries.get(url).read();
//		}
//		if (audio.containsKey(url)) {
//			return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
//		}
	}

	public boolean contains (String url) {
		return texts.containsKey(url) || images.containsKey(url) || binaries.containsKey(url) || audio.containsKey(url) || directories.containsKey(url);
	}

	public boolean isText (String url) {
		return texts.containsKey(url);
	}

	public boolean isImage (String url) {
		return images.containsKey(url);
	}

	public boolean isBinary (String url) {
		return binaries.containsKey(url);
	}

	public boolean isAudio (String url) {
		return audio.containsKey(url);
	}

	public boolean isDirectory (String url) {
		return directories.containsKey(url);
	}

	private boolean isChild(String path, String url) {
		return path.startsWith(url) && (path.indexOf('/', url.length() + 1) < 0);
	}

	public FileHandle[] list (String url) {
		List<FileHandle> files = new ArrayList<FileHandle>();
		for (String path : texts.keySet()) {
			if (isChild(path, url)) {
				files.add(new GwtFileHandle(this, path));
			}
		}
		FileHandle[] list = new FileHandle[files.size()];
		System.arraycopy(files.toArray(), 0, list, 0, list.length);
		return list;
	}

	public FileHandle[] list (String url, java.io.FileFilter filter) {
		List<FileHandle> files = new ArrayList<FileHandle>();
		for (String path : texts.keySet()) {
			if (isChild(path, url) && filter.accept(new File(path))) {
				files.add(new GwtFileHandle(this, path));
			}
		}
		FileHandle[] list = new FileHandle[files.size()];
		System.arraycopy(files.toArray(), 0, list, 0, list.length);
		return list;
	}

	public FileHandle[] list (String url, FilenameFilter filter) {
		List<FileHandle> files = new ArrayList<FileHandle>();
		for (String path : texts.keySet()) {
			if (isChild(path, url) && filter.accept(new File(url), path.substring(url.length() + 1))) {
				files.add(new GwtFileHandle(this, path));
			}
		}
		FileHandle[] list = new FileHandle[files.size()];
		System.arraycopy(files.toArray(), 0, list, 0, list.length);
		return list;
	}

	public FileHandle[] list (String url, String suffix) {
		List<FileHandle> files = new ArrayList<FileHandle>();
		for (String path : texts.keySet()) {
			if (isChild(path, url) && path.endsWith(suffix)) {
				files.add(new GwtFileHandle(this, path));
			}
		}
		FileHandle[] list = new FileHandle[files.size()];
		System.arraycopy(files.toArray(), 0, list, 0, list.length);
		return list;
	}

	public Asset get (String url) {
		if (texts.containsKey(url)) {
			return texts.get(url);
		}
		else if (images.containsKey(url)) {
			return images.get(url);
		}
		else if (binaries.containsKey(url)) {
			return binaries.get(url);
		}
		else if (audio.containsKey(url)) {
			return audio.get(url);
		}

		return null;
	}

	public long length (String url) {
		Asset file = this.get(url);
		if(file != null)
			return file.size;

		return 0;
	}

}
