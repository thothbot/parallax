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

package org.parallax3d.parallax.system;

import org.parallax3d.parallax.files.FileHandle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SourceBundleProxy {

    public static <T> T create(final Class<? extends SourceBundle> classLiteral) {

        return (T) Proxy.newProxyInstance(classLiteral.getClassLoader(),
                new Class[]{classLiteral},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        SourceBundle.Source source = method.getAnnotation(SourceBundle.Source.class);
                        return new FileHandle(SourceBundleProxy.class.getResource(classLiteral.getPackage().getName().replace(".", "/") + source).getFile());
                    }
                });

    }

}
