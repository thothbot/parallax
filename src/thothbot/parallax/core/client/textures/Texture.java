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

package thothbot.parallax.core.client.textures;

import thothbot.parallax.core.client.gl2.WebGLConstants;
import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.WebGLTexture;
import thothbot.parallax.core.client.gl2.enums.DataType;
import thothbot.parallax.core.client.gl2.enums.PixelFormat;
import thothbot.parallax.core.client.gl2.enums.PixelType;
import thothbot.parallax.core.client.gl2.enums.TextureMagFilter;
import thothbot.parallax.core.client.gl2.enums.TextureMinFilter;
import thothbot.parallax.core.client.gl2.enums.TextureParameterName;
import thothbot.parallax.core.client.gl2.enums.TextureTarget;
import thothbot.parallax.core.client.gl2.enums.TextureWrapMode;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.math.Vector2;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

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
	/**
	 * This callback will be called when the image has been loaded.
	 */
	public static interface ImageLoadHandler 
	{
		void onImageLoad(Texture texture);
	}

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
	private PixelType type;

	private boolean isGenerateMipmaps = true;
	private boolean isPremultiplyAlpha = false;
	private boolean isFlipY = true;
	private int unpackAlignment = 4; // valid values: 1, 2, 4, 8 (see http://www.khronos.org/opengles/sdk/docs/man/xhtml/glPixelStorei.xml)

	private boolean isNeedsUpdate = false;
	
	private WebGLTexture webglTexture;
	
	private int anisotropy;
	
	private int cache_oldAnisotropy;

	/**
	 * Default constructor will create new instance of texture.
	 */
	public Texture() 
	{
		this((Element)Element.createObject());
	}
	
	public Texture(String url)
	{
		this(url, null);
	}

	public Texture(String url, final ImageLoadHandler imageLoadHandler)
	{
		this(new Image(url), imageLoadHandler);
	}

	/**
	 * Constructor
	 * 
	 * @param image              the Image
	 * @param imageLoadHandler   the {@link Texture.ImageLoadHandler}. Not necessary.
	 */
	public Texture(Image image, final ImageLoadHandler imageLoadHandler)
	{
		this(image.getElement());

		loadImage(image, new Loader() {
			
			@Override
			public void onLoad() {
				
				setNeedsUpdate(true);
				if (imageLoadHandler != null)
					imageLoadHandler.onImageLoad(Texture.this);
			}
		});
	}

	/**
	 * Constructor will create a texture instance.
	 *  
	 * @param image the media element.
	 */
	public Texture(Element image) 
	{
		this(image, 
				Texture.MAPPING_MODE.UV, 
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
	 * @param wrapS     the wrap parameter for texture coordinate S. @see {@link TextureWrapMode}.
	 * @param wrapT     the wrap parameter for texture coordinate T. @see {@link TextureWrapMode}.
	 * @param magFilter the texture magnification function. @see {@link TextureMagFilter}.
	 * @param minFilter the texture minifying function. @see {@link TextureMinFilter}.
	 * @param format    the {@link PixelFormat} value.
	 * @param type      the {@link DataType} value.
	 * @param anisotropy the anisotropy value.
	 */
	public Texture(Element image, Texture.MAPPING_MODE mapping, TextureWrapMode wrapS,
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
	 * Sets the {@link PixelType} value.
	 * 
	 * @return the {@link PixelType} value.
	 */
	public PixelType getType() {
		return type;
	}

	/**
	 * Sets the {@link PixelType} value.
	 * 
	 * @param type the {@link PixelType} value.
	 */
	public void setType(PixelType type) {
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
	
	public int getAnisotropy() {
		return this.anisotropy;
	}
	
	/**
	 * Method of enhancing the image quality of texture on surfaces 
	 * that are at oblique viewing angles.
	 */
	public void setAnisotropy(int anisotropy) {
		this.anisotropy = anisotropy;
	}

	/**
	 * Sets premultiply alpha flag.
	 */
	public void setPremultiplyAlpha(boolean premultiplyAlpha) {
		this.isPremultiplyAlpha = premultiplyAlpha;
	}
	
	public boolean isFlipY() {
		return this.isFlipY;
	}
	
	public void setFlipY(boolean isFlipY) {
		this.isFlipY = isFlipY;
	}
	
	public int getUnpackAlignment() {
		return unpackAlignment;
	}

	public void setUnpackAlignment(int unpackAlignment) {
		this.unpackAlignment = unpackAlignment;
	}

	public WebGLTexture getWebGlTexture() {
		return webglTexture;
	}

	public void setWebGlTexture(WebGLTexture webglTexture) {
		this.webglTexture = webglTexture;
	}

	public void setTextureParameters (WebGLRenderingContext gl, TextureTarget textureType, boolean isImagePowerOfTwo )
	{
		setTextureParameters(gl, 0, textureType, isImagePowerOfTwo);
	}

	public void setTextureParameters (WebGLRenderingContext gl, int maxAnisotropy, TextureTarget textureType, boolean isImagePowerOfTwo ) 
	{	
		if ( isImagePowerOfTwo ) 
		{
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_WRAP_S, this.wrapS.getValue() );
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_WRAP_T, this.wrapT.getValue() );
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_MAG_FILTER, this.magFilter.getValue() );
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_MIN_FILTER, this.minFilter.getValue() );
		} 
		else 
		{
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_WRAP_S, WebGLConstants.CLAMP_TO_EDGE );
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_WRAP_T, WebGLConstants.CLAMP_TO_EDGE );
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_MAG_FILTER, filterFallback( this.magFilter.getValue() ) );
			gl.texParameteri( textureType, TextureParameterName.TEXTURE_MIN_FILTER, filterFallback( this.minFilter.getValue() ) );
		}
		
		if ( maxAnisotropy > 0 ) 
		{
			if ( this.anisotropy > 1 || this.cache_oldAnisotropy > 1 ) 
			{
				gl.texParameterf( textureType, TextureParameterName.TEXTURE_MAX_ANISOTROPY_EXT, Math.min( this.anisotropy, maxAnisotropy ) );
				this.cache_oldAnisotropy = this.anisotropy;
			}
		}
	}
	
	/**
	 * Fallback filters for non-power-of-2 textures.
	 */
	private int filterFallback ( int f ) 
	{
		if(f == WebGLConstants.NEAREST || f == WebGLConstants.NEAREST_MIPMAP_NEAREST || f == WebGLConstants.NEAREST_MIPMAP_LINEAR)
			return WebGLConstants.NEAREST;

		return WebGLConstants.LINEAR;
	}
	
	/**
	 * Releases a texture from the GL context.
	 * texture � an instance of Texture
	 */
	public void deallocate( WebGLRenderer renderer ) 
	{
		if ( getWebGlTexture() == null ) return;

		renderer.getGL().deleteTexture( getWebGlTexture() );

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
	
	private static FlowPanel loadingArea = new FlowPanel();
	static {
		loadingArea.getElement().getStyle().setProperty("visibility", "hidden");
        loadingArea.getElement().getStyle().setProperty("position", "absolute");
        loadingArea.getElement().getStyle().setProperty("width", "1px");
        loadingArea.getElement().getStyle().setProperty("height", "1px");
        loadingArea.getElement().getStyle().setProperty("overflow", "hidden");
        RootPanel.get().add(loadingArea);
	}
	
	protected interface Loader 
	{
		void onLoad();
	}
	
	protected void loadImage(final Image image, final Loader loader)
	{		
		loadingArea.add(image);
		
	    // Hook up an error handler, so that we can be informed if the image fails
	    // to load.
		image.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event)
			{
				Log.error("An error occurred while loading image: " + image.getUrl());
			}
		});

		image.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) 
			{			
				Log.info("Loaded image: " + image.getUrl());
				loader.onLoad();
			}
		});
	}
}
