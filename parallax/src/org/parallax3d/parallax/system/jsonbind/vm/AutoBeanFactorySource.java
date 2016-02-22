/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.parallax3d.parallax.system.jsonbind.vm;

import org.parallax3d.parallax.system.jsonbind.AutoBean;
import org.parallax3d.parallax.system.jsonbind.AutoBeanFactory;
import org.parallax3d.parallax.system.jsonbind.impl.EnumMap;
import org.parallax3d.parallax.system.jsonbind.vm.impl.FactoryHandler;
import org.parallax3d.parallax.system.jsonbind.vm.impl.ProxyAutoBean;

/**
 * Generates JVM-compatible implementations of AutoBeanFactory and AutoBean
 * types.
 * <p>
 * This implementation is written assuming that the AutoBeanFactory and
 * associated declarations will validate if compiled and used with the
 * AutoBeanFactoyModel.
 * <p>
 * <span style='color: red'>This is experimental, unsupported code.</span>
 */
public class AutoBeanFactorySource {
  /*
   * NB: This implementation is excessively dynamic, however the inability to
   * create a TypeOracle fram a ClassLoader prevents re-using the existing model
   * code. If the model code could be reused, it would be straightforward to
   * simply generate implementations of the various interfaces.
   */
  private static final AutoBeanFactory EMPTY = create(AutoBeanFactory.class);

  /**
   * Create an instance of an AutoBeanFactory.
   * 
   * @param <F> the factory type
   * @param clazz the Class representing the factory interface
   * @return an instance of the AutoBeanFactory
   */
  public static <F extends AutoBeanFactory> F create(Class<F> clazz) {
    Configuration.Builder builder = new Configuration.Builder();
    AutoBeanFactory.Category cat = clazz.getAnnotation(AutoBeanFactory.Category.class);
    if (cat != null) {
      builder.setCategories(cat.value());
    }
    AutoBeanFactory.NoWrap noWrap = clazz.getAnnotation(AutoBeanFactory.NoWrap.class);
    if (noWrap != null) {
      builder.setNoWrap(noWrap.value());
    }

    return ProxyAutoBean.makeProxy(clazz, new FactoryHandler(builder.build()), EnumMap.class);
  }

  /**
   * Create an instance of an AutoBean directly.
   * 
   * @param <T> the interface type implemented by the AutoBean
   * @param clazz the interface type implemented by the AutoBean
   * @return an instance of an AutoBean
   */
  public static <T> AutoBean<T> createBean(Class<T> clazz, Configuration configuration) {
    return new ProxyAutoBean<T>(EMPTY, clazz, configuration);
  }
}
