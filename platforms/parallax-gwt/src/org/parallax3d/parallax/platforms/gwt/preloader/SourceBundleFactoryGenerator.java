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

package org.parallax3d.parallax.platforms.gwt.preloader;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.resources.rebind.context.InlineClientBundleGenerator;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.SourceBundle;

import java.io.PrintWriter;
import java.util.Map;

public class SourceBundleFactoryGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {

        PrintWriter pw = context.tryCreate(logger,
                "org.parallax3d.parallax.platforms.gwt.preloader",
                "SourceBundleFactoryImpl");

        if (pw != null)
        {
            // write package, imports, whatever
            pw.append("package org.parallax3d.parallax.platforms.gwt.preloader;");
            pw.append("import org.parallax3d.parallax.system.SourceBundle;");
            pw.append("import org.parallax3d.parallax.system.SourceBundleFactory;");
            pw.append("import com.google.gwt.core.client.GWT;");
            pw.append("import org.parallax3d.parallax.system.FastMap;");

            // iterates over all the classes to find those with EntryPointWidget annotation
            TypeOracle oracle = context.getTypeOracle();
            JPackage[] packages = oracle.getPackages();

            FastMap<JClassType> genClasses = new FastMap<>();
            for (JPackage pack : packages)
            {
                for (JClassType classtype : pack.getTypes())
                {
                    for (JClassType nestedClass : classtype.getNestedTypes())
                    {
                        String cls = generateStaticInstance(oracle, logger, context, nestedClass);
                        if(cls != null)
                            genClasses.put(cls, nestedClass);
                    }

                    String cls = generateStaticInstance(oracle, logger, context, classtype);
                    if(cls != null)
                        genClasses.put(cls, classtype);
                }
            }

            // import generated classes
            for(String genClass: genClasses.keySet())
                pw.append("import " + genClass + ";");

            // the class
            pw.append("public class SourceBundleFactoryImpl implements SourceBundleFactory {");

            pw.append("   private static final FastMap<SourceBundle> MAP = new FastMap<SourceBundle>(){{");

            for(Map.Entry<String,JClassType> entry: genClasses.entrySet())
                pw.append("      put(\"" + entry.getValue().getQualifiedSourceName() + "\", new " +  entry.getKey() + "() );");

            pw.append("   }};");

            // get method
            pw.append("   public <T> T get(Class<? extends SourceBundle> classLiteral) {");
            pw.append("       if (MAP.containsKey(classLiteral.getCanonicalName())) {");
            pw.append("           return (T) MAP.get(classLiteral.getCanonicalName());");
            pw.append("       }");
            pw.append("       return null;");
            pw.append("   }"); // method
            pw.append("}"); // class

            context.commit(logger, pw);
        }

        // return the name of the generated class
        return "org.parallax3d.parallax.platforms.gwt.preloader.SourceBundleFactoryImpl";
    }

    private String generateStaticInstance(TypeOracle oracle, TreeLogger logger, GeneratorContext context, JClassType classtype) throws UnableToCompleteException {
        if(checkImplementedInterface(oracle, classtype.getImplementedInterfaces()))
        {
            String fullName = classtype.getQualifiedSourceName();

            System.out.println(" Generate Bundle for class " + fullName);

            InlineClientBundleGenerator gen = new InlineClientBundleGenerator();

            return gen.generate(logger, context, fullName);
        }

        return null;
    }

    private boolean checkImplementedInterface(TypeOracle oracle, JClassType[] interfaces) {

        for(JClassType in : interfaces)
        {
            if(in.isAssignableTo( oracle.findType(SourceBundle.class.getName()) ))
                return true;
        }

        return false;
    }
}
