/**
 * Copyright 2009-2010 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package thothbot.parallax.core.client.gl2;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The {@link WebGLActiveInfo} is used as result of the methods
 * {@link WebGLRenderingContext#getActiveAttrib(WebGLProgram, int)} and
 * {@link WebGLRenderingContext#getActiveUniform(WebGLProgram, int)}.
 * 
 */
public final class WebGLActiveInfo extends JavaScriptObject {

  /**
   * protected standard constructor as specified by
   * {@link com.google.gwt.core.client.JavaScriptObject}.
   */
  protected WebGLActiveInfo() {
  }

  /**
   * The name of the requested variable.
   * 
   * @return the name of the requested variable
   */
  public native int getName() /*-{
		return this.name;
  }-*/;

  /**
   * The size of the requested variable.
   * 
   * @return the size of the requested variable
   */
  public native int getSize() /*-{
		return this.size;
  }-*/;

  /**
   * The data type of the requested variable.
   * 
   * @return the data type of the requested variable
   */
  public native int getType() /*-{
		return this.type;
  }-*/;

}
