/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.parallax3d.parallax.platforms.gwt.preloader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import org.parallax3d.parallax.Files;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.platforms.gwt.GwtFileHandle;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Preloader {
	
	public interface PreloaderCallback {
		
		public void update(PreloaderState state);
		
		public void error(String file);
		
	}

	public FastMap<Void> directories = new FastMap<Void>();
	public FastMap<ImageElement> images = new FastMap<ImageElement>();
	public FastMap<Void> audio = new FastMap<Void>();
	public FastMap<String> texts = new FastMap<String>();
	public FastMap<Blob> binaries = new FastMap<Blob>();

	public static class Asset {
		public Asset (String url, AssetFilter.AssetType type, long size, String mimeType) {
			this.url = url;
			this.type = type;
			this.size = size;
			this.mimeType = mimeType;
		}

		public boolean succeed;
		public boolean failed;
		public long loaded;
		public final String url;
		public final AssetFilter.AssetType type;
		public final long size;
		public final String mimeType;
	}
	
	public static class PreloaderState {
		
		public PreloaderState(List<Asset> assets) {
			this.assets = assets;
		}
		
		public long getDownloadedSize() {
			long size = 0;
			for (int i = 0; i < assets.size(); i++) {
				Asset asset = assets.get(i);
				size += (asset.succeed || asset.failed) ? asset.size : Math.min(asset.size, asset.loaded);
			}
			return size;
		}
		
		public long getTotalSize() {
			long size = 0;
			for (int i = 0; i < assets.size(); i++) {
				Asset asset = assets.get(i);
				size += asset.size;
			}
			return size;
		}
		
		public float getProgress() {
			long total = getTotalSize();
			return total == 0 ? 1 : (getDownloadedSize() / (float) total);
		}
		
		public boolean hasEnded() {
			return getDownloadedSize() == getTotalSize();
		}
		
		public final List<Asset> assets;
		
	}

	public final String baseUrl;

	
	public Preloader (String newBaseURL) {
		
		baseUrl = newBaseURL;
	
		// trigger copying of assets and creation of assets.txt
		GWT.create(PreloaderBundle.class);
	}

	public void preload (final String assetFileUrl, final PreloaderCallback callback) {
		final AssetDownloader loader = new AssetDownloader();
		
		loader.loadText(baseUrl + assetFileUrl, new AssetDownloader.AssetLoaderListener<String>() {
			@Override
			public void onProgress (double amount) {
			}
			@Override
			public void onFailure () {
				callback.error(assetFileUrl);
			}
			@Override
			public void onSuccess (String result) {
				String[] lines = result.split("\n");
				List<Asset> assets = new ArrayList<Asset>(lines.length);
				for (String line : lines) {
					String[] tokens = line.split(":");
					if (tokens.length != 4) {
						throw new ParallaxRuntimeException("Invalid assets description file.");
					}
					AssetFilter.AssetType type = AssetFilter.AssetType.Text;
					if (tokens[0].equals("i")) type = AssetFilter.AssetType.Image;
					if (tokens[0].equals("b")) type = AssetFilter.AssetType.Binary;
					if (tokens[0].equals("a")) type = AssetFilter.AssetType.Audio;
					if (tokens[0].equals("d")) type = AssetFilter.AssetType.Directory;
					long size = Long.parseLong(tokens[2]);
					if (type == AssetFilter.AssetType.Audio && !loader.isUseBrowserCache()) {
						size = 0;
					}
					assets.add(new Asset(tokens[1].trim(), type, size, tokens[3]));
				}
				final PreloaderState state = new PreloaderState(assets);
				for (int i = 0; i < assets.size(); i++) {
					final Asset asset = assets.get(i);
					
					if (contains(asset.url)) {
						asset.loaded = asset.size;
						asset.succeed = true;
						continue;
					}
					
					loader.load(baseUrl + asset.url, asset.type, asset.mimeType, new AssetDownloader.AssetLoaderListener<Object>() {
						@Override
						public void onProgress (double amount) {
							asset.loaded = (long) amount;
							callback.update(state);
						}
						@Override
						public void onFailure () {
							asset.failed = true;
							callback.error(asset.url);
							callback.update(state);
						}
						@Override
						public void onSuccess (Object result) {
							switch (asset.type) {
							case Text:
								texts.put(asset.url, (String) result);					
								break;
							case Image:
								images.put(asset.url, (ImageElement) result);
								break;
							case Binary:
								binaries.put(asset.url, (Blob) result);
								break;
							case Audio:
								audio.put(asset.url, null);
								break;
							case Directory:
								directories.put(asset.url, null);
								break;
							}
							asset.succeed = true;
							callback.update(state);
						}
					});
				}
				callback.update(state);
			}
		});
	}
	
	public InputStream read (String url) {
		if (texts.containsKey(url)) {
			try {
				return new ByteArrayInputStream(texts.get(url).getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
		if (images.containsKey(url)) {
			return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
		}
		if (binaries.containsKey(url)) {
			return binaries.get(url).read();
		}
		if (audio.containsKey(url)) {
			return new ByteArrayInputStream(new byte[1]); // FIXME, sensible?
		}
		return null;
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
				files.add(new GwtFileHandle(this, path, Files.FileType.Internal));
			}
		}
		FileHandle[] list = new FileHandle[files.size()];
		System.arraycopy(files.toArray(), 0, list, 0, list.length);
		return list;
	}

	public FileHandle[] list (String url, FileFilter filter) {
		List<FileHandle> files = new ArrayList<FileHandle>();
		for (String path : texts.keySet()) {
			if (isChild(path, url) && filter.accept(new File(path))) {
				files.add(new GwtFileHandle(this, path, Files.FileType.Internal));
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
				files.add(new GwtFileHandle(this, path, Files.FileType.Internal));
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
				files.add(new GwtFileHandle(this, path, Files.FileType.Internal));
			}
		}
		FileHandle[] list = new FileHandle[files.size()];
		System.arraycopy(files.toArray(), 0, list, 0, list.length);
		return list;
	}

	public long length (String url) {
		if (texts.containsKey(url)) {
			try {
				return texts.get(url).getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				return texts.get(url).getBytes().length;
			}
		}
		if (images.containsKey(url)) {
			return 1; // FIXME, sensible?
		}
		if (binaries.containsKey(url)) {
			return binaries.get(url).length();
		}
		if (audio.containsKey(url)) {
			return 1; // FIXME sensible?
		}
		return 0;
	}

}
