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
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import org.parallax3d.parallax.tests.ParallaxTest;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ParallaxTestCasesGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

        PrintWriter pw = context.tryCreate(logger,
                "org.parallax3d.parallax.tests",
                "ParallaxTestCasesImpl");

        if (pw != null)
        {
            // write package, imports, whatever
            pw.append("package org.parallax3d.parallax.tests;");
            pw.append("import org.parallax3d.parallax.system.FastMap;");
            pw.append("import java.util.ArrayList;");
            pw.append("import java.util.Arrays;");
            pw.append("import java.util.List;");

            // iterates over all the classes to find those with EntryPointWidget annotation
            TypeOracle oracle = context.getTypeOracle();
            JPackage[] packages = oracle.getPackages();

            System.out.println(" Start GWT test generation ");

            List<JClassType> classes = new ArrayList<>();
            JClassType testClass = oracle.findType(ParallaxTest.class.getName());
            for (JPackage pack : packages)
                for (JClassType classtype : pack.getTypes())
                    if(classtype != testClass && testClass.isAssignableFrom(classtype))
                        classes.add(classtype);

            System.out.println("   " + classes.size() + " test cases found");

            // the class
            pw.append("public class ParallaxTestCasesImpl implements ParallaxTestCases {");

            pw.append("   static final FastMap<List<ParallaxTest>> byGroup = new FastMap<>();");
            pw.append("   static final FastMap<ParallaxTest> byToken = new FastMap<>();");

            pw.append("   static final List<ParallaxTest> all = new ArrayList<ParallaxTest>() {{");

            for(JClassType cls: classes)
                pw.append("      add( new " + cls.getQualifiedSourceName() + "() );");

            pw.append("   }};");

            pw.append("   static {");
            pw.append("        for (ParallaxTest test : all) {");
            pw.append("             byToken.put(test.getContentWidgetToken(), test);");
            pw.append("             String group = getProposalGroupName(test.getClass().getName());");
            pw.append("             if (byGroup.containsKey(group)) byGroup.get(group).add(test);");
            pw.append("             else { List<ParallaxTest> tests = new ArrayList<>(); tests.add(test); byGroup.put(group, tests);}");
            pw.append("        }");
            pw.append("   }");

            // get method
            pw.append("   private static String getProposalGroupName(String cls) {");
            pw.append("       String name = cls.substring(0, cls.lastIndexOf(\".\") ).replace(\"org.parallax3d.parallax.tests.cases\",\"\");");
            pw.append("       if(name.length() == 0) name = \"Unspecified\";");
            pw.append("       else name = name.substring(1);");
            pw.append("       return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);");
            pw.append("   }"); // method
            pw.append("   public FastMap<List<ParallaxTest>> getAllTests() { return byGroup; }"); // method
            pw.append("   public ParallaxTest getContentWidgetForToken(String token) { return byToken.get(token); }"); // method
            pw.append("}"); // class

            context.commit(logger, pw);
        }

        // return the name of the generated class
        return "org.parallax3d.parallax.tests.ParallaxTestCasesImpl";
    }


}
