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

package thothbot.parallax.core.client.gl2;

import thothbot.parallax.core.client.gl2.arrays.ArrayBuffer;
import thothbot.parallax.core.client.gl2.arrays.ArrayBufferView;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.arrays.Int32Array;
import thothbot.parallax.core.client.gl2.arrays.JsArrayUtil;
import thothbot.parallax.core.client.gl2.arrays.TypeArray;
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BlendEquationMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.gl2.enums.BufferParameterName;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.gl2.enums.CullFaceMode;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.DepthFunction;
import thothbot.parallax.core.client.gl2.enums.DrawElementsType;
import thothbot.parallax.core.client.gl2.enums.EnableCap;
import thothbot.parallax.core.client.gl2.enums.ErrorCode;
import thothbot.parallax.core.client.gl2.enums.FramebufferErrorCode;
import thothbot.parallax.core.client.gl2.enums.FramebufferParameterName;
import thothbot.parallax.core.client.gl2.enums.FramebufferSlot;
import thothbot.parallax.core.client.gl2.enums.FrontFaceDirection;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.PixelStoreParameter;
import thothbot.parallax.core.client.gl2.enums.PixelType;
import thothbot.parallax.core.client.gl2.enums.ProgramParameter;
import thothbot.parallax.core.client.gl2.enums.RenderbufferInternalFormat;
import thothbot.parallax.core.client.gl2.enums.RenderbufferParameterName;
import thothbot.parallax.core.client.gl2.enums.ShaderPrecisionSpecifiedTypes;
import thothbot.parallax.core.client.gl2.enums.Shaders;
import thothbot.parallax.core.client.gl2.enums.StencilFunction;
import thothbot.parallax.core.client.gl2.enums.StencilOp;
import thothbot.parallax.core.client.gl2.enums.TextureParameterName;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.client.gl2.enums.TextureUnit;

