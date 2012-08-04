/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.parallax.core.client.textures;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLTexture;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.GLenum;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.gl2.enums.TextureWrapMode;
import thothbot.parallax.core.shared.core.Vector2;

import com.google.gwt.dom.client.Element;

/**
 * Basic implementation of texture.
 * <p>
 * This code based on three.js code.
 * 
 * @author thothbot
 *
 */
public class Texture
{
	private static int TextureCount = 0;
	
	public static enum OPERATIONS 
	{
		MULTIPLY(0), // MultiplyOperation
		MIX(1); // MixOperation

		private final int value;
		private OPERATIONS(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	/**
	 * Mapping modes
	 */
	public static enum MAPPING_MODE 
	{
		UV, // UVMapping = function () {};

		CUBE_REFLECTION, // CubeReflectionMapping = function () {};
		CUBE_REFRACTION, // CubeRefractionMapping = function () {};

		SPHERICAL_REFLECTION, // SphericalReflectionMapping = function () {};
		SPHERICAL_REFRACTION // SphericalRefractionMapping = function () {};
	};

	private int id;

	private Element image;

	private Vector2 offset;
	private Vector2 repeat;

	private Texture.MAPPING_MODE mapping;

	private TextureWrapMode wrapS;
	private TextureWrapMode wrapT;

	private TextureMagFilter magFilter;
	private TextureMinFilter minFilter;

	private PixelFormat format;
	private DataType type;

	private boolean isGenerateMipmaps = true;
	private boolean isPremultiplyAlpha = false;

	private boolean isNeedsUpdate = false;
	
	private WebGLTexture webglTexture;

	/**
	 * Default constructor will create new instance of texture.
	 */
	public Texture() 
	{
		this(null);
	}

	/**
	 * Constructor will create a texture instance.
	 *  
	 * @param image the media element.
	 */
	public Texture(Element image) 
	{
		this(image, Texture.MAPPING_MODE.UV);
	}

	/**
	 * Constructor will create a texture instance.
	 * 
	 * @param image    the media element
	 * @param mapping  the @{link Texture.MAPPING_MODE} value
	 */
	public Texture(Element image, Texture.MAPPING_MODE mapping)
	{
		this(image, mapping, TextureWrapMode.CLAMP_TO_EDGE, TextureWrapMode.CLAMP_TO_EDGE);
	}
	
	public Texture(Element image, Texture.MAPPING_MODE mapping, TextureWrapMode wrapS,
			TextureWrapMode wrapT)
	{
		this(image, mapping, wrapS, wrapT, TextureMagFilter.LINEAR, 
				TextureMinFilter.LINEAR_MIPMAP_LINEAR);
	}

	public Texture(Element image, Texture.MAPPING_MODE mapping, TextureWrapMode wrapS,
			TextureWrapMode wrapT, TextureMagFilter magFilter, TextureMinFilter minFilter)
	{
		this(image, mapping, wrapS, wrapT, magFilter, minFilter, PixelFormat.RGBA, DataType.UNSIGNED_BYTE);
	}
	
	/**
	 * Constructor will create a texture instance.
	 * 
	 * @param image     the media element
	 * @param mapping   the @{link Texture.MAPPING_MODE} value
	 * @param wrapS     the wrap parameter for texture coordinate S. @see {@link TextureWrapMode}.
	 * @param wrapT     the wrap parameter for texture coordinate T. @see {@link TextureWrapMode}.
	 * @param magFilter the texture magnification function. @see {@link TextureMagFilter}.
	 * @param minFilter the texture minifying function. @see {@link TextureMinFilter}.
	 * @param format    the {@link PixelFormat} value.
	 * @param type      the {@link DataType} value.
	 */
	public Texture(Element image, Texture.MAPPING_MODE mapping, TextureWrapMode wrapS,
			TextureWrapMode wrapT, TextureMagFilter magFilter, TextureMinFilter minFilter,
			PixelFormat format, DataType type) 
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
	public void setMapping(Texture.MAPPING_MODE mapping) {
		this.mapping = mapping;
	}

	/**
	 * Sets the wrap parameter for texture coordinate S.
	 * 
	 * @param wrapS the wrap parameter 
	 */
	public void setWrapS(TextureWrapMode wrapS)	{
		this.wrapS = wrapS;
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
	public void setWrapT(TextureWrapMode wrapT) {
		this.wrapT = wrapT;
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
	public void setMagFilter(TextureMagFilter magFilter) {
		this.magFilter = magFilter;
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
	public void setMinFilter(TextureMinFilter minFilter) {
		this.minFilter = minFilter;
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
	public void setNeedsUpdate(Boolean needsUpdate) {
		this.isNeedsUpdate = needsUpdate;
	}
	
	/**
	 * Gets texture media element.
	 * 
	 * @return the media element: image or canvas.
	 */
	public Element getImage() {
		return this.image;
	}
	
	/**
	 * Sets texture media element.
	 */
	public void setImage(Element image) {
		this.image = image;
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
	public void setOffset(Vector2 offset) {
		this.offset = offset;
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
	public void setRepeat(Vector2 repeat) {
		this.repeat = repeat;
	}

	/**
	 * Gets the {@link PixelFormat} value.
	 * 
	 * @return the {@link PixelFormat} value.
	 */
	public PixelFormat getFormat() {
		return format;
	}

	/**
	 * Sets the {@link PixelFormat} value.
	 * 
	 * @param format the {@link PixelFormat} value.
	 */
	public void setFormat(PixelFormat format) {
		this.format = format;
	}

	/**
	 * Sets the {@link DataType} value.
	 * 
	 * @return the {@link DataType} value.
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * Sets the {@link DataType} value.
	 * 
	 * @param type the {@link DataType} value.
	 */
	public void setType(DataType type) {
		this.type = type;
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
	public void setGenerateMipmaps(boolean generateMipmaps) {
		this.isGenerateMipmaps = generateMipmaps;
	}

	/**
	 * Gets premultiply alpha flag.
	 */
	public boolean isPremultiplyAlpha() {
		return isPremultiplyAlpha;
	}

	/**
	 * Sets premultiply alpha flag.
	 */
	public void setPremultiplyAlpha(boolean premultiplyAlpha) {
		this.isPremultiplyAlpha = premultiplyAlpha;
	}

	public WebGLTexture getWebGlTexture() {
		return webglTexture;
	}

	public void setWebGlTexture(WebGLTexture webglTexture) {
		this.webglTexture = webglTexture;
	}

	/**
	 * Clone the texture, where
	 * {@code this.clone() != this}
	 */
	public Texture clone()
	{
		Texture clonedTexture = new Texture(this.image, this.mapping, this.wrapS, this.wrapT,
				this.magFilter, this.minFilter, this.format, this.type);

		clonedTexture.offset.copy(this.offset);
		clonedTexture.repeat.copy(this.repeat);

		return clonedTexture;
	}

	public void setTextureParameters (WebGLRenderingContext gl, int textureType, boolean isImagePowerOfTwo ) 
	{	
		if ( isImagePowerOfTwo ) 
		{
			gl.texParameteri( textureType, GLenum.TEXTURE_WRAP_S.getValue(), this.wrapS.getValue() );
			gl.texParameteri( textureType, GLenum.TEXTURE_WRAP_T.getValue(), this.wrapT.getValue() );
			gl.texParameteri( textureType, GLenum.TEXTURE_MAG_FILTER.getValue(), this.magFilter.getValue() );
			gl.texParameteri( textureType, GLenum.TEXTURE_MIN_FILTER.getValue(), this.minFilter.getValue() );
		} 
		else 
		{
			gl.texParameteri( textureType, GLenum.TEXTURE_WRAP_S.getValue(), GLenum.CLAMP_TO_EDGE.getValue() );
			gl.texParameteri( textureType, GLenum.TEXTURE_WRAP_T.getValue(), GLenum.CLAMP_TO_EDGE.getValue() );
			gl.texParameteri( textureType, GLenum.TEXTURE_MAG_FILTER.getValue(), filterFallback( this.magFilter.getEnum() ) );
			gl.texParameteri( textureType, GLenum.TEXTURE_MIN_FILTER.getValue(), filterFallback( this.minFilter.getEnum() ) );
		}
	}
	
	/**
	 * Fallback filters for non-power-of-2 textures.
	 */
	private int filterFallback ( GLenum f ) 
	{
		switch ( f ) {

		case NEAREST:
		case NEAREST_MIPMAP_NEAREST:
		case NEAREST_MIPMAP_LINEAR: 
			return GLenum.NEAREST.getValue();

		case LINEAR:
		case LINEAR_MIPMAP_NEAREST:
		case LINEAR_MIPMAP_LINEAR:
		default:
			return GLenum.LINEAR.getValue();

		}
	}
}
