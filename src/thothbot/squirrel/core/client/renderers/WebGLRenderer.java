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

package thothbot.squirrel.core.client.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.squirrel.core.client.context.Canvas3d;
import thothbot.squirrel.core.client.gl2.WebGLFramebuffer;
import thothbot.squirrel.core.client.gl2.WebGLRenderingContext;
import thothbot.squirrel.core.client.gl2.WebGLUniformLocation;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.gl2.enums.BlendEquationMode;
import thothbot.squirrel.core.client.gl2.enums.BlendingFactorDest;
import thothbot.squirrel.core.client.gl2.enums.BlendingFactorSrc;
import thothbot.squirrel.core.client.gl2.enums.GLenum;
import thothbot.squirrel.core.client.gl2.enums.TextureMinFilter;
import thothbot.squirrel.core.client.shader.Program;
import thothbot.squirrel.core.client.shader.ProgramParameters;
import thothbot.squirrel.core.client.shader.Shader;
import thothbot.squirrel.core.client.shader.Uniform;
import thothbot.squirrel.core.client.textures.CubeTexture;
import thothbot.squirrel.core.client.textures.DataTexture;
import thothbot.squirrel.core.client.textures.RenderTargetCubeTexture;
import thothbot.squirrel.core.client.textures.RenderTargetTexture;
import thothbot.squirrel.core.client.textures.Texture;
import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.cameras.Camera;
import thothbot.squirrel.core.shared.cameras.OrthographicCamera;
import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Frustum;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.GeometryBuffer;
import thothbot.squirrel.core.shared.core.GeometryGroup;
import thothbot.squirrel.core.shared.core.Mathematics;
import thothbot.squirrel.core.shared.core.Matrix3f;
import thothbot.squirrel.core.shared.core.Matrix4f;
import thothbot.squirrel.core.shared.core.Vector2f;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.core.Vector4f;
import thothbot.squirrel.core.shared.core.WebGLCustomAttribute;
import thothbot.squirrel.core.shared.lights.AmbientLight;
import thothbot.squirrel.core.shared.lights.DirectionalLight;
import thothbot.squirrel.core.shared.lights.LensFlare;
import thothbot.squirrel.core.shared.lights.Light;
import thothbot.squirrel.core.shared.lights.PointLight;
import thothbot.squirrel.core.shared.lights.SpotLight;
import thothbot.squirrel.core.shared.materials.AbstractMapMaterial;
import thothbot.squirrel.core.shared.materials.LineBasicMaterial;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.materials.MeshDepthMaterial;
import thothbot.squirrel.core.shared.materials.MeshFaceMaterial;
import thothbot.squirrel.core.shared.materials.MeshLambertMaterial;
import thothbot.squirrel.core.shared.materials.MeshNormalMaterial;
import thothbot.squirrel.core.shared.materials.MeshPhongMaterial;
import thothbot.squirrel.core.shared.materials.ParticleBasicMaterial;
import thothbot.squirrel.core.shared.materials.ShaderMaterial;
import thothbot.squirrel.core.shared.objects.GeometryObject;
import thothbot.squirrel.core.shared.objects.Line;
import thothbot.squirrel.core.shared.objects.Mesh;
import thothbot.squirrel.core.shared.objects.Object3D;
import thothbot.squirrel.core.shared.objects.ParticleSystem;
import thothbot.squirrel.core.shared.objects.Ribbon;
import thothbot.squirrel.core.shared.objects.SidesObject;
import thothbot.squirrel.core.shared.objects.SkinnedMesh;
import thothbot.squirrel.core.shared.objects.Sprite;
import thothbot.squirrel.core.shared.objects.WebGLObject;
import thothbot.squirrel.core.shared.scenes.Fog;
import thothbot.squirrel.core.shared.scenes.FogExp2;
import thothbot.squirrel.core.shared.scenes.Scene;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/*
 * The WebGL renderer displays your beautifully crafted scenes using WebGL, if your device supports it.
 */
public class WebGLRenderer
{
	public static enum PRECISION 
	{
		HIGHP,
		MEDIUMP,
		LOWP
	};

	// The HTML5 Canvas's 'webgl' context obtained from the canvas where the renderer will draw.
	private Canvas3d canvas;
	
	private Map<String, Program> _programs;
	
	// An object with a series of statistical information about the graphics board memory and 
	// the rendering process. Useful for debugging or just for the sake of curiosity.
	private WebGLRenderInfo info;

	//////////////////////////////////////////////////////////////
	// parameters
	
	// shader precision. Can be "highp", "mediump" or "lowp".
	private WebGLRenderer.PRECISION precision = WebGLRenderer.PRECISION.HIGHP;
				
	// Integer, default is Color3f(0x000000).
	private Color3f clearColor = new Color3f(0x000000);

	// Float, default is 0
	private float clearAlpha = 1.0f;
	
	// Integer, default is 4
	private int maxLights = 4;
	
	//////////////////////////////////////////////////////////////
	// public properties

	// clearing

	// Defines whether the renderer should automatically clear its output before rendering.
	public boolean autoClear = true;
	
	// If autoClear is true, defines whether the renderer should clear the color buffer. Default is true.
	public boolean autoClearColor = true;
	
	// If autoClear is true, defines whether the renderer should clear the depth buffer. Default is true.
	public boolean autoClearDepth = true;
	
	// If autoClear is true, defines whether the renderer should clear the stencil buffer. Default is true.
	public boolean autoClearStencil = true;

	// scene graph

	// Defines whether the renderer should sort objects. Default is true.
	public boolean sortObjects = true;

	// Defines whether the renderer should auto update objects. Default is true.
	public boolean autoUpdateObjects = true;
	
	// Defines whether the renderer should auto update the scene. Default is true.
	public boolean autoUpdateScene = true;

	// physically based shading

	public boolean gammaInput = false;
	public boolean gammaOutput = false;
	public boolean physicallyBasedShading = false;

	// shadow map

	public boolean shadowMapEnabled = false;
	public boolean shadowMapAutoUpdate = true;
	public boolean shadowMapSoft = true;
	public boolean shadowMapCullFrontFaces = true;
	public boolean shadowMapDebug = false;
	public boolean shadowMapCascade = false;

	// morphs

	public int maxMorphTargets = 8;
	public int maxMorphNormals = 4;

	// flags

	public boolean autoScaleCubemaps = true;

	// custom render plugins

	// An array with render plugins to be applied before rendering.
	private List<Object> renderPluginsPre;
	
	//An array with render plugins to be applied after rendering.
	private List<Object> renderPluginsPost;

	// internal state cache

	private Program cache_currentProgram = null;
	private WebGLFramebuffer cache_currentFramebuffer = null;
	private int cache_currentMaterialId = -1;
	private int cache_currentGeometryGroupHash = -1;
	private Camera cache_currentCamera = null;
	
	// GL state cache

	private Boolean cache_oldDoubleSided = null;
	private Boolean cache_oldFlipSided = null;

	private Material.BLENDING cache_oldBlending = null;
	private BlendEquationMode cache_oldBlendEquation = null;

	private BlendingFactorSrc cache_oldBlendSrc = null;
	private BlendingFactorDest cache_oldBlendDst = null;

	private Boolean cache_oldDepthTest = null;
	private Boolean cache_oldDepthWrite = null;

	private Boolean cache_oldPolygonOffset = null;
	private Float cache_oldPolygonOffsetFactor = null;
	private Float cache_oldPolygonOffsetUnits = null;
			
	private int viewportX = 0;
	private int viewportY = 0;
	private int viewportWidth = 0;
	private int viewportHeight = 0;
	private int _currentWidth = 0;
	private int _currentHeight = 0;
	
	// frustum

	private Frustum frustum;

	 // camera matrices cache

	public Matrix4f _projScreenMatrix;
	public Matrix4f _projScreenMatrixPS;

	public Vector4f _vector3;

	// light arrays cache

	private Vector3f direction;

	private boolean lightsNeedUpdate = true;

	private WebGLRenderLights lights;

	// GPU capabilities
	private int maxVertexTextures;
	private int maxTextureSize;
	private int maxCubemapSize;
	
