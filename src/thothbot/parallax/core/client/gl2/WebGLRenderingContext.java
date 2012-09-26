/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This code based on code written by Sönke Sothmann, Steffen Schäfer and others.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.client.gl2;

import thothbot.parallax.core.client.gl2.arrays.ArrayBuffer;
import thothbot.parallax.core.client.gl2.arrays.ArrayBufferView;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Int32Array;
import thothbot.parallax.core.client.gl2.arrays.JsArrayUtil;
import thothbot.parallax.core.client.gl2.arrays.TypeArray;
import thothbot.parallax.core.client.gl2.enums.BlendEquationMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.gl2.enums.ClearBufferMask;
import thothbot.parallax.core.client.gl2.enums.FramebufferErrorCode;
import thothbot.parallax.core.client.gl2.enums.GLEnum;
import thothbot.parallax.core.client.gl2.enums.PixelInternalFormat;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.client.gl2.enums.TextureUnit;

import com.google.gwt.canvas.dom.client.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;

/**
 * 
 */
public final class WebGLRenderingContext extends JavaScriptObject implements Context 
{

  /**
   * Protected standard constructor as specified by
   * {@link com.google.gwt.core.client.JavaScriptObject}.
   */
  protected WebGLRenderingContext() {
  }

  /**
   * @see #activeTexture(TextureUnit, int)
   * 
   * @param texture
   */
  public void activeTexture(TextureUnit texture) {
	  activeTexture(texture.getValue());
  }
  
  /**
   * Select active texture unit.
   * 
   * @param texture
   * @param slot the texture offset value
   */
  public void activeTexture(TextureUnit texture, int slot) {
	  activeTexture(texture.getValue() + slot);
  }

  private native void activeTexture(int texture) /*-{
		this.activeTexture(texture);
  }-*/;

  /**
   * Attach a shader object to a program object.
   * 
   * @param program Specifies the program object to which a shader object will 
   * 				be attached.
   * @param shader Specifies the shader object that is to be attached.
   */
  public native void attachShader(WebGLProgram program, WebGLShader shader) /*-{
		this.attachShader(program, shader);
  }-*/;

  /**
   * Associate a generic vertex attribute index with a named attribute variable.
   * 
   * @param program Specifies the handle of the program object in which the 
   * 				association is to be made.
   * @param index Specifies the index of the generic vertex attribute to be 
   * 				bound.
   * @param name Specifies a null terminated string containing the name of the 
   * 				vertex shader attribute variable to which index is to be bound.
   */
  public native void bindAttribLocation(WebGLProgram program, int index, String name) /*-{
		this.bindAttribLocation(program, index, name);
  }-*/;

  /**
   * Bind a named buffer object.
   * 
   * @param target Specifies the target to which the buffer object is bound.
   * @param buffer Specifies the name of a buffer object.
   */
  public void bindBuffer(BufferTarget target, WebGLBuffer buffer) {
	  bindBuffer(target.getValue(), buffer);
  }

  private native void bindBuffer(int target, WebGLBuffer buffer) /*-{
		this.bindBuffer(target, buffer);
  }-*/;

  /**
   * Bind a named framebuffer object.
   * @param buffer Specifies the name of a framebuffer object.
   */

  public void bindFramebuffer(WebGLFramebuffer buffer) {
	  bindFramebuffer(GLEnum.FRAMEBUFFER.getValue(), buffer);
  }

  private native void bindFramebuffer(int target, WebGLFramebuffer buffer) /*-{
		this.bindFramebuffer(target, buffer);
  }-*/;

  /**
   * Bind a named renderbuffer object.
   * 
   * @param buffer Specifies the name of a renderbuffer object.
   */
  public void bindRenderbuffer(WebGLRenderbuffer buffer) {
	  bindRenderbuffer(GLEnum.RENDERBUFFER.getValue(), buffer);
  }

  private native void bindRenderbuffer(int target, WebGLRenderbuffer buffer) /*-{
		this.bindRenderbuffer(target, buffer);
  }-*/;

