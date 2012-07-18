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

package thothbot.parallax.core.shared.materials;

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.client.gl2.enums.BlendEquationMode;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorDest;
import thothbot.parallax.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.parallax.core.client.shader.Program;
import thothbot.parallax.core.client.shader.Shader;
import thothbot.parallax.core.client.shader.Uniform;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.GeometryGroup;
import thothbot.parallax.core.shared.core.WebGLCustomAttribute;
import thothbot.parallax.core.shared.objects.GeometryObject;

public class Material
{
	public static int MaterialCount;

	/**
	 * Shading
	 */
	public static enum SHADING 
	{
		NO, // NoShading = 0;
		FLAT, // FlatShading = 1;
		SMOOTH // SmoothShading = 2;
	};

	/**
	 * Colors
	 */
	public static enum COLORS 
	{
		NO, // NoColors = 0;
		FACE, // FaceColors = 1;
		VERTEX // VertexColors = 2;
	};

	/**
	 * Blending modes
	 */
	public static enum BLENDING 
	{
		NO, // NoBlending = 0;
		NORMAL, // NormalBlending = 1;
		ADDITIVE, // AdditiveBlending = 2;
		SUBTRACTIVE, // SubtractiveBlending = 3;
		MULTIPLY, // MultiplyBlending = 4;
		ADDITIVE_ALPHA, // AdditiveAlphaBlending = 5;
		CUSTOM // CustomBlending = 6;
	};

	private int id;
	
	private float opacity;
	private boolean transparent;
	private boolean depthTest;

	public String name;

	public Material.COLORS vertexColors;
	private Material.BLENDING blending;
	private Material.SHADING shading;
	private BlendingFactorSrc blendSrc;
	private BlendingFactorDest blendDst;
	private BlendEquationMode blendEquation;

	private boolean depthWrite;

	private boolean polygonOffset;
	private float polygonOffsetFactor;
	private float polygonOffsetUnits;

	private int alphaTest;

	// Boolean for fixing antialiasing gaps in CanvasRenderer
	private boolean overdraw;

	public boolean visible = true;

	public boolean needsUpdate = true;
	
	public Map<String, Uniform> uniforms;
	public Program program;
	public Map<String, WebGLCustomAttribute> attributes;

	public String vertexShader;
	public String fragmentShader;
	
	public boolean lights;
	public boolean fog;

	public boolean sizeAttenuation;

	public boolean skinning;

	public boolean morphTargets;
	public int numSupportedMorphTargets;
	public boolean morphNormals;
	public int numSupportedMorphNormals;
	
	public boolean metal;
	public boolean wrapAround;
	public boolean wireframe;
	public int wireframeLinewidth;
	public int linewidth;

	static class MaterialOptions 
	{
		public float opacity = 1.0f;
		public boolean transparent = false;
		public Material.BLENDING blending = Material.BLENDING.NORMAL;
		public BlendingFactorSrc blendSrc = BlendingFactorSrc.SRC_ALPHA;
		public BlendingFactorDest blendDst = BlendingFactorDest.ONE_MINUS_SRC_ALPHA;
		public BlendEquationMode blendEquation = BlendEquationMode.FUNC_ADD;
		public boolean depthTest = true;
		public boolean depthWrite = true;
		public boolean polygonOffset = false;
		public float polygonOffsetFactor = 0f;
		public float polygonOffsetUnits = 0f;
		public int alphaTest = 0;
		public boolean overdraw = false;
		public boolean wireframe = false;
		public int wireframeLinewidth = 1;
		public Material.COLORS vertexColors = Material.COLORS.NO;
		public boolean fog = false;
		public boolean lights = false;
	}

	public Material(MaterialOptions parameters)
	{
		this.id = Material.MaterialCount++;

		this.opacity = parameters.opacity;
		this.transparent = parameters.transparent;
		this.blending = parameters.blending;
		this.blendSrc = parameters.blendSrc;
		this.blendDst = parameters.blendDst;
		this.blendEquation = parameters.blendEquation;
				
		this.depthTest = parameters.depthTest;
		this.depthWrite = parameters.depthWrite;
		this.polygonOffset = parameters.polygonOffset;
		this.polygonOffsetFactor = parameters.polygonOffsetFactor;
		this.polygonOffsetUnits = parameters.polygonOffsetUnits;
		this.alphaTest = parameters.alphaTest;
		this.overdraw = parameters.overdraw;
		this.wireframe = parameters.wireframe;
		this.wireframeLinewidth = parameters.wireframeLinewidth;
		this.vertexColors = parameters.vertexColors;
		
		this.fog = parameters.fog;
		this.lights = parameters.lights;
	}
	
	public int getId()
	{
		return id;
	}

	public float getOpacity()
	{
		return opacity;
	}