	public WebGLRenderer(Canvas3d canvas)
	{
		setCanvas(canvas);

		this.setInfo(new WebGLRenderInfo());
		
		this.frustum = new Frustum();
		
		this._projScreenMatrix   = new Matrix4f();
		this._projScreenMatrixPS = new Matrix4f();
		this._vector3            = new Vector4f();
		this.direction          = new Vector3f();
		this.lights             = new WebGLRenderLights();
		this._programs           = new HashMap<String, Program>();
		
		this.maxVertexTextures = getGL().getParameteri(GLenum.MAX_VERTEX_TEXTURE_IMAGE_UNITS.getValue());
		this.maxTextureSize    = getGL().getParameteri(GLenum.MAX_TEXTURE_SIZE.getValue());
		this.maxCubemapSize    = getGL().getParameteri(GLenum.MAX_CUBE_MAP_TEXTURE_SIZE.getValue());

		setViewport(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
		setDefaultGLState();
	}
	
	public WebGLRenderInfo getInfo()
	{
		return info;
	}
	
	private void setInfo(WebGLRenderInfo info)
	{
		this.info = info;
	}
	
	public Canvas3d getCanvas()
	{
		return this.canvas;
	}
	
	private void setCanvas(Canvas3d canvas)
	{
		this.canvas = canvas;
	}
	
	/**
	 * Gets the WebGL context.
	 * 
	 * @return the underlying context implementation for drawing onto the
	 *         canvas, or null if no context implementation is available.
	 */
	public WebGLRenderingContext getGL()
	{
		return getCanvas().getGL();
	}

	private void setDefaultGLState () 
	{
		getGL().clearColor( 0.0f, 0.0f, 0.0f, 1.0f );
		getGL().clearDepth( 1 );
		getGL().clearStencil( 0 );

		getGL().enable( GLenum.DEPTH_TEST.getValue() );
		getGL().depthFunc( GLenum.LEQUAL.getValue() );

		getGL().frontFace( GLenum.CCW.getValue() );
		getGL().cullFace( GLenum.BACK.getValue() );
		getGL().enable( GLenum.CULL_FACE.getValue() );

		getGL().enable( GLenum.BLEND.getValue() );
		getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
		getGL().blendFunc( GLenum.SRC_ALPHA.getValue(), GLenum.ONE_MINUS_SRC_ALPHA.getValue() );
	}

	/*
	 * Return a Boolean true if the context supports vertex textures.
	 */
	public boolean supportsVertexTextures()
	{
		return this.maxVertexTextures > 0;
	}

	public void setSize(int width, int height)
	{
		getCanvas().setSize(width, height);
		setViewport(0, 0, width, height);
	}

	/*
	 * Sets the viewport to render from (x, y) to (x + width, y + height).
	 */
	public void setViewport(int x, int y, int width, int height)
	{
		this.viewportX = x;
		this.viewportY = y;

		this.viewportWidth = width;
		this.viewportHeight = height;

		getGL().viewport(this.viewportX, this.viewportY, this.viewportWidth, this.viewportHeight);
	}

	/*
	 * Sets the scissor area from (x, y) to (x + width, y + height).
	 */
	public void setScissor(int x, int y, int width, int height)
	{
		getGL().scissor(x, y, width, height);
	}

	/*
	 * Enable the scissor test. When this is enabled, only the pixels within the defined scissor area will be affected by further renderer actions.
	 */
	public void enableScissorTest(boolean enable)
	{
		if (enable)
			getGL().enable(GLenum.SCISSOR_TEST.getValue());
		else
			getGL().disable(GLenum.SCISSOR_TEST.getValue());
	}
	
	public void setPrecision(WebGLRenderer.PRECISION precision) 
	{
		this.precision = precision; 
	}
	
	public void setMaxLights(int maxLights) 
	{
		this.maxLights = maxLights;
	}
	
	/*
	 * Sets the clear color, using hex for the color and alpha for the opacity.
	 */
	public void setClearColorHex( int hex, float alpha ) 
	{
		this.clearColor.setHex( hex );
		this.clearAlpha = alpha;

		getGL().clearColor( this.clearColor.getR(), this.clearColor.getG(), this.clearColor.getB(), this.clearAlpha );
	}
	
	/*
	 * Sets the clear color, using color for the color and alpha for the opacity.
	 */
	public void setClearColor( Color3f color, float alpha ) 
	{
		this.clearColor.copy(color);
		this.clearAlpha = alpha;

		getGL().clearColor( this.clearColor.getR(), this.clearColor.getG(), this.clearColor.getB(), this.clearAlpha );
	}

	/*
	 * Returns a Color3f instance with the current clear color.
	 */
	public Color3f getClearColor() 
	{
		return this.clearColor;
	}

	/*
	 * Returns a float with the current clear alpha. Ranges from 0 to 1.
	 */
	public float getClearAlpha() 
	{
		return this.clearAlpha;
	}

	/*
	 * Tells the renderer to clear its color, depth or stencil drawing buffer(s).
	 * If no parameters are passed, no buffer will be cleared.
	 */
	public void clear( boolean color, boolean depth, boolean stencil ) 
	{
		int bits = 0;

		if ( color ) bits |= GLenum.COLOR_BUFFER_BIT.getValue();
		if ( depth ) bits |= GLenum.DEPTH_BUFFER_BIT.getValue();
		if ( stencil ) bits |= GLenum.STENCIL_BUFFER_BIT.getValue();

		getGL().clear( bits );
	}

	public void clearTarget( RenderTargetTexture renderTarget, boolean color, boolean depth, boolean stencil ) 
	{
		setRenderTarget( renderTarget );
		clear( color, depth, stencil );
	}
	
	/**************************************
	 * Deallocation
	 **************************************/
	/*
	 * object — an instance of Object3D
	 * Removes an object from the GL context and releases all the data (geometry, matrices...) 
	 * that the GL context keeps about the object, but it doesn't release textures or affect any 
	 * JavaScript data.
	 */
	public void deallocateObject( GeometryObject object ) 
	{
		if ( ! object.__webglInit ) return;

		object.__webglInit = false;

		object._modelViewMatrix = null;
		object._normalMatrix = null;

		object._normalMatrixArray = null;
		object._modelViewMatrixArray = null;
		object._objectMatrixArray = null;

		if ( object.getClass() == Mesh.class )
			for ( GeometryGroup g : object.getGeometry().geometryGroups.values() )
				deleteMeshBuffers( g );
					
		else if ( object.getClass() == Ribbon.class )
			deleteRibbonBuffers( object.getGeometry() );

		else if ( object.getClass() == Line.class )
			deleteLineBuffers( object.getGeometry() );

		else if ( object.getClass() == ParticleSystem.class )
			deleteParticleBuffers( object.getGeometry() );
	}

	/*
	 * Releases a texture from the GL context.
	 * texture — an instance of Texture
	 */
	public void deallocateTexture( Texture texture ) 
	{
		if ( ! texture.__webglInit ) return;

		texture.__webglInit = false;
		getGL().deleteTexture( texture.__webglTexture );

		this.getInfo().getMemory().textures--;
	}

	/*
	 * Releases a render target from the GL context.
	 * @parameter
	 * 		renderTarget — an instance of WebGLRenderTarget
	 */
	public void deallocateRenderTarget ( RenderTargetTexture renderTarget ) 
	{
		renderTarget.deallocate(getGL());
	}

	
	/**************************************
	 * Rendering
	 **************************************/
	/*
	 * scene — an instance of Scene
	 * camera — an instance of Camera
	 * Tells the shadow map plugin to update using the passed scene and camera parameters.
	 */
	public void updateShadowMap( Scene scene, Camera camera ) 
	{
		this.cache_currentProgram = null;
		this.cache_oldBlending = null;
		this.cache_oldDepthTest = null;
		this.cache_oldDepthWrite = null;
		this.cache_currentGeometryGroupHash = -1;
		this.cache_currentMaterialId = -1;
		this.lightsNeedUpdate = true;
		this.cache_oldDoubleSided = null;
		this.cache_oldFlipSided = null;

		//TODO: this is extras 
		//shadowMapPlugin.update( scene, camera );
	}
	
	/**************************************
	 * Internal functions
	 **************************************/
	// Buffer deallocation

	private void deleteParticleBuffers ( Geometry geometry ) 
	{
		getGL().deleteBuffer( geometry.__webglVertexBuffer );
		getGL().deleteBuffer( geometry.__webglColorBuffer );

		this.getInfo().getMemory().geometries --;
	}
	
	private void deleteLineBuffers (Geometry geometry ) 
	{
		deleteParticleBuffers(geometry);
	};

	private void deleteRibbonBuffers (Geometry geometry ) 
	{
		deleteParticleBuffers(geometry);
	}
	
	private void deleteMeshBuffers ( GeometryGroup geometryGroup ) 
	{
		getGL().deleteBuffer( geometryGroup.__webglVertexBuffer );
		getGL().deleteBuffer( geometryGroup.__webglNormalBuffer );
		getGL().deleteBuffer( geometryGroup.__webglTangentBuffer );
		getGL().deleteBuffer( geometryGroup.__webglColorBuffer );
		getGL().deleteBuffer( geometryGroup.__webglUVBuffer );
		getGL().deleteBuffer( geometryGroup.__webglUV2Buffer );

		getGL().deleteBuffer( geometryGroup.__webglSkinVertexABuffer );
		getGL().deleteBuffer( geometryGroup.__webglSkinVertexBBuffer );
		getGL().deleteBuffer( geometryGroup.__webglSkinIndicesBuffer );
		getGL().deleteBuffer( geometryGroup.__webglSkinWeightsBuffer );

		getGL().deleteBuffer( geometryGroup.__webglFaceBuffer );
		getGL().deleteBuffer( geometryGroup.__webglLineBuffer );

		if ( geometryGroup.numMorphTargets != 0) {

			for ( int m = 0; m < geometryGroup.numMorphTargets; m ++ ) {
				getGL().deleteBuffer( geometryGroup.__webglMorphTargetsBuffers.get( m ) );
			}
		}

		if ( geometryGroup.numMorphNormals != 0 ) {

			for ( int m = 0; m <  geometryGroup.numMorphNormals; m ++ ) {
				getGL().deleteBuffer( geometryGroup.__webglMorphNormalsBuffers.get( m ) );
			}
		}


		if ( geometryGroup.__webglCustomAttributesList != null) {

			for ( WebGLCustomAttribute att : geometryGroup.__webglCustomAttributesList ) {
				getGL().deleteBuffer( att.buffer );
			}
		}

		this.getInfo().getMemory().geometries --;
	}
	
	// Buffer initialization
	
	public void setupMorphTargets ( Material material, GeometryGroup geometryGroup, Object3D object ) 
	{
		// set base
		Map<String, Integer> attributes = material.program.attributes;

		if ( object.morphTargetBase != - 1 ) 
		{
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphTargetsBuffers.get( object.morphTargetBase ) );
			getGL().vertexAttribPointer( attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );

		} else if ( attributes.get("position") >= 0 ) {
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglVertexBuffer );
			getGL().vertexAttribPointer( attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
		}

		if ( object.morphTargetForcedOrder.size() > 0 ) 
		{
			// set forced order

			int m = 0;
			List<Integer> order = object.morphTargetForcedOrder;
			List<Integer> influences = object.morphTargetInfluences;

			while ( m < material.numSupportedMorphTargets && m < order.size() ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphTargetsBuffers.get( order.get( m ) ) );
				getGL().vertexAttribPointer( attributes.get("morphTarget" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );

				if ( material.morphNormals ) {
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphNormalsBuffers.get( order.get( m ) ) );
					getGL().vertexAttribPointer( attributes.get("morphNormal" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
				}

				object.__webglMorphTargetInfluences.set( m , influences.get( order.get( m ) ));

				m ++;
			}

		} else {

			// find most influencing

			List<Boolean> used = new ArrayList<Boolean>();
			int candidateInfluence = - 1;
			int candidate = 0;
			List<Integer> influences = object.morphTargetInfluences;			

			if ( object.morphTargetBase != - 1 )
				used.set( object.morphTargetBase, true);

			int m = 0;
			while ( m < material.numSupportedMorphTargets ) {

				for ( int i = 0; i < influences.size(); i ++ ) {
					if ( !used.get( i ) && influences.get(i) > candidateInfluence ) 
					{
						candidate = i;
						candidateInfluence = influences.get( candidate );
					}
				}

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphTargetsBuffers.get( candidate ) );
				getGL().vertexAttribPointer( attributes.get( "morphTarget" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );

				if ( material.morphNormals ) {
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryGroup.__webglMorphNormalsBuffers.get( candidate ) );
					getGL().vertexAttribPointer( attributes.get( "morphNormal" + m ), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
				}

				object.__webglMorphTargetInfluences.set( m, candidateInfluence);

				used.set( candidate, true);
				candidateInfluence = -1;
				m ++;
			}
		}

		// load updated influences uniform
		if( material.program.uniforms.get("morphTargetInfluences") != null ) {
			List<Integer> vals = object.__webglMorphTargetInfluences;
			float[] val2 = new float[vals.size()];
			for (int i = 0; i < vals.size(); i++) {
			    Integer f = vals.get(i);
			    val2[i] = (f != null ? f : Float.NaN); // Or whatever default you want.
			}
			getGL().uniform1fv( material.program.uniforms.get("morphTargetInfluences"), val2 );
		}
	}

	// Rendering
	public void render( Scene scene, Camera camera)
	{
		render(scene, camera, null);
	}
	
	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget)
	{
		render(scene, camera, renderTarget, false);
	}

