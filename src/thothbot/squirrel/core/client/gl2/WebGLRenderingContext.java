/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This code based on code written by Sönke Sothmann, Steffen Schäfer and others.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.client.gl2;

import thothbot.squirrel.core.client.gl2.arrays.ArrayBuffer;
import thothbot.squirrel.core.client.gl2.arrays.ArrayBufferView;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.gl2.arrays.Int32Array;
import thothbot.squirrel.core.client.gl2.arrays.JsArrayUtil;
import thothbot.squirrel.core.client.gl2.arrays.TypeArray;

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

  public native void activeTexture(int texture) /*-{
		this.activeTexture(texture);
  }-*/;

  public native void attachShader(WebGLProgram program, WebGLShader shader) /*-{
		this.attachShader(program, shader);
  }-*/;

  public native void bindAttribLocation(WebGLProgram program, int index, String name) /*-{
		this.bindAttribLocation(program, index, name);
  }-*/;

  public native void bindBuffer(int target, WebGLBuffer buffer) /*-{
		this.bindBuffer(target, buffer);
  }-*/;

  public native void bindFramebuffer(int target, WebGLFramebuffer buffer) /*-{
		this.bindFramebuffer(target, buffer);
  }-*/;

  public native void bindRenderbuffer(int target, WebGLRenderbuffer buffer) /*-{
		this.bindRenderbuffer(target, buffer);
  }-*/;

  public native void bindTexture(int target, WebGLTexture texture) /*-{
		this.bindTexture(target, texture);
  }-*/;

  public native void blendColor(float red, float green, float blue, float alpha) /*-{
		this.blendColor(red, green, blue, alpha);
  }-*/;

  public native void blendEquation(int mode) /*-{
		this.blendEquation(mode);
  }-*/;

  public native void blendEquationSeparate(int modeRGB, int modeAlpha) /*-{
		this.blendEquationSeparate(modeRGB, modeAlpha);
  }-*/;

  public native void blendFunc(int sfactor, int dfactor) /*-{
		this.blendFunc(sfactor, dfactor);
  }-*/;

  public native void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) /*-{
		this.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
  }-*/;

  public native void bufferData(int target, ArrayBuffer dta, int usage) /*-{
		this.bufferData(target, dta, usage);
  }-*/;

  public native void bufferData(int target, int size, int usage) /*-{
		this.bufferData(target, size, usage);
  }-*/;

  public native void bufferData(int target, TypeArray dta, int usage) /*-{
		this.bufferData(target, dta, usage);
  }-*/;

  public native void bufferSubData(int target, int offset, ArrayBuffer data) /*-{
		this.bufferSubData(target, offset, data);
  }-*/;

  public native void bufferSubData(int target, int offset, TypeArray data) /*-{
		this.bufferSubData(target, offset, data);
  }-*/;

  public native int checkFramebufferStatus(int target) /*-{
		return this.checkFramebufferStatus(target);
  }-*/;

  public native void clear(int mask) /*-{
		this.clear(mask);
  }-*/;

  public native void clearColor(float red, float green, float blue, float alpha) /*-{
		this.clearColor(red, green, blue, alpha);
  }-*/;

  public native void clearDepth(float depth) /*-{
		this.clearDepth(depth);
  }-*/;

  public native void clearStencil(int s) /*-{
		this.clearStencil(s);
  }-*/;

  public native void colorMask(boolean red, boolean green, boolean blue, boolean alpha) /*-{
		this.colorMask(red, green, blue, alpha);
  }-*/;

  public native void compileShader(WebGLShader shader) /*-{
		this.compileShader(shader);
  }-*/;

  public native void copyTexImage2D(int target, int level, int intformat, int x, int y, int width,
      int height, int border) /*-{
		this.copyTexImage2D(target, level, intformat, x, y, width, height,
				border);
  }-*/;

  public native void copyTexSubImage2D(int target, int level, int intformat, int xoffset,
      int yoffset, int x, int y, int width, int height) /*-{
		this.copyTexSubImage2D(target, level, intformat, xoffset, yoffset, x,
				y, width, height);
  }-*/;

  public native WebGLBuffer createBuffer() /*-{
		return this.createBuffer();
  }-*/;

  public native WebGLFramebuffer createFramebuffer() /*-{
		return this.createFramebuffer();
  }-*/;

  public native WebGLProgram createProgram() /*-{
		return this.createProgram();
  }-*/;

  public native WebGLRenderbuffer createRenderbuffer() /*-{
		return this.createRenderbuffer();
  }-*/;

  public native WebGLShader createShader(int shaderType) /*-{
		return this.createShader(shaderType);
  }-*/;

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

  public native void depthRange(float nearVal, float farVal) /*-{
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

  public native float getParameterf(int pname) /*-{
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

  public native <T extends thothbot.squirrel.core.client.gl2.arrays.TypeArray> T getUniforma(
      WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native boolean getUniformb(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native float getUniformf(WebGLProgram program, WebGLUniformLocation location) /*-{
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

  public native void lineWidth(float width) /*-{
		this.lineWidth(width);
  }-*/;

  public native void linkProgram(WebGLProgram program) /*-{
		this.linkProgram(program);
  }-*/;

  public native void pixelStorei(int pname, int param) /*-{
		this.pixelStorei(pname, param);
  }-*/;

  public native void polygonOffset(float factor, float units) /*-{
		this.polygonOffset(factor, units);
  }-*/;

  public native void readPixels(int x, int y, int width, int height, int format, int type,
      ArrayBufferView pixels) /*-{
		this.readPixels(x, y, width, height, format, type, pixels);
  }-*/;

  public native void renderbufferStorage(int target, int format, int width, int height) /*-{
		this.renderbufferStorage(target, format, width, height);
  }-*/;

  public native void sampleCoverage(float value, boolean invert) /*-{
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

  public native void texParameterf(int target, int pname, float value) /*-{
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

  public native void uniform1f(WebGLUniformLocation location, float v0) /*-{
		this.uniform1f(location, v0);
  }-*/;

  public void uniform1fv(WebGLUniformLocation location, float[] values) {
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

  public native void uniform2f(WebGLUniformLocation location, float v0, float v1) /*-{
		this.uniform2f(location, v0, v1);
  }-*/;

  public void uniform2fv(WebGLUniformLocation location, float[] values) {
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

  public native void uniform3f(WebGLUniformLocation location, float v0, float v1, float v2) /*-{
		this.uniform3f(location, v0, v1, v2);
  }-*/;

  public void uniform3fv(WebGLUniformLocation location, float[] values) {
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

  public native void uniform4f(WebGLUniformLocation location, float v0, float v1, float v2, float v3) /*-{
		this.uniform4f(location, v0, v1, v2, v3);
  }-*/;

  public void uniform4fv(WebGLUniformLocation location, float[] values) {
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

  public void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, float[] value) {
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

  public void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, float[] value) {
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

  public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, float[] value) {
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

  public native void vertexAttrib1f(int index, float x) /*-{
		this.vertexAttrib1f(index, x);
  }-*/;

  public void vertexAttrib1fv(int index, float[] values) {
    vertexAttrib1fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib1fv(int index, Float32Array values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  public native void vertexAttrib1fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  public native void vertexAttrib2f(int index, float x, float y) /*-{
		this.vertexAttrib2f(index, x, y);
  }-*/;

  public void vertexAttrib2fv(int index, float[] values) {
    vertexAttrib2fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib2fv(int index, Float32Array values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  public native void vertexAttrib2fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  public native void vertexAttrib3f(int index, float x, float y, float z) /*-{
		this.vertexAttrib3f(index, x, y, z);
  }-*/;

  public void vertexAttrib3fv(int index, float[] values) {
    vertexAttrib3fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib3fv(int index, Float32Array values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  public native void vertexAttrib3fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  public native void vertexAttrib4f(int index, float x, float y, float z, float w) /*-{
		this.vertexAttrib4f(index, x, y, z, w);
  }-*/;

  public void vertexAttrib4fv(int index, float[] values) {
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
