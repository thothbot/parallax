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

package org.parallax3d.parallax.tests.generator;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import org.parallax3d.parallax.tests.client.ContentWidget;
import org.parallax3d.parallax.tests.resources.DemoResources;

import java.io.*;

public class FacebookGenerator extends Generator 
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
		RegExp pattern = RegExp.compile("super\\ *\\(\"([^\"|^\\\"]+)\",\\ *\"([^\"|^\\\"]+)\"\\)");
		
		// Get the file contents
		String filename = type.getQualifiedSourceName().replace('.', '/') + ".java";
		String fileContents = getResourceContents(filename);

		String title = "", description = "";
		for (MatchResult result = pattern.exec(fileContents); result != null; result = pattern.exec(fileContents)) 
		{
		    title = result.getGroup(1);
		    description = result.getGroup(2);
			break;
		}		
		
		// Save the source code to a file
		String dstPath = DemoResources.DST_FACEBOOK + type.getSimpleSourceName()
				+ ".html";
		createPublicResource(dstPath, template(type.getSimpleSourceName(), title, description));
	}
	
	private static String removeHtmlTags(String html) {
        String regex = "(<([^>]+)>)";
        return html.replaceAll(regex, "");
	}
	
	private String template(String className, String title, String description) {
		
		String template = "---\n---\n";
		template += "{% assign description = '" + FacebookGenerator.removeHtmlTags(description) + "' %}\n";
		template += "{% capture url %}http://{{ site.host }}{{ page.url | remove: '/tests/fb/" + className + ".html'}}{% endcapture %}\n";
		template += "{% capture image %}{{ url }}/static/thumbs/" + className + ".jpg{% endcapture %}\n";

		template += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n";
		template += "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:og=\"http://ogp.me/ns#\" xmlns:fb=\"https://www.facebook.com/2008/fbml\">\n";
		template += "<head>\n";

		template += "\t<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n";
		template += "\t<meta property=\"og:locale\" content=\"en_US\">\n";
		template += "\t<meta property=\"og:type\" content=\"website\">\n";
		template += "\t<meta property=\"fb:app_id\" content=\"1606041236283596\">\n";
		template += "\t<meta property=\"og:site_name\" content=\"Parallax 3D library\">\n";
		
		template += "\t<meta property=\"og:title\" content=\"Parallax 3D library: " + title + "\">\n";
		template += "\t<meta property=\"og:description\" content=\"{{description}}\">\n";
		template += "\t<meta property=\"og:image\" content=\"{{image}}\">\n";
		template += "\t<meta property=\"og:url\" content=\"http://{{ site.host }}{{ page.url }}\">\n";
			
		template += "\t<title>Parallax 3D library: " + title + "</title>\n";

		template += "</head>\n";
		template += "<body>\n";
		template += "\t<div id=\"fb-root\"></div>\n";

		template += "\t<script>\n"
				+ " \t\twindow.fbAsyncInit = function() { FB.init({ appId : '1606041236283596', xfbml : true, version : 'v2.2'}); };\n"
				+ "\t\t(function(d, s, id) {\n"
				+ "\t\tvar js, fjs = d.getElementsByTagName(s)[0];\n"
				+ "\t\tif (d.getElementById(id)) return;\n"
				+ "\t\tjs = d.createElement(s); js.id = id;\n"
				+ "\t\tjs.src = \"//connect.facebook.net/en_US/sdk.js\";\n"
				+ "\t\tfjs.parentNode.insertBefore(js, fjs);\n"
				+ "\t\t}(document, 'script', 'facebook-jssdk'));</script>\n";

		template +="\t<div style='visibility: hidden;'><h1>" + title + "</h1>\n";
		template +="\t<img src=\"{{image}}\"/>\n";
		template +="\t<p>{{description}}</p></div>\n";
		
		template +="\t<script>window.location.replace(\"{{ url }}#!" + className + "\");</script>\n";
		
		template += "</body>\n";
		template += "</html>\n";

		return template;
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
		String placeholder = "generated.fb.tmp";
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
