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

import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsTestFile extends JsFile {

    private static final String TEST_NAME_NODE_ID = "module";
    private static final String TEST_CASE_NODE_ID = "test";

    JsTestFile(Path path) throws Exception {
        super(path);
    }

    public boolean isTest() {
        return getTestId() != null;
    }

    public String getTestId() {

        for(Statement st: mainNode.getBody().getStatements()) {

            String arg = findFunctionFirstArgById(st, TEST_NAME_NODE_ID);
            if(arg == null) continue;

            return arg ;
        }

        return null;
    }

    public List<String> getTestCaseNames() {

        List<String> names = new ArrayList<>();

        for(Statement st: mainNode.getBody().getStatements()) {

            String arg = findFunctionFirstArgById(st, TEST_CASE_NODE_ID);
            if(arg == null) continue;

            names.add( normalizeTestCaseName( arg ) );
        }

        return names;
    }

    public String generateJavaTest(File dir) throws FileNotFoundException {

        String clsName = getTestId();

        File file = new File(dir, clsName + ".java");

        PrintWriter out = new PrintWriter(file);

        out.println(Helpers.getCopyHeader());
        out.println();
        out.println("package org.parallax3d.parallax.math;");

        out.println();
        out.println("import org.junit.Test;");
        out.println("import org.parallax3d.parallax.system.ThreejsTest;");
        out.println("import static org.junit.Assert.*;");
        out.println();

        out.println("@ThreejsTest(\"" + clsName + "\")");
        out.println("public class " + clsName);
        out.println("{");
        out.println("}");

        out.close();

        return clsName;
    }

    private String normalizeTestCaseName(String originalTestCaseName) {

        String name = "test";
        name += originalTestCaseName.substring(0, 1).toUpperCase() + originalTestCaseName.substring(1);

        return name.replaceAll("/", "_");

    }

    private String findFunctionFirstArgById(Statement st, String id)
    {
        if(st.getClass() != ExpressionStatement.class) return null;

        Expression ex = ((ExpressionStatement)st).getExpression();

        if(ex.getType() != Type.OBJECT || ex.getClass() != CallNode.class ) return null;

        CallNode node = (CallNode) ((ExpressionStatement)st).getExpression();

        if(((IdentNode)node.getFunction()).getName().equals( id ))
            return ((LiteralNode<String>)node.getArgs().get(0)).getString();

        return null;
    }
}
