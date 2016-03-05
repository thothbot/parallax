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

package org.parallax3d.parallax.graphics.textures;

import org.parallax3d.parallax.Parallax;
import org.parallax3d.parallax.files.FileHandle;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.enums.*;

/**
 * Basic implementation of texture.
 * <p>
 * This code based on three.js code.
 * 
 * @author thothbot
 *
 */
@ThreejsObject("THREE.Texture")
public class Texture
{
	/**
	 * This callback will be called when the image has been loaded.
	 */
	public interface ImageLoadHandler
	{
		void onImageLoad(Texture texture);
	}

	public enum OPERATIONS
	{
		MULTIPLY(0), // MultiplyOperation
		MIX(1); // MixOperation

		private final int value;
		OPERATIONS(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	/**
	 * Mapping modes
	 */
	public enum MAPPING_MODE
	{
		UV,

		CUBE_REFLECTION,
		CUBE_REFRACTION,

		SPHERICAL_REFLECTION,
		SPHERICAL_REFRACTION
	};

	private static int TextureCount = 0;

	private TextureData image;

	private int id;

	private Vector2 offset;
	private Vector2 repeat;

	private MAPPING_MODE mapping;

	private TextureWrapMode wrapS;
	private TextureWrapMode wrapT;

	private TextureMagFilter magFilter;
	private TextureMinFilter minFilter;

	private PixelFormat format;
	private PixelType type;

	private boolean isGenerateMipmaps = true;
	private boolean isPremultiplyAlpha = false;
	private boolean isFlipY = true;
	private int unpackAlignment = 4; // valid values: 1, 2, 4, 8 (see http://www.khronos.org/opengles/sdk/docs/man/xhtml/glPixelStorei.xml)

	private boolean isNeedsUpdate = false;

	protected int webglTexture = 0; //WebGLTexture

	private int anisotropy;

	private int cache_oldAnisotropy;

	public Texture(){
		this(new EmptyTextureData());
	}

	public Texture (String internalPath) {
		this( Parallax.asset(internalPath), null );
	}

	public Texture (String internalPath, final ImageLoadHandler imageLoadHandler) {
		this( Parallax.asset(internalPath), imageLoadHandler );
	}

	public Texture (FileHandle file, final ImageLoadHandler imageLoadHandler)
	{
		this( new PixmapTextureData() );

		image.load(file, new PixmapTextureData.TextureLoadHandler() {

			@Override
			public void onLoaded(boolean success) {
				setNeedsUpdate(true);
				if (imageLoadHandler != null)
					imageLoadHandler.onImageLoad(Texture.this);
			}
		});
	}

	/**
	 * Constructor will create a texture instance.
	 *
	 * @param image the Image.
	 */
	public Texture(TextureData image)
	{
		this(image,
				MAPPING_MODE.UV,
				TextureWrapMode.CLAMP_TO_EDGE,
				TextureWrapMode.CLAMP_TO_EDGE,
				TextureMagFilter.LINEAR,
				TextureMinFilter.LINEAR_MIPMAP_LINEAR,
				PixelFormat.RGBA,
				PixelType.UNSIGNED_BYTE,
				1);
	}

	/**
	 * Constructor will create a texture instance.
	 *
	 * @param image     the media element
	 * @param mapping   the @{link Texture.MAPPING_MODE} value
	 * @param wrapS     the wrap parameter for texture coordinate S.
	 * @param wrapT     the wrap parameter for texture coordinate T
	 * @param magFilter the texture magnification function.
	 * @param minFilter the texture minifying function.
	 * @param format    the PixelFormat value.
	 * @param type      the DataType value.
	 * @param anisotropy the anisotropy value.
	 */
	public Texture(TextureData image, MAPPING_MODE mapping, TextureWrapMode wrapS,
				   TextureWrapMode wrapT, TextureMagFilter magFilter, TextureMinFilter minFilter,
				   PixelFormat format, PixelType type, int anisotropy)
	{
		this.image = image;
		this.mapping = mapping;

		this.wrapS = wrapS;
		this.wrapT = wrapT;

		this.magFilter = magFilter;
		this.minFilter = minFilter;

		this.format = format;
		this.type = type;

		this.id = Texture.TextureCount++;
		this.offset = new Vector2(0, 0);
		this.repeat = new Vector2(1, 1);

		this.anisotropy = anisotropy;
	}

	/**
	 * Gets texture ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the @{link Texture.MAPPING_MODE} value.
	 */
	public Texture.MAPPING_MODE getMapping() {
		return this.mapping;
	}

	/**
	 * Sets the @{link Texture.MAPPING_MODE} value.
	 */
	public Texture setMapping(Texture.MAPPING_MODE mapping) {
		this.mapping = mapping;
		return this;
	}

	/**
	 * Sets the wrap parameter for texture coordinate S.
	 *
	 * @param wrapS the wrap parameter
	 */
	public Texture setWrapS(TextureWrapMode wrapS)	{
		this.wrapS = wrapS;
		return this;
	}

	/**
	 * Gets the wrap parameter for texture coordinate S.
	 *
	 * @return the wrap parameter.
	 */
	public TextureWrapMode getWrapS(){
		return this.wrapS;
	}

	/**
	 * Sets the wrap parameter for texture coordinate T.
	 *
	 * @param wrapT the wrap parameter
	 */
	public Texture setWrapT(TextureWrapMode wrapT) {
		this.wrapT = wrapT;
		return this;
	}

	/**
	 * Gets the wrap parameter for texture coordinate T.
	 *
	 * @return the wrap parameter.
	 */
	public TextureWrapMode getWrapT() {
		return this.wrapT;
	}

	/**
	 * Gets the texture magnification function.
	 *
	 * @return the texture magnification function.
	 */
	public TextureMagFilter getMagFilter() {
		return this.magFilter;
	}

	/**
	 * Sets the texture magnification function.
	 */
	public Texture setMagFilter(TextureMagFilter magFilter) {
		this.magFilter = magFilter;
		return this;
	}

	/**
	 * Gets the texture minifying function.
	 *
	 * @return the texture minifying function.
	 */
	public TextureMinFilter getMinFilter() {
		return this.minFilter;
	}

	/**
	 * Sets the texture minifying function.
	 */
	public Texture setMinFilter(TextureMinFilter minFilter) {
		this.minFilter = minFilter;
		return this;
	}

	/**
	 * Checks if the texture needs to be updated.
	 */
	public Boolean isNeedsUpdate()	{
		return this.isNeedsUpdate;
	}

	/**
	 * Sets flag to updated the texture.
	 */
	public Texture setNeedsUpdate(Boolean needsUpdate) {
		this.isNeedsUpdate = needsUpdate;
		return this;
	}

	/**
	 * Gets texture media element.
	 *
	 * @return the media element: image or canvas.
	 */
	public TextureData getImage() {
		return this.image;
	}

	/**
	 * Sets texture media element.
	 */
	public Texture setImage(TextureData image) {
		this.image = image;
		return this;
	}

	/**
	 * Gets texture offset.
	 *
	 * @return the offset vector.
	 */
	public Vector2 getOffset() {
		return offset;
	}

	/**
	 * Set texture offset vector.
	 *
	 * @param offset the offset vector.
	 */
	public Texture setOffset(Vector2 offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Gets repeat vector.
	 *
	 * @return the repeat vector.
	 */
	public Vector2 getRepeat() {
		return repeat;
	}

	/**
	 * Sets the repeat vector.
	 *
	 * @param repeat the repeat vector.
	 */
	public Texture setRepeat(Vector2 repeat) {
		this.repeat = repeat;
		return this;
	}

	/**
	 * Gets the PixelFormat value.
	 *
	 * @return the PixelFormat value.
	 */
	public PixelFormat getFormat() {
		return format;
	}

	/**
	 * Sets the {@link PixelFormat} value.
	 *
	 * @param format the {@link PixelFormat} value.
	 */
	public Texture setFormat(PixelFormat format) {
		this.format = format;
		return this;
	}

	/**
	 * Sets the PixelType value.
	 *
	 * @return the PixelType value.
	 */
	public PixelType getType() {
		return type;
	}

	/**
	 * Sets the {@link PixelType} value.
	 *
	 * @param type the {@link PixelType} value.
	 */
	public Texture setType(PixelType type) {
		this.type = type;
		return this;
	}

	/**
	 * Checks if needed to generate Mipmaps.
	 */
	public boolean isGenerateMipmaps() {
		return isGenerateMipmaps;
	}

	/**
	 * Sets generate Mipmaps flag.
	 */
	public Texture setGenerateMipmaps(boolean generateMipmaps) {
		this.isGenerateMipmaps = generateMipmaps;
		return this;
	}

	/**
	 * Gets premultiply alpha flag.
	 */
	public boolean isPremultiplyAlpha() {
		return isPremultiplyAlpha;
	}

	public int getAnisotropy() {
		return this.anisotropy;
	}

	/**
	 * Method of enhancing the image quality of texture on surfaces
	 * that are at oblique viewing angles.
	 */
	public Texture setAnisotropy(int anisotropy) {
		this.anisotropy = anisotropy;
		return this;
	}

	/**
	 * Sets premultiply alpha flag.
	 */
	public Texture setPremultiplyAlpha(boolean premultiplyAlpha) {
		this.isPremultiplyAlpha = premultiplyAlpha;
		return this;
	}

	public boolean isFlipY() {
		return this.isFlipY;
	}

	public Texture setFlipY(boolean isFlipY) {
		this.isFlipY = isFlipY;
		return this;
	}

	public int getUnpackAlignment() {
		return unpackAlignment;
	}

	public Texture setUnpackAlignment(int unpackAlignment) {
		this.unpackAlignment = unpackAlignment;
		return this;
	}

	public int getWebGlTexture() {
		return webglTexture;
	}

	public Texture setWebGlTexture(int webglTexture) {
		this.webglTexture = webglTexture;
		return this;
	}

	public Texture setTextureParameters ( GL20 gl, TextureTarget textureType, boolean isImagePowerOfTwo )
	{
		return setTextureParameters( gl, 0, textureType, isImagePowerOfTwo);
	}

	public Texture setTextureParameters ( GL20 gl, int maxAnisotropy, TextureTarget textureType, boolean isImagePowerOfTwo )
	{
		if ( isImagePowerOfTwo )
		{
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_S.getValue(), this.wrapS.getValue());
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_T.getValue(), this.wrapT.getValue());
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MAG_FILTER.getValue(), this.magFilter.getValue() );
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MIN_FILTER.getValue(), this.minFilter.getValue());
		}
		else
		{
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_S.getValue(), GL20.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_WRAP_T.getValue(), GL20.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MAG_FILTER.getValue(), filterFallback(this.magFilter.getValue()));
			gl.glTexParameteri(textureType.getValue(), TextureParameterName.TEXTURE_MIN_FILTER.getValue(), filterFallback(this.minFilter.getValue()));
		}

		if ( maxAnisotropy > 0 )
		{
			if ( this.anisotropy > 1 || this.cache_oldAnisotropy > 1 )
			{
				gl.glTexParameterf(textureType.getValue(), TextureParameterName.TEXTURE_MAX_ANISOTROPY_EXT.getValue(), Math.min(this.anisotropy, maxAnisotropy));
				this.cache_oldAnisotropy = this.anisotropy;
			}
		}

		return this;
	}

	/**
	 * Fallback filters for non-power-of-2 textures.
	 */
	private int filterFallback ( int f )
	{
		if(f == GL20.GL_NEAREST || f == GL20.GL_NEAREST_MIPMAP_NEAREST || f == GL20.GL_NEAREST_MIPMAP_LINEAR)
			return GL20.GL_NEAREST;

		return GL20.GL_LINEAR;
	}

	/**
	 * Releases a texture from the GL context.
	 */
	public void deallocate( GLRenderer renderer )
	{
		if ( getWebGlTexture() == 0 ) return;

		renderer.gl.glDeleteTexture(getWebGlTexture());

		renderer.getInfo().getMemory().textures--;
	}

	public Texture clone(Texture texture)
	{
		texture.offset.copy(this.offset);
		texture.repeat.copy(this.repeat);

		texture.setGenerateMipmaps(this.isGenerateMipmaps);
		texture.setPremultiplyAlpha(this.isPremultiplyAlpha);
		texture.setFlipY(this.isFlipY);

		return texture;
	}

	/**
	 * Clone the texture, where
	 * {@code this.clone() != this}
	 */
	public Texture clone()
	{
		return clone(new Texture(this.image, this.mapping, this.wrapS, this.wrapT,
				this.magFilter, this.minFilter, this.format, this.type, this.anisotropy));
	}

}
