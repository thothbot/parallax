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

package org.parallax3d.parallax.loaders;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.files.FileListener;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;

public abstract class Loader
{
	FileHandle file;

	protected Loader(final String url)
	{
		Parallax.asset(url, new FileListener<FileHandle>() {
			@Override
			public void onProgress(double amount) {

			}

			@Override
			public void onFailure() {
				Log.error("An error occurred while loading file: " + url);
			}

			@Override
			public void onSuccess(FileHandle result) {
				Log.info("Loaded file: " + url);
				Loader.this.file = result;

				parse(result);
				onReady();
			}
		});

	}

	protected abstract void parse(FileHandle result);
	protected abstract void onReady();

}
