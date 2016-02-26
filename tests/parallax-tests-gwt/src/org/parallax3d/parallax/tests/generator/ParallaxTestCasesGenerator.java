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
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.ParallaxTest;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParallaxTestCasesGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

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
        factory.addImport(ArrayList.class.getName());
        factory.addImport(Arrays.class.getName());
        factory.addImport(List.class.getName());
        factory.addImport(FastMap.class.getName());

        System.out.println(" Start GWT test generation ");

        List<JClassType> classes = new ArrayList<>();
        JClassType testClass = oracle.findType(ParallaxTest.class.getName());
        for (JPackage pack : oracle.getPackages())
            for (JClassType classtype : pack.getTypes())
                if(classtype != testClass && testClass.isAssignableFrom(classtype))
                    classes.add(classtype);

        System.out.printf("   %s test cases found%n", classes.size());

        SourceWriter sw = factory.createSourceWriter(context, pw);
        
        sw.println("static final FastMap<List<ParallaxTest>> byGroup = new FastMap<>();");
        sw.println("static final FastMap<ParallaxTest> byToken = new FastMap<>();");

        sw.println("static final List<ParallaxTest> all = new ArrayList<ParallaxTest>() {{");

        for(JClassType cls: classes)
            sw.println("add( new %s() );", cls.getQualifiedSourceName());

        sw.println("}};");

        sw.println("static {");
            sw.println("for (ParallaxTest test : all) {");
                sw.println("byToken.put(test.getTestName(), test);");
                sw.println("String group = test.getTestGroupName();");
                sw.println("if (byGroup.containsKey(group)) byGroup.get(group).add(test);");
                sw.println("else { List<ParallaxTest> tests = new ArrayList<>(); tests.add(test); byGroup.put(group, tests);}");
            sw.println("}");
        sw.println("}");

        // get method
        sw.println("public FastMap<List<ParallaxTest>> getAllTests() { return byGroup; }"); // method
        sw.println("public ParallaxTest getContentWidgetForToken(String token) { return byToken.get(token); }"); // method

        sw.commit(logger);
        return factory.getCreatedClassName();
    }

}
