/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * This file based on libgdx sources
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

package org.parallax3d.parallax.platforms.gwt.generator;

import com.google.gwt.core.ext.*;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import org.parallax3d.parallax.platforms.gwt.generator.assets.FileWrapper;
import org.parallax3d.parallax.platforms.gwt.system.assets.Asset;
import org.parallax3d.parallax.platforms.gwt.system.assets.AssetDirectory;
import org.parallax3d.parallax.platforms.gwt.system.assets.AssetFile;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ParallaxRuntimeException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AssetsGenerator extends Generator {

	List<FileWrapper> files;
	GeneratorContext context;
	String assetOutputPath;

	@Override
	public String generate (TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException
	{

		this.context = context;
		this.files = new ArrayList<>();
		this.assetOutputPath = getOutputPath();

		TypeOracle oracle = context.getTypeOracle();
		JClassType toGenerate = oracle.findType(typeName).isInterface();
		if (toGenerate == null) {
			logger.log(TreeLogger.ERROR, typeName + " is not an interface type");
			throw new UnableToCompleteException();
		}

		String packageName = toGenerate.getPackage().getName();
		String simpleSourceName = toGenerate.getName().replace('.', '_') + "Impl";
		PrintWriter pw = context.tryCreate(logger, packageName, simpleSourceName);
		if (pw == null) {
			return packageName + "." + simpleSourceName;
		}

		ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName, simpleSourceName);

		factory.addImplementedInterface(typeName);
		factory.addImport(AssetDirectory.class.getName());
		factory.addImport(AssetFile.class.getName());
		factory.addImport(Asset.class.getName());
		factory.addImport(List.class.getName());
		factory.addImport(FastMap.class.getName());

		System.out.println(" Start assets generation");

		// ----

		System.out.println(new File(".").getAbsolutePath());
		String assetPath = getAssetPath();

		FileWrapper source = new FileWrapper(assetPath);
		if (!source.exists()) {
			source = new FileWrapper("../" + assetPath);
			if (!source.exists())
				throw new RuntimeException("assets path '" + assetPath
					+ "' does not exist. Check your parallax.assetpath property in your GWT project's module gwt.xml file");
		}

		if (!source.isDirectory())
			throw new RuntimeException("assets path '" + assetPath
				+ "' is not a directory. Check your parallax.assetpath property in your GWT project's module gwt.xml file");

		System.out.println("Copying resources from " + assetPath + " to " + assetOutputPath );
		System.out.println(source.file().getAbsolutePath());
		FileWrapper target = new FileWrapper("assets/"); // this should always be the war/ directory of the GWT project.
		System.out.println(target.file().getAbsolutePath());

		if (!target.file().getAbsolutePath().replace("\\", "/").endsWith(assetOutputPath + "assets"))
			target = new FileWrapper(assetOutputPath + "assets/");

		if (target.exists())
			if (!target.deleteDirectory()) throw new RuntimeException("Couldn't clean target path '" + target + "'");

		copyDirectory(source, target);

		// ----

		System.out.printf("   %s assets have been generated%n", files.size());
		SourceWriter sw = factory.createSourceWriter(context, pw);

		sw.println("private static final FastMap<SourceBundle> MAP = new FastMap<SourceBundle>(){{");

		for(FileWrapper file: files)
		{
			String path = getPath( file );
			if(file.isDirectory())
			{
				sw.println("put(\"%s\", new AssetDirectory(\"%s\") );", path, path);
			}
			else
			{
				String mime = null;
				try {
					mime = Files.probeContentType(Paths.get(file.path()));
				} catch (IOException e) {
					e.printStackTrace();
				};

				sw.println("put(\"%s\", new AssetFile(\"%s\", %d, \"%s\");", path, path, file.length(), mime);
			}
		}

		sw.println("}};");

		// get method
		sw.println("public List<Asset> getAll() {");
			sw.println("return MAP.values();");
		sw.println("}");

		sw.println("public boolean contains(String path) {");
		sw.println("return MAP.containsKey(path);");
		sw.println("}");

		sw.println("public Asset get(String path) {");
			sw.println("if (MAP.containsKey(path)) {");
				sw.println("return MAP.get(path);");
			sw.println("}");
			sw.println("return null;");
		sw.println("}");

		sw.commit(logger);
		return factory.getCreatedClassName();
	}

	private String getPath(FileWrapper file)
	{
		String path = file.path().replace('\\', '/').replace(assetOutputPath, "").replaceFirst("assets/", "");
		if (path.startsWith("/")) path = path.substring(1);

		return path;
	}

	private void copyFile (FileWrapper source, FileWrapper dest)
	{
		try {
			files.add( dest );
			dest.write(source.read(), false);
		} catch (Exception ex) {
			throw new ParallaxRuntimeException("Error copying source file: " + source + "\n" //
				+ "To destination: " + dest, ex);
		}
	}

	private void copyDirectory (FileWrapper sourceDir, FileWrapper destDir)
	{
		files.add( destDir );
		destDir.mkdirs();
		FileWrapper[] files = sourceDir.list();

		for (FileWrapper srcFile : files) {
			FileWrapper destFile = destDir.child(srcFile.name());
			if (srcFile.isDirectory())
				copyDirectory(srcFile, destFile);
			else
				copyFile(srcFile, destFile);
		}
	}

	private String getAssetPath () {
		ConfigurationProperty assetPathProperty = null;
		try {
			assetPathProperty = context.getPropertyOracle().getConfigurationProperty("parallax.assetpath");
		} catch (BadPropertyValueException e) {
			throw new RuntimeException(
				"No parallax.assetpath defined. Add <set-configuration-property name=\"parallax.assetpath\" value=\"relative/path/to/assets/\"/> to your GWT projects gwt.xml file");
		}
		if (assetPathProperty.getValues().size() == 0) {
			throw new RuntimeException(
				"No parallax.assetpath defined. Add <set-configuration-property name=\"parallax.assetpath\" value=\"relative/path/to/assets/\"/> to your GWT projects gwt.xml file");
		}
		String paths = assetPathProperty.getValues().get(0);
		if(paths == null) {
			throw new RuntimeException(
				"No parallax.assetpath defined. Add <set-configuration-property name=\"parallax.assetpath\" value=\"relative/path/to/assets/\"/> to your GWT projects gwt.xml file");
		} else {
			ArrayList<String> existingPaths = new ArrayList<String>();
			String[] tokens = paths.split(",");
			for(String token: tokens) {
				System.out.println(token);
				if(new FileWrapper(token).exists() || new FileWrapper("../" + token).exists()) {
					return token;
				}
			}
			throw new RuntimeException(
				"No valid parallax.assetpath defined. Fix <set-configuration-property name=\"parallax.assetpath\" value=\"relative/path/to/assets/\"/> in your GWT projects gwt.xml file");
		}
	}
	
	private String getOutputPath()
	{
		assetOutputPath = "war/";

		ConfigurationProperty assetPathProperty;
		try {
			assetPathProperty = context.getPropertyOracle().getConfigurationProperty("parallax.assetoutputpath");
		} catch (BadPropertyValueException e) {
			return assetOutputPath;
		}
		if (assetPathProperty.getValues().size() == 0) {
			return assetOutputPath;
		}
		String paths = assetPathProperty.getValues().get(0);
		if(paths == null)
		{
			return assetOutputPath;
		}
		else
		{
			String[] tokens = paths.split(",");
			String path = null;
			for(String token: tokens) {
				if (new FileWrapper(token).exists() || new FileWrapper(token).mkdirs()) {
					path = token;
				}
			}
			if (path != null && !path.endsWith("/")){
				path += "/";
			}
			return path;
		}
	}
}