  /**
   * Bind a named texture to a texturing target.
   * 
   * @param target Specifies the target to which the texture is bound.
   * @param texture Specifies the name of a texture.
   */
  public void bindTexture(TextureTarget target, WebGLTexture texture) {
	  bindTexture(target.getValue(), texture);		
  }

  private native void bindTexture(int target, WebGLTexture texture) /*-{
		this.bindTexture(target, texture);
  }-*/;

  /**
   * Set the blend color.
   * 
   * @param red
   * @param green
   * @param blue
   * @param alpha
   */
  public native void blendColor(double red, double green, double blue, double alpha) /*-{
		this.blendColor(red, green, blue, alpha);
  }-*/;

  /**
   * Specify the equation used for both the RGB blend equation and the Alpha 
   * blend equation.
   * 
   * @param mode
   */
  public void blendEquation(BlendEquationMode mode) {
	  blendEquation(mode.getValue());
  }

  private native void blendEquation(int mode) /*-{
		this.blendEquation(mode);
  }-*/;

  /**
   * Set the RGB blend equation and the alpha blend equation separately.
   */
  public void blendEquationSeparate(BlendEquationMode modeRGB, BlendEquationMode modeAlpha) {
	  blendEquationSeparate(modeRGB.getValue(), modeAlpha.getValue());
  }

  private native void blendEquationSeparate(int modeRGB, int modeAlpha) /*-{
		this.blendEquationSeparate(modeRGB, modeAlpha);
  }-*/;

  /**
   * Specify pixel arithmetic.
   */
  public void blendFunc(BlendingFactorSrc sfactor, BlendingFactorDest dfactor) {
	  blendFunc(sfactor.getValue(), dfactor.getValue());
  }

  private native void blendFunc(int sfactor, int dfactor) /*-{
		this.blendFunc(sfactor, dfactor);
  }-*/;

  /**
   * Set the RGB blend equation and the alpha blend equation separately.
   */
  public void blendFuncSeparate(BlendingFactorSrc srcRGB,
		  BlendingFactorDest dstRGB, 
		  BlendingFactorSrc srcAlpha,
		  BlendingFactorDest dstAlpha) 
  {
	  blendFuncSeparate(srcRGB.getValue(), dstRGB.getValue(), 
			  srcAlpha.getValue(), dstAlpha.getValue());
  }

