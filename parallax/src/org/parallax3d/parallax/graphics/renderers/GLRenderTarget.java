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

package org.parallax3d.parallax.graphics.renderers;

import org.parallax3d.parallax.graphics.textures.AbstractTexture;
import org.parallax3d.parallax.graphics.textures.EmptyTextureData;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Vector4;
import org.parallax3d.parallax.system.AbstractPropertyObject;
import org.parallax3d.parallax.system.Disposable;
import org.parallax3d.parallax.system.EventBus;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.events.DisposeEvent;
import org.parallax3d.parallax.system.gl.enums.*;

@ThreejsObject("THREE.WebGLRenderTarget")
public class GLRenderTarget extends AbstractPropertyObject implements AbstractTexture, Disposable {

    public static class GLRenderTargetOptions
    {
        public boolean depthBuffer = true;
        public boolean stencilBuffer = true;

        public TextureWrapMode wrapS = TextureWrapMode.CLAMP_TO_EDGE;
        public TextureWrapMode wrapT = TextureWrapMode.CLAMP_TO_EDGE;

        public TextureMagFilter magFilter = TextureMagFilter.LINEAR;
        public TextureMinFilter minFilter = TextureMinFilter.LINEAR_MIPMAP_LINEAR;

        public PixelFormat format = PixelFormat.RGBA;
        public PixelType type = PixelType.UNSIGNED_BYTE;

        public int anisotropy = 1;
    }

    int width;
    int height;

    Vector4 scissor;
    boolean scissorTest = false;

    Vector4 viewport;

    boolean depthBuffer = true;
    boolean stencilBuffer = true;

    Texture texture;

    public GLRenderTarget(int width, int height) {
        this(width, height, new GLRenderTargetOptions());
    }

    public GLRenderTarget(int width, int height, GLRenderTargetOptions options) {

        this.scissor = new Vector4(0, 0, width, height);
        this.viewport = new Vector4(0, 0, width, height);

        this.texture = new Texture(new EmptyTextureData(), Texture.MAPPING_MODE.UVMapping,
                options.wrapS, options.wrapT, options.magFilter, options.minFilter,
                options.format, options.type, options.anisotropy);

        this.depthBuffer = options.depthBuffer;
        this.stencilBuffer = options.stencilBuffer;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector4 getScissor() {
        return scissor;
    }

    public void setScissor(Vector4 scissor) {
        this.scissor = scissor;
    }

    public boolean isScissorTest() {
        return scissorTest;
    }

    public void setScissorTest(boolean scissorTest) {
        this.scissorTest = scissorTest;
    }

    public Vector4 getViewport() {
        return viewport;
    }

    public void setViewport(Vector4 viewport) {
        this.viewport = viewport;
    }

    public boolean isDepthBuffer() {
        return depthBuffer;
    }

    public void setDepthBuffer(boolean depthBuffer) {
        this.depthBuffer = depthBuffer;
    }

    public boolean isStencilBuffer() {
        return stencilBuffer;
    }

    public void setStencilBuffer(boolean stencilBuffer) {
        this.stencilBuffer = stencilBuffer;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setSize(int width, int height) {

        if (this.width != width || this.height != height) {

            this.width = width;
            this.height = height;

            this.dispose();

        }

        this.viewport.set(0, 0, width, height);
        this.scissor.set(0, 0, width, height);

    }

    @Override
    public GLRenderTarget clone() {

        return new GLRenderTarget(0, 0).copy(this);

    }

    public GLRenderTarget copy(GLRenderTarget source) {

        this.width = source.width;
        this.height = source.height;

        this.viewport.copy(source.viewport);

        this.texture = source.texture.clone();

        this.depthBuffer = source.depthBuffer;
        this.stencilBuffer = source.stencilBuffer;

        return this;

    }

    @Override
    public void dispose() {

        EventBus.dispatchEvent( new DisposeEvent( this ) );

    }
}
