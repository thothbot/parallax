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
import thothbot.squirrel.core.client.gl2.enums.DataType;
import thothbot.squirrel.core.client.gl2.enums.GLenum;
import thothbot.squirrel.core.client.gl2.enums.PixelFormat;
import thothbot.squirrel.core.client.gl2.enums.TextureMagFilter;
import thothbot.squirrel.core.client.gl2.enums.TextureMinFilter;
import thothbot.squirrel.core.client.gl2.enums.TextureWrapMode;
import thothbot.squirrel.core.shared.core.Vector2f;

import com.google.gwt.dom.client.Element;
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

	public int id;

	private Element image;

	public Vector2f offset;
	public Vector2f repeat;

	private Texture.MAPPING_MODE mapping = Texture.MAPPING_MODE.UV;

	private TextureWrapMode wrapS = TextureWrapMode.CLAMP_TO_EDGE;
	private TextureWrapMode wrapT = TextureWrapMode.CLAMP_TO_EDGE;

	private TextureMagFilter magFilter = TextureMagFilter.LINEAR;
	private TextureMinFilter minFilter = TextureMinFilter.LINEAR_MIPMAP_LINEAR;

	public PixelFormat format = PixelFormat.RGBA;
	public DataType type = DataType.UNSIGNED_BYTE;

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

	public Texture(Element image) 
	{
		this();
		this.image = image;
	}

	public Texture(Element image, Texture.MAPPING_MODE mapping)
	{
		this(image);
		this.mapping = mapping;
	}

	public Texture(Element image, Texture.MAPPING_MODE mapping, TextureWrapMode wrapS,
			TextureWrapMode wrapT, TextureMagFilter magFilter, TextureMinFilter minFilter,
			PixelFormat format, DataType type) 
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

	public void setWrapS(TextureWrapMode wrapS)
	{
		this.wrapS = wrapS;
	}

	public TextureWrapMode getWrapS()
	{
		return this.wrapS;
	}

	public void setWrapT(TextureWrapMode wrapT)
	{
		this.wrapT = wrapT;
	}
	
	public TextureWrapMode getWrapT()
	{
		return this.wrapT;
	}

	public TextureMagFilter getMagFilter()
	{
		return this.magFilter;
	}

	public TextureMinFilter getMinFilter()
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
	
	public Element getImage()
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
		gl.activeTexture( GLenum.TEXTURE0.getValue() + slot );
		gl.bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), this.__webglTexture );
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
			gl.texParameteri( textureType, GLenum.TEXTURE_MAG_FILTER.getValue(), this.magFilter.getValue() );
			gl.texParameteri( textureType, GLenum.TEXTURE_MIN_FILTER.getValue(), this.minFilter.getValue() );
		}
	}
}