	public void render( Scene scene, Camera camera, RenderTargetTexture renderTarget, boolean forceClear ) 
	{
		Log.debug("Called render()");

		List<Light> lights = scene.getLights();
		Fog fog = scene.getFog();

		// reset caching for this frame
		this.cache_currentMaterialId = -1;
		this.lightsNeedUpdate = true;

		// update scene graph
		if ( camera.getParent() == null ) 
		{
			Log.warn("DEPRECATED: Camera hasn\'t been added to a Scene. Adding it...");
			scene.addChild( camera );
		}

		Log.debug("render() this.autoUpdateScene=" + this.autoUpdateScene);
		if ( this.autoUpdateScene ) 
				scene.updateMatrixWorld(false);

		// update camera matrices and frustum

		if ( camera._viewMatrixArray == null ) 
			camera._viewMatrixArray = Float32Array.create( 16 );

		if ( camera._projectionMatrixArray == null ) 
			camera._projectionMatrixArray = Float32Array.create( 16 );

		camera.getMatrixWorldInverse().getInverse( camera.getMatrixWorld() );
		camera.getMatrixWorldInverse().flattenToArray( camera._viewMatrixArray );
		camera.getProjectionMatrix().flattenToArray( camera._projectionMatrixArray );

		this._projScreenMatrix.multiply( camera.getProjectionMatrix(), camera.getMatrixWorldInverse() );
		this.frustum.setFromMatrix( _projScreenMatrix );

		// update WebGL objects
		if ( this.autoUpdateObjects ) 
			initWebGLObjects( scene );

		// custom render plugins (pre pass)
		// renderPlugins( this.renderPluginsPre, scene, camera );

		this.getInfo().getRender().calls = 0;
		this.getInfo().getRender().vertices = 0;
		this.getInfo().getRender().faces = 0;
		this.getInfo().getRender().points = 0;

		setRenderTarget( renderTarget );

		if ( this.autoClear || forceClear )
			clear( this.autoClearColor, this.autoClearDepth, this.autoClearStencil );

		// set matrices for regular objects (frustum culled)
		List<WebGLObject> renderList = scene.__webglObjects;
		Log.debug("render(): Render list size is: " + renderList.size());

		for(WebGLObject webglObject: renderList) 
		{
			GeometryObject object = webglObject.object;
			webglObject.render = false;

			if ( object.isVisible() ) 
			{
				if ( ! ( object.getClass() == Mesh.class 
						|| object.getClass() == ParticleSystem.class ) 
						|| ! ( object.frustumCulled ) 
						|| frustum.contains( object ) )
				{
					setupMatrices( (Object3D) object, camera );
					unrollBufferMaterial( webglObject );
					webglObject.render = true;

					if ( this.sortObjects ) 
					{

						if ( object.renderDepth > 0 ) 
						{
							webglObject.z = object.renderDepth;
						} 
						else 
						{
							this._vector3.copy( object.getMatrixWorld().getPosition() );
							this._projScreenMatrix.multiplyVector3( _vector3 );

							webglObject.z = _vector3.getZ();
						}
					}
				}
			}
		}

		if ( this.sortObjects )
			Collections.sort(renderList);

		// set matrices for immediate objects

		List<WebGLObject> renderListI = scene.__webglObjectsImmediate;

		for(WebGLObject webglObject: renderListI)
		{
			GeometryObject object = webglObject.object;

			if ( object.isVisible() ) 
			{
				Log.debug("render(): set matrices for immediate objects");

				setupMatrices( (Object3D) object, camera );

				unrollImmediateBufferMaterial( webglObject );
			}
		}

		if ( scene.overrideMaterial != null ) 
		{
			Log.error("render(): override material");
			
			Material material = scene.overrideMaterial;

			//TODO: FIX
//			this.setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );
//			this.setDepthTest( material.isDepthTest() );
//			this.setDepthWrite( material.isDepthWrite() );
//			setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits() );
//
//			renderObjects( scene.__webglObjects, false, "", camera, lights, fog, true, material );
//			renderObjectsImmediate( scene.__webglObjectsImmediate, "", camera, lights, fog, false, material );

		} 
		else 
		{
			Log.debug("render(): NON override material");
			// opaque pass (front-to-back order)

			setBlending( Material.BLENDING.NORMAL);

			renderObjects( scene.__webglObjects, true, "opaque", camera, lights, fog, false );
			renderObjectsImmediate( scene.__webglObjectsImmediate, "opaque", camera, lights, fog, false );

			// transparent pass (back-to-front order)

			renderObjects( scene.__webglObjects, false, "transparent", camera, lights, fog, true );
			renderObjectsImmediate( scene.__webglObjectsImmediate, "transparent", camera, lights, fog, true );
		}

		// custom render plugins (post pass)
		// renderPlugins( this.renderPluginsPost, scene, camera );


		// Generate mipmap if we're using any kind of mipmap filtering
		if ( renderTarget != null && renderTarget.isGenerateMipmaps() 
				&& renderTarget.getMinFilter() != TextureMinFilter.NEAREST 
				&& renderTarget.getMinFilter() != TextureMinFilter.LINEAR)
		{
			renderTarget.updateRenderTargetMipmap(getGL());
		}

		// Ensure depth buffer writing is enabled so it can be cleared on next render

		this.setDepthTest( true );
		this.setDepthWrite( true );

//		 getGL().finish();
	}

////	function renderPlugins( plugins, scene, camera ) {
////
////		if ( ! plugins.length ) return;
////
////		for ( var i = 0, il = plugins.length; i < il; i ++ ) {
////
////			// reset state for plugin (to start from clean slate)
////
////			_currentProgram = null;
////			_currentCamera = null;
////
////			_oldBlending = -1;
////			_oldDepthTest = -1;
////			_oldDepthWrite = -1;
////			_oldDoubleSided = -1;
////			_oldFlipSided = -1;
////			_currentGeometryGroupHash = -1;
////			_currentMaterialId = -1;
////
////			_lightsNeedUpdate = true;
////
////			plugins[ i ].render( scene, camera, _currentWidth, _currentHeight );
////
////			// reset state after plugin (anything could have changed)
////
////			_currentProgram = null;
////			_currentCamera = null;
////
////			_oldBlending = -1;
////			_oldDepthTest = -1;
////			_oldDepthWrite = -1;
////			_oldDoubleSided = -1;
////			_oldFlipSided = -1;
////			_currentGeometryGroupHash = -1;
////			_currentMaterialId = -1;
////
////			_lightsNeedUpdate = true;
////
////		}
////
////	};

	// TODO: CHECK callback
	public void renderImmediateObject( Camera camera, List<Light> lights, Fog fog, Material material, GeometryObject object ) 
	{
		Program program = setProgram( camera, lights, fog, material, object );

		this.cache_currentGeometryGroupHash = -1;

		setObjectFaces( (SidesObject) object );

//		if ( object.immediateRenderCallback )
//			object.immediateRenderCallback( program, this._gl, this._frustum );
//
//		else
//			object.render( function( object ) { _this.renderBufferImmediate( object, program, material.shading ); } );
	}
	
	public void renderObjectsImmediate ( List<WebGLObject> renderList, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending) 
	{
		renderObjectsImmediate ( renderList, materialType, camera, lights, fog,  useBlending, null);
	}

