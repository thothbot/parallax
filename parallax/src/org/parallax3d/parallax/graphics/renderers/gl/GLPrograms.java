/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.graphics.renderers.gl;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.materials.*;
import org.parallax3d.parallax.graphics.objects.SkinnedMesh;
import org.parallax3d.parallax.graphics.renderers.GLRenderTarget;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.scenes.AbstractFog;
import org.parallax3d.parallax.graphics.scenes.FogExp2;
import org.parallax3d.parallax.graphics.textures.AbstractTexture;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.system.FastMap;

public class GLPrograms {

    GLRenderer renderer;
    GLCapabilities capabilities;

    static final String[] parameterNames = new String[]{
        "precision", "supportsVertexTextures", "map", "mapEncoding", "envMap", "envMapMode", "envMapEncoding",
        "lightMap", "aoMap", "emissiveMap", "emissiveMapEncoding", "bumpMap", "normalMap", "displacementMap", "specularMap",
        "roughnessMap", "metalnessMap",
        "alphaMap", "combine", "vertexColors", "fog", "useFog", "fogExp",
        "flatShading", "sizeAttenuation", "logarithmicDepthBuffer", "skinning",
        "maxBones", "useVertexTexture", "morphTargets", "morphNormals",
        "maxMorphTargets", "maxMorphNormals", "premultipliedAlpha",
        "numDirLights", "numPointLights", "numSpotLights", "numHemiLights",
        "shadowMapEnabled", "pointLightShadows", "toneMapping", "physicallyCorrectLights",
        "shadowMapType",
        "alphaTest", "doubleSided", "flipSided"
    };
    
    public GLPrograms(GLRenderer renderer, GLCapabilities capabilities) {

        this.renderer = renderer;
        this.capabilities = capabilities;

    }

    private int allocateBones( GeometryObject object ) {

        if ( capabilities.floatVertexTextures
                && object != null
                && object instanceof SkinnedMesh
                && ((SkinnedMesh) object).getSkeleton() != null
                && ((SkinnedMesh) object).getSkeleton().isUseVertexTexture()) {

            return 1024;

        } else {

            // default for when object is not specified
            // ( for example when prebuilding shader to be used with multiple objects )
            //
            //  - leave some extra space for other uniforms
            //  - limit here is ANGLE's 254 max uniform vectors
            //    (up to 54 should be safe)

            int nVertexUniforms = capabilities.maxVertexUniforms;
            int nVertexMatrices = (int) Math.floor( ( nVertexUniforms - 20 ) / 4 );

            int maxBones = nVertexMatrices;

            if ( object != null && object instanceof SkinnedMesh ) {

                maxBones = Math.min( ((SkinnedMesh) object).getSkeleton().getBones().size(), maxBones );

                if ( maxBones < ((SkinnedMesh) object).getSkeleton().getBones().size() ) {

                    Log.warn( " GLRenderer: too many bones - " + ((SkinnedMesh) object).getSkeleton().getBones().size()
                            + ", this GPU supports just " + maxBones + " (try OpenGL instead of ANGLE)" );

                }

            }

            return maxBones;

        }

    }

    private Texture.ENCODINGS getTextureEncodingFromMap(AbstractTexture map, boolean gammaOverrideLinear) {

        Texture.ENCODINGS encoding = null;

        if ( map == null ) {

            encoding = Texture.ENCODINGS.LinearEncoding;

        } else if ( map instanceof Texture ) {

            encoding = ((Texture)map).getEncoding();

        } else if ( map instanceof GLRenderTarget) {

            encoding = ((GLRenderTarget) map).getTexture().getEncoding();

        }

        // add backwards compatibility for WebGLRenderer.gammaInput/gammaOutput parameter, should probably be removed at some point.
        if ( encoding == Texture.ENCODINGS.LinearEncoding && gammaOverrideLinear ) {

            encoding = Texture.ENCODINGS.GammaEncoding;

        }

        return encoding;

    }