import com.google.gwt.canvas.dom.client.Context;
import com.google.gwt.canvas.dom.client.ImageData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.VideoElement;

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
	  bindFramebuffer(WebGLConstants.FRAMEBUFFER, buffer);
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
	  bindRenderbuffer(WebGLConstants.RENDERBUFFER, buffer);
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
	  return FramebufferErrorCode.parseErrorCode(checkFramebufferStatus(WebGLConstants.FRAMEBUFFER));
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
		  PixelFormat internalformat, int x, int y, int width, int height,
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

  /**
   * Specify whether front- or back-facing facets can be culled.
   * 
   * @param mode
   */
  public void cullFace(CullFaceMode mode) {
	  cullFace(mode.getValue());
  }

  private native void cullFace(int mode) /*-{
		this.cullFace(mode);
  }-*/;

  /**
   * Delete the buffer object contained in the passed WebGLBuffer as if by 
   * calling glDeleteBuffers. If the buffer has already been deleted the call 
   * has no effect. Note that the buffer object will be deleted when the 
   * WebGLBuffer object is destroyed. This method merely gives the author 
   * greater control over when the buffer object is destroyed.
   * 
   * @param buffer
   */
  public native void deleteBuffer(WebGLBuffer buffer) /*-{
		this.deleteBuffer(buffer);
  }-*/;

  /**
   * Delete the framebuffer object contained in the passed WebGLFramebuffer as 
   * if by calling glDeleteFramebuffers. If the framebuffer has already been 
   * deleted the call has no effect. Note that the framebuffer object will be 
   * deleted when the WebGLFramebuffer object is destroyed. This method merely 
   * gives the author greater control over when the framebuffer object is 
   * destroyed.
   * 
   * @param buffer
   */
  public native void deleteFramebuffer(WebGLFramebuffer buffer) /*-{
		this.deleteFramebuffer(buffer);
  }-*/;

  /**
   * Delete the program object contained in the passed WebGLProgram as if by 
   * calling glDeleteProgram. If the program has already been deleted the call 
   * has no effect. Note that the program object will be deleted when the 
   * WebGLProgram object is destroyed. This method merely gives the author 
   * greater control over when the program object is destroyed.
   * 
   * @param program
   */
  public native void deleteProgram(WebGLProgram program) /*-{
		this.deleteProgram(program);
  }-*/;

  /**
   * Delete the renderbuffer object contained in the passed WebGLRenderbuffer 
   * as if by calling glDeleteRenderbuffers. If the renderbuffer has already 
   * been deleted the call has no effect. Note that the renderbuffer object 
   * will be deleted when the WebGLRenderbuffer object is destroyed. This 
   * method merely gives the author greater control over when the renderbuffer 
   * object is destroyed.
   * 
   * @param buffer
   */
  public native void deleteRenderbuffer(WebGLRenderbuffer buffer) /*-{
		this.deleteRenderbuffer(buffer);
  }-*/;

  /**
   * Delete the shader object contained in the passed WebGLShader as if by 
   * calling glDeleteShader. If the shader has already been deleted the call
   * has no effect. Note that the shader object will be deleted when the 
   * WebGLShader object is destroyed. This method merely gives the author 
   * greater control over when the shader object is destroyed.
   * 
   * @param shader
   */
  public native void deleteShader(WebGLShader shader) /*-{
		this.deleteShader(shader);
  }-*/;

  /**
   * Delete the texture object contained in the passed WebGLTexture as if by 
   * calling glDeleteTextures. If the texture has already been deleted the 
   * call has no effect. Note that the texture object will be deleted when the 
   * WebGLTexture object is destroyed. This method merely gives the author 
   * greater control over when the texture object is destroyed.
   * 
   * @param texture
   */
  public native void deleteTexture(WebGLTexture texture) /*-{
		this.deleteTexture(texture);
  }-*/;

  /**
   * Specify the value used for depth buffer comparisons.
   * 
   * @param func
   */
  public void depthFunc(DepthFunction func) {
	  depthFunc(func.getValue());
  }

  private native void depthFunc(int func) /*-{
		this.depthFunc(func);
  }-*/;

  /**
   * Enable or disable writing into the depth buffer.
   * 
   * @param flag
   */
  public native void depthMask(boolean flag) /*-{
		this.depthMask(flag);
  }-*/;

  /**
   * Specify mapping of depth values from normalized device coordinates to 
   * window coordinates.
   * 
   * @param nearVal Specifies the mapping of the near clipping plane to window 
   * 				coordinates. The initial value is 0.
   * @param farVal Specifies the mapping of the far clipping plane to window
   * 				coordinates. The initial value is 1.
   */
  public native void depthRange(double nearVal, double farVal) /*-{
		this.depthRange(nearVal, farVal);
  }-*/;

  /**
   * Detach a shader object from a program object.
   * 
   * @param program Specifies the program object from which to detach the 
   * 				shader object.
   * @param shader Specifies the shader object to be detached.
   */
  public native void detachShader(WebGLProgram program, WebGLShader shader) /*-{
		this.detachShader(program, shader);
  }-*/;

  /**
   * Disable server-side GL capabilities.
   * 
   * @param cap
   */
  public void disable(EnableCap cap) {
	  disable(cap.getValue());
  }

  private native void disable(int param) /*-{
		this.disable(param);
  }-*/;

  /**
   * Disable a generic vertex attribute array.
   * 
   * @param index Specifies the index of the generic vertex attribute to be 
   * 				disabled.
   */
  public native void disableVertexAttribArray(int index) /*-{
		this.disableVertexAttribArray(index);
  }-*/;

  /**
   * Draw using the currently bound index array.
   * 
   * @param mode Specifies what kind of primitives to render.
   * @param first Specifies the starting index in the enabled arrays.
   * @param count Specifies the number of indices to be rendered.
   */
  public void drawArrays(BeginMode mode, int first, int count) {
	  drawArrays(mode.getValue(), first, count);
  }

  private native void drawArrays(int mode, int first, int count) /*-{
		this.drawArrays(mode, first, count);
  }-*/;

  /**
   * Draw using the currently bound index array. The given offset is in bytes, 
   * and must be a valid multiple of the size of the given type or an 
   * INVALID_VALUE error will be raised.
   * 
   * @param mode Specifies what kind of primitives to render.
   * @param count Specifies the number of elements to be rendered.
   * @param type Specifies the type of the values in indices.
   * @param offset Specifies a pointer to the location where the indices are 
   * 				stored.
   */
  public void drawElements(BeginMode mode, int count, DrawElementsType type, int offset) {
	  drawElements(mode.getValue(), count, type.getValue(), offset);
  }

  private native void drawElements(int mode, int count, int type, int offset) /*-{
		this.drawElements(mode, count, type, offset);
  }-*/;

  /**
   * Enable server-side GL capabilities.
   * 
   * @param cap
   */
  public void enable(EnableCap cap) {
	  enable(cap.getValue());
  }

  private native void enable(int param) /*-{
		this.enable(param);
  }-*/;

  /**
   * Enable a generic vertex attribute array.
   * 
   * @param index Specifies the index of the generic vertex attribute to be 
   * 				disabled.
   */
  public native void enableVertexAttribArray(int index) /*-{
		this.enableVertexAttribArray(index);
  }-*/;

  /**
   * Block until all GL execution is complete.
   */
  public native void finish() /*-{
		this.finish();
  }-*/;

  /**
   * Force execution of GL commands in finite time
   */
  public native void flush() /*-{
		this.flush();
  }-*/;

  /**
   * Attach a renderbuffer object to a framebuffer object.
   * 
   * @param attachment Specifies the attachment point to which renderbuffer 
   * 				should be attached.
   * @param renderbuffer Specifies the renderbuffer object that is to be 
   * 				attached.
   */
  public void framebufferRenderbuffer(FramebufferSlot attachment, WebGLRenderbuffer renderbuffer) {
	  framebufferRenderbuffer(WebGLConstants.FRAMEBUFFER, attachment.getValue(),
			  WebGLConstants.RENDERBUFFER, renderbuffer);
  }

  private native void framebufferRenderbuffer(int target, int attachment, int rbtarget,
      WebGLRenderbuffer rbuffer) /*-{
		this.framebufferRenderbuffer(target, attachment, rbtarget, rbuffer);
  }-*/;

  /**
   * Attach a texture image to a framebuffer object.
   * 
   * @param attachment Specifies the attachment point to which an image from 
   * 				texture should be attached.
   * @param textarget Specifies the texture target.
   * @param texture Specifies the texture object whose image is to be attached.
   * @param level Specifies the mipmap level of the texture image to be 
   * 				attached, which must be 0.
   */
  public void framebufferTexture2D(FramebufferSlot attachment, TextureTarget textarget,
		  WebGLTexture texture, int level) {
	  framebufferTexture2D(WebGLConstants.FRAMEBUFFER, attachment.getValue(),
			  textarget.getValue(), texture, level);
  }
  
  /**
   * @see #framebufferTexture2D(FramebufferSlot, TextureTarget, WebGLTexture, int)
   * @param attachment
   * @param textarget
   * @param slot
   */
  public void framebufferTexture2D(FramebufferSlot attachment, TextureTarget textarget, int slot,
		  WebGLTexture texture, int level) {
	  framebufferTexture2D(WebGLConstants.FRAMEBUFFER, attachment.getValue(),
			  textarget.getValue() + slot, texture, level);
  }

  private native void framebufferTexture2D(int target, int att, int textarget, WebGLTexture tex,
      int level) /*-{
		this.framebufferTexture2D(target, att, textarget, tex, level);
  }-*/;

  /**
   * Define front- and back-facing polygons.
   * 
   * @param mode
   */
  public void frontFace(FrontFaceDirection mode) {
	  frontFace(mode.getValue());
  }

  private native void frontFace(int mode) /*-{
		this.frontFace(mode);
  }-*/;

  /**
   * If an attempt is made to call this function with no WebGLTexture bound, 
   * an INVALID_OPERATION error is raised.
   * 
   * @param target
   */
  public void generateMipmap(TextureTarget target) {
	  generateMipmap(target.getValue());
  }

  private native void generateMipmap(int target) /*-{
		this.generateMipmap(target);
  }-*/;

  /**
   * Returns information about the size, type and name of the vertex attribute 
   * at the passed index of the passed program object.
   * 
   * @param program
   * @param index
   */
  public native WebGLActiveInfo getActiveAttrib(WebGLProgram program, int index) /*-{
		return this.getActiveAttrib(program, index);
  }-*/;

  /**
   * Returns information about the size, type and name of the uniform at the 
   * passed index of the passed program object.
   * 
   * @param program
   * @param index
   */
  public native WebGLActiveInfo getActiveUniform(WebGLProgram program, int index) /*-{
		return this.getActiveUniform(program, index);
  }-*/;
  
  public WebGLShaderPrecisionFormat getShaderPrecisionFormat(Shaders shader, ShaderPrecisionSpecifiedTypes precisionType)
  {
	  return getShaderPrecisionFormat(shader.getValue(), precisionType.getValue());
  }

  protected native WebGLShaderPrecisionFormat getShaderPrecisionFormat(int shader, int precisionType) /*-{
  		if ( this.getShaderPrecisionFormat === undefined ) 
  		{
			this.getShaderPrecisionFormat = function () {

				return {
					'rangeMin': 1,
					'rangeMax': 1,
					'precision': 1
				};
			}
		}
  	
		return this.getShaderPrecisionFormat(shader, precisionType);
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
  
  private native com.google.gwt.core.client.JsArray<WebGLShader> getAttachedShadersDev(
	      WebGLProgram program) /*-{
			return this.getAttachedShaders(program);
  }-*/;

  private native WebGLShader[] getAttachedShadersProd(WebGLProgram program) /*-{
			return this.getAttachedShaders(program);
  }-*/;

  /**
   * Returns the location of an attribute variable.
   * 
   * @param program Specifies the program object to be queried.
   * @param name Points to string containing the name of the attribute variable 
   * 				whose location is to be queried.
   */
  public native int getAttribLocation(WebGLProgram program, String name) /*-{
		return this.getAttribLocation(program, name);
  }-*/;
  
  /**
   * Return parameters of a buffer object
   * 
   * @param target Specifies the target buffer object.
   * @param pname Specifies the symbolic name of a buffer object parameter.
   * @return the value for the passed pname. The type returned is the natural 
   * 				type for the requested pname.
   */
  public int getBufferParameteri(BufferTarget target, BufferParameterName pname) {
	  return getBufferParameteri(target.getValue(), pname.getValue());
  }

  private native int getBufferParameteri(int target, int pname) /*-{
		return this.getBufferParameter(target, pname);
  }-*/;

  /**
   * Return error information.
   */
  public ErrorCode getError() {
	  return ErrorCode.parseErrorCode(getErrorImpl());
  }

  private native int getErrorImpl() /*-{
		return this.getError();
  }-*/;

  /**
   * Returns an object if the passed extension is supported, or null if not. 
   * The object returned from getExtension contains any constants or 
   * functions used by the extension, if any. A returned object may have no 
   * constants or functions if the extension does not define any, but a unique 
   * object must still be returned. That object is used to indicate that the 
   * extension has been enabled.
   */
  public native WebGLExtension getExtension(String name) /*-{
		return this.getExtension(name);
  }-*/;

  /**
   * Return the value for the passed pname given the passed target and 
   * attachment.
   * 
   * @param attachment Specifies the symbolic name of a framebuffer object 
   * 				attachment point.
   * @param pname Specifies the symbolic name of a framebuffer object 
   * 				attachment parameter.
   */
  public JavaScriptObject getFramebufferAttachmentParameter(FramebufferSlot attachment, 
		  FramebufferParameterName pname) {
	  return getFramebufferAttachmentParameter(WebGLConstants.FRAMEBUFFER,
			  attachment.getValue(), pname.getValue());
  }

  private native JavaScriptObject getFramebufferAttachmentParameter(int target, int attachment,
      int pname) /*-{
		return this
				.getFramebufferAttachmentParameter(target, attachment, pname);
  }-*/;

  /**
   * Return the value for the passed pname given the passed target and 
   * attachment.
   * 
   * @param attachment Specifies the symbolic name of a framebuffer object 
   * 				attachment point.
   * @param pname Specifies the symbolic name of a framebuffer object 
   * 				attachment parameter.
   */
  public int getFramebufferAttachmentParameteri(
		  FramebufferSlot attachment, FramebufferParameterName pname) {
	  return getFramebufferAttachmentParameteri(WebGLConstants.FRAMEBUFFER,
			  attachment.getValue(), pname.getValue());
  }

  private native int getFramebufferAttachmentParameteri(int target, int attachment, int pname) /*-{
		return this
				.getFramebufferAttachmentParameter(target, attachment, pname);
  }-*/;

  /**
   * Return the value or values of a selected parameter.
   * 
   * @param pname
   */
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

  /**
   * Returns the information log for a program object.
   * 
   * @param program Specifies the program object whose information log is to 
   * 				be queried.
   */
  public native String getProgramInfoLog(WebGLProgram program) /*-{
		return this.getProgramInfoLog(program);
  }-*/;
  
  /**
   * Return the value for the passed pname given the passed program.
   * 
   * @param program Specifies the program object to be queried.
   * @param pname Specifies the object parameter.
   */
  public boolean getProgramParameterb(WebGLProgram program,
		  ProgramParameter pname) {
	  return getProgramParameterb(program, pname.getValue());
  }

  private native boolean getProgramParameterb(WebGLProgram program, int pname) /*-{
		return this.getProgramParameter(program, pname);
  }-*/;

  /**
   * @see #getProgramParameterb(WebGLProgram, ProgramParameter)
   * 
   * @param program
   * @param pname
   */
  public int getProgramParameteri(WebGLProgram program, ProgramParameter pname) {
	  return getProgramParameteri(program, pname.getValue());
  }

  private native int getProgramParameteri(WebGLProgram program, int pname) /*-{
		return this.getProgramParameter(program, pname);
  }-*/;

  /**
   * Return the value for the passed pname given the passed target.
   * 
   * @param pname Specifies the symbolic name of a renderbuffer object 
   * 				parameter.
   */
  public int getRenderbufferParameteri(RenderbufferParameterName pname) {
	  return getRenderbufferParameteri(WebGLConstants.RENDERBUFFER, pname.getValue());
  }

  private native int getRenderbufferParameteri(int target, int pname) /*-{
		return this.getRenderbufferParameter(target, pname);
  }-*/;

  /**
   * Returns the information log for a shader object.
   * 
   * @param shader Specifies the shader object whose information log is to be 
   * 				queried.
   */
  public native String getShaderInfoLog(WebGLShader shader) /*-{
		return this.getShaderInfoLog(shader);
  }-*/;

  /**
   * Return the value for the passed pname given the passed shader.
   * 
   * @param shader Specifies the shader object to be queried.
   * @param pname Specifies the object parameter.
   */
  public native boolean getShaderParameterb(WebGLShader shader, int pname) /*-{
		return this.getShaderParameter(shader, pname);
  }-*/;

  /**
   * @see #getShaderParameterb(WebGLShader, int)
   * 
   * @param shader
   * @param pname
   */
  public native int getShaderParameteri(WebGLShader shader, int pname) /*-{
		return this.getShaderParameter(shader, pname);
  }-*/;

  /**
   * Returns the source code string from a shader object.
   * 
   * @param shader Specifies the shader object to be queried.
   */
  public native String getShaderSource(WebGLShader shader) /*-{
		return this.getShaderSource(shader);
  }-*/;

  /**
   * Returns an array of all the supported extension strings. Any string in 
   * this list, when passed to getExtension must return a valid object. Any 
   * other string passed to getExtension must return null.
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

  /**
   * @see #getSupportedExtensions()
   */
  public native JsArrayString getSupportedExtensionsAsJsArray() /*-{
		return this.getSupportedExtensions();
  }-*/;

  public native int getTexParameteri(int target, int pname) /*-{
		return this.getTexParameter(target, pname);
  }-*/;

  /**
   * Return the uniform value at the passed location in the passed program. 
   * 
   * @param <T> return type is dependent on the type of the uniform variable.
   * @param program Specifies the program object to be queried.
   * @param location Specifies the location of the uniform variable to be 
   * 				queried.
   * @return The type returned is dependent on the uniform type.
   */
  public native <T extends thothbot.parallax.core.client.gl2.arrays.TypeArray> T getUniform(
      WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  /**
   * Return the uniform value at the passed location in the passed program. 
   * 
   * @param program Specifies the program object to be queried.
   * @param location Specifies the location of the uniform variable to be 
   * 				queried.
   */
  public native boolean getUniformb(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  /**
   * Return the uniform value at the passed location in the passed program. 
   * 
   * @param program Specifies the program object to be queried.
   * @param location Specifies the location of the uniform variable to be 
   * 				queried.
   * @return The type returned is dependent on the uniform type.
   */
  public native double getUniformf(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  /**
   * Return the uniform value at the passed location in the passed program. 
   * 
   * @param program Specifies the program object to be queried.
   * @param location Specifies the location of the uniform variable to be 
   * 				queried.
   */
  public native int getUniformi(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  /**
   * Return the location of a uniform variable.
   * 
   * @param program Specifies the program object to be queried.
   * @param name Points to a string containing the name of the uniform variable 
   * 				whose location is to be queried.
   */
  public native WebGLUniformLocation getUniformLocation(WebGLProgram program, String name) /*-{
		return this.getUniformLocation(program, name);
  }-*/;

  /**
   * Return the information requested in pname about the vertex attribute at 
   * the passed index. 
   * 
   * @param <T> return type is dependent on pname.
   * @param index Specifies the generic vertex attribute parameter to be 
   * 				queried.
   * @param pname Specifies the symbolic name of the vertex attribute parameter
   * 				to be queried.
   * @return The type returned is dependent on the information requested.
   */
  public native <T extends JavaScriptObject> T getVertexAttrib(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  /**
   * Return the information requested in pname about the vertex attribute at 
   * the passed index. 
   * 
   * @param index Specifies the generic vertex attribute parameter to be 
   * 				queried.
   * @param pname Specifies the symbolic name of the vertex attribute parameter
   * 				to be queried.
   * @return The type returned is dependent on the information requested.
   */
  public native boolean getVertexAttribb(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  /**
   * Return the information requested in pname about the vertex attribute at 
   * the passed index. 
   * 
   * @param index Specifies the generic vertex attribute parameter to be 
   * 				queried.
   * @param pname Specifies the symbolic name of the vertex attribute parameter
   * 				to be queried.
   * @return The type returned is dependent on the information requested.
   */
  public native int getVertexAttribi(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native int getVertexAttribOffset(int index, String pname) /*-{
		return this.getVertexAttribOffset(index, pname);
  }-*/;

  /**
   * Determine if a name corresponds to a buffer object.
   * 
   * @param buffer
   */
  public native boolean isBuffer(WebGLBuffer buffer) /*-{
		return this.isBuffer(buffer);
  }-*/;

  /**
   * Return true if the passed RendererObject is a WebGLFramebuffer and false 
   * otherwise.
   * 
   * @param buffer
   */
  public native boolean isFramebuffer(JavaScriptObject buffer) /*-{
		return this.isFramebuffer(buffer);
  }-*/;

  /**
   * Return true if the passed RendererObject is a WebGLProgram and false 
   * otherwise.
   * 
   * @param program
   */
  public native boolean isProgram(WebGLProgram program) /*-{
		return this.isProgram(program);
  }-*/;

  /**
   * Return true if the passed RendererObject is a WebGLRenderbuffer and false 
   * otherwise.
   * 
   * @param buffer
   */
  public native boolean isRenderbuffer(WebGLRenderbuffer buffer) /*-{
		return this.isRenderbuffer(buffer);
  }-*/;

  /**
   * Return true if the passed RendererObject is a WebGLShader and false 
   * otherwise.
   * 
   * @param shader
   */
  public native boolean isShader(JavaScriptObject shader) /*-{
		return this.isShader(shader);
  }-*/;

  /**
   * Return true if the passed RendererObject is a WebGLTexture and false 
   * otherwise.
   * 
   * @param texture
   */
  public native boolean isTexture(WebGLTexture texture) /*-{
		return this.isTexture(texture);
  }-*/;

  /**
   * Specifies the width of rasterized lines. The initial value is 1.
   * 
   * @param width
   */
  public native void lineWidth(double width) /*-{
		this.lineWidth(width);
  }-*/;

  /**
   * Link a program object.
   * 
   * @param program Specifies the handle of the program object to be linked.
   */
  public native void linkProgram(WebGLProgram program) /*-{
		this.linkProgram(program);
  }-*/;

  /**
   * Set pixel storage modes.
   * 
   * @param pname
   * @param param
   */
  public void pixelStorei(PixelStoreParameter pname, int param) {
	  pixelStorei(pname.getValue(), param);
  }

  private native void pixelStorei(int pname, int param) /*-{
		this.pixelStorei(pname, param);
  }-*/;

  /**
   * Set the scale and units used to calculate depth values.
   * 
   * @param factor Specifies a scale factor that is used to create a variable 
   * 				depth offset for each polygon. The initial value is 0.
   * @param units Is multiplied by an implementation-specific value to create a
   * 				constant depth offset. The initial value is 0.
   */
  public native void polygonOffset(double factor, double units) /*-{
		this.polygonOffset(factor, units);
  }-*/;

  /**
   * Fills pixels with the pixel data in the specified rectangle of the frame 
   * buffer. The data returned from readPixels must be up-to-date as of the 
   * most recently sent drawing command.
   *  
   * For any pixel lying outside the frame buffer, the value read contains 0 
   * in all channels.
   *  
   * @param x
   * @param y
   * @param width
   * @param height
   * @param format
   * @param type The type of pixels must match the type of the data to be read.
   * 				If it is UNSIGNED_BYTE, a Uint8Array must be supplied; if it is
   * 				UNSIGNED_SHORT_5_6_5, UNSIGNED_SHORT_4_4_4_4, or 
   * 				UNSIGNED_SHORT_5_5_5_1, a Uint16Array must be supplied. If the 
   * 				types do not match, an INVALID_OPERATION error is generated.
   * @param pixels If pixels is null, an INVALID_VALUE error is generated. 
   * 				If pixels is non-null, but is not large enough to retrieve all of 
   * 				the pixels in the specified rectangle taking into account pixel 
   * 				store modes, an INVALID_OPERATION value is generated.
   */
  public void readPixels(int x, int y, int width, int height,
		  PixelFormat format, PixelType type, ArrayBufferView pixels) {
	  readPixels(x, y, width, height, format.getValue(), type.getValue(),
			  pixels);
  }

  private native void readPixels(int x, int y, int width, int height, int format, int type,
      ArrayBufferView pixels) /*-{
		this.readPixels(x, y, width, height, format, type, pixels);
  }-*/;

  /**
   * Create and initialize a renderbuffer object's data store.
   * 
   * @param internalformat Specifies the color-renderable, depth-renderable, 
   * 				or stencil-renderable format of the renderbuffer.
   * @param width Specifies the width of the renderbuffer in pixels.
   * @param height Specifies the height of the renderbuffer in pixels.
   */
  public void renderbufferStorage(RenderbufferInternalFormat internalformat, int width, int height) {
	  renderbufferStorage(WebGLConstants.RENDERBUFFER, internalformat.getValue(), 
			  width, height);
  }

  private native void renderbufferStorage(int target, int format, int width, int height) /*-{
		this.renderbufferStorage(target, format, width, height);
  }-*/;

  /**
   * Specify multisample coverage parameters.
   * 
   * @param value Specify a single floating-point sample coverage value. The 
   * 				value is clamped to the range 0 1 . The initial value is 1.0.
   * @param invert Specify a single boolean value representing if the coverage 
   * 				masks should be inverted.
   */
  public native void sampleCoverage(double value, boolean invert) /*-{
		this.sampleCoverage(value, invert);
  }-*/;

  /**
   * Define the scissor box.
   * 
   * @param x Specify the lower left corner of	the scissor box. Initially 0.
   * @param y Specify the lower left corner of	the scissor box. Initially 0.
   * @param width Specify the width of the scissor box. When a GL context is 
   * 				first attached to a window, width and height are set to the 
   * 				dimensions	of that	window.
   * @param height Specify the height of the scissor box. When a GL context is 
   * 				first attached to a window, width and height are set to the 
   * 				dimensions	of that	window.
   */
  public native void scissor(int x, int y, int width, int height) /*-{
		this.scissor(x, y, width, height);
  }-*/;

  /**
   * Replace the source code in a shader object.
   * 
   * @param shader Specifies the handle of the shader object whose source code 
   * 				is to be replaced.
   * @param source Specifies a string containing the source code to be loaded 
   * 				into the shader.
   */
  public native void shaderSource(WebGLShader shader, String source) /*-{
		this.shaderSource(shader, source);
  }-*/;

  /**
   * Set front and back function and reference value for stencil testing.
   * 
   * @param func Specifies the test function.
   * @param ref Specifies the reference value for the stencil test. ref is 
   * 				clamped to the range 0 to 2^n - 1 , where n is the number of 
   * 				bitplanes	in the stencil buffer. The initial value is 0.
   * @param mask Specifies a mask that is ANDed with both the reference value 
   * 				and the stored stencil value when the test is done. The initial 
   * 				value is all 1's.
   */
  public void stencilFunc(StencilFunction func, int ref, int mask) {
	  stencilFunc(func.getValue(), ref, mask);
  }

  private native void stencilFunc(int func, int ref, int mask) /*-{
		this.stencilFunc(func, ref, mask);
  }-*/;

  /**
   * Set front and/or back function and reference value for stencil testing.
   * 
   * @param face Specifies whether front and/or back stencil state is updated.
   * @param func Specifies the test function.
   * @param ref Specifies the reference value for the stencil test. ref is 
   * 				clamped to the range 0 to 2^n - 1 , where n is the number of 
   * 				bitplanes	in the stencil buffer. The initial value is 0.
   * @param mask Specifies a mask that is ANDed with both the reference value 
   * 				and the stored stencil value when the test is done. The initial 
   * 				value is all 1's.
   */
  public void stencilFuncSeparate(CullFaceMode face, StencilFunction func,
		  int ref, int mask) {
	  stencilFuncSeparate(face.getValue(), func.getValue(), ref, mask);
  }

  public native void stencilFuncSeparate(int face, int func, int ref, int mask) /*-{
		this.stencilFuncSeparate(face, func, ref, mask);
  }-*/;

  /**
   * Specifies a bit mask to enable and disable writing of individual bits in 
   * the stencil planes. Initially, the mask is all 1's.
   * 
   * @param mask Specifies a bit mask to enable and disable writing of 
   * 				individual bits in the stencil planes. Initially, the mask is 
   * 				all 1's.
   */
  public native void stencilMask(int mask) /*-{
		this.stencilMask(mask);
  }-*/;

  /**
   * Control the front and/or back writing of individual bits in the stencil 
   * planes.
   * 
   * @param face Specifies whether the front and/or back stencil writemask is 
   * 				updated.
   * @param mask Specifies a bit mask to enable and disable writing of 
   * 				individual bits in the stencil planes. Initially, the mask is 
   * 				all 1's.
   */
  public void stencilMaskSeparate(CullFaceMode face, int mask) {
	  stencilMaskSeparate(face.getValue(), mask);
  }

  private native void stencilMaskSeparate(int face, int mask) /*-{
		this.stencilMaskSeparate(face, mask);
  }-*/;

  /**
   * Sets front and back stencil test actions.
   * 
   * @param fail
   * @param zfail
   * @param zpass
   */
  public void stencilOp(StencilOp fail, StencilOp zfail, StencilOp zpass) {
	  stencilOp(fail.getValue(), zfail.getValue(), zpass.getValue());
  }

  private native void stencilOp(int sfail, int dpfail, int dppass) /*-{
		this.stencilOp(sfail, dpfail, dppass);
  }-*/;

  /**
   * Sets front and/or back stencil test actions.
   * 
   * @param face
   * @param fail
   * @param zfail
   * @param zpass
   */
  public void stencilOpSeparate(CullFaceMode face, StencilOp fail, StencilOp zfail, StencilOp zpass) {
	  stencilOpSeparate(face.getValue(), fail.getValue(), zfail.getValue(),
			  zpass.getValue());
  }

  private native void stencilOpSeparate(int face, int sfail, int dpfail, int dppass) /*-{
		this.stencilOpSeparate(face, sfail, dpfail, dppass);
  }-*/;

  /**
   * Specify a two-dimensional texture image.
   * 
   * If the passed pixels value is null a buffer of sufficient size initialized 
   * to 0 is passed. If an attempt is made to call this function with no 
   * WebGLTexture bound, an INVALID_OPERATION error is raised.
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param width Specifies the width of the texture subimage.
   * @param height Specifies the height of the texture subimage.
   * @param border Specifies the width of the border. Must be 0.
   * @param format Specifies the format of the texel data. 
   * @param type Specifies the data type of the texel data.
   * @param pixels Specifies a pointer to the image data in memory.
   */
  public void texImage2D(TextureTarget target, int level,
		  int width, int height, int border, PixelFormat format, PixelType type, ArrayBufferView pixels) {
	  texImage2D(target.getValue(), level, format.getValue(), 
			  width, height, border, format.getValue(), type.getValue(), pixels);
  }
  
  /**
   * Specify a two-dimensional texture image.
   * 
   * If the passed pixels value is null a buffer of sufficient size initialized 
   * to 0 is passed. If an attempt is made to call this function with no 
   * WebGLTexture bound, an INVALID_OPERATION error is raised.
   * 
   * @param target Specifies the target texture.
   * @param slot   the target texture offset
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param width Specifies the width of the texture subimage.
   * @param height Specifies the height of the texture subimage.
   * @param border Specifies the width of the border. Must be 0.
   * @param format Specifies the format of the texel data. 
   * @param type Specifies the data type of the texel data.
   * @param pixels Specifies a pointer to the image data in memory.
   */
  public void texImage2D(TextureTarget target, int slot, int level,
		  int width, int height, int border, PixelFormat format, PixelType type, ArrayBufferView pixels) {
	  texImage2D(target.getValue() + slot, level, format.getValue(), 
			  width, height, border, format.getValue(), type.getValue(), pixels);
  }

  private native void texImage2D(int target, int level, int internalformat, int width, int height,
		  int border, int format, int type, ArrayBufferView pixels) /*-{
				this.texImage2D(target, level, internalformat, width, height, border,
						format, type, pixels);
  }-*/;

  /**
   * Uploads the given element or image data to the currently bound WebGLTexture.
   * 
   * The source image data is conceptually first converted to the data type and format 
   * specified by the format and type arguments, and then transferred to the OpenGL 
   * implementation. If a packed pixel format is specified which would imply loss of 
   * bits of precision from the image data, this loss of precision must occur. 
   * 
   * If the source image is an RGB or RGBA lossless image with 8 bits per channel, the 
   * browser guarantees that the full precision of all channels is preserved. 
   * 
   * If the original image semantically contains an alpha channel and the 
   * UNPACK_PREMULTIPLY_ALPHA_WEBGL pixel storage parameter is false, then the alpha 
   * channel is guaranteed to never have been premultiplied by the RGB values, whether 
   * those values are derived directly from the original file format or converted from 
   * some other color format. 
   * 
   * If an attempt is made to call this function with no WebGLTexture bound (see above), 
   * an INVALID_OPERATION error is generated. 
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param format Specifies the format of the texel data. 
   * @param type Specifies the data type of the texel data.
   * @param pixels
   */
  public void texImage2D(TextureTarget target, int level, 
		  PixelFormat format, 
		  PixelType type, CanvasElement pixels) {
	  texImage2D(target.getValue(), level, format.getValue(), 
			  format.getValue(), type.getValue(), pixels);
  }
  
  public void texImage2D(TextureTarget target, int slot, int level, 
		  PixelFormat format, 
		  PixelType type, CanvasElement pixels) {
	  texImage2D(target.getValue() + slot, level, format.getValue(), 
			  format.getValue(), type.getValue(), pixels);
  }

  /**
   * Uploads the given element or image data to the currently bound WebGLTexture.
   * 
   * The source image data is conceptually first converted to the data type and format 
   * specified by the format and type arguments, and then transferred to the OpenGL 
   * implementation. If a packed pixel format is specified which would imply loss of 
   * bits of precision from the image data, this loss of precision must occur. 
   * 
   * If the source image is an RGB or RGBA lossless image with 8 bits per channel, the 
   * browser guarantees that the full precision of all channels is preserved. 
   * 
   * If the original image semantically contains an alpha channel and the 
   * UNPACK_PREMULTIPLY_ALPHA_WEBGL pixel storage parameter is false, then the alpha 
   * channel is guaranteed to never have been premultiplied by the RGB values, whether 
   * those values are derived directly from the original file format or converted from 
   * some other color format. 
   * 
   * If an attempt is made to call this function with no WebGLTexture bound (see above), 
   * an INVALID_OPERATION error is generated. 
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param format Specifies the format of the texel data.
   * @param type Specifies the data type of the texel data.
   * @param pixels
   */
  public void texImage2D(TextureTarget target, int level, 
		  PixelFormat format, 
		  PixelType type, ImageData pixels) {
	  texImage2D(target.getValue(), level, format.getValue(), 
			  format.getValue(), type.getValue(), pixels);
  }

  /**
   * Uploads the given element or image data to the currently bound WebGLTexture.
   * 
   * The source image data is conceptually first converted to the data type and format 
   * specified by the format and type arguments, and then transferred to the OpenGL 
   * implementation. If a packed pixel format is specified which would imply loss of 
   * bits of precision from the image data, this loss of precision must occur. 
   * 
   * If the source image is an RGB or RGBA lossless image with 8 bits per channel, the 
   * browser guarantees that the full precision of all channels is preserved. 
   * 
   * If the original image semantically contains an alpha channel and the 
   * UNPACK_PREMULTIPLY_ALPHA_WEBGL pixel storage parameter is false, then the alpha 
   * channel is guaranteed to never have been premultiplied by the RGB values, whether 
   * those values are derived directly from the original file format or converted from 
   * some other color format. 
   * 
   * If an attempt is made to call this function with no WebGLTexture bound (see above), 
   * an INVALID_OPERATION error is generated. 
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param format Specifies the format of the texel data.
   * @param type Specifies the data type of the texel data.
   * @param pixels
   */
  public void texImage2D(TextureTarget target, int level, 
		  PixelFormat format, 
		  PixelType type, ImageElement pixels) {
	  texImage2D(target.getValue(), level, format.getValue(), 
			  format.getValue(), type.getValue(), pixels);
  }
  
  public void texImage2D(TextureTarget target, int slot, int level, 
		  PixelFormat format, 
		  PixelType type, ImageElement pixels) {
	  texImage2D(target.getValue() + slot, level, format.getValue(), 
			  format.getValue(), type.getValue(), pixels);
  }

  /**
   * Uploads the given element or image data to the currently bound WebGLTexture.
   * 
   * The source image data is conceptually first converted to the data type and format 
   * specified by the format and type arguments, and then transferred to the OpenGL 
   * implementation. If a packed pixel format is specified which would imply loss of 
   * bits of precision from the image data, this loss of precision must occur. 
   * 
   * If the source image is an RGB or RGBA lossless image with 8 bits per channel, the 
   * browser guarantees that the full precision of all channels is preserved. 
   * 
   * If the original image semantically contains an alpha channel and the 
   * UNPACK_PREMULTIPLY_ALPHA_WEBGL pixel storage parameter is false, then the alpha 
   * channel is guaranteed to never have been premultiplied by the RGB values, whether 
   * those values are derived directly from the original file format or converted from 
   * some other color format. 
   * 
   * If an attempt is made to call this function with no WebGLTexture bound (see above), 
   * an INVALID_OPERATION error is generated. 
   * 
   * @param target Specifies the target texture.
   * @param level Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param format Specifies the format of the texel data. 
   * @param type Specifies the data type of the texel data.
   * @param pixels
   */
  public void texImage2D(TextureTarget target, int level, 
		  PixelFormat format, 
		  PixelType type, VideoElement pixels) {
	  texImage2D(target.getValue(), level, format.getValue(), 
			  format.getValue(), type.getValue(), pixels);
  }
	
  private native void texImage2D(int target, int level, int internalformat, int format, int type,
		  JavaScriptObject data) /*-{
		this.texImage2D(target, level, internalformat, format, type, data);
  }-*/;
  
  /**
   * Specify a two-dimensional compressed texture.
   * 
   * If the passed pixels value is null a buffer of sufficient size initialized 
   * to 0 is passed. If an attempt is made to call this function with no 
   * WebGLTexture bound, an INVALID_OPERATION error is raised.
   * 
   * @param target Specifies the target texture.
   * @param level  Specifies the level-of-detail number. Level 0 is the base 
   * 				image level. Level n is the nth mipmap reduction image.
   * @param format Specifies the format of the compressed texel data. 
   * @param width  Specifies the width of the texture subimage.
   * @param height Specifies the height of the texture subimage.
   * @param border Specifies the width of the border. Must be 0.
   * @param pixels Specifies a pointer to the image data in memory.
   */
  public void compressedTexImage2D(TextureTarget target, int level, int format,  
		  int width, int height, int border, ArrayBufferView pixels) {
	  compressedTexImage2D(target.getValue(), level, format, width, height, border, pixels);
  }
  
  private native void compressedTexImage2D(int target, int level, int internalformat, int width, int height,
		  int border, ArrayBufferView pixels) /*-{
				this.compressedTexImage2D(target, level, internalformat, width, height, border, pixels);
  }-*/;

  /**
   * If an attempt is made to call this function with no WebGLTexture bound, 
   * an INVALID_OPERATION error is raised.
   */
  public void texParameterf(TextureTarget target, TextureParameterName pname, double param) {
	  texParameterf(target.getValue(), pname.getValue(), param);
  }

  private native void texParameterf(int target, int pname, double value) /*-{
		this.texParameterf(target, pname, value);
  }-*/;

  /**
   * If an attempt is made to call this function with no WebGLTexture bound, 
   * an INVALID_OPERATION error is raised.
   */
  public void texParameteri(TextureTarget target, TextureParameterName pname, int param) {
	  texParameteri(target.getValue(), pname.getValue(), param);
  }

  private native void texParameteri(int target, int pname, int value) /*-{
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

  /**
   * Sets the specified uniform to the values provided. 
   * 
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   */
  public native void uniform1f(WebGLUniformLocation location, double v0) /*-{
		this.uniform1f(location, v0);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform1fv(WebGLUniformLocation location, double[] values) {
    uniform1fv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform1fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform1fv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform1fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform1fv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   */
  public native void uniform1i(WebGLUniformLocation location, int v0) /*-{
		this.uniform1i(location, v0);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform1iv(WebGLUniformLocation location, int[] values) {
    uniform1iv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform1iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform1iv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform1iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform1iv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   * @param v1
   */
  public native void uniform2f(WebGLUniformLocation location, double v0, double v1) /*-{
		this.uniform2f(location, v0, v1);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform2fv(WebGLUniformLocation location, double[] values) {
    uniform2fv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform2fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform2fv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform2fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform2fv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   * @param v1
   */
  public native void uniform2i(WebGLUniformLocation location, int v0, int v1) /*-{
		this.uniform2i(location, v0, v1);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform2iv(WebGLUniformLocation location, int[] values) {
    uniform2iv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform2iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform2iv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform2iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform2iv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   * @param v1
   * @param v2
   */
  public native void uniform3f(WebGLUniformLocation location, double v0, double v1, double v2) /*-{
		this.uniform3f(location, v0, v1, v2);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform3fv(WebGLUniformLocation location, double[] values) {
    uniform3fv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform3fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform3fv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform3fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform3fv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   * @param v1
   * @param v2
   */
  public native void uniform3i(WebGLUniformLocation location, int v0, int v1, int v2) /*-{
		this.uniform3i(location, v0, v1, v2);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform3iv(WebGLUniformLocation location, int[] values) {
    uniform3iv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform3iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform3iv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform3iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform3iv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   * @param v1
   * @param v2
   * @param v3
   */
  public native void uniform4f(WebGLUniformLocation location, double v0, double v1, double v2, double v3) /*-{
		this.uniform4f(location, v0, v1, v2, v3);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform4fv(WebGLUniformLocation location, double[] values) {
    uniform4fv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform4fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform4fv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform4fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform4fv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v0
   * @param v1
   * @param v2
   * @param v3
   */
  public native void uniform4i(WebGLUniformLocation location, int v0, int v1, int v2, int v3) /*-{
		this.uniform4i(location, v0, v1, v2, v3);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public void uniform4iv(WebGLUniformLocation location, int[] values) {
    uniform4iv(location, JsArrayUtil.wrapArray(values));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param v
   */
  public native void uniform4iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform4iv(location, v);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param values
   */
  public native void uniform4iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform4iv(location, values);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, double[] value) {
    uniformMatrix2fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public native void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix2fv(location, transpose, value);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public native void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix2fv(location, transpose, value);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, double[] value) {
    uniformMatrix3fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public native void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix3fv(location, transpose, value);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public native void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix3fv(location, transpose, value);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, double[] value) {
    uniformMatrix4fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public native void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix4fv(location, transpose, value);
  }-*/;

  /**
   * Sets the specified uniform to the values provided.
   *  
   * @param location must have been obtained from the currently used program 
   * 				via an earlier call to getUniformLocation, or an INVALID_VALUE 
   * 				error will be raised.
   * @param transpose
   * @param value
   */
  public native void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix4fv(location, transpose, value);
  }-*/;

  /**
   * Install a program object as part of current rendering state.
   * 
   * @param program Specifies the handle of the program object whose 
   * 				executables are to be used as part of current rendering state.
   */
  public native void useProgram(WebGLProgram program) /*-{
		this.useProgram(program);
  }-*/;

  /**
   * Validate a program object.
   * 
   * @param program Specifies the handle of the program object to be validated.
   */
  public native void validateProgram(WebGLProgram program) /*-{
		this.validateProgram(program);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param x Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib1f(int index, double x) /*-{
		this.vertexAttrib1f(index, x);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public void vertexAttrib1fv(int index, double[] values) {
    vertexAttrib1fv(index, JsArrayUtil.wrapArray(values));
  }

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib1fv(int index, Float32Array values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib1fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param x Specifies the new values to be used for the specified vertex 
   * 				attribute.
   * @param y Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib2f(int index, double x, double y) /*-{
		this.vertexAttrib2f(index, x, y);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public void vertexAttrib2fv(int index, double[] values) {
    vertexAttrib2fv(index, JsArrayUtil.wrapArray(values));
  }

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib2fv(int index, Float32Array values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;
  
  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib2fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param x Specifies the new values to be used for the specified vertex 
   * 				attribute.
   * @param y Specifies the new values to be used for the specified vertex 
   * 				attribute.
   * @param z Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib3f(int index, double x, double y, double z) /*-{
		this.vertexAttrib3f(index, x, y, z);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public void vertexAttrib3fv(int index, double[] values) {
    vertexAttrib3fv(index, JsArrayUtil.wrapArray(values));
  }

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib3fv(int index, Float32Array values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib3fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param x Specifies the new values to be used for the specified vertex 
   * 				attribute.
   * @param y Specifies the new values to be used for the specified vertex 
   * 				attribute.
   * @param z Specifies the new values to be used for the specified vertex 
   * 				attribute.
   * @param w Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib4f(int index, double x, double y, double z, double w) /*-{
		this.vertexAttrib4f(index, x, y, z, w);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public void vertexAttrib4fv(int index, double[] values) {
    vertexAttrib4fv(index, JsArrayUtil.wrapArray(values));
  }

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib4fv(int index, Float32Array values) /*-{
		this.vertexAttrib4fv(index, values);
  }-*/;

  /**
   * Specifies the value of a generic vertex attribute.
   * 
   * @param index Specifies the index of the generic vertex attribute to 
   * 				be modified.
   * @param values Specifies the new values to be used for the specified vertex 
   * 				attribute.
   */
  public native void vertexAttrib4fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib4fv(index, values);
  }-*/;

  /**
   * Assign the currently bound WebGLBuffer object to the passed vertex attrib 
   * index. Size is number of components per attribute. Stride and offset are 
   * in units of bytes. Passed stride and offset must be appropriate for the 
   * passed type and size or an INVALID_VALUE error will be raised.
   * 
   * @param indx Specifies the index of the generic vertex attribute to be 
   * 				modified.
   * @param size Specifies the number of components per generic vertex 
   * 				attribute. Must be 1, 2, 3, or 4. The initial value is 4.
   * @param type Specifies the data type of each component in the array.
   * @param normalized Specifies whether fixed-point data values should be 
   * 				normalized true or converted directly as fixed-point values 
   * 				false when they are accessed.
   * @param stride Specifies the byte offset between consecutive generic vertex
   * 				attributes. If stride is 0, the generic vertex attributes are 
   * 				understood to be tightly packed in the array. The initial value 
   * 				is 0.
   * @param offset Specifies a pointer to the first component of the first 
   * 				generic vertex attribute in the array. The initial value is 0.
   */
  public void vertexAttribPointer(int indx, int size, DataType type,
		  boolean normalized, int stride, int offset) {
	  vertexAttribPointer(indx, size, type.getValue(), normalized,
			  stride, offset);
  }

  private native void vertexAttribPointer(int index, int size, int type, boolean normalized,
      int stride, int offset) /*-{
		this.vertexAttribPointer(index, size, type, normalized, stride, offset);
  }-*/;

  /**
   * Set the viewport.
   * 
   * @param x Specify the lower left corner of the viewport rectangle, 
   * 				in pixels. The initial value is 0.
   * @param y Specify the lower left corner of the viewport rectangle, 
   * 				in pixels. The initial value is 0.
   * @param width Specify the width of the viewport. When a GL context is 
   * 				first attached to a window, width and height are set to the 
   * 				dimensions of that window.
   * @param height Specify the height of the viewport. When a GL context is 
   * 				first attached to a window, width and height are set to the 
   * 				dimensions of that window.
   */
  public native void viewport(int x, int y, int width, int height) /*-{
		this.viewport(x, y, width, height);
  }-*/;
}