	public void setOpacity(float opacity)
	{
		this.opacity = opacity;
	}

	public boolean isTransparent()
	{
		return transparent;
	}

	public void setTransparent(boolean transparent)
	{
		this.transparent = transparent;
	}

	public boolean isDepthTest()
	{
		return depthTest;
	}

	public void setDepthTest(boolean depthTest)
	{
		this.depthTest = depthTest;
	}

	public Material.BLENDING getBlending()
	{
		return blending;
	}

	public void setBlending(Material.BLENDING blending)
	{
		this.blending = blending;
	}

	public BlendingFactorSrc getBlendSrc()
	{
		return blendSrc;
	}

	public void setBlendSrc(BlendingFactorSrc blendSrc)
	{
		this.blendSrc = blendSrc;
	}

	public BlendingFactorDest getBlendDst()
	{
		return blendDst;
	}

	public void setBlendDst(BlendingFactorDest blendDst)
	{
		this.blendDst = blendDst;
	}

	public BlendEquationMode getBlendEquation()
	{
		return blendEquation;
	}

	public void setBlendEquation(BlendEquationMode blendEquation)
	{
		this.blendEquation = blendEquation;
	}

	public boolean isDepthWrite()
	{
		return depthWrite;
	}

	public void setDepthWrite(boolean depthWrite)
	{
		this.depthWrite = depthWrite;
	}

	public boolean isPolygonOffset()
	{
		return polygonOffset;
	}

	public void setPolygonOffset(boolean polygonOffset)
	{
		this.polygonOffset = polygonOffset;
	}

	public float getPolygonOffsetFactor()
	{
		return polygonOffsetFactor;
	}

	public void setPolygonOffsetFactor(float polygonOffsetFactor)
	{
		this.polygonOffsetFactor = polygonOffsetFactor;
	}

	public float getPolygonOffsetUnits()
	{
		return polygonOffsetUnits;
	}

	public void setPolygonOffsetUnits(float polygonOffsetUnits)
	{
		this.polygonOffsetUnits = polygonOffsetUnits;
	}

	public int getAlphaTest()
	{
		return alphaTest;
	}

	public void setAlphaTest(int alphaTest)
	{
		this.alphaTest = alphaTest;
	}

	public boolean isOverdraw()
	{
		return overdraw;
	}
	

	public void setVertexColors(Material.COLORS vertexColors) 
	{
		this.vertexColors = vertexColors;
	}

	public Material.COLORS getVertexColors() 
	{
		return vertexColors;
	}
	
	public void setShading(Material.SHADING shading) 
	{
		this.shading = shading;
	}

	public Material.SHADING getShading() 
	{
		return this.shading;
	}

	public void setOverdraw(boolean overdraw)
	{
		this.overdraw = overdraw;
	}

	// TODO: check what is this
	// Must be overwriten
	public Shader getShaderId() {
		return null;
	}

	public void setMaterialShaders( Shader shader) 
	{
		Log.debug("Called Material.setMaterialShaders()");

		this.uniforms = new HashMap<String, Uniform>();
		this.uniforms.putAll(shader.getUniforms());

		this.vertexShader = shader.getVertexSource();
		this.fragmentShader = shader.getFragmentSource();
	}
	

	private boolean materialNeedsSmoothNormals() 
	{
		return this.shading != null && this.shading == Material.SHADING.SMOOTH;
	}
	
	public Material.SHADING bufferGuessNormalType () 
	{
		// only MeshBasicMaterial and MeshDepthMaterial don't need normals
		if (materialNeedsSmoothNormals())
			return Material.SHADING.SMOOTH;
		else
			return Material.SHADING.FLAT;
	}
	
	public Material.COLORS bufferGuessVertexColorType () 
	{
		if ( this.vertexColors != Material.COLORS.NO )
			return this.vertexColors;

		return null;
	}
	
	public boolean bufferGuessUVType () 
	{
		return false;
	}

	public static Material getBufferMaterial( GeometryObject object, GeometryGroup geometryGroup ) 
	{
		if ( object.getMaterial() != null && !( object.getMaterial() instanceof MeshFaceMaterial ) )
			return object.getMaterial();

		else if ( geometryGroup.materialIndex >= 0 )
			return object.getGeometry().getMaterials().get( geometryGroup.materialIndex );
		
		return null;
	}
	
	public boolean areCustomAttributesDirty() 
	{
		for ( String a : this.attributes.keySet())
			if ( this.attributes.get( a ).needsUpdate ) return true;

		return false;
	}

	// TODO: Check
	public void clearCustomAttributes() 
	{
		if(this.attributes == null)
			return;

		for ( String a : this.attributes.keySet() )
			this.attributes.get( a ).needsUpdate = false;
	}
}
