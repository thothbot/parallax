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

import java.nio.file.Path;

public class JsTestFile extends JsFile {

    private static final String TEST_NAME_NODE_ID = "module";

    JsTestFile(Path path) throws Exception {
        super(path);
    }

    public boolean isTest() {
        return getTestId() != null;
    }

    public String getTestId() {

        for(Statement st: mainNode.getBody().getStatements()) {

            if(st.getClass() != ExpressionStatement.class) continue;

            Expression ex = ((ExpressionStatement)st).getExpression();

            if(ex.getType() != Type.OBJECT || ex.getClass() != CallNode.class ) continue;

            CallNode node = (CallNode) ((ExpressionStatement)st).getExpression();

            if(((IdentNode)node.getFunction()).getName().equals(TEST_NAME_NODE_ID))
                return ((LiteralNode<String>)node.getArgs().get(0)).getString();

        }

        return null;
    }
}
