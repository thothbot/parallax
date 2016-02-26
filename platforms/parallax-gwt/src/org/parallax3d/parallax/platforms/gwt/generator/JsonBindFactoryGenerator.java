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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import org.parallax3d.parallax.platforms.gwt.generator.jsonbind.AutoBeanFactoryGenerator;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.jsonbind.AutoBeanFactory;
import org.parallax3d.parallax.system.jsonbind.JsonBindFactory;

import java.io.PrintWriter;
import java.util.Map;

public class JsonBindFactoryGenerator extends Generator {

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
        factory.addImport(JsonBindFactory.class.getName());
        factory.addImport(AutoBeanFactory.class.getName());
        factory.addImport(GWT.class.getName());
        factory.addImport(FastMap.class.getName());

        System.out.println(" Start JSON binding generation ");

        FastMap<JClassType> genClasses = new FastMap<>();
        for (JPackage pack : oracle.getPackages())
        {
            for (JClassType classtype : pack.getTypes())
            {
                String cls = generateStaticInstance(oracle, logger, context, classtype);
                if(cls != null)
                    genClasses.put(cls, classtype);
            }
        }

        System.out.printf("   %s bindings have been generated%n", genClasses.size());

        SourceWriter sw = factory.createSourceWriter(context, pw);

        sw.println("private static final FastMap<AutoBeanFactory> MAP = new FastMap<AutoBeanFactory>(){{");

        for(Map.Entry<String,JClassType> entry: genClasses.entrySet())
            sw.println("put(\"%s\", new %s() );", entry.getValue().getQualifiedSourceName(), entry.getKey());

        sw.println("}};");

            // get method
        sw.println("public <T> T get(Class<? extends AutoBeanFactory> classLiteral) {");
            sw.println("if (MAP.containsKey(classLiteral.getCanonicalName())) {");
                sw.println("return (T) MAP.get(classLiteral.getCanonicalName());");
            sw.println("}");
            sw.println("return null;");
        sw.println("}"); // method

        sw.commit(logger);
        return factory.getCreatedClassName();
    }

    private String generateStaticInstance(TypeOracle oracle, TreeLogger logger, GeneratorContext context, JClassType classtype) throws UnableToCompleteException {
        if(classtype.isInterface() != null && checkImplementedInterface(oracle, classtype.getImplementedInterfaces()))
        {
            String fullName = classtype.getQualifiedSourceName();
            AutoBeanFactoryGenerator gen = new AutoBeanFactoryGenerator();

            return gen.generate(logger, context, fullName);
        }

        return null;
    }

    private boolean checkImplementedInterface(TypeOracle oracle, JClassType[] interfaces) {
        for(JClassType in : interfaces)
        {
            if(in.isAssignableTo( oracle.findType(AutoBeanFactory.class.getName()) ))
                return true;
        }

        return false;
    }
}