    public FastMap<Object> getParameters(final Material material, lights, final AbstractFog fog, final GeometryObject object) {

        final Shader shaderID = material.getAssociatedShader();

        // heuristics to create shader parameters according to lights in the scene
        // (not to blow over maxLights budget)

        final int maxBones = allocateBones( object );
        final Shader.PRECISION precision = renderer.getPrecision();

        if ( material.getPrecision() != null )
        {
            precision = capabilities.getPrecision();

            if ( precision != material.getPrecision()) {

                Log.warn( "WebGLProgram.getParameters: " + material.getPrecision() + " not supported, using " + precision + " instead." );

            }

        }

        FastMap<Object> parameters = new FastMap<Object>(){{

			put("shaderID",  shaderID );

			put("precision",  precision );
			put("supportsVertexTextures",  capabilities.vertexTextures );
			put("outputEncoding",  getTextureEncodingFromMap( renderer.getCurrentRenderTarget(), renderer.isGammaOutput()) );
			put("map",  material instanceof HasMap && ((HasMap)material).getMap() != null );
			put("mapEncoding",  getTextureEncodingFromMap( material.map, renderer.isGammaInput()) );
			put("envMap",  material instanceof HasEnvMap && ((HasEnvMap) material).getEnvMap() != null );
			put("envMapMode", material instanceof HasEnvMap && ((HasEnvMap) material).getEnvMap() != null && ((HasEnvMap) material).getEnvMap().getMapping() != null );
			put("envMapEncoding",  getTextureEncodingFromMap( material.envMap, renderer.isGammaInput()) );
			put("envMapCubeUV",  material instanceof HasEnvMap && ((HasEnvMap) material).getEnvMap() != null && ( ( ((HasEnvMap) material).getEnvMap().getMapping() == Texture.MAPPING_MODE.CubeUVReflectionMapping ) || ( ((HasEnvMap) material).getEnvMap().getMapping() == Texture.MAPPING_MODE.CubeUVRefractionMapping ) ) );
			put("lightMap",  material instanceof HasLightMap && ((HasLightMap) material).getLightMap() != null );
			put("aoMap",  material instanceof HasAoMap && ((HasAoMap) material).getAoMap() != null );
			put("emissiveMap",  material instanceof HasEmissiveMap && ((HasEmissiveMap) material).getEmissiveMap() != null );
			put("emissiveMapEncoding",  getTextureEncodingFromMap( material.emissiveMap, renderer.isGammaInput()) );
			put("bumpMap",  material instanceof HasBumpMap && ((HasBumpMap) material).getBumpMap()  != null );
			put("normalMap",  material instanceof HasNormalMap && ((HasNormalMap) material).getNormalMap() != null);
			put("displacementMap", material instanceof HasDisplacementMap && ((HasDisplacementMap) material).getDisplacementMap() != null );
			put("roughnessMap",  material instanceof  HasRoughnessMap && ((HasRoughnessMap) material).getRoughnessMap() != null );
			put("metalnessMap",  material instanceof HasMetalnessMap && ((HasMetalnessMap) material).getMetalnessMap() != null);
			put("specularMap",  material instanceof HasSpecularMap && ((HasSpecularMap) material).getSpecularMap() != null );
			put("alphaMap",  material instanceof HasAlphaMap && ((HasAlphaMap) material).getAlphaMap() != null );

			put("combine", material instanceof HasEnvMap ? ((HasEnvMap) material).getCombine() : null );

			put("vertexColors", material instanceof HasVertexColors ? ((HasVertexColors)material).getVertexColors() : false );

			put("fog",  fog );
			put("useFog", material instanceof HasFog && ((HasFog) material).isFog());
			put("fogExp",  fog instanceof FogExp2);

			put("flatShading", material instanceof HasShading && ((HasShading) material).getShading() == Material.SHADING.FLAT );

			put("sizeAttenuation",  material instanceof PointsMaterial && ((PointsMaterial)material).isSizeAttenuation() );
			put("logarithmicDepthBuffer",  capabilities.logarithmicDepthBuffer );

			put("skinning", material instanceof HasSkinning && ((HasSkinning) material).isSkinning() );
			put("maxBones",  maxBones );
			put("useVertexTexture",  capabilities.floatVertexTextures && object && object.skeleton && object.skeleton.useVertexTexture );

			put("morphTargets",  material.morphTargets );
			put("morphNormals",  material.morphNormals );
			put("maxMorphTargets",  renderer.maxMorphTargets );
			put("maxMorphNormals",  renderer.maxMorphNormals );

			put("numDirLights",  lights.directional.length );
			put("numPointLights",  lights.point.length );
			put("numSpotLights",  lights.spot.length );
			put("numHemiLights",  lights.hemi.length );

			put("pointLightShadows",  lights.shadowsPointLight );

			put("shadowMapEnabled",  renderer.shadowMap.enabled && object.receiveShadow && lights.shadows.length > 0 );
			put("shadowMapType",  renderer.shadowMap.type );

			put("toneMapping",  renderer.toneMapping );
			put("physicallyCorrectLights",  renderer.physicallyCorrectLights );

			put("premultipliedAlpha", material.isPremultipliedAlpha());

			put("alphaTest", material.getAlphaTest());
			put("doubleSided",  material.getSides() == Material.SIDE.DOUBLE );
			put("flipSided",  material.getSides() == Material.SIDE.BACK );

        }};

        return parameters;

    };

    public void getProgramCode( Material material, FastMap<Object> parameters ) {

        var chunks = [];

        if ( parameters.shaderID ) {

            chunks.push( parameters.shaderID );

        } else {

            chunks.push( material.fragmentShader );
            chunks.push( material.vertexShader );

        }

        if ( material.defines != undefined ) {

            for ( var name in material.defines ) {

                chunks.push( name );
                chunks.push( material.defines[ name ] );

            }

        }

        for ( int i = 0; i < parameterNames.length; i ++ ) {

            var parameterName = parameterNames[ i ];
            chunks.push( parameterName );
            chunks.push( parameters[ parameterName ] );

        }

        return chunks.join();

    };

    public void acquireProgram( material, parameters, code ) {

        var program;

        // Check if code has been already compiled
        for ( var p = 0, pl = programs.length; p < pl; p ++ ) {

            var programInfo = programs[ p ];

            if ( programInfo.code === code ) {

                program = programInfo;
                ++ program.usedTimes;

                break;

            }

        }

        if ( program === undefined ) {

            program = new THREE.WebGLProgram( renderer, code, material, parameters );
            programs.push( program );

        }

        return program;

    };

    public void releaseProgram( program ) {

        if ( -- program.usedTimes == 0 ) {

            // Remove from unordered set
            var i = programs.indexOf( program );
            programs[ i ] = programs[ programs.length - 1 ];
            programs.pop();

            // Free WebGL resources
            program.destroy();

        }

    };

}
