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

package org.parallax3d.parallax.platforms.gwt.generator.assets;

import org.parallax3d.parallax.system.FastMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MIME {
    static final FastMap<String> extension = new FastMap() {{
		put("java", "text/x-java-source,java");
		put("js", "application/javascript");
		put("json", "application/json");
		put("dae", "model/vnd.collada+xml");
		put("stl", "application/sla");
		put("xml", "application/xml");
    }};

	public static String get(FileWrapper file) {

		String mime = null;
		try {
			mime = Files.probeContentType(Paths.get(file.path()));
		} catch (IOException e) {
			e.printStackTrace();
		};

		if(mime == null)
		{
			String ext = file.ext();
			if(extension.containsKey(ext))
			{
				mime = extension.get(ext);
			}
		}

		return mime;
	}
}