  private native void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) /*-{
		this.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
  }-*/;

  /**
   * @see #bufferData(BufferTarget, TypeArray, BufferUsage)
   */
  public void bufferData(BufferTarget target, ArrayBuffer data, BufferUsage usage) {
	  bufferData(target.getValue(), data, usage.getValue());
  }

  private native void bufferData(int target, ArrayBuffer data, int usage) /*-{
		this.bufferData(target, data, usage);
  }-*/;

  /**
   * Set the size of the currently bound WebGLBuffer object for the passed 
   * target. The buffer is initialized to 0.
   * 
   * @param target Specifies the target buffer object.
   * @param size Specifies the size in bytes of the buffer object's new data 
   * 				store.
   * @param usage Specifies the expected usage pattern of the data store.
   */
  public void bufferData(BufferTarget target, int size, BufferUsage usage) {
	  bufferData(target.getValue(), size, usage.getValue());
  }
	
  private native void bufferData(int target, int size, int usage) /*-{
		this.bufferData(target, size, usage);
  }-*/;

  /**
   * Set the size of the currently bound WebGLBuffer object for the passed 
   * target to the size of the passed data, then write the contents of data to 
   * the buffer object.
   * 
   * @param target Specifies the target buffer object.
   * @param data Specifies a pointer to data that will be copied into the data 
   * 				store for initialization
   * @param usage Specifies the expected usage pattern of the data store.
   */
  public void bufferData(BufferTarget target, TypeArray data, BufferUsage usage) {
	  bufferData(target.getValue(), data, usage.getValue());
  }

  private native void bufferData(int target, TypeArray data, int usage) /*-{
		this.bufferData(target, data, usage);
  }-*/;

  /**
   * @see #bufferSubData(BufferTarget, int, TypeArray)
   */
  public void bufferSubData(BufferTarget target, int offset, ArrayBuffer data) {
	  bufferSubData(target.getValue(), offset, data);
  }

  private native void bufferSubData(int target, int offset, ArrayBuffer data) /*-{
		this.bufferSubData(target, offset, data);
  }-*/;

  /**
   * For the WebGLBuffer object bound to the passed target write the passed 
   * data starting at the passed offset. If the data would be written past the 
   * end of the buffer object an INVALID_VALUE error is raised.
   * 
   * @param target Specifies the target buffer object.
   * @param offset Specifies the offset into the buffer object's data store 
   * 				where data replacement will begin, measured in bytes.
   * @param data Specifies a pointer to the new data that will be copied into 
   * 				the data store.
   */
  public void bufferSubData(BufferTarget target, int offset, TypeArray data) {
	  bufferSubData(target.getValue(), offset, data);
  }

  private native void bufferSubData(int target, int offset, TypeArray data) /*-{
		this.bufferSubData(target, offset, data);
  }-*/;

  /**
   * Return the framebuffer completeness status of a framebuffer object.
   *
   * @return identifies whether or not the currently bound framebuffer is 
   * 				framebuffer complete, and if not, which of the rules of framebuffer 
   * 				completeness is violated.
   */
  public FramebufferErrorCode checkFramebufferStatus() {
	  return FramebufferErrorCode.parseErrorCode(checkFramebufferStatus(
			  GLEnum.FRAMEBUFFER.getValue()));
  }

  private native int checkFramebufferStatus(int target) /*-{
		return this.checkFramebufferStatus(target);
  }-*/;

  /**
   * Clear buffers to preset values.
   * 
   * @param mask
   */
  public native void clear(int mask) /*-{
		this.clear(mask);
  }-*/;

  /**
   * Specify the red, green, blue, and alpha values used when the color buffers 
   * are cleared. The initial values are all 0.
   * 
   * @param red 
   * @param green
   * @param blue
   * @param alpha
   */
  public native void clearColor(double red, double green, double blue, double alpha) /*-{
		this.clearColor(red, green, blue, alpha);
  }-*/;

  /**
   * Specifies the depth value used when the depth buffer is cleared. The 
   * initial value is 1.
   * 
   * @param depth
   */
  public native void clearDepth(double depth) /*-{
		this.clearDepth(depth);
  }-*/;

  /**
   * Specifies the index used when the stencil buffer is cleared. The initial 
   * value is 0.
   * 
   * @param s
   */
  public native void clearStencil(int s) /*-{
		this.clearStencil(s);
  }-*/;

  /**
   * Specify whether red, green, blue, and alpha can or cannot be written into 
   * the frame buffer. The initial values are all true, indicating that the 
   * color components can be written.
   * 
   * @param red
   * @param green
   * @param blue
   * @param alpha
   */
  public native void colorMask(boolean red, boolean green, boolean blue, boolean alpha) /*-{
		this.colorMask(red, green, blue, alpha);
  }-*/;

  /**
   * Compile a shader object.
   * 
   * @param shader Specifies the shader object to be compiled.
   */
  public native void compileShader(WebGLShader shader) /*-{
		this.compileShader(shader);
  }-*/;

  /**
   * If an attempt is made to call this function with no WebGLTexture bound, 
   * an INVALID_OPERATION error is raised.
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param internalformat Specifies the internal format of the texture.
   * @param x Specify the window coordinates of the x-coordinate of the 
   * 				rectangular region of pixels to be copied.
   * @param y Specify the window coordinates of the y-coordinate of the 
   * 				rectangular region of pixels to be copied.
   * @param width Specifies the width of the texture image. All implementations 
   * 				support 2D texture images that are at least 64 texels wide and 
   * 				cube-mapped texture images that are at least 16 texels wide.
   * @param height Specifies the height of the texture image. All 
   * 				implementations support 2D texture images that are at least 64 
   * 				texels high and cube-mapped texture images that are at least 16 
   * 				texels high.
   * @param border Specifies the width of the border. Must be 0.
   */
  public void copyTexImage2D(TextureTarget target, int level,
		  PixelInternalFormat internalformat, int x, int y, int width, int height,
		  int border) 
  {
	  copyTexImage2D(target.getValue(), level, internalformat.getValue(), 
			  x, y, width, height, border);
  }

  private native void copyTexImage2D(int target, int level, int intformat, int x, int y, int width,
      int height, int border) /*-{
		this.copyTexImage2D(target, level, intformat, x, y, width, height,
				border);
  }-*/;

  /**
   * If an attempt is made to call this function with no WebGLTexture bound, 
   * an INVALID_OPERATION error is raised.
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param xoffset Specifies a texel offset in the x direction within the 
   * 				texture array.
   * @param yoffset Specifies a texel offset in the y direction within the 
   * 				texture array.
   * @param x Specify the window coordinates of the lower left corner of the 
   * 				rectangular region of pixels to be copied.
   * @param y Specify the window coordinates of the lower left corner of the 
   * 				rectangular region of pixels to be copied.
   * @param width Specifies the width of the texture subimage.
   * @param height Specifies the height of the texture subimage.
   */
  public void copyTexSubImage2D(TextureTarget target, int level, int xoffset,
		  int yoffset, int x, int y, int width, int height) {
	  copyTexSubImage2D(target.getValue(), level, xoffset, yoffset, x, y, 
			  width, height);
  }

  private native void copyTexSubImage2D(int target, int level, int xoffset,
      int yoffset, int x, int y, int width, int height) /*-{
		this.copyTexSubImage2D(target, level, xoffset, yoffset, x,
				y, width, height);
  }-*/;

  /**
   * Create a WebGLBuffer object and initialize it with a buffer object name as 
   * if by calling glGenBuffers.
   */
  public native WebGLBuffer createBuffer() /*-{
		return this.createBuffer();
  }-*/;

  /**
   * Create a WebGLFramebuffer object and initialize it with a framebuffer 
   * object name as if by calling glGenFramebuffers.
   */
  public native WebGLFramebuffer createFramebuffer() /*-{
		return this.createFramebuffer();
  }-*/;

  /**
   * Create a WebGLProgram object and initialize it with a program object name 
   * as if by calling glCreateProgram.
   */
  public native WebGLProgram createProgram() /*-{
		return this.createProgram();
  }-*/;

  /**
   * Create a WebGLRenderbuffer object and initialize it with a renderbuffer 
   * object name as if by calling glGenRenderbuffers.
   */
  public native WebGLRenderbuffer createRenderbuffer() /*-{
		return this.createRenderbuffer();
  }-*/;

  /**
   * Create a WebGLShader object and initialize it with a shader object name 
   * as if by calling glCreateShader.
   */
  public native WebGLShader createShader(int shaderType) /*-{
		return this.createShader(shaderType);
  }-*/;

  /**
   * Create a WebGLTexture object and initialize it with a texture object name 
   * as if by calling glGenTextures.
   */
  public native WebGLTexture createTexture() /*-{
		return this.createTexture();
  }-*/;

  public native void cullFace(int mode) /*-{
		this.cullFace(mode);
  }-*/;

  public native void deleteBuffer(WebGLBuffer buffer) /*-{
		this.deleteBuffer(buffer);
  }-*/;

  public native void deleteFramebuffer(WebGLFramebuffer buffer) /*-{
		this.deleteFramebuffer(buffer);
  }-*/;

  public native void deleteProgram(WebGLProgram program) /*-{
		this.deleteProgram(program);
  }-*/;

  public native void deleteRenderbuffer(WebGLRenderbuffer buffer) /*-{
		this.deleteRenderbuffer(buffer);
  }-*/;

  public native void deleteShader(WebGLShader shader) /*-{
		this.deleteShader(shader);
  }-*/;

  public native void deleteTexture(WebGLTexture texture) /*-{
		this.deleteTexture(texture);
  }-*/;

  public native void depthFunc(int func) /*-{
		this.depthFunc(func);
  }-*/;

  public native void depthMask(boolean flag) /*-{
		this.depthMask(flag);
  }-*/;

  public native void depthRange(double nearVal, double farVal) /*-{
		this.depthRange(nearVal, farVal);
  }-*/;

  public native void detachShader(WebGLProgram program, WebGLShader shader) /*-{
		this.detachShader(program, shader);
  }-*/;

  public native void disable(int param) /*-{
		this.disable(param);
  }-*/;

  public native void disableVertexAttribArray(int index) /*-{
		this.disableVertexAttribArray(index);
  }-*/;

  public native void drawArrays(int mode, int first, int count) /*-{
		this.drawArrays(mode, first, count);
  }-*/;

  public native void drawElements(int mode, int count, int type, int offset) /*-{
		this.drawElements(mode, count, type, offset);
  }-*/;

  public native void enable(int param) /*-{
		this.enable(param);
  }-*/;

  public native void enableVertexAttribArray(int index) /*-{
		this.enableVertexAttribArray(index);
  }-*/;

  public native void finish() /*-{
		this.finish();
  }-*/;

  public native void flush() /*-{
		this.flush();
  }-*/;

  public native void framebufferRenderbuffer(int target, int attachment, int rbtarget,
      WebGLRenderbuffer rbuffer) /*-{
		this.framebufferRenderbuffer(target, attachment, rbtarget, rbuffer);
  }-*/;

  public native void framebufferTexture2D(int target, int att, int textarget, WebGLTexture tex,
      int level) /*-{
		this.framebufferTexture2D(target, att, textarget, tex, level);
  }-*/;

  public native void frontFace(int mode) /*-{
		this.frontFace(mode);
  }-*/;

  public native void generateMipmap(int target) /*-{
		this.generateMipmap(target);
  }-*/;

  public native WebGLActiveInfo getActiveAttrib(WebGLProgram program, int index) /*-{
		return this.getActiveAttrib(program, index);
  }-*/;

  public native WebGLActiveInfo getActiveUniform(WebGLProgram program, int idx) /*-{
		return this.getActiveUniform(program, idx);
  }-*/;

  /**
   * Return the list of {@link WebGLShader}s attached to the passed {@link WebGLProgram}.
   * 
   * @param program {@link WebGLProgram} object to be queried.
   * @return array of {@link WebGLShader}s attached to the passed {@link WebGLProgram}
   * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetAttachedShaders.xml"
   */
  public WebGLShader[] getAttachedShaders(WebGLProgram program) {
    // TODO implement this in the generator
    try {
      if (GWT.isProdMode()) {
        return getAttachedShadersProd(program);
      }
      JsArray<WebGLShader> shaders = getAttachedShadersDev(program);

      WebGLShader[] result = new WebGLShader[shaders.length()];
      for (int i = 0; i < shaders.length(); i++) {
        result[i] = shaders.get(i);
      }
      return result;
    } catch (Exception e) {
      return new WebGLShader[0];
    }
  }

  public native int getAttribLocation(WebGLProgram program, String name) /*-{
		return this.getAttribLocation(program, name);
  }-*/;

  public native int getBufferParameteri(int target, int pname) /*-{
		return this.getBufferParameter(target, pname);
  }-*/;

  public native int getError() /*-{
		return this.getError();
  }-*/;

  public native JavaScriptObject getExtension(String name) /*-{
		return this.getExtension(name);
  }-*/;

  public native int getExtensioni(String name) /*-{
		return this.getExtension(name);
  }-*/;

  public native JavaScriptObject getFramebufferAttachmentParameter(int target, int attachment,
      int pname) /*-{
		return this
				.getFramebufferAttachmentParameter(target, attachment, pname);
  }-*/;

  public native int getFramebufferAttachmentParameteri(int target, int attachment, int pname) /*-{
		return this
				.getFramebufferAttachmentParameter(target, attachment, pname);
  }-*/;

  public native <T extends JavaScriptObject> T getParameter(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native boolean getParameterb(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native double getParameterf(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native int getParameteri(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native String getProgramInfoLog(WebGLProgram program) /*-{
		return this.getProgramInfoLog(program);
  }-*/;

  public native boolean getProgramParameterb(WebGLProgram program, int pname) /*-{
		return this.getProgramParameter(program, pname);
  }-*/;

  public native int getProgramParameteri(WebGLProgram program, int pname) /*-{
		return this.getProgramParameter(program, pname);
  }-*/;

  public native int getRenderbufferParameteri(int target, int pname) /*-{
		return this.getRenderbufferParameter(target, pname);
  }-*/;

  public native String getShaderInfoLog(WebGLShader shader) /*-{
		return this.getShaderInfoLog(shader);
  }-*/;

  public native boolean getShaderParameterb(WebGLShader shader, int pname) /*-{
		return this.getShaderParameter(shader, pname);
  }-*/;

  public native int getShaderParameteri(WebGLShader shader, int pname) /*-{
		return this.getShaderParameter(shader, pname);
  }-*/;

  public native String getShaderSource(WebGLShader shader) /*-{
		return this.getShaderSource(shader);
  }-*/;

  /**
   * Determines the extensions supported by the WebGL implementation.
   * 
   * @return an array containing the names of the supported extensions.
   */
   public String[] getSupportedExtensions() {
		JsArrayString supportedExts = getSupportedExtensionsAsJsArray();
		String[] outSupportedExts = new String[supportedExts.length()];
		for (int i = 0; i < outSupportedExts.length; i++) {
			outSupportedExts[i] = supportedExts.get(i);
		}
		return outSupportedExts;
  }

  public native JsArrayString getSupportedExtensionsAsJsArray() /*-{
		return this.getSupportedExtensions();
  }-*/;

  public native int getTexParameteri(int target, int pname) /*-{
		return this.getTexParameter(target, pname);
  }-*/;

  public native <T extends thothbot.parallax.core.client.gl2.arrays.TypeArray> T getUniforma(
      WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native boolean getUniformb(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native double getUniformf(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native int getUniformi(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native WebGLUniformLocation getUniformLocation(WebGLProgram program, String name) /*-{
		return this.getUniformLocation(program, name);
  }-*/;

  public native <T extends JavaScriptObject> T getVertexAttrib(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native boolean getVertexAttribb(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native int getVertexAttribi(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native int getVertexAttribOffset(int index, String pname) /*-{
		return this.getVertexAttribOffset(index, pname);
  }-*/;

  public native boolean isBuffer(WebGLBuffer buffer) /*-{
		return this.isBuffer(buffer);
  }-*/;

  public native boolean isFramebuffer(JavaScriptObject buffer) /*-{
		return this.isFramebuffer(buffer);
  }-*/;

  public native boolean isProgram(WebGLProgram program) /*-{
		return this.isProgram(program);
  }-*/;

  public native boolean isRenderbuffer(WebGLRenderbuffer buffer) /*-{
		return this.isRenderbuffer(buffer);
  }-*/;

  public native boolean isShader(JavaScriptObject shader) /*-{
		return this.isShader(shader);
  }-*/;

  public native boolean isTexture(WebGLTexture texture) /*-{
		return this.isTexture(texture);
  }-*/;

  public native void lineWidth(double width) /*-{
		this.lineWidth(width);
  }-*/;

  public native void linkProgram(WebGLProgram program) /*-{
		this.linkProgram(program);
  }-*/;

  public native void pixelStorei(int pname, int param) /*-{
		this.pixelStorei(pname, param);
  }-*/;

  public native void polygonOffset(double factor, double units) /*-{
		this.polygonOffset(factor, units);
  }-*/;

  public native void readPixels(int x, int y, int width, int height, int format, int type,
      ArrayBufferView pixels) /*-{
		this.readPixels(x, y, width, height, format, type, pixels);
  }-*/;

  public native void renderbufferStorage(int target, int format, int width, int height) /*-{
		this.renderbufferStorage(target, format, width, height);
  }-*/;

  public native void sampleCoverage(double value, boolean invert) /*-{
		this.sampleCoverage(value, invert);
  }-*/;

  public native void scissor(int x, int y, int width, int height) /*-{
		this.scissor(x, y, width, height);
  }-*/;

  public native void shaderSource(WebGLShader shader, String shaderSrc) /*-{
		this.shaderSource(shader, shaderSrc);
  }-*/;

  public native void stencilFunc(int func, int ref, int mask) /*-{
		this.stencilFunc(func, ref, mask);
  }-*/;

  public native void stencilFuncSeparate(int face, int func, int ref, int mask) /*-{
		this.stencilFuncSeparate(face, func, ref, mask);
  }-*/;

  public native void stencilMask(int mask) /*-{
		this.stencilMask(mask);
  }-*/;

  public native void stencilMaskSeparate(int face, int mask) /*-{
		this.stencilMaskSeparate(face, mask);
  }-*/;

  public native void stencilOp(int sfail, int dpfail, int dppass) /*-{
		this.stencilOp(sfail, dpfail, dppass);
  }-*/;

  public native void stencilOpSeparate(int face, int sfail, int dpfail, int dppass) /*-{
		this.stencilOpSeparate(face, sfail, dpfail, dppass);
  }-*/;

  public native void texImage2D(int target, int level, int internalformat, int width, int height,
      int border, int format, int type, ArrayBufferView pixels) /*-{
		this.texImage2D(target, level, internalformat, width, height, border,
				format, type, pixels);
  }-*/;

  public native void texImage2D(int target, int level, int internalformat, int format, int type,
      JavaScriptObject data) /*-{
		this.texImage2D(target, level, internalformat, format, type, data);
  }-*/;

  public native void texParameterf(int target, int pname, double value) /*-{
		this.texParameterf(target, pname, value);
  }-*/;

  public native void texParameteri(int target, int pname, int value) /*-{
		this.texParameteri(target, pname, value);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset, int width,
      int height, int format, int type, TypeArray data) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, width, height,
				format, type, data);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset,
      JavaScriptObject data) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, data);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset,
      JavaScriptObject data, boolean flipY) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, data, flipY);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset,
      JavaScriptObject data, boolean flipY, boolean asPremultipliedAlpha) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, data, flipY,
				asPremultipliedAlpha);
  }-*/;

  public native void uniform1f(WebGLUniformLocation location, double v0) /*-{
		this.uniform1f(location, v0);
  }-*/;

  public void uniform1fv(WebGLUniformLocation location, double[] values) {
    uniform1fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform1fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform1fv(location, values);
  }-*/;

  public native void uniform1fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform1fv(location, v);
  }-*/;

  public native void uniform1i(WebGLUniformLocation location, int v0) /*-{
		this.uniform1i(location, v0);
  }-*/;

  public void uniform1iv(WebGLUniformLocation location, int[] values) {
    uniform1iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform1iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform1iv(location, v);
  }-*/;

  public native void uniform1iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform1iv(location, values);
  }-*/;

  public native void uniform2f(WebGLUniformLocation location, double v0, double v1) /*-{
		this.uniform2f(location, v0, v1);
  }-*/;

  public void uniform2fv(WebGLUniformLocation location, double[] values) {
    uniform2fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform2fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform2fv(location, v);
  }-*/;

  public native void uniform2fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform2fv(location, values);
  }-*/;

  public native void uniform2i(WebGLUniformLocation location, int v0, int v1) /*-{
		this.uniform2i(location, v0, v1);
  }-*/;

  public void uniform2iv(WebGLUniformLocation location, int[] values) {
    uniform2iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform2iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform2iv(location, v);
  }-*/;

  public native void uniform2iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform2iv(location, values);
  }-*/;

  public native void uniform3f(WebGLUniformLocation location, double v0, double v1, double v2) /*-{
		this.uniform3f(location, v0, v1, v2);
  }-*/;

  public void uniform3fv(WebGLUniformLocation location, double[] values) {
    uniform3fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform3fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform3fv(location, v);
  }-*/;

  public native void uniform3fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform3fv(location, values);
  }-*/;

  public native void uniform3i(WebGLUniformLocation location, int v0, int v1, int v2) /*-{
		this.uniform3i(location, v0, v1, v2);
  }-*/;

  public void uniform3iv(WebGLUniformLocation location, int[] values) {
    uniform3iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform3iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform3iv(location, values);
  }-*/;

  public native void uniform3iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform3iv(location, v);
  }-*/;

  public native void uniform4f(WebGLUniformLocation location, double v0, double v1, double v2, double v3) /*-{
		this.uniform4f(location, v0, v1, v2, v3);
  }-*/;

  public void uniform4fv(WebGLUniformLocation location, double[] values) {
    uniform4fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform4fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform4fv(location, v);
  }-*/;

  public native void uniform4fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform4fv(location, values);
  }-*/;

  public native void uniform4i(WebGLUniformLocation location, int v0, int v1, int v2, int v3) /*-{
		this.uniform4i(location, v0, v1, v2, v3);
  }-*/;

  public void uniform4iv(WebGLUniformLocation location, int[] values) {
    uniform4iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform4iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform4iv(location, v);
  }-*/;

  public native void uniform4iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform4iv(location, values);
  }-*/;

  public void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, double[] value) {
    uniformMatrix2fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  public native void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix2fv(location, transpose, value);
  }-*/;

  public native void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix2fv(location, transpose, value);
  }-*/;

  public void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, double[] value) {
    uniformMatrix3fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  public native void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix3fv(location, transpose, value);
  }-*/;

  public native void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix3fv(location, transpose, value);
  }-*/;

  public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, double[] value) {
    uniformMatrix4fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  public native void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix4fv(location, transpose, value);
  }-*/;

  public native void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix4fv(location, transpose, value);
  }-*/;

  public native void useProgram(WebGLProgram program) /*-{
		this.useProgram(program);
  }-*/;

  public native void validateProgram(WebGLProgram program) /*-{
		this.validateProgram(program);
  }-*/;

  public native void vertexAttrib1f(int index, double x) /*-{
		this.vertexAttrib1f(index, x);
  }-*/;

  public void vertexAttrib1fv(int index, double[] values) {
    vertexAttrib1fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib1fv(int index, Float32Array values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  public native void vertexAttrib1fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  public native void vertexAttrib2f(int index, double x, double y) /*-{
		this.vertexAttrib2f(index, x, y);
  }-*/;

  public void vertexAttrib2fv(int index, double[] values) {
    vertexAttrib2fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib2fv(int index, Float32Array values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  public native void vertexAttrib2fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  public native void vertexAttrib3f(int index, double x, double y, double z) /*-{
		this.vertexAttrib3f(index, x, y, z);
  }-*/;

  public void vertexAttrib3fv(int index, double[] values) {
    vertexAttrib3fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib3fv(int index, Float32Array values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  public native void vertexAttrib3fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  public native void vertexAttrib4f(int index, double x, double y, double z, double w) /*-{
		this.vertexAttrib4f(index, x, y, z, w);
  }-*/;

  public void vertexAttrib4fv(int index, double[] values) {
    vertexAttrib4fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib4fv(int index, Float32Array values) /*-{
		this.vertexAttrib4fv(index, values);
  }-*/;

  public native void vertexAttrib4fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib4fv(index, values);
  }-*/;

  public native void vertexAttribPointer(int index, int size, int type, boolean normalized,
      int stride, int offset) /*-{
		this.vertexAttribPointer(index, size, type, normalized, stride, offset);
  }-*/;

  public native void viewport(int x, int y, int w, int h) /*-{
		this.viewport(x, y, w, h);
  }-*/;

  protected native com.google.gwt.core.client.JsArray<WebGLShader> getAttachedShadersDev(
      WebGLProgram program) /*-{
		return this.getAttachedShaders(program);
  }-*/;

  protected native WebGLShader[] getAttachedShadersProd(WebGLProgram program) /*-{
		return this.getAttachedShaders(program);
  }-*/;

}
