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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class JsTestFile extends JsFile {

    private static final String JAVA_PKG = "org.parallax3d.parallax";
    private static final String TEST_DIR = "unit";
    private static final String TEST_NAME_NODE_ID = "module";
    private static final String TEST_CASE_NODE_ID = "test";

    JsTestFile(Path path) throws Exception {
        super(path);
    }

    public boolean isTest() {
        return getTestId() != null;
    }

    public String getProposalJavaPackageName()
    {
        String path = getTestRelativePath();

        return JAVA_PKG + "." + path.replaceAll("\\" + File.separator, ".");
    }

    public String getTestRelativePath() {

        List<String> dirs = Arrays.asList( getDirectory().toString().split(Pattern.quote(File.separator)) );

        int idx = dirs.lastIndexOf(TEST_DIR);

        return String.join(File.separator, dirs.subList(idx + 1, dirs.size()));
    }

    public String getTestId() {

        for(Statement st: mainNode.getBody().getStatements()) {

            List<Expression> args = findFunctionArgsById(st, TEST_NAME_NODE_ID);
            if(args == null) continue;

            return ((LiteralNode<String>)args.get(0)).getString();
        }

        return null;
    }

    public List<String> getTestCaseNames() {

        List<String> names = new ArrayList<>();

        for(Statement st: mainNode.getBody().getStatements()) {

            List<Expression> args = findFunctionArgsById(st, TEST_CASE_NODE_ID);
            if(args == null) continue;

            String arg = ((LiteralNode<String>)args.get(0)).getString();

            names.add( normalizeTestCaseName( arg ) );
        }

        return names;
    }

    public String generateJavaTest(File dir) throws FileNotFoundException {
        String threeName = getTestId();
        String clsName = threeName + "Test";

        dir = new File(dir, getTestRelativePath());
        dir.mkdirs();

        File file = new File(dir, clsName + ".java");

        PrintWriter out = new PrintWriter(file);

        out.println(Helpers.getCopyHeader());
        out.println();
        out.println("package " + getProposalJavaPackageName() + ";");

        out.println();
        out.println("import org.junit.Test;");
        out.println("import org.parallax3d.parallax.system.ThreejsTest;");
        out.println("import static org.junit.Assert.*;");
        out.println();

        out.println("@ThreejsTest(\"" + threeName + "\")");
        out.println("public class " + clsName + " {");

        for(Statement st: mainNode.getBody().getStatements()) {

            List<Expression> args = findFunctionArgsById(st, TEST_CASE_NODE_ID);
            if(args == null) continue;

            String testCaseName = normalizeTestCaseName(((LiteralNode<String>)args.get(0)).getString());

            String body = normalizeBody(((FunctionNode)args.get(1)).getBody().toString(false));

            out.println("    @Test");
            out.println("    public void " + testCaseName + "() {");
            out.println(body);
            out.println("    }");
            out.println();
        }

        out.println("}");

        out.close();

        return clsName;
    }

    private String normalizeTestCaseName(String originalTestCaseName) {

        String name = "test";
        name += originalTestCaseName.substring(0, 1).toUpperCase() + originalTestCaseName.substring(1);

        return name.replaceAll("[^a-zA-Z0-9]+", "_").replaceAll("_$", "");

    }

    private String normalizeBody(String body) {
        body = body.replaceAll("THREE.", "");

        // === | !==
        body = body.replaceAll("([!=])==", "$1=");

        body = body.replaceAll(";", ";\n");

        // ok()
        body = body.replaceAll("ok\\s*\\(\\s*(.+)\\s*,\\s*[^,]+\\s*\\);", "assertTrue( $1 );");

        // equal()
        body = body.replaceAll("equal\\s*\\(", "assertEquals(");

        // null a = new Vector3
        body = body.replaceAll("null\\s*(\\w+)\\s*=\\s*new\\s+([^(]+)", "$2 $1 = new $2");


        return body;
    }

    private List<Expression> findFunctionArgsById(Statement st, String id)
    {
        if(st.getClass() != ExpressionStatement.class) return null;

        Expression ex = ((ExpressionStatement)st).getExpression();

        if(ex.getType() != Type.OBJECT || ex.getClass() != CallNode.class ) return null;

        CallNode node = (CallNode) ((ExpressionStatement)st).getExpression();

        if(((IdentNode)node.getFunction()).getName().equals( id ))
            return node.getArgs();

        return null;
    }

}
