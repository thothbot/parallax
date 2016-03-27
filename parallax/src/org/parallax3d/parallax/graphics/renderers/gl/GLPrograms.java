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
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.objects.SkinnedMesh;
import org.parallax3d.parallax.graphics.renderers.GLRenderTarget;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.textures.Texture;

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

    private Texture.ENCODINGS getTextureEncodingFromMap(Texture map, boolean gammaOverrideLinear) {

        Texture.ENCODINGS encoding = null;

        if ( map == null ) {

            encoding = Texture.ENCODINGS.LinearEncoding;

        } else if ( map instanceof Texture ) {

            encoding = map.getEncoding();

        } else if ( map instanceof GLRenderTarget) {

            encoding = map.texture.encoding;

        }

        // add backwards compatibility for WebGLRenderer.gammaInput/gammaOutput parameter, should probably be removed at some point.
        if ( encoding == Texture.ENCODINGS.LinearEncoding && gammaOverrideLinear ) {

            encoding = Texture.ENCODINGS.GammaEncoding;

        }

        return encoding;

    }

    public void getParameters(Material material, lights, fog, object) {

        var shaderID = shaderIDs[ material.type ];

        // heuristics to create shader parameters according to lights in the scene
        // (not to blow over maxLights budget)

        var maxBones = allocateBones( object );
        var precision = renderer.getPrecision();

        if ( material.precision != null ) {

            precision = capabilities.getMaxPrecision( material.precision );

            if ( precision != material.precision ) {

                Log.warn( "WebGLProgram.getParameters: " + material.precision + " not supported, using " + precision + " instead." );

            }

        }

        var parameters = {

                shaderID: shaderID,

                precision: precision,
                supportsVertexTextures: capabilities.vertexTextures,
                outputEncoding: getTextureEncodingFromMap( renderer.getCurrentRenderTarget(), renderer.gammaOutput ),
                map: !! material.map,
                mapEncoding: getTextureEncodingFromMap( material.map, renderer.gammaInput ),
                envMap: !! material.envMap,
                envMapMode: material.envMap && material.envMap.mapping,
                envMapEncoding: getTextureEncodingFromMap( material.envMap, renderer.gammaInput ),
                envMapCubeUV: ( !! material.envMap ) && ( ( material.envMap.mapping === THREE.CubeUVReflectionMapping ) || ( material.envMap.mapping === THREE.CubeUVRefractionMapping ) ),
                lightMap: !! material.lightMap,
                aoMap: !! material.aoMap,
                emissiveMap: !! material.emissiveMap,
                emissiveMapEncoding: getTextureEncodingFromMap( material.emissiveMap, renderer.gammaInput ),
                bumpMap: !! material.bumpMap,
                normalMap: !! material.normalMap,
                displacementMap: !! material.displacementMap,
                roughnessMap: !! material.roughnessMap,
                metalnessMap: !! material.metalnessMap,
                specularMap: !! material.specularMap,
                alphaMap: !! material.alphaMap,

                combine: material.combine,

                vertexColors: material.vertexColors,

                fog: fog,
                useFog: material.fog,
                fogExp: fog instanceof THREE.FogExp2,

                flatShading: material.shading === THREE.FlatShading,

                sizeAttenuation: material.sizeAttenuation,
                logarithmicDepthBuffer: capabilities.logarithmicDepthBuffer,

                skinning: material.skinning,
                maxBones: maxBones,
                useVertexTexture: capabilities.floatVertexTextures && object && object.skeleton && object.skeleton.useVertexTexture,

                morphTargets: material.morphTargets,
                morphNormals: material.morphNormals,
                maxMorphTargets: renderer.maxMorphTargets,
                maxMorphNormals: renderer.maxMorphNormals,

                numDirLights: lights.directional.length,
                numPointLights: lights.point.length,
                numSpotLights: lights.spot.length,
                numHemiLights: lights.hemi.length,

                pointLightShadows: lights.shadowsPointLight,

                shadowMapEnabled: renderer.shadowMap.enabled && object.receiveShadow && lights.shadows.length > 0,
                shadowMapType: renderer.shadowMap.type,

                toneMapping: renderer.toneMapping,
                physicallyCorrectLights: renderer.physicallyCorrectLights,

                premultipliedAlpha: material.premultipliedAlpha,

                alphaTest: material.alphaTest,
                doubleSided: material.side === THREE.DoubleSide,
                flipSided: material.side === THREE.BackSide

        };

        return parameters;

    };

    public void getProgramCode( material, parameters ) {

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
