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

package org.parallax3d.parallax.platforms.gwt.generator;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.dev.util.Util;
import com.google.gwt.resources.ext.*;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;
import org.parallax3d.parallax.system.SourceTextResource;

import java.net.URL;

public class SourceTextResourceGenerator extends AbstractResourceGenerator  implements SupportsGeneratorResultCaching {
    private static final int MAX_STRING_CHUNK = 16383;

    public SourceTextResourceGenerator() {
    }

    public String createAssignment(TreeLogger logger, ResourceContext context, JMethod method) throws UnableToCompleteException {
        URL[] resources = ResourceGeneratorUtil.findResources(logger, context, method);
        if(resources.length != 1) {
            logger.log(TreeLogger.ERROR, "Exactly one resource must be specified", null);
            throw new UnableToCompleteException();
        } else {
            URL resource = resources[0];
            StringSourceWriter sw = new StringSourceWriter();
            sw.println("new " + SourceTextResource.class.getName() + "() {");
            sw.indent();
            if(!AbstractResourceGenerator.STRIP_COMMENTS) {
                sw.println("// " + resource.toExternalForm());
            }

            sw.println("public String getText() {");
            sw.indent();
            String toWrite = Util.readURLAsString(resource);
            if(toWrite.length() > 16383) {
                this.writeLongString(sw, toWrite);
            } else {
                sw.println("return \"" + Generator.escape(toWrite) + "\";");
            }

            sw.outdent();
            sw.println("}");
            sw.println("public String getName() {");
            sw.indent();
            sw.println("return \"" + method.getName() + "\";");
            sw.outdent();
            sw.println("}");
            sw.outdent();
            sw.println("}");
            return sw.toString();
        }
    }

    private void writeLongString(SourceWriter sw, String toWrite) {
        sw.println("StringBuilder builder = new StringBuilder();");
        int offset = 0;

        int subLength;
        for(int length = toWrite.length(); offset < length - 1; offset += subLength) {
            subLength = Math.min(16383, length - offset);
            sw.print("builder.append(\"");
            sw.print(Generator.escape(toWrite.substring(offset, offset + subLength)));
            sw.println("\");");
        }

        sw.println("return builder.toString();");
    }
}
