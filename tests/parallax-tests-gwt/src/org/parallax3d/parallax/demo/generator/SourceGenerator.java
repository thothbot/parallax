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

package org.parallax3d.parallax.demo.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.parallax3d.parallax.demo.client.ContentWidget;
import org.parallax3d.parallax.demo.client.DemoAnnotations;
import org.parallax3d.parallax.demo.resources.DemoResources;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;

/**
 * Generate the source code used in Demo examples.
 */
public class SourceGenerator extends Generator
{
	/**
	 * The class loader used to get resources.
	 */
	private ClassLoader classLoader = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	/**
	 * The {@link TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException
	{
		this.logger = logger;
		this.context = context;
		this.classLoader = Thread.currentThread().getContextClassLoader();

		// Only generate files on the first permutation
		if (!isFirstPass())
			return null;

		// Get the Showcase ContentWidget subtypes to examine
		JClassType cwType = null;
		try 
		{
			cwType = context.getTypeOracle().getType(ContentWidget.class.getName());
		} 
		catch (NotFoundException e) 
		{
			logger.log(TreeLogger.ERROR, "Cannot find ContentWidget class", e);
			throw new UnableToCompleteException();
		}
		JClassType[] types = cwType.getSubtypes();

		// Generate the source and raw source files
		for (JClassType type : types) 
			generateSourceFiles(type);

		return null;
	}

	/**
	 * Set the full contents of a resource in the public directory.
	 * 
	 * @param partialPath
	 *            the path to the file relative to the public directory
	 * @param contents
	 *            the file contents
	 */
	private void createPublicResource(String partialPath, String contents)
			throws UnableToCompleteException
	{
		try 
		{
			OutputStream outStream = context.tryCreateResource(logger, partialPath);
			if (outStream == null) 
			{
				String message = "Attempting to generate duplicate public resource: " + partialPath
						+ ".\nAll generated source files must have unique names.";
				logger.log(TreeLogger.ERROR, message);
				throw new UnableToCompleteException();
			}

			outStream.write(contents.getBytes());
			context.commitResource(logger, outStream);
		} 
		catch (IOException e) 
		{
			logger.log(TreeLogger.ERROR, "Error writing file: " + partialPath, e);
			throw new UnableToCompleteException();
		}
	}

	/**
	 * Generate the formatted source code for a {@link ContentWidget}.
	 * 
	 * @param type
	 *            the {@link ContentWidget} subclass
	 */
	private void generateSourceFiles(JClassType type) throws UnableToCompleteException
	{
		// Get the file contents
		String filename = type.getQualifiedSourceName().replace('.', '/') + ".java";
		String fileContents = getResourceContents(filename);

		// Get each data code block
		String formattedSource = "";
		String sourceTag = "@" + DemoAnnotations.DemoSource.class.getSimpleName();
		int srcTagIndex = fileContents.indexOf(sourceTag);

		while (srcTagIndex >= 0) 
		{
			// Get the boundaries of a SRC tag
			int beginIndex = fileContents.lastIndexOf("/*", srcTagIndex) - 2;
			int beginTagIndex = fileContents.lastIndexOf("\n", srcTagIndex) + 1;
			int endTagIndex = fileContents.indexOf("\n", srcTagIndex) + 1;
			// TDO: Fix this (\n\t):
			int endIndex = fileContents.indexOf("\n\t}", beginIndex) + 4;

			// Add to the formatted source
			String srcCode = fileContents.substring(beginIndex, beginTagIndex)
					+ fileContents.substring(endTagIndex, endIndex);
			formattedSource += srcCode + "\n\n";

			// Get the next tag
			srcTagIndex = fileContents.indexOf(sourceTag, endIndex + 1);
		}

		// Make the source pretty
		formattedSource = formattedSource.replace("\n\t", "\n");
		formattedSource = formattedSource.replace("\t", "   ");
		formattedSource = formattedSource.replace("<", "&lt;");
		formattedSource = formattedSource.replace(">", "&gt;");
		formattedSource = formattedSource.replace("* \n   */\n", "*/\n");
		formattedSource = "<pre><code class='hljs java'>" + formattedSource + "</code></pre>";

		// Save the source code to a file
		String dstPath = DemoResources.DST_SOURCE_EXAMPLE + type.getSimpleSourceName()
				+ ".html";
		createPublicResource(dstPath, formattedSource);
	}

	/**
	 * Get the full contents of a resource.
	 * 
	 * @param path
	 *            the path to the resource
	 * @return the contents of the resource
	 */
	private String getResourceContents(String path) throws UnableToCompleteException
	{
		InputStream in = classLoader.getResourceAsStream(path);
		if (in == null) 
		{
			logger.log(TreeLogger.ERROR, "Resource not found: " + path);
			throw new UnableToCompleteException();
		}

		StringBuffer fileContentsBuf = new StringBuffer();
		BufferedReader br = null;
		try 
		{
			br = new BufferedReader(new InputStreamReader(in));
			String temp;
			while ((temp = br.readLine()) != null)
				fileContentsBuf.append(temp).append('\n');

		} 
		catch (IOException e) 
		{
			logger.log(TreeLogger.ERROR, "Cannot read resource", e);
			throw new UnableToCompleteException();
		} 
		finally 
		{
			if (br != null) 
			{
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		// Return the file contents as a string
		return fileContentsBuf.toString();
	}

	/**
	 * Ensure that we only generate files once by creating a placeholder file,
	 * then looking for it on subsequent generates.
	 * 
	 * @return true if this is the first pass, false if not
	 */
	private boolean isFirstPass()
	{
		String placeholder = "generated.sources.tmp";
		try 
		{
			OutputStream outStream = context.tryCreateResource(logger, placeholder);
			if (outStream == null)
				return false;
			else
				context.commitResource(logger, outStream);

		} 
		catch (UnableToCompleteException e) 
		{
			logger.log(TreeLogger.ERROR, "Unable to generate", e);
			return false;
		}
		return true;
	}
}
