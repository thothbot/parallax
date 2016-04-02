/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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
package org.parallax3d.parallax.utils;

import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.*;
import jdk.nashorn.internal.runtime.options.Options;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class JsFile {

    protected Path path;
    protected Context context;

    protected FunctionNode mainNode;

    JsFile(Path path) throws Exception {
        this.path = path;

        Options options = new Options("nashorn");
        options.set("anon.functions", true);
        options.set("parse.only", true);
        options.set("scripting", true);

        ErrorManager errors = new ErrorManager();
        context = new Context(options, errors, Thread.currentThread().getContextClassLoader());
        Context.setGlobal(context.createGlobal());

        parse();
    }

    private void parse() throws Exception {

            String code = getCode();
            String name = "<unknown>";

            Parser parser = new Parser(context.getEnv(), Source.sourceFor(name, code), new Context.ThrowErrorManager(), context.getEnv()._strict, context.getLogger(Parser.class));
            mainNode = parser.parse();
    }

    private String getCode() throws IOException
    {
        return String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
    }

}