	public void renderObjectsImmediate ( List<WebGLObject> renderList, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending, Material overrideMaterial ) 
	{
		for ( int i = 0; i < renderList.size(); i ++ ) 
		{

			WebGLObject webglObject = renderList.get( i );
			GeometryObject object = webglObject.object;

			if ( object.isVisible()) 
			{

				Material material = null;
				if ( overrideMaterial != null)
				{
					material = overrideMaterial;

				} 
				else 
				{
					if(materialType == "opaque")
						material = webglObject.opaque;
					else if(materialType == "transparent")
						material = webglObject.transparent;

					if ( material == null ) continue;

					if ( useBlending ) 
						this.setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst() );

					this.setDepthTest( material.isDepthTest() );
					this.setDepthWrite( material.isDepthWrite() );
					setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits());
				}

				renderImmediateObject( camera, lights, fog, material, object );
			}
		}
	}

	public void renderObjects ( List<WebGLObject> renderList, boolean reverse, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending) 
	{
		renderObjects ( renderList, reverse, materialType, camera, lights, fog, useBlending, null);
	}

	public void renderObjects ( List<WebGLObject> renderList, boolean reverse, String materialType, Camera camera, List<Light> lights, Fog fog, boolean useBlending, Material overrideMaterial ) 
	{
		Log.debug("Called renderObjects() render list contains = " + renderList.size());
		
		int start = 0;
		int end = 0;
		int delta = 0;
			
		if ( reverse ) 
		{
			start = renderList.size() - 1;
			end = -1;
			delta = -1;

		} 
		else 
		{
			start = 0;
			end = renderList.size();
			delta = 1;
		}

		Material material = null;

		for ( int i = start; i != end; i += delta ) {

			WebGLObject webglObject = renderList.get( i );

			if ( webglObject.render ) 
			{
 
				GeometryObject object = webglObject.object;
				GeometryBuffer buffer = webglObject.buffer;

				if ( overrideMaterial != null ) 
				{
					material = overrideMaterial;

				} 
				else 
				{
					if(materialType == "opaque")
						material = webglObject.opaque;
					else if(materialType == "transparent")
						material = webglObject.transparent;

					if ( material == null ) continue;

					if ( useBlending ) 
						setBlending( material.getBlending(), material.getBlendEquation(), material.getBlendSrc(), material.getBlendDst());

					setDepthTest( material.isDepthTest());
					setDepthWrite( material.isDepthWrite());
					setPolygonOffset( material.isPolygonOffset(), material.getPolygonOffsetFactor(), material.getPolygonOffsetUnits());
				}

				if(object instanceof SidesObject)
					setObjectFaces( (SidesObject) object );

				// TODO: Extras
//				if ( buffer instanceof THREE.BufferGeometry )
//					_this.renderBufferDirect( camera, lights, fog, material, buffer, object );
//				else
					renderBuffer( camera, lights, fog, material, buffer, (GeometryObject) object );
			}
		}
	}

	// Buffer rendering
	// Render GeometryObject vis material
	public void renderBuffer( Camera camera, List<Light> lights, Fog fog, Material material, GeometryBuffer geometryBuffer, GeometryObject object ) 
	{
		if ( material.visible == false ) 
			return;

		Program program = setProgram( camera, lights, fog, material, object );

		Map<String, Integer> attributes = program.attributes;

		boolean updateBuffers = false;
		int wireframeBit = material.wireframe ? 1 : 0;


		int geometryGroupHash = ( geometryBuffer.getId() * 0xffffff ) + ( program.id * 2 ) + wireframeBit;

//		GWT.log("--- renderBuffer() geometryGroupHash=" + geometryGroupHash 
//				+ ", _currentGeometryGroupHash=" +  this._currentGeometryGroupHash
//				+ ", program.id=" + program.id
//				+ ", geometryGroup.id=" + geometryBuffer.getId()
//				+ ", __webglLineCount=" + geometryBuffer.__webglLineCount
//				+ ", object.id=" + object.getId()
//				+ ", wireframeBit=" + wireframeBit);

		if ( geometryGroupHash != this.cache_currentGeometryGroupHash ) 
		{
			this.cache_currentGeometryGroupHash = geometryGroupHash;
			updateBuffers = true;
		}

		// vertices
		if ( !material.morphTargets && attributes.get("position") >= 0 ) 
		{
			if ( updateBuffers ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglVertexBuffer );
				getGL().vertexAttribPointer( attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

		} 
		else if ( object.morphTargetBase > 0 ) 
		{
				Log.error("????????? object.morphTargetBase=" + object.morphTargetBase);
//				setupMorphTargets( material, geometryGroup, object );
		}

		
		if ( updateBuffers ) 
		{
			// custom attributes

			// Use the per-geometryGroup custom attribute arrays which are setup in initMeshBuffers

			if ( geometryBuffer.__webglCustomAttributesList != null ) 
			{
				for ( int i = 0; i < geometryBuffer.__webglCustomAttributesList.size(); i ++ ) 
				{
					WebGLCustomAttribute attribute = geometryBuffer.__webglCustomAttributesList.get( i );

					if( attributes.get( attribute.belongsToAttribute ) >= 0 ) 
					{
						getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), attribute.buffer );
						getGL().vertexAttribPointer( attributes.get( attribute.belongsToAttribute ), attribute.size, GLenum.FLOAT.getValue(), false, 0, 0 );
					}
				}
			}

			// colors
			if ( attributes.get("color") >= 0 ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglColorBuffer );
				getGL().vertexAttribPointer( attributes.get("color"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

			// normals
			if ( attributes.get("normal") >= 0 ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglNormalBuffer );
				getGL().vertexAttribPointer( attributes.get("normal"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

			// tangents
			if ( attributes.get("tangent") >= 0 ) 
			{
				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglTangentBuffer );
				getGL().vertexAttribPointer( attributes.get("tangent"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );
			}

			// uvs
			if ( attributes.get("uv") >= 0 ) 
			{
				if ( geometryBuffer.__webglUVBuffer != null) 
				{
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglUVBuffer );
					getGL().vertexAttribPointer( attributes.get("uv"), 2, GLenum.FLOAT.getValue(), false, 0, 0 );

					getGL().enableVertexAttribArray( attributes.get("uv") );

				} else {
					getGL().disableVertexAttribArray( attributes.get("uv") );
				}
			}

			if ( attributes.get("uv2") >= 0 ) 
			{
				if ( geometryBuffer.__webglUV2Buffer != null) 
				{
					getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglUV2Buffer );
					getGL().vertexAttribPointer( attributes.get("uv2"), 2, GLenum.FLOAT.getValue(), false, 0, 0 );

					getGL().enableVertexAttribArray( attributes.get("uv2") );

				} else {
					getGL().disableVertexAttribArray( attributes.get("uv2") );
				}
			}

			if ( material.skinning &&
				 attributes.get("skinVertexA") >= 0 && attributes.get("skinVertexB") >= 0 &&
				 attributes.get("skinIndex") >= 0 && attributes.get("skinWeight") >= 0 ) 
			{

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinVertexABuffer );
				getGL().vertexAttribPointer( attributes.get("skinVertexA"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinVertexBBuffer );
				getGL().vertexAttribPointer( attributes.get("skinVertexB"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinIndicesBuffer );
				getGL().vertexAttribPointer( attributes.get("skinIndex"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );

				getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), geometryBuffer.__webglSkinWeightsBuffer );
				getGL().vertexAttribPointer( attributes.get("skinWeight"), 4, GLenum.FLOAT.getValue(), false, 0, 0 );
			}
		}

		Log.debug(" -> renderBuffer() ID " + object.getId() + " = " + object.getClass().getName());

		// Render object's buffers
		object.renderBuffer(this, geometryBuffer, updateBuffers);
	}

	public void renderBufferImmediate( Object3D object, Program program, Material.SHADING shading ) 
	{
		if ( object.__webglVertexBuffer == null ) 
			object.__webglVertexBuffer = getGL().createBuffer();

		if ( object.__webglNormalBuffer == null ) 
			object.__webglNormalBuffer = getGL().createBuffer();

		if ( object.hasPos ) {
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), object.__webglVertexBuffer );
			getGL().bufferData( GLenum.ARRAY_BUFFER.getValue(), object.positionArray, GLenum.DYNAMIC_DRAW.getValue() );
			getGL().enableVertexAttribArray( program.attributes.get("position") );
			getGL().vertexAttribPointer( program.attributes.get("position"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
		}

		if ( object.hasNormal ) 
		{
			getGL().bindBuffer( GLenum.ARRAY_BUFFER.getValue(), object.__webglNormalBuffer );

			if ( shading == Material.SHADING.FLAT ) 
			{
				for(int  i = 0; i < (object.count * 3); i += 9 ) 
				{
					Float32Array normalArray = object.normalArray;

					float nax  = normalArray.get( i );
					float nay  = normalArray.get( i + 1 );
					float naz  = normalArray.get( i + 2 );

					float nbx  = normalArray.get( i + 3 );
					float nby  = normalArray.get( i + 4 );
					float nbz  = normalArray.get( i + 5 );

					float ncx  = normalArray.get( i + 6 );
					float ncy  = normalArray.get( i + 7 );
					float ncz  = normalArray.get( i + 8 );

					float nx = ( nax + nbx + ncx ) / 3f;
					float ny = ( nay + nby + ncy ) / 3f;
					float nz = ( naz + nbz + ncz ) / 3f;

					normalArray.set(i, nx);
					normalArray.set( i + 1, ny);
					normalArray.set( i + 2, nz);

					normalArray.set( i + 3, nx);
					normalArray.set( i + 4, ny);
					normalArray.set( i + 5, nz);

					normalArray.set( i + 6, nx);
					normalArray.set( i + 7, ny);
					normalArray.set( i + 8, nz);
				}
			}

			getGL().bufferData( GLenum.ARRAY_BUFFER.getValue(), object.normalArray, GLenum.DYNAMIC_DRAW.getValue() );
			getGL().enableVertexAttribArray( program.attributes.get("normal") );
			getGL().vertexAttribPointer( program.attributes.get("normal"), 3, GLenum.FLOAT.getValue(), false, 0, 0 );
		}

		getGL().drawArrays( GLenum.TRIANGLES.getValue(), 0, object.count );
		object.count = 0;
	}
	
//	public void renderBufferDirect ( Camera camera, List<Light> lights, Fog fog, Material material, GeometryGroup geometryGroup, Object3D object ) 
//	{
//		if ( !material.visible ) return;
//
//		Program program = setProgram( camera, lights, fog, material, object );
//		Map<String, Integer> attributes = program.attributes;
//
//		boolean updateBuffers = false;
//		int wireframeBit = material.wireframe ? 1 : 0;
//		int geometryGroupHash = ( geometryGroup.id * 0xffffff ) + ( program.id * 2 ) + wireframeBit;
//
//		if ( geometryGroupHash != this._currentGeometryGroupHash ) {
//			this._currentGeometryGroupHash = geometryGroupHash;
//			updateBuffers = true;
//		}
//
//		// render mesh
//		if ( object instanceof Mesh ) {
//
//			List<Integer> offsets = geometryGroup.offsets;
//
//			for ( int i = 0; i < offsets.size(); ++ i ) {
//				if ( updateBuffers ) {
//
//					// vertices
//
//					getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexPositionBuffer );
//					getGL().vertexAttribPointer( attributes.get("position"), 
//							geometryGroup.vertexPositionBuffer.itemSize, 
//							GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 3 );
//
//					// normals
//
//					if ( attributes.get("normal") >= 0 && geometryGroup.vertexNormalBuffer != null) {
//						getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexNormalBuffer );
//						getGL().vertexAttribPointer( attributes.get("normal"), 
//								geometryGroup.vertexNormalBuffer.itemSize, 
//								GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 3 );
//					}
//
//					// uvs
//
//					if ( attributes.uv >= 0 && geometryGroup.vertexUvBuffer != null ) {
//
//						if ( geometryGroup.vertexUvBuffer != null) {
//							getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexUvBuffer );
//							getGL().vertexAttribPointer(  attributes.get("uv"), 
//									geometryGroup.vertexUvBuffer.itemSize, 
//									GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 2 );
//							getGL().enableVertexAttribArray( attributes.uv );
//						} else {
//							getGL().disableVertexAttribArray( attributes.uv );
//						}
//
//					}
//
//					// colors
//
//					if ( attributes.color >= 0 && geometryGroup.vertexColorBuffer != null ) {
//						getGL().bindBuffer( GLenum.ARRAY_BUFFER, geometryGroup.vertexColorBuffer );
//						getGL().vertexAttribPointer( attributes.get("color"), 
//								geometryGroup.vertexColorBuffer.itemSize, 
//								GLenum.FLOAT, false, 0, offsets.get( i ).index * 4 * 4 );
//					}
//
//					getGL().bindBuffer( GLenum.ELEMENT_ARRAY_BUFFER, geometryGroup.vertexIndexBuffer );
//				}
//
//				// render indexed triangles
//				getGL().drawElements( GLenum.TRIANGLES, offsets.get( i ).count, GLenum.UNSIGNED_SHORT, offsets.get( i ).start * 2 ); // 2 = Uint16
//
//				this.info.render.calls ++;
//				// not really true, here vertices can be shared
//				this.info.render.vertices += offsets.get( i ).count; 
//				this.info.render.faces += offsets.get( i ).count / 3;
//			}
//		}
//	}

	public void unrollImmediateBufferMaterial ( WebGLObject globject ) 
	{
		GeometryObject object = globject.object;
		Material material = object.getMaterial();

		if ( material.isTransparent()) {
			globject.transparent = material;
			globject.opaque = null;

		} else {
			globject.opaque = material;
			globject.transparent = null;
		}
	}

	public void unrollBufferMaterial (  WebGLObject globject ) 
	{
		GeometryObject object = globject.object;

		Material meshMaterial = object.getMaterial();

		if ( meshMaterial instanceof MeshFaceMaterial ) 
		{
			GeometryGroup buffer = (GeometryGroup) globject.buffer;
			int materialIndex = buffer.materialIndex;

			if ( materialIndex >= 0 ) 
			{
				Material material = object.getGeometry().getMaterials().get( materialIndex );

				if ( material.isTransparent() ) 
				{
					globject.transparent = material;
					globject.opaque = null;
					
				} 
				else 
				{
					globject.opaque = material;
					globject.transparent = null;
				}
			}

		} 
		else 
		{

			Material material = meshMaterial;

			if ( material != null) 
			{
				if ( material.isTransparent() ) 
				{
					globject.transparent = material;
					globject.opaque = null;

				} 
				else 
				{
					globject.opaque = material;
					globject.transparent = null;
				}
			}
		}
	}

	/**
	 * Refresh Scene's objects
	 * 
	 * @param scene the Scene with child objects
	 */
	public void initWebGLObjects(Scene scene ) 
	{
		if ( scene.__webglObjects == null ) 
		{
			scene.__webglObjects = new ArrayList<WebGLObject>();
			scene.__webglObjectsImmediate = new ArrayList<WebGLObject>();
			scene.__webglSprites = new ArrayList<Sprite>();
			scene.__webglFlares = new ArrayList<LensFlare>();
		}

		Log.debug("initWebGLObjects() objectsAdded=" + scene.getObjectsAdded().size() 
				+ ", objectsRemoved=" + scene.getObjectsRemoved().size());
		
		while ( scene.getObjectsAdded().size() > 0 ) 
		{
			if(scene.getObjectsAdded().get( 0 ) instanceof GeometryObject)
				addObject( (GeometryObject) scene.getObjectsAdded().get( 0 ), scene );
			scene.getObjectsAdded().remove(0);
		}

		while ( scene.getObjectsRemoved().size() > 0 ) 
		{
			removeObject( (GeometryObject) scene.getObjectsRemoved().get( 0 ), scene );
			scene.getObjectsRemoved().remove(0);
		}

		// update must be called after objects adding / removal
		for(WebGLObject object: scene.__webglObjects)
		{
			object.object.setBuffer(this);
		}			
	}

	// Objects adding
	public void addObject ( GeometryObject object, Scene scene )
	{
		Log.debug("addObject() object=" + object.getClass().getName());

		if ( ! object.__webglInit ) 
		{
			object.__webglInit = true;

			object._modelViewMatrix = new Matrix4f();
			object._normalMatrix = new Matrix3f();

			Log.debug("addObject() initBuffer()");
			object.initBuffer(this);
		}

		if ( ! object.__webglActive ) 
		{
			object.__webglActive = true;

			Log.debug("addObject() addObjectAddBuffer()");
			addObjectAddBuffer(object, scene);
		}
	}
	
	public void addObjectAddBuffer(GeometryObject object, Scene scene)
	{
		if ( object instanceof Mesh ) 
		{
			Mesh mesh = (Mesh)object;
			Geometry geometry = mesh.getGeometry();
			Log.debug("addObject() add Mesh buffer");
//			if(geometry instanceof BufferGeometry) 
//			{
//				addBuffer( scene.__webglObjects, geometry, object );
//			}
//			else {				
				for ( GeometryGroup geometryGroup : geometry.geometryGroups.values())
					addBuffer( scene.__webglObjects, geometryGroup, object );
//			}

		} else if ( object.getClass() == Ribbon.class ||
					object.getClass() == Line.class ||
					object.getClass() == ParticleSystem.class ) {

			Geometry geometry = object.getGeometry();
			addBuffer( scene.__webglObjects, geometry, object );

			// TODO: fix this
//		} else if ( object.getClass() instanceof THREE.ImmediateRenderObject || object.immediateRenderCallback ) {
//
//			addBufferImmediate( scene.__webglObjectsImmediate, object );
//
//		} else if ( object.getClass() == Sprite.class ) {
//			scene.__webglSprites.add( (Sprite) object );
//
//		} else if ( object.getClass() == LensFlare.class ) {
//			scene.__webglFlares.add( (LensFlare) object );
		}
	}

	public void addBuffer ( List<WebGLObject> objlist, GeometryBuffer buffer, GeometryObject object ) 
	{
		objlist.add(new WebGLObject(buffer, object, null, null));
	}

//	function addBufferImmediate ( objlist, object ) {
//
//		objlist.push(
//			{
//				object: object,
//				opaque: null,
//				transparent: null
//			}
//		);
//	}

	/*
	 * Objects removal
	 */
	public static void removeObject ( GeometryObject object, Scene scene ) 
	{
		if ( object instanceof GeometryObject) 
		{
			removeInstances( scene.__webglObjects, object );

			// TODO: fix this
//		} else if ( object.getClass() == Sprite.class ) {
//			removeInstancesDirect( scene.__webglSprites, object );
//
//		} else if ( object.getClass() == LensFlare.class ) {
//			removeInstancesDirect( scene.__webglFlares, object );

//		} else if ( object instanceof ImmediateRenderObject || object.immediateRenderCallback ) {
//			removeInstances( scene.__webglObjectsImmediate, object );
		}

		object.__webglActive = false;
	}

	public static void removeInstances ( List<WebGLObject> objlist, Object3D object ) 
	{
		for ( int o = objlist.size() - 1; o >= 0; o -- )
			if ( objlist.get( o ).object == object )
				objlist.remove(o);
	}

	public static void removeInstancesDirect ( List<WebGLObject> objlist, Object3D object ) 
	{
		for ( int o = objlist.size() - 1; o >= 0; o -- )
			if ( objlist.get( o ).object == object )
				objlist.remove(o);
	}

	// Materials

	public void initMaterial ( Material material, List<Light> lights, Fog fog, GeometryObject object ) 
	{
		Log.debug("Called initMaterial for material: " + material.getClass().getName() + " and object " + object.getClass().getName());

		Shader shaderPrototype = material.getShaderId(); 

		if ( shaderPrototype != null )
			material.setMaterialShaders(shaderPrototype);

		// heuristics to create shader parameters according to lights in the scene
		// (not to blow over maxLights budget)

		Map<String, Integer> maxLightCount = allocateLights( lights );
		int maxShadows = allocateShadows( lights );
		int maxBones = allocateBones( object );
		
		ProgramParameters parameters = new ProgramParameters(maxLightCount.get("directional"), 
				maxLightCount.get("point"), maxLightCount.get("spot"), maxShadows, maxBones, this.maxMorphTargets, this.maxMorphNormals);

		if(material instanceof AbstractMapMaterial)
		{
			AbstractMapMaterial materialMap = (AbstractMapMaterial) material;
			parameters.map = (materialMap.getMap() != null);
			parameters.envMap = (materialMap.getEnvMap() != null);
			parameters.lightMap = (materialMap.getLightMap() != null);
		}

		parameters.vertexColors = (material.vertexColors != Material.COLORS.NO);

		parameters.useFog = (fog != null);
		parameters.useFog2 = (fog != null && fog.getClass() == FogExp2.class);

		parameters.sizeAttenuation = material.sizeAttenuation;

		parameters.skinning = material.skinning;

		parameters.morphTargets = material.morphTargets;
		parameters.morphNormals = material.morphNormals;

		parameters.shadowMapEnabled = this.shadowMapEnabled && object.receiveShadow;
		parameters.shadowMapSoft = this.shadowMapSoft;
		parameters.shadowMapDebug = this.shadowMapDebug;
		parameters.shadowMapCascade = this.shadowMapCascade;

		parameters.alphaTest = material.getAlphaTest();
		parameters.metal = material.metal;
		parameters.perPixel = material.perPixel;
		parameters.wrapAround = material.wrapAround;
		// TODO: Fix this
//		parameters.doubleSided = object != null && object.doubleSided;

		Log.debug("initMaterial() called new Program");

		material.program = buildProgram(this.precision, this.maxVertexTextures, 
				material.fragmentShader, material.vertexShader, material.uniforms, material.attributes, parameters );

		Map<String, Integer> attributes = material.program.attributes;
		
		if ( attributes.get("position") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("position") );

		if ( attributes.get("color") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("color") );

		if ( attributes.get("normal") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("normal") );

		if ( attributes.get("tangent") >= 0 ) 
			getGL().enableVertexAttribArray( attributes.get("tangent") );

		if ( material.skinning &&
			 attributes.get("skinVertexA") >=0 && attributes.get("skinVertexB") >= 0 &&
			 attributes.get("skinIndex") >= 0 && attributes.get("skinWeight") >= 0 ) {

			getGL().enableVertexAttribArray( attributes.get("skinVertexA") );
			getGL().enableVertexAttribArray( attributes.get("skinVertexB") );
			getGL().enableVertexAttribArray( attributes.get("skinIndex") );
			getGL().enableVertexAttribArray( attributes.get("skinWeight") );

		}

		if ( material.attributes != null )
			for ( String a : material.attributes.keySet() )
				if( attributes.get( a ) != null && attributes.get( a ) >= 0 ) 
					getGL().enableVertexAttribArray( attributes.get( a ) );

		if ( material.morphTargets) {
			material.numSupportedMorphTargets = 0;
			for ( int i = 0; i < this.maxMorphTargets; i ++ ) {
				String id = "morphTarget" + i;

				if ( attributes.get( id ) >= 0 ) {
					getGL().enableVertexAttribArray( attributes.get( id ) );
					material.numSupportedMorphTargets ++;
				}
			}
		}

		if ( material.morphNormals ) {
			material.numSupportedMorphNormals = 0;
			for ( int i = 0; i < this.maxMorphNormals; i ++ ) {
				String id = "morphNormal" + i;

				if ( attributes.get( id ) >= 0 ) {
					getGL().enableVertexAttribArray( attributes.get( id ) );
					material.numSupportedMorphNormals ++;
				}
			}
		}
	}

	public Program setProgram( Camera camera, List<Light> lights, Fog fog, Material material, GeometryObject object ) 
	{
		if ( material.program == null || material.needsUpdate ) 
		{
			initMaterial( material, lights, fog, object );
			material.needsUpdate = false;
		}

		// TODO: update
		if ( material.morphTargets ) 
		{
//			if ( ! object.__webglMorphTargetInfluences ) {
//				object.__webglMorphTargetInfluences = new Float32Array( _this.maxMorphTargets );
//
//				for ( var i = 0, il = _this.maxMorphTargets; i < il; i ++ )
//					object.__webglMorphTargetInfluences[ i ] = 0;
//			}
		}

		boolean refreshMaterial = false;

		Program program = material.program;
		Map<String, WebGLUniformLocation> p_uniforms = program.uniforms;
		Map<String, Uniform> m_uniforms = material.uniforms;

		if ( program != cache_currentProgram ) 
		{
			getGL().useProgram( program.getProgram() );
			this.cache_currentProgram = program;

			refreshMaterial = true;
		}

		if ( material.getId() != this.cache_currentMaterialId ) 
		{
			this.cache_currentMaterialId = material.getId();
			refreshMaterial = true;
		}

		if ( refreshMaterial || camera != this.cache_currentCamera ) 
		{
			getGL().uniformMatrix4fv( p_uniforms.get("projectionMatrix"), false, camera._projectionMatrixArray );

			if ( camera != this.cache_currentCamera ) 
				this.cache_currentCamera = camera;
		}

		if ( refreshMaterial ) 
		{
			// refresh uniforms common to several materials

			if ( fog != null && material.fog)
				fog.refreshUniforms( m_uniforms );

			if ( material.getClass() == MeshPhongMaterial.class ||
				 material.getClass() == MeshLambertMaterial.class ||
				 material.lights != null) 
			{

				if (this.lightsNeedUpdate ) 
				{
					setupLights( program, lights );
					this.lightsNeedUpdate = false;
				}

				refreshUniformsLights( m_uniforms, this.lights );
			}

			// HasMaterialMap
			if ( material instanceof AbstractMapMaterial)
				refreshUniformsCommon( m_uniforms, (AbstractMapMaterial) material );

			// refresh single material specific uniforms

			if ( material.getClass() == LineBasicMaterial.class ) 
			{
				refreshUniformsLine( m_uniforms, (LineBasicMaterial) material );
			} 
			else if ( material.getClass() == ParticleBasicMaterial.class ) 
			{
				refreshUniformsParticle( m_uniforms, (ParticleBasicMaterial) material );
			} 
			else if ( material.getClass() == MeshPhongMaterial.class ) 
			{
				refreshUniformsPhong( m_uniforms, (MeshPhongMaterial) material );
			} 
			else if ( material.getClass() == MeshLambertMaterial.class ) 
			{
				refreshUniformsLambert( m_uniforms,  (MeshLambertMaterial) material );
			}
			else if ( material.getClass() == MeshDepthMaterial.class ) 
			{
				if(camera.getClass() == OrthographicCamera.class) 
				{
					OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
					m_uniforms.get("mNear").value = orthographicCamera.getNear();
					m_uniforms.get("mFar").value = orthographicCamera.getFar();
				}
				
				m_uniforms.get("opacity").value = material.getOpacity();
			} 
			else if ( material.getClass() == MeshNormalMaterial.class ) 
			{
				m_uniforms.get("opacity").value = material.getOpacity();
			}

			// TODO: fix this
//			if ( object.receiveShadow && ! material._shadowPass )
//				refreshUniformsShadow( m_uniforms, lights );

			// load common uniforms
			
			loadUniformsGeneric( program, material.uniforms );

			// load material specific uniforms
			// (shader material also gets them for the sake of genericity)

			if ( material.getClass() == ShaderMaterial.class ||
				 material.getClass() == MeshPhongMaterial.class ||
				 material instanceof AbstractMapMaterial ) {

				if ( p_uniforms.get("cameraPosition") != null ) 
				{
					Vector3f position = camera.getMatrixWorld().getPosition();
					getGL().uniform3f( p_uniforms.get("cameraPosition"), position.getX(), position.getY(), position.getZ() );
				}
			}

			if ( material.getClass() == MeshPhongMaterial.class ||
				 material.getClass() == MeshLambertMaterial.class ||
				 material.getClass() == ShaderMaterial.class ||
				 material.skinning ) {

				if ( p_uniforms.get("viewMatrix") != null ) 
					getGL().uniformMatrix4fv( p_uniforms.get("viewMatrix"), false, camera._viewMatrixArray );
			}

			// TODO: fix this
//			if ( material.skinning )
//				getGL().uniformMatrix4fv( p_uniforms.get("boneGlobalMatrices"), false, object.boneMatrices );
		}

		loadUniformsMatrices( p_uniforms, object );

		if ( p_uniforms.get("objectMatrix") != null )
			getGL().uniformMatrix4fv( p_uniforms.get("objectMatrix"), false, object.getMatrixWorld().getArray() );

		return program;
	}

	/**
	 * Refresh uniforms objects.
	 * 
	 * @param uniforms
	 * @param material
	 */
	private void refreshUniformsCommon ( Map<String, Uniform> uniforms, AbstractMapMaterial material ) 
	{
		uniforms.get("opacity").value = material.getOpacity();

		if ( this.gammaInput ) 
		{
			Color3f color = (Color3f) uniforms.get("diffuse").value;
			color.copyGammaToLinear( material.getColor() );

		} 
		else
		{
			uniforms.get("diffuse").value = material.getColor();
		}

		uniforms.get("map").texture = material.getMap();

		if ( material.getMap() != null) 
		{
			Vector4f vector4 = (Vector4f)uniforms.get("offsetRepeat").value;
			vector4.set( 
					material.getMap().getOffset().getX(), 
					material.getMap().getOffset().getY(), 
					material.getMap().getRepeat().getX(), 
					material.getMap().getRepeat().getY() );
		}

		uniforms.get("lightMap").texture = material.getLightMap();

		uniforms.get("envMap").texture = material.getEnvMap();
		uniforms.get("flipEnvMap").value = ( material.getEnvMap() != null && material.getEnvMap().getClass() == RenderTargetCubeTexture.class ) ? 1.0f : -1.0f;

		if ( this.gammaInput ) 
		{
//			uniforms.reflectivity.value = material.reflectivity * material.reflectivity;
			uniforms.get("reflectivity").value = material.getReflectivity();
		} 
		else
		{
			uniforms.get("reflectivity").value = material.getReflectivity();
		}

		uniforms.get("refractionRatio").value = material.getRefractionRatio();
		uniforms.get("combine").value = material.getCombine().getValue();
		uniforms.get("useRefract").value = ( material.getEnvMap() != null 
				&& material.getEnvMap().getMapping() == Texture.MAPPING_MODE.CUBE_REFRACTION ) ? 1 : 0;
	};

	private void refreshUniformsLine ( Map<String, Uniform> uniforms, LineBasicMaterial material ) 
	{
		uniforms.get("diffuse").value = material.getColor();
		uniforms.get("opacity").value = material.getOpacity();
	}

	private void refreshUniformsParticle ( Map<String, Uniform> uniforms, ParticleBasicMaterial material ) 
	{
		uniforms.get("psColor").value = material.getColor();
		uniforms.get("opacity").value = material.getOpacity();
		uniforms.get("size").value    = material.getSize();
		uniforms.get("scale").value   = getCanvas().getHeight() / 2.0f;

		uniforms.get("map").texture   = material.getMap();
	}

	private void refreshUniformsPhong ( Map<String, Uniform> uniforms, MeshPhongMaterial material ) {

		uniforms.get("shininess").value = material.getShininess();

		if ( this.gammaInput ) {

			Color3f ambient = (Color3f) uniforms.get("ambient").value;
			ambient.copyGammaToLinear(material.getAmbient());
			
			Color3f emissive = (Color3f) uniforms.get("emissive").value;
			emissive.copyGammaToLinear(material.getEmissive());

			Color3f specular = (Color3f) uniforms.get("specular").value;
			specular.copyGammaToLinear( material.getSpecular() );

		} else {

			uniforms.get("ambient").value = material.getAmbient();
			if(material.getEmissive() != null)
				uniforms.get("emissive").value = material.getEmissive();
			uniforms.get("specular").value = material.getSpecular();

		}

		if ( material.wrapAround ) 
		{
			Vector3f wrapRGB = (Vector3f) uniforms.get("wrapRGB").value;
			wrapRGB.copy(material.getWrapRGB());
		}
	}

	private void refreshUniformsLambert ( Map<String, Uniform> uniforms, MeshLambertMaterial material ) 
	{
		if ( this.gammaInput ) 
		{
			Color3f ambient = (Color3f) uniforms.get("ambient").value;
			ambient.copyGammaToLinear(material.getAmbient());
			
			Color3f emissive = (Color3f) uniforms.get("emissive").value;
			emissive.copyGammaToLinear(material.getEmissive());
		} 
		else 
		{
			uniforms.get("ambient").value = material.getAmbient();
			if(material.getEmissive() != null)
				uniforms.get("emissive").value = material.getEmissive();
		}

		if ( material.wrapAround ) 
		{
			Vector3f wrapRGB = (Vector3f) uniforms.get("wrapRGB").value;
			wrapRGB.copy(material.getWrapRGB());
		}
	}

	private void refreshUniformsLights ( Map<String, Uniform> uniforms, WebGLRenderLights lights ) 
	{
		uniforms.get("ambientLightColor").value = lights.ambient;

		uniforms.get("directionalLightColor").value = lights.directional.colors;
		uniforms.get("directionalLightDirection").value = lights.directional.positions;

		uniforms.get("pointLightColor").value = lights.point.colors;
		uniforms.get("pointLightPosition").value = lights.point.positions;
		uniforms.get("pointLightDistance").value = lights.point.distances;

		uniforms.get("spotLightColor").value = lights.spot.colors;
		uniforms.get("spotLightPosition").value = lights.spot.positions;
		uniforms.get("spotLightDistance").value = lights.spot.distances;
		uniforms.get("spotLightDirection").value = lights.spot.directions;
		uniforms.get("spotLightAngle").value = lights.spot.angles;
		uniforms.get("spotLightExponent").value = lights.spot.exponents;
	}

	private void refreshUniformsShadow ( Map<String, Uniform> uniforms, WebGLRenderLights lights ) 
	{
		if ( uniforms.containsKey("shadowMatrix") ) 
		{
Log.error("?????????????");
//			var j = 0;
//
//			for ( int i = 0, il = lights.size(); i < il; i ++ ) {
//
//				var light = lights[ i ];
//
//				if ( ! light.castShadow ) continue;
//
//				if ( light instanceof THREE.SpotLight || ( light instanceof THREE.DirectionalLight && ! light.shadowCascade ) ) {
//
//					uniforms.shadowMap.texture[ j ] = light.shadowMap;
//					uniforms.shadowMapSize.value[ j ] = light.shadowMapSize;
//
//					uniforms.shadowMatrix.value[ j ] = light.shadowMatrix;
//
//					uniforms.shadowDarkness.value[ j ] = light.shadowDarkness;
//					uniforms.shadowBias.value[ j ] = light.shadowBias;
//
//					j ++;
//				}
//			}
		}
	}

	// Uniforms (load to GPU)

	private void loadUniformsMatrices ( Map<String, WebGLUniformLocation> uniforms, GeometryObject object ) 
	{
		GeometryObject objectImpl = (GeometryObject) object;
		getGL().uniformMatrix4fv( uniforms.get("modelViewMatrix"), false, objectImpl._modelViewMatrix.getArray() );

		if ( uniforms.containsKey("normalMatrix") )
			getGL().uniformMatrix3fv( uniforms.get("normalMatrix"), false, objectImpl._normalMatrix.getArray() );
	}

	private void loadUniformsGeneric( Program program, Map<String, Uniform> materialUniforms ) 
	{
		for ( String u : materialUniforms.keySet() ) 
		{
			WebGLUniformLocation location = program.uniforms.get( u );
			if ( location == null ) continue;

			Uniform uniform = (Uniform) materialUniforms.get(u);

			Uniform.TYPE type = uniform.type;
			Object value = uniform.value;
			
			Log.debug("loadUniformsGeneric() u=" + u + ", " + uniform);
			
			switch ( type ) {

				case I: // single integer
					getGL().uniform1i( location, (Integer) value );
					break;

				case F: // single float
					getGL().uniform1f( location, (Float) value );
					break;

				case V2: // single THREE.Vector2
					Vector2f vector2f = (Vector2f) value;
					getGL().uniform2f( location, vector2f.getX(), vector2f.getX() );
					break;

				case V3: // single THREE.Vector3
					Vector3f vector3f = (Vector3f) value;
					getGL().uniform3f( location, vector3f.getX(), vector3f.getY(), vector3f.getZ() );
					break;

				case V4: // single THREE.Vector4
					Vector4f vector4f = (Vector4f) value;
					getGL().uniform4f( location, vector4f.getX(), vector4f.getY(), vector4f.getZ(), vector4f.getW() );
					break;

				case C: // single THREE.Color
					Color3f color = (Color3f) value;
					getGL().uniform3f( location, color.getR(), color.getG(), color.getB() );
					break;

				case FV1: // flat array of floats (JS or typed array)
					Float32Array float1 = (Float32Array) value;
					getGL().uniform1fv( location, float1 );
					break;

				case FV: // flat array of floats with 3 x N size (JS or typed array)
					Float32Array float2 = (Float32Array) value;
					getGL().uniform3fv( location, float2 );
					break;

				case V2V: // array of THREE.Vector2
					List<Vector2f> listVector2f = (List<Vector2f>) value;
					if ( uniform._array == null )
						uniform._array = Float32Array.create( 2 * listVector2f.size() );

					for ( int i = 0, il = listVector2f.size(); i < il; i ++ ) 
					{
						int offset = i * 2;

						uniform._array.set(offset, listVector2f.get(i).getX());
						uniform._array.set(offset + 1, listVector2f.get(i).getY());
					}

					getGL().uniform2fv( location, uniform._array );
					break;

				case V3V: // array of THREE.Vector3
					List<Vector3f> listVector3f = (List<Vector3f>) value;
					if ( uniform._array == null )
						uniform._array = Float32Array.create( 3 * listVector3f.size() );

					for ( int i = 0, il = listVector3f.size(); i < il; i ++ ) 
					{
						int offset = i * 3;

						uniform._array.set(offset, listVector3f.get( i ).getX());
						uniform._array.set(offset + 1, listVector3f.get( i ).getY());
						uniform._array.set(offset + 2 , listVector3f.get( i ).getZ());
					}

					getGL().uniform3fv( location, uniform._array );
					break;

				case V4V: // array of THREE.Vector4
					List<Vector4f> listVector4f = (List<Vector4f>) value;
					if ( uniform._array == null)
						uniform._array = Float32Array.create( 4 * listVector4f.size() );


					for ( int i = 0, il = listVector4f.size(); i < il; i ++ ) 
					{
						int offset = i * 4;

						uniform._array.set(offset, listVector4f.get( i ).getX());
						uniform._array.set(offset + 1, listVector4f.get( i ).getY());
						uniform._array.set(offset + 2, listVector4f.get( i ).getZ());
						uniform._array.set(offset + 3, listVector4f.get( i ).getW());
					}

					getGL().uniform4fv( location, uniform._array );
					break;

				case M4: // single THREE.Matrix4
					Matrix4f matrix4f = (Matrix4f) value;
					if ( uniform._array == null )
						uniform._array = Float32Array.create( 16 );

					matrix4f.flattenToArray( uniform._array );
					getGL().uniformMatrix4fv( location, false, uniform._array );
					break;

				case M4V: // array of THREE.Matrix4
					List<Matrix4f> listMatrix4f = (List<Matrix4f>) value;
					if ( uniform._array == null )
						uniform._array = Float32Array.create( 16 * listMatrix4f.size() );

					for ( int i = 0, il = listMatrix4f.size(); i < il; i ++ )
						listMatrix4f.get( i ).flattenToArray( uniform._array, i * 16 );

					getGL().uniformMatrix4fv( location, false, uniform._array );
					break;

				case T: // single THREE.Texture (2d or cube)
					getGL().uniform1i( location, (Integer)value );

					Texture texture = uniform.texture;

					if ( texture == null ) continue;

					if ( texture.getClass() == CubeTexture.class )
						setCubeTexture( (CubeTexture) texture, (Integer)value );

					else if ( texture.getClass() == RenderTargetCubeTexture.class )
						setCubeTextureDynamic( (RenderTargetCubeTexture)texture, (Integer)value );

					else
						setTexture( texture, (Integer)value );

					break;

				case TV: // array of THREE.Texture (2d)
					Log.error("WebGL Render: Fix this");
//					if ( uniform._array == null ) 
//					{
//						uniform._array = (Float32Array) Float32Array.createArray();
//
//						for( int i = 0, il = uniform.texture.length; i < il; i ++ )
//							uniform._array.set( i, (Integer)value + i);
//					}
//
//					getGL().uniform1iv( name, uniform._array );
//
//					for( i = 0, il = uniform.texture.length; i < il; i ++ ) {
//
//						texture = uniform.texture[ i ];
//
//						if ( !texture ) continue;
//
//						_this.setTexture( texture, uniform._array[ i ] );
//
//					}
//
					break;
			}
		}
	}

	private void setupMatrices ( Object3D object, Camera camera ) 
	{
		object._modelViewMatrix.multiply( camera.getMatrixWorldInverse(), object.getMatrixWorld());

		object._normalMatrix.getInverse(object._modelViewMatrix );
		object._normalMatrix.transpose();
	}

	private void setupLights ( Program program, List<Light> lights ) 
	{
		Log.debug("Called setupLights()");

		WebGLRenderLights zlights = this.lights; 

		Float32Array dcolors = zlights.directional.colors;
		Float32Array dpositions = zlights.directional.positions;

		Float32Array pcolors = zlights.point.colors;
		Float32Array ppositions = zlights.point.positions;
		Float32Array pdistances = zlights.point.distances;

		Float32Array scolors = zlights.spot.colors;
		Float32Array spositions = zlights.spot.positions;
		Float32Array sdistances = zlights.spot.distances;
		Float32Array sdirections = zlights.spot.directions;
		Float32Array sangles = zlights.spot.angles;
		Float32Array sexponents = zlights.spot.exponents;

		int dlength = 0;
		int plength = 0;
		int slength = 0;

		int doffset = 0;
		int poffset = 0;
		int soffset = 0;
		float r = 0, g = 0, b = 0;

		for ( int l = 0, ll = lights.size(); l < ll; l ++ ) 
		{
			Light light = lights.get( l );

			if ( light.onlyShadow ) continue;

			Color3f color = light.getColor();

			if ( light.getClass() == AmbientLight.class ) 
			{
				if ( this.gammaInput ) 
				{
					r += color.getR() * color.getR();
					g += color.getG() * color.getG();
					b += color.getB() * color.getB();
				} 
				else 
				{
					r += color.getR();
					g += color.getG();
					b += color.getB();
				}

			} 
			else if ( light.getClass() == DirectionalLight.class ) 
			{

				DirectionalLight directionalLight = (DirectionalLight) light;
				float intensity = directionalLight.intensity;

				doffset = dlength * 3;

				if ( this.gammaInput ) 
				{
					dcolors.set( doffset, color.getR() * color.getR() * intensity * intensity);
					dcolors.set( doffset + 1, color.getG() * color.getG() * intensity * intensity);
					dcolors.set( doffset + 2, color.getB() * color.getB() * intensity * intensity);
				} 
				else 
				{
					dcolors.set( doffset, color.getR() * intensity);
					dcolors.set( doffset + 1, color.getG() * intensity);
					dcolors.set( doffset + 2, color.getB() * intensity);
				}

				this.direction.copy( directionalLight.getMatrixWorld().getPosition() );
				this.direction.sub( directionalLight.target.getMatrixWorld().getPosition() );
				this.direction.normalize();

				dpositions.set( doffset, this.direction.getX());
				dpositions.set( doffset + 1, this.direction.getY());
				dpositions.set( doffset + 2, this.direction.getZ());

				dlength += 1;

			} 
			else if( light.getClass() == PointLight.class ) 
			{

				PointLight pointLight = (PointLight) light;
				float intensity = pointLight.getIntensity();
				float distance = pointLight.getDistance();
				poffset = plength * 3;

				if ( this.gammaInput ) 
				{
					pcolors.set(  poffset, color.getR() * color.getR() * intensity * intensity);
					pcolors.set(  poffset + 1, color.getG() * color.getG() * intensity * intensity);
					pcolors.set(  poffset + 2, color.getB() * color.getB() * intensity * intensity);
				} 
				else 
				{
					pcolors.set(  poffset, color.getR() * intensity);
					pcolors.set(  poffset + 1, color.getG() * intensity);
					pcolors.set(  poffset + 2, color.getB() * intensity);
				}

				Vector3f position = pointLight.getMatrixWorld().getPosition();

				ppositions.set(  poffset, position.getX());
				ppositions.set(  poffset + 1, position.getY());
				ppositions.set(  poffset + 2, position.getZ());

				pdistances.set( plength, distance);

				plength += 1;

			} 
			else if( light.getClass() == SpotLight.class ) 
			{
				SpotLight spotLight = (SpotLight) light;
				float intensity = spotLight.intensity;
				float distance = spotLight.distance;

				soffset = slength * 3;

				if ( this.gammaInput ) 
				{
					scolors.set(soffset, color.getR() * color.getR() * intensity * intensity);
					scolors.set(soffset + 1, color.getG() * color.getG() * intensity * intensity);
					scolors.set(soffset + 2, color.getB() * color.getB() * intensity * intensity);
				} 
				else 
				{
					scolors.set(soffset, color.getR() * intensity);
					scolors.set(soffset + 1, color.getG() * intensity);
					scolors.set(soffset + 2, color.getB() * intensity);
				}

				Vector3f position = spotLight.getMatrixWorld().getPosition();

				spositions.set(soffset, position.getX());
				spositions.set(soffset + 1, position.getY());
				spositions.set(soffset + 2, position.getZ());

				sdistances.set(slength, distance);

				this.direction.copy( position );
				this.direction.sub( spotLight.target.getMatrixWorld().getPosition() );
				this.direction.normalize();

				sdirections.set(soffset, this.direction.getX());
				sdirections.set(soffset + 1, this.direction.getY());
				sdirections.set(soffset + 2, this.direction.getZ());

				sangles.set(slength, (float)Math.cos( spotLight.angle ));
				sexponents.set( slength, spotLight.exponent);

				slength += 1;
			}
		}

		// null eventual remains from removed lights
		// (this is to avoid if in shader)

		for ( int l = dlength * 3, ll = dcolors.getLength(); l < ll; l ++ ) dcolors.set( l, 0.0f);
		for ( int l = plength * 3, ll = pcolors.getLength(); l < ll; l ++ ) pcolors.set( l, 0.0f);
		for ( int l = slength * 3, ll = scolors.getLength(); l < ll; l ++ ) scolors.set( l, 0.0f);

		zlights.directional.length = dlength;
		zlights.point.length = plength;
		zlights.spot.length = slength;

		zlights.ambient.set( 0, r);
		zlights.ambient.set( 1, g);
		zlights.ambient.set( 2, b);

	}

	// GL state setting
	
	private void setFaceCulling(String frontFace ) 
	{
		getGL().disable( GLenum.CULL_FACE.getValue() );
	}

	private void setFaceCulling(String cullFace, String frontFace) 
	{
		if ( frontFace == null || frontFace.equals("ccw") )
			getGL().frontFace( GLenum.CCW.getValue() );
		else
			getGL().frontFace( GLenum.CW.getValue() );

		if( cullFace.equals("back") )
			getGL().cullFace( GLenum.BACK.getValue() );
			
		else if( cullFace.equals("front") )
			getGL().cullFace( GLenum.FRONT.getValue() );
			
		else
			getGL().cullFace( GLenum.FRONT_AND_BACK.getValue() );

		getGL().enable( GLenum.CULL_FACE.getValue() );
	}

	private void setObjectFaces( SidesObject object ) 
	{
		if ( this.cache_oldDoubleSided == null || this.cache_oldDoubleSided != object.getDoubleSided() ) 
		{
			if ( object.getDoubleSided() )
				getGL().disable( GLenum.CULL_FACE.getValue() );
			else
				getGL().enable( GLenum.CULL_FACE.getValue() );

			this.cache_oldDoubleSided = object.getDoubleSided();
		}

		if ( this.cache_oldFlipSided == null || this.cache_oldFlipSided != object.getFlipSided() ) 
		{
			if ( object.getFlipSided() )
				getGL().frontFace( GLenum.CW.getValue() );
			else
				getGL().frontFace( GLenum.CCW.getValue() );

			this.cache_oldFlipSided = object.getFlipSided();
		}
	}

	private void setDepthTest( boolean depthTest ) 
	{
		if ( this.cache_oldDepthTest == null || this.cache_oldDepthTest != depthTest ) 
		{
			if ( depthTest )
				getGL().enable( GLenum.DEPTH_TEST.getValue() );
			else 
				getGL().disable( GLenum.DEPTH_TEST.getValue() );

			this.cache_oldDepthTest = depthTest;
		}
	}

	private void setDepthWrite(boolean depthWrite ) 
	{
		if ( this.cache_oldDepthWrite == null || this.cache_oldDepthWrite != depthWrite ) 
		{
			getGL().depthMask( depthWrite );
			cache_oldDepthWrite = depthWrite;
		}
	}

	private void setPolygonOffset( boolean polygonoffset, float factor, float units ) 
	{
		if ( this.cache_oldPolygonOffset == null || this.cache_oldPolygonOffset != polygonoffset ) 
		{
			if ( polygonoffset )
				getGL().enable( GLenum.POLYGON_OFFSET_FILL.getValue() );
			else
				getGL().disable( GLenum.POLYGON_OFFSET_FILL.getValue() );

			this.cache_oldPolygonOffset = polygonoffset;
		}

		if ( polygonoffset && ( cache_oldPolygonOffsetFactor == null || 
				cache_oldPolygonOffsetUnits == null || 
				cache_oldPolygonOffsetFactor != factor || 
				cache_oldPolygonOffsetUnits != units ) 
		) {
			getGL().polygonOffset( factor, units );

			this.cache_oldPolygonOffsetFactor = factor;
			this.cache_oldPolygonOffsetUnits = units;
		}
	}

	private void setBlending( Material.BLENDING blending) 
	{
		if ( blending != this.cache_oldBlending ) 
		{
			if( blending == Material.BLENDING.NO) 
			{
				getGL().disable( GLenum.BLEND.getValue() );
				
			} 
			else if( blending == Material.BLENDING.ADDITIVE) 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
				getGL().blendFunc( GLenum.SRC_ALPHA.getValue(), GLenum.ONE.getValue() );
				
			// TODO: Find blendFuncSeparate() combination
			} 
			else if( blending == Material.BLENDING.SUBTRACTIVE) 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
				getGL().blendFunc( GLenum.ZERO.getValue(), GLenum.ONE_MINUS_SRC_COLOR.getValue() );

			// TODO: Find blendFuncSeparate() combination
			} 
			else if( blending == Material.BLENDING.MULTIPLY) 
			{
				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquation( GLenum.FUNC_ADD.getValue() );
				getGL().blendFunc( GLenum.ZERO.getValue(), GLenum.SRC_COLOR.getValue() );

			} 
			else if( blending == Material.BLENDING.CUSTOM) 
			{
				getGL().enable( GLenum.BLEND.getValue() );

			} 
			else 
			{

				getGL().enable( GLenum.BLEND.getValue() );
				getGL().blendEquationSeparate( GLenum.FUNC_ADD.getValue(), GLenum.FUNC_ADD.getValue() );
				getGL().blendFuncSeparate( GLenum.SRC_ALPHA.getValue(), GLenum.ONE_MINUS_SRC_ALPHA.getValue(), GLenum.ONE.getValue(), GLenum.ONE_MINUS_SRC_ALPHA.getValue() );
			}

			this.cache_oldBlending = blending;
		}
		
		this.cache_oldBlendEquation = null;
		this.cache_oldBlendSrc = null;
		this.cache_oldBlendDst = null;
	}

	private void setBlending( Material.BLENDING blending, BlendEquationMode blendEquation, BlendingFactorSrc blendSrc, BlendingFactorDest blendDst ) 
	{
		setBlending(blending);

		if ( blending == Material.BLENDING.CUSTOM ) 
		{
			if ( blendEquation != this.cache_oldBlendEquation ) 
			{
				getGL().blendEquation( blendEquation.getValue() );
				this.cache_oldBlendEquation = blendEquation;
			}

			if ( blendSrc != cache_oldBlendSrc || blendDst != cache_oldBlendDst ) 
			{
				getGL().blendFunc( blendSrc.getValue(), blendDst.getValue());

				this.cache_oldBlendSrc = blendSrc;
				this.cache_oldBlendDst = blendDst;
			}
		}
	}

	// Shaders

	public Program buildProgram (WebGLRenderer.PRECISION _precision,
			int _maxVertexTextures, String fragmentShader, String vertexShader,
			Map<String, Uniform> uniforms, Map<String, WebGLCustomAttribute> attributes, ProgramParameters parameters ) 
	{
		String cashKey = fragmentShader + vertexShader + parameters.toString();
		if(this._programs.containsKey(cashKey))
			return this._programs.get(cashKey);

		Program program = new Program(getGL(), this.precision, this.maxVertexTextures, fragmentShader, vertexShader, uniforms, attributes, parameters);

		program.setId(_programs.size());
		this._programs.put(cashKey, program);

		this.getInfo().getMemory().programs = _programs.size();

		return program;
	}

	// Textures
	
	private void setCubeTextureDynamic(RenderTargetCubeTexture texture, int slot) 
	{
		getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
		getGL().bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), texture.__webglTexture );
	}

	private void setTexture( Texture texture, int slot ) 
	{
		if ( texture.isNeedsUpdate()) 
		{
			if ( ! texture.__webglInit ) 
			{
				texture.__webglInit = true;
				texture.__webglTexture = getGL().createTexture();

				this.getInfo().getMemory().textures ++;
			}

			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_2D.getValue(), texture.__webglTexture );

			getGL().pixelStorei( GLenum.UNPACK_PREMULTIPLY_ALPHA_WEBGL.getValue(), texture.isPremultiplyAlpha() ? -1 : 1 );

			Element image = texture.getImage();
			boolean isImagePowerOfTwo = Mathematics.isPowerOfTwo( image.getOffsetWidth() ) 
					&& Mathematics.isPowerOfTwo( image.getOffsetHeight() );

			texture.setTextureParameters( getGL(), GLenum.TEXTURE_2D.getValue(), isImagePowerOfTwo );

			if ( texture instanceof DataTexture ) 
			{
				Log.error("WebGLRenderer. Todo: fix DataTexture");
//				getGL().texImage2D( GLenum.TEXTURE_2D, 0, texture.getFormat().getValue(), 
//						image.getWidth(), image.getHeight(), 0, texture.getFormat().getValue(), texture.getType().getValue(), image.getdata );
			} 
			else 
			{
				getGL().texImage2D( GLenum.TEXTURE_2D.getValue(), 0, texture.getFormat().getValue(), 
						texture.getFormat().getValue(), texture.getType().getValue(), image);
			}

			if ( texture.isGenerateMipmaps() && isImagePowerOfTwo ) 
				getGL().generateMipmap( GLenum.TEXTURE_2D.getValue() );

			texture.setNeedsUpdate(false);

//			if ( texture.onUpdate ) 
//				texture.onUpdate();
		} 
		else 
		{
			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_2D.getValue(), texture.__webglTexture );
		}
	}

	/**
	 * Warning: Scaling through the canvas will only work with images that use
	 * premultiplied alpha.
	 * 
	 * @param image    the image element
	 * @param maxSize  the max size of width or height
	 * 
	 * @return the image element (Canvas or Image)
	 */
	private Element clampToMaxSize ( Element image, int maxSize ) 
	{
		int imgWidth = image.getOffsetWidth();
		int imgHeight = image.getOffsetHeight();
		if ( imgWidth <= maxSize && imgHeight <= maxSize )
			return image;

		int maxDimension = Math.max( imgWidth, imgHeight );
		int newWidth = (int) Math.floor( imgWidth * maxSize / maxDimension );
		int newHeight = (int) Math.floor( imgHeight * maxSize / maxDimension );

		CanvasElement canvas = Document.get().createElement("canvas").cast();
		canvas.setWidth(newWidth);
		canvas.setHeight(newHeight);
		
		Context2d context = canvas.getContext2d();
		context.drawImage((CanvasElement) image, 0, 0, imgWidth, imgHeight, 0, 0, newWidth, newHeight );

		return canvas;
	}

	private void setCubeTexture ( CubeTexture texture, int slot ) 
	{
		if ( !texture.isValid() )
			return;

		if ( texture.isNeedsUpdate() ) 
		{
			if ( texture.__webglTexture == null )
			{
				texture.__webglTexture = getGL().createTexture();
				this.getInfo().getMemory().textures += 6;
			}

			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), texture.__webglTexture );

			List<Element> cubeImage = new ArrayList<Element>();

			for ( int i = 0; i < 6; i ++ ) 
			{
				if ( this.autoScaleCubemaps )
					cubeImage.add(clampToMaxSize( texture.getImage( i ), this.maxCubemapSize ));

				else
					cubeImage.add(texture.getImage( i ));

			}

			Element image = cubeImage.get( 0 );
			boolean isImagePowerOfTwo = Mathematics.isPowerOfTwo( image.getOffsetWidth() ) 
					&& Mathematics.isPowerOfTwo( image.getOffsetHeight() );

			texture.setTextureParameters( getGL(), GLenum.TEXTURE_CUBE_MAP.getValue(), isImagePowerOfTwo );

			for ( int i = 0; i < 6; i ++ )
				getGL().texImage2D( GLenum.TEXTURE_CUBE_MAP_POSITIVE_X.getValue() + i, 0, 
						texture.getFormat().getValue(), texture.getFormat().getValue(), texture.getType().getValue(), cubeImage.get( i ) );

			if ( texture.isGenerateMipmaps() && isImagePowerOfTwo )	
				getGL().generateMipmap( GLenum.TEXTURE_CUBE_MAP.getValue() );

			texture.setNeedsUpdate(false);

