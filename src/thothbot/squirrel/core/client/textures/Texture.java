/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
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

package thothbot.squirrel.core.client.textures;

import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.WebGLTexture;
import thothbot.squirrel.core.shared.core.Vector2f;

import com.google.gwt.user.client.ui.Image;

public class Texture
{
	public static int TextureCount = 0;
	
	public static enum OPERATIONS {
		MULTIPLY(0), // MultiplyOperation
		MIX(1); // MixOperation

		private final int value;
		private OPERATIONS(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	// Mapping modes
	public static enum MAPPING_MODE {
		UV, // UVMapping = function () {};

		CUBE_REFLECTION, // CubeReflectionMapping = function () {};
		CUBE_REFRACTION, // CubeRefractionMapping = function () {};

		SPHERICAL_REFLECTION, // SphericalReflectionMapping = function () {};
		SPHERICAL_REFRACTION // SphericalRefractionMapping = function () {};
	};

	// Wrapping modes
	public static enum WRAPPING_MODE {
		REPEAT(WebGLRenderingContext.REPEAT), // RepeatWrapping = 0;
		CLAMP_TO_EDGE(WebGLRenderingContext.CLAMP_TO_EDGE), // ClampToEdgeWrapping = 1;
		MIRRORED_REPEAT(WebGLRenderingContext.MIRRORED_REPEAT); // MirroredRepeatWrapping = 2;
		
		private final int value;
		private WRAPPING_MODE(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	// Filters
	public static enum FILTER {
		NEAREST(WebGLRenderingContext.NEAREST), // NearestFilter = 3;
		NEAREST_MIP_MAP_NEAREST(WebGLRenderingContext.NEAREST_MIPMAP_NEAREST), // NearestMipMapNearestFilter = 4;
		NEAREST_MIP_MAP_LINEAR(WebGLRenderingContext.NEAREST_MIPMAP_LINEAR), // NearestMipMapLinearFilter = 5;
		LINEAR(WebGLRenderingContext.LINEAR), // LinearFilter = 6;
		LINEAR_MIP_MAP_NEAREST(WebGLRenderingContext.LINEAR_MIPMAP_NEAREST), // LinearMipMapNearestFilter = 7;
		LINEAR_MIP_MAP_LINEAR(WebGLRenderingContext.LINEAR_MIPMAP_LINEAR); // LinearMipMapLinearFilter = 8;
		
		private final int value;
		private FILTER(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	// Types
	public static enum TYPE {
		BYTE(WebGLRenderingContext.BYTE), // ByteType = 9;
		UNSIGNED_BYTE(WebGLRenderingContext.UNSIGNED_BYTE), // UnsignedByteType = 10;
		SHORT(WebGLRenderingContext.SHORT), // ShortType = 11;
		UNSIGNED_SHORT(WebGLRenderingContext.UNSIGNED_SHORT), // UnsignedShortType = 12;
		INT(WebGLRenderingContext.INT), // IntType = 13;
		UNSIGNED_INT(WebGLRenderingContext.UNSIGNED_INT), // UnsignedIntType = 14;
		FLOAT(WebGLRenderingContext.FLOAT); // FloatType = 15;
		
		private final int value;
		private TYPE(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	// Formats
	public static enum FORMAT {
		ALPHA(WebGLRenderingContext.ALPHA), // THREE.AlphaFormat = 16;
		RGB(WebGLRenderingContext.RGB), // RGBFormat = 17;
		RGBA(WebGLRenderingContext.RGBA), // RGBAFormat = 18;
		LUMINANCE(WebGLRenderingContext.LUMINANCE), // LuminanceFormat = 19;
		LUMINANCE_ALPHA(WebGLRenderingContext.LUMINANCE_ALPHA); // LuminanceAlphaFormat = 20;
		
		private final int value;
		private FORMAT(int value) { this.value = value; }
		public int getValue() { return value; }
	};

	public int id;

	private Image image;

	public Vector2f offset;
	public Vector2f repeat;

	private Texture.MAPPING_MODE mapping = Texture.MAPPING_MODE.UV;

	private Texture.WRAPPING_MODE wrapS = Texture.WRAPPING_MODE.CLAMP_TO_EDGE;
	private Texture.WRAPPING_MODE wrapT = Texture.WRAPPING_MODE.CLAMP_TO_EDGE;

	private Texture.FILTER magFilter = Texture.FILTER.LINEAR;
	private Texture.FILTER minFilter = Texture.FILTER.LINEAR_MIP_MAP_LINEAR;

	public Texture.FORMAT format = Texture.FORMAT.RGBA;
	public Texture.TYPE type = Texture.TYPE.UNSIGNED_BYTE;

	public boolean generateMipmaps = true;
	public boolean premultiplyAlpha = false;

	private boolean needsUpdate = false;
	
	public boolean __webglInit;
	public WebGLTexture __webglTexture;

	public Texture() 
	{
		this.id = Texture.TextureCount++;
		this.offset = new Vector2f(0, 0);
		this.repeat = new Vector2f(1, 1);
	}

	public Texture(Image image) 
	{
		this();
		this.image = image;
	}

	public Texture(Image image, Texture.MAPPING_MODE mapping)
	{
		this(image);
		this.mapping = mapping;
	}

	public Texture(Image image, Texture.MAPPING_MODE mapping, Texture.WRAPPING_MODE wrapS,
			Texture.WRAPPING_MODE wrapT, Texture.FILTER magFilter, Texture.FILTER minFilter,
			Texture.FORMAT format, Texture.TYPE type) 
	{	
		this(image, mapping);

		this.wrapS = wrapS;
		this.wrapT = wrapT;

		this.magFilter = magFilter;
		this.minFilter = minFilter;

		this.format = format;
		this.type = type;
	}
	
	
	public Texture.MAPPING_MODE getMapping()
	{
		return this.mapping;
	}

	public void setWrapS(Texture.WRAPPING_MODE wrapS)
	{
		this.wrapS = wrapS;
	}

	public Texture.WRAPPING_MODE getWrapS()
	{
		return this.wrapS;
	}

	public void setWrapT(Texture.WRAPPING_MODE wrapT)
	{
		this.wrapT = wrapT;
	}
	
	public Texture.WRAPPING_MODE getWrapT()
	{
		return this.wrapT;
	}

	public Texture.FILTER getMagFilter()
	{
		return this.magFilter;
	}

	public Texture.FILTER getMinFilter()
	{
		return this.minFilter;
	}
	
	public Boolean getNeedsUpdate()
	{
		return this.needsUpdate;
	}
	
	public void setNeedsUpdate(Boolean needsUpdate)
	{
		this.needsUpdate = needsUpdate;
	}
	
	public Image getImage()
	{
		return this.image;
	}

	public Texture clone()
	{
		Texture clonedTexture = new Texture(this.image, this.mapping, this.wrapS, this.wrapT,
				this.magFilter, this.minFilter, this.format, this.type);

		clonedTexture.offset.copy(this.offset);
		clonedTexture.repeat.copy(this.repeat);

		return clonedTexture;
	}

	public void setCubeTextureDynamic(WebGLRenderingContext gl, int slot) 
	{
		gl.activeTexture( WebGLRenderingContext.TEXTURE0 + slot );
		gl.bindTexture( WebGLRenderingContext.TEXTURE_CUBE_MAP, this.__webglTexture );
	}

	public void setTextureParameters (WebGLRenderingContext gl, int textureType, boolean isImagePowerOfTwo ) 
	{	
		if ( isImagePowerOfTwo ) 
		{
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_WRAP_S, this.wrapS.getValue() );
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_WRAP_T, this.wrapT.getValue() );
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_MAG_FILTER, this.magFilter.getValue() );
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_MIN_FILTER, this.minFilter.getValue() );
		} 
		else 
		{
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_WRAP_S, WebGLRenderingContext.CLAMP_TO_EDGE );
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_WRAP_T, WebGLRenderingContext.CLAMP_TO_EDGE );
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_MAG_FILTER, this.magFilter.getValue() );
			gl.texParameteri( textureType, WebGLRenderingContext.TEXTURE_MIN_FILTER, this.minFilter.getValue() );
		}
	}
}