//			if ( texture.onUpdate ) 
//				texture.onUpdate();
		} 
		else 
		{
			getGL().activeTexture( GLenum.TEXTURE0.getValue() + slot );
			getGL().bindTexture( GLenum.TEXTURE_CUBE_MAP.getValue(), texture.__webglTexture );
		}

	}

	/**
	 * Setup render target
	 * 
	 * @param renderTarget the render target
	 */
	private void setRenderTarget( RenderTargetTexture renderTarget ) 
	{
		Log.debug("Called setRenderTarget(params)");
		WebGLFramebuffer framebuffer = null;
		
		if(renderTarget != null) 
		{
			renderTarget.setRenderTarget(getGL());
		    framebuffer = renderTarget.getWebGLFramebuffer();

			this._currentWidth = renderTarget.getWidth();
			this._currentHeight = renderTarget.getHeight();

		} 
		else 
		{
			this._currentWidth = this.viewportWidth;
			this._currentHeight = this.viewportHeight;
		}

		if ( framebuffer != this.cache_currentFramebuffer ) 
		{
			getGL().bindFramebuffer( GLenum.FRAMEBUFFER.getValue(), framebuffer );
			getGL().viewport( 0, 0, this._currentWidth, this._currentHeight );

			this.cache_currentFramebuffer = framebuffer;
		}
	}

	/**
	 * Default for when object is not specified
	 * ( for example when prebuilding shader to be used with multiple objects )
	 * 
	 * - leave some extra space for other uniforms
	 * - limit here is ANGLE's 254 max uniform vectors (up to 54 should be safe)
	 * 
	 * @param object
	 * @return
	 */
	private int allocateBones (GeometryObject object ) 
	{
		int maxBones = 50;

		if ( object !=null && object instanceof SkinnedMesh )
			maxBones = ((SkinnedMesh)object).bones.size();

		return maxBones;
	}

	private Map<String, Integer> allocateLights ( List<Light> lights ) 
	{
		int dirLights = 0;
		int pointLights = 0;
		int spotLights = 0;
		
		int maxDirLights;
		int maxPointLights;
		int maxSpotLights;
		
		for(Light light: lights) 
		{
			if ( light.onlyShadow ) continue;

			if ( light instanceof DirectionalLight ) dirLights ++;
			if ( light instanceof PointLight ) pointLights ++;
			if ( light instanceof SpotLight ) spotLights ++;
		}

		if ( ( pointLights + spotLights + dirLights ) <= this.maxLights ) 
		{
			maxDirLights = dirLights;
			maxPointLights = pointLights;
			maxSpotLights = spotLights;

		} 
		else 
		{
			maxDirLights = (int) Math.ceil( this.maxLights * dirLights / ( pointLights + dirLights ) );
			maxPointLights = this.maxLights - maxDirLights;
			maxSpotLights = maxPointLights; // this is not really correct
		}

		Map<String, Integer> retval = new HashMap<String, Integer>();
		retval.put("directional", maxDirLights);
		retval.put("point", maxPointLights);
		retval.put("spot", maxSpotLights);

		return retval;
	}

	private int allocateShadows ( List<Light> lights ) 
	{
		int maxShadows = 0;

		for (Light light: lights)
			if ( light.isAllocateShadows() )
				maxShadows ++;

		return maxShadows;
	}

	// default plugins (order is important)

//	this.shadowMapPlugin = new THREE.ShadowMapPlugin();
//	this.addPrePlugin( this.shadowMapPlugin );
//
//	this.addPostPlugin( new THREE.SpritePlugin() );
//	this.addPostPlugin( new THREE.LensFlarePlugin() );
}
