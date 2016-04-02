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
import org.parallax3d.parallax.graphics.materials.HasEnvMap;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.RawShaderMaterial;
import org.parallax3d.parallax.graphics.materials.ShaderMaterial;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.GLHelpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static jdk.nashorn.internal.objects.Global.undefined;
import static org.parallax3d.parallax.graphics.renderers.gl.GLExtensions.extensions;

@ThreejsObject("WebGLProgram")
public class GLProgram {

    static int programIdCount = 0;

    // TODO: Combine the regex
    static Pattern structRe = Pattern.compile("^([\\w\\d_]+)\\.([\\w\\d_]+)$");
    static Pattern arrayStructRe = Pattern.compile("^([\\w\\d_]+)\\[(\\d+)\\]\\.([\\w\\d_]+)$");
    static Pattern arrayRe = Pattern.compile("^([\\w\\d_]+)\\[0\\]$");

    private String[] getEncodingComponents( Texture.ENCODINGS encoding ) {

        switch ( encoding ) {

            case LinearEncoding:
                return new String[]{ "Linear","( value )" };
            case sRGBEncoding:
                return new String[]{ "sRGB","( value )" };
            case RGBEEncoding:
                return new String[]{ "RGBE","( value )" };
            case RGBM7Encoding:
                return new String[]{ "RGBM","( value, 7.0 )" };
            case RGBM16Encoding:
                return new String[]{ "RGBM","( value, 16.0 )" };
            case RGBDEncoding:
                return new String[]{ "RGBD","( value, 256.0 )" };
            case GammaEncoding:
                return new String[]{ "Gamma","( value, float( GAMMA_FACTOR ) )" };
            default:
                throw new Error( "unsupported encoding: " + encoding );

        }

    }

    public GLProgram(GLRenderer renderer, String code, Material material, FastMap<Object> parameters) {

        GL20 gl = renderer.gl();

//        var extensions = material.extensions;
//        FastMap<Boolean> defines = material.defines;

        String vertexShader = material.getShader().getVertexSource();
        String fragmentShader = material.getShader().getFragmentSource();

        String shadowMapTypeDefine = "SHADOWMAP_TYPE_BASIC";

        if ( parameters.get( "shadowMapType" ) == ShadowMap.TYPE.PCFShadowMap ) {

            shadowMapTypeDefine = "SHADOWMAP_TYPE_PCF";

        } else if ( parameters.get( "shadowMapType" ) == ShadowMap.TYPE.PCFSoftShadowMap ) {

            shadowMapTypeDefine = "SHADOWMAP_TYPE_PCF_SOFT";

        }

        String envMapTypeDefine = "ENVMAP_TYPE_CUBE";
        String envMapModeDefine = "ENVMAP_MODE_REFLECTION";
        String envMapBlendingDefine = "ENVMAP_BLENDING_MULTIPLY";

        if ( (Boolean) parameters.get( "envMap" ) ) {

            switch (((HasEnvMap) material).getEnvMap().getMapping()) {

                case CubeReflectionMapping:
                case CubeRefractionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_CUBE";
                    break;

                case CubeUVReflectionMapping:
                case CubeUVRefractionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_CUBE_UV";
                    break;

                case EquirectangularReflectionMapping:
                case EquirectangularRefractionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_EQUIREC";
                    break;

                case SphericalReflectionMapping:
                    envMapTypeDefine = "ENVMAP_TYPE_SPHERE";
                    break;

            }

            switch ( ((HasEnvMap) material).getEnvMap().getMapping() ) {

                case CubeRefractionMapping:
                case EquirectangularRefractionMapping:
                    envMapModeDefine = "ENVMAP_MODE_REFRACTION";
                    break;

            }

            switch ( ((HasEnvMap) material).getCombine() ) {

                case MultiplyOperation:
                    envMapBlendingDefine = "ENVMAP_BLENDING_MULTIPLY";
                    break;

                case MixOperation:
                    envMapBlendingDefine = "ENVMAP_BLENDING_MIX";
                    break;

                case AddOperation:
                    envMapBlendingDefine = "ENVMAP_BLENDING_ADD";
                    break;

            }

        }

        double gammaFactorDefine = ( renderer.getGammaFactor() > 0 ) ? renderer.getGammaFactor() : 1.0;

        //
//
//        var customExtensions = generateExtensions( extensions, parameters, renderer.extensions );
//
//        var customDefines = generateDefines( defines );

        //

        int program = gl.glCreateProgram();

        String prefixVertex, prefixFragment;

        if ( material instanceof RawShaderMaterial) {

            prefixVertex = "";
            prefixFragment = "";

        } else {

            List<String> prefixVertexArray = Arrays.asList(

                "precision " + parameters.get( "precision" ) + " float;",
                "precision " + parameters.get( "precision" ) + " int;",
                
//                "#define SHADER_NAME " + material.__webglShader.name,
//
//                customDefines,
                
                (Boolean)parameters.get( "supportsVertexTextures" ) ? "#define VERTEX_TEXTURES" : "",
                
                "#define GAMMA_FACTOR " + gammaFactorDefine,
                
                "#define MAX_BONES " + parameters.get( "maxBones" ),

                (Boolean)parameters.get( "map" ) ? "#define USE_MAP" : "",
                (Boolean)parameters.get( "envMap" ) ? "#define USE_ENVMAP" : "",
                (Boolean)parameters.get( "envMap" ) ? "#define " + envMapModeDefine : "",
                (Boolean)parameters.get( "lightMap" ) ? "#define USE_LIGHTMAP" : "",
                (Boolean)parameters.get( "aoMap" ) ? "#define USE_AOMAP" : "",
                (Boolean)parameters.get( "emissiveMap" ) ? "#define USE_EMISSIVEMAP" : "",
                (Boolean)parameters.get( "bumpMap" ) ? "#define USE_BUMPMAP" : "",
                (Boolean)parameters.get( "normalMap" ) ? "#define USE_NORMALMAP" : "",
                (Boolean)parameters.get( "displacementMap" ) && (Boolean)parameters.get( "supportsVertexTextures" ) ? "#define USE_DISPLACEMENTMAP" : "",
                (Boolean)parameters.get( "specularMap" ) ? "#define USE_SPECULARMAP" : "",
                (Boolean)parameters.get( "roughnessMap" ) ? "#define USE_ROUGHNESSMAP" : "",
                (Boolean)parameters.get( "metalnessMap" ) ? "#define USE_METALNESSMAP" : "",
                (Boolean)parameters.get( "alphaMap" ) ? "#define USE_ALPHAMAP" : "",
                (Boolean)parameters.get( "vertexColors" ) ? "#define USE_COLOR" : "",

                (Boolean)parameters.get( "flatShading" ) ? "#define FLAT_SHADED" : "",

                (Boolean)parameters.get( "skinning" ) ? "#define USE_SKINNING" : "",
                (Boolean)parameters.get( "useVertexTexture" ) ? "#define BONE_TEXTURE" : "",

                (Boolean)parameters.get( "morphTargets" ) ? "#define USE_MORPHTARGETS" : "",
                (Boolean)parameters.get( "morphNormals" ) && !(Boolean)parameters.get( "flatShading" ) ? "#define USE_MORPHNORMALS" : "",
                (Boolean)parameters.get( "doubleSided" ) ? "#define DOUBLE_SIDED" : "",
                (Boolean)parameters.get( "flipSided" ) ? "#define FLIP_SIDED" : "",

                (Boolean)parameters.get( "shadowMapEnabled" ) ? "#define USE_SHADOWMAP" : "",
                (Boolean)parameters.get( "shadowMapEnabled" ) ? "#define " + shadowMapTypeDefine : "",
                (Integer)parameters.get( "pointLightShadows" ) > 0 ? "#define POINT_LIGHT_SHADOWS" : "",

                (Boolean)parameters.get( "sizeAttenuation" ) ? "#define USE_SIZEATTENUATION" : "",

                (Boolean)parameters.get( "logarithmicDepthBuffer" ) ? "#define USE_LOGDEPTHBUF" : "",
//                (Boolean)parameters.get( "logarithmicDepthBuffer" ) && renderer.extensions.get( "EXT_frag_depth" ) ? "#define USE_LOGDEPTHBUF_EXT" : "",
                
                
                "uniform mat4 modelMatrix;",
                "uniform mat4 modelViewMatrix;",
                "uniform mat4 projectionMatrix;",
                "uniform mat4 viewMatrix;",
                "uniform mat3 normalMatrix;",
                "uniform vec3 cameraPosition;",
                
                "attribute vec3 position;",
                "attribute vec3 normal;",
                "attribute vec2 uv;",
                
                "#ifdef USE_COLOR",
                
                "	attribute vec3 color;",
                
                "#endif",
                
                "#ifdef USE_MORPHTARGETS",
                
                "	attribute vec3 morphTarget0;",
                "	attribute vec3 morphTarget1;",
                "	attribute vec3 morphTarget2;",
                "	attribute vec3 morphTarget3;",
                
                "	#ifdef USE_MORPHNORMALS",
                
                "		attribute vec3 morphNormal0;",
                "		attribute vec3 morphNormal1;",
                "		attribute vec3 morphNormal2;",
                "		attribute vec3 morphNormal3;",
                
                "	#else",
                
                "		attribute vec3 morphTarget4;",
                "		attribute vec3 morphTarget5;",
                "		attribute vec3 morphTarget6;",
                "		attribute vec3 morphTarget7;",
                
                "	#endif",
                
                "#endif",
                
                "#ifdef USE_SKINNING",
                
                "	attribute vec4 skinIndex;",
                "	attribute vec4 skinWeight;",
                
                "#endif",
                
                "\n"

            );

            prefixVertexArray.removeAll(Arrays.asList("", null));
            prefixVertex = String.join("\n", prefixVertexArray);

            List<String> prefixFragmentArray = Arrays.asList(

//                customExtensions,

                "precision " + parameters.get( "precision" ) + " float;",
                "precision " + parameters.get( "precision" ) + " int;",
                
//                "#define SHADER_NAME " + material.__webglShader.name,
                
//                customDefines,

                (Boolean)parameters.get( "alphaTest" ) ? "#define ALPHATEST " + parameters.get( "alphaTest" ) : "",
                
                "#define GAMMA_FACTOR " + gammaFactorDefine,
                
                ( (Boolean)parameters.get( "useFog" ) && (Boolean)parameters.get( "fog" ) ) ? "#define USE_FOG" : "",
                ( (Boolean)parameters.get( "useFog" ) && (Boolean)parameters.get( "fogExp" ) ) ? "#define FOG_EXP2" : "",

                (Boolean)parameters.get( "map" ) ? "#define USE_MAP" : "",
                (Boolean)parameters.get( "envMap" ) ? "#define USE_ENVMAP" : "",
                (Boolean)parameters.get( "envMap" ) ? "#define " + envMapTypeDefine : "",
                (Boolean)parameters.get( "envMap" ) ? "#define " + envMapModeDefine : "",
                (Boolean)parameters.get( "envMap" ) ? "#define " + envMapBlendingDefine : "",
                (Boolean)parameters.get( "lightMap" ) ? "#define USE_LIGHTMAP" : "",
                (Boolean)parameters.get( "aoMap" ) ? "#define USE_AOMAP" : "",
                (Boolean)parameters.get( "emissiveMap" ) ? "#define USE_EMISSIVEMAP" : "",
                (Boolean)parameters.get( "bumpMap" ) ? "#define USE_BUMPMAP" : "",
                (Boolean)parameters.get( "normalMap" ) ? "#define USE_NORMALMAP" : "",
                (Boolean)parameters.get( "specularMap" ) ? "#define USE_SPECULARMAP" : "",
                (Boolean)parameters.get( "roughnessMap" ) ? "#define USE_ROUGHNESSMAP" : "",
                (Boolean)parameters.get( "metalnessMap" ) ? "#define USE_METALNESSMAP" : "",
                (Boolean)parameters.get( "alphaMap" ) ? "#define USE_ALPHAMAP" : "",
                (Boolean)parameters.get( "vertexColors" ) ? "#define USE_COLOR" : "",

                (Boolean)parameters.get( "flatShading" ) ? "#define FLAT_SHADED" : "",

                (Boolean)parameters.get( "doubleSided" ) ? "#define DOUBLE_SIDED" : "",
                (Boolean)parameters.get( "flipSided" ) ? "#define FLIP_SIDED" : "",

                (Boolean)parameters.get( "shadowMapEnabled" ) ? "#define USE_SHADOWMAP" : "",
                (Boolean)parameters.get( "shadowMapEnabled" ) ? "#define " + shadowMapTypeDefine : "",
                (Integer)parameters.get( "pointLightShadows" ) > 0 ? "#define POINT_LIGHT_SHADOWS" : "",

                (Boolean)parameters.get( "premultipliedAlpha" ) ? "#define PREMULTIPLIED_ALPHA" : "",

                (Boolean)parameters.get( "physicallyCorrectLights" ) ? "#define PHYSICALLY_CORRECT_LIGHTS" : "",

                (Boolean)parameters.get( "logarithmicDepthBuffer" ) ? "#define USE_LOGDEPTHBUF" : "",
//                parameters.get( "logarithmicDepthBuffer" ) && renderer.extensions.get( "EXT_frag_depth" ) ? "#define USE_LOGDEPTHBUF_EXT" : "",
//
//                parameters.get( "envMap" ) && renderer.extensions.get( "EXT_shader_texture_lod" ) ? "#define TEXTURE_LOD_EXT" : "",
                
                "uniform mat4 viewMatrix;",
                "uniform vec3 cameraPosition;",
                
                ( parameters.get( "toneMapping" ) != Texture.TONE_MAPPING_MODE.NoToneMapping ) ? "#define TONE_MAPPING" : "",
//                ( parameters.get( "toneMapping" ) != Texture.TONE_MAPPING_MODE.NoToneMapping ) ? ShaderChunk[ "tonemapping_pars_fragment" ] : "",  // this code is required here because it is used by the toneMapping() function defined below
                ( parameters.get( "toneMapping" ) != Texture.TONE_MAPPING_MODE.NoToneMapping ) ? getToneMappingFunction( "toneMapping", (Texture.TONE_MAPPING_MODE) parameters.get( "toneMapping" ) ) : "",
                
//                ( parameters.get( "outputEncoding" ) != null || parameters.get( "mapEncoding" ) != null || parameters.get( "envMapEncoding" )  != null || parameters.get( "emissiveMapEncoding" ) != null )  ? ShaderChunk[ "encodings_pars_fragment" ] : "", // this code is required here because it is used by the various encoding/decoding function defined below
                parameters.get( "mapEncoding" ) != null ? getTexelDecodingFunction( "mapTexelToLinear", (Texture.ENCODINGS) parameters.get( "mapEncoding" ) ) : "",
                parameters.get( "envMapEncoding" ) != null ? getTexelDecodingFunction( "envMapTexelToLinear", (Texture.ENCODINGS)parameters.get( "envMapEncoding" ) ) : "",
                parameters.get( "emissiveMapEncoding" ) != null ? getTexelDecodingFunction( "emissiveMapTexelToLinear", (Texture.ENCODINGS)parameters.get( "emissiveMapEncoding" ) ) : "",
                parameters.get( "outputEncoding" ) != null ? getTexelEncodingFunction( "linearToOutputTexel", (Texture.ENCODINGS)parameters.get( "outputEncoding" ) ) : "",
                
                "\n"

            );

            prefixFragmentArray.removeAll(Arrays.asList("", null));
            prefixFragment = String.join("\n", prefixFragmentArray);

        }

        vertexShader = parseIncludes( vertexShader );
        vertexShader = replaceLightNums( vertexShader, parameters );

        fragmentShader = parseIncludes( fragmentShader );
        fragmentShader = replaceLightNums( fragmentShader, parameters );

        if ( !(material instanceof ShaderMaterial)) {

            vertexShader = unrollLoops( vertexShader );
            fragmentShader = unrollLoops( fragmentShader );

        }

        String vertexGlsl = prefixVertex + vertexShader;
        String fragmentGlsl = prefixFragment + fragmentShader;

        // console.log( "*VERTEX*", vertexGlsl );
        // console.log( "*FRAGMENT*", fragmentGlsl );

        int glVertexShader = GLShader.getShader( gl, GL20.GL_VERTEX_SHADER, vertexGlsl );
        int glFragmentShader = GLShader.getShader( gl, GL20.GL_FRAGMENT_SHADER, fragmentGlsl );

        gl.glAttachShader( program, glVertexShader );
        gl.glAttachShader( program, glFragmentShader );

        // Force a particular attribute to index 0.

//        if ( material.index0AttributeName != undefined ) {
//
//            gl.glBindAttribLocation( program, 0, material.index0AttributeName );
//
//        } else if ( parameters.get( "morphTargets" ) == true ) {
//
//            // programs with morphTargets displace position out of attribute 0
//            gl.glBindAttribLocation( program, 0, "position" );
//
//        }

        gl.glLinkProgram( program );

        String programLog = gl.glGetProgramInfoLog( program );
        String vertexLog = gl.glGetShaderInfoLog( glVertexShader );
        String fragmentLog = gl.glGetShaderInfoLog( glFragmentShader );

        boolean runnable = true;
        boolean haveDiagnostics = true;

        if (!GLHelpers.isProgramLinked( gl, program ) ) {

            runnable = false;

            Log.error( "GLProgram: shader error: ", gl.glGetError(), "gl.VALIDATE_STATUS", GLHelpers.getProgramValidStatus( gl, program), "gl.getProgramInfoLog", programLog, vertexLog, fragmentLog );

        } else if (!Objects.equals(programLog, "")) {

            Log.warn( "GLProgram: gl.getProgramInfoLog(): " + programLog );

        } else if (Objects.equals(vertexLog, "") || Objects.equals(fragmentLog, "")) {

            haveDiagnostics = false;

        }

        if ( haveDiagnostics ) {

//            this.diagnostics = {
//
//                    runnable: runnable,
//                    material: material,
//
//                    programLog: programLog,
//
//                    vertexShader: {
//
//                log: vertexLog,
//                        prefix: prefixVertex
//
//            },
//
//            fragmentShader: {
//
//                log: fragmentLog,
//                        prefix: prefixFragment
//
//            }
//
//            };

        }

        // clean up

        gl.glDeleteShader( glVertexShader );
        gl.glDeleteShader( glFragmentShader );

        // set up caching for uniform locations

//        var cachedUniforms;
//
//        this.getUniforms = function() {
//
//            if ( cachedUniforms == undefined ) {
//
//                cachedUniforms = fetchUniformLocations( gl, program );
//
//            }
//
//            return cachedUniforms;
//
//        };
//
//        // set up caching for attribute locations
//
//        var cachedAttributes;
//
//        this.getAttributes = function() {
//
//            if ( cachedAttributes == undefined ) {
//
//                cachedAttributes = fetchAttributeLocations( gl, program );
//
//            }
//
//            return cachedAttributes;
//
//        };
//
//        // free resource
//
//        this.destroy = function() {
//
//            gl.deleteProgram( program );
//            this.program = undefined;
//
//        };
//
//        //
//
//        this.id = programIdCount ++;
//        this.code = code;
//        this.usedTimes = 1;
//        this.program = program;
//        this.vertexShader = glVertexShader;
//        this.fragmentShader = glFragmentShader;

    }

    private String getTexelDecodingFunction( String functionName, Texture.ENCODINGS encoding ) {

        String[] components = getEncodingComponents( encoding );
        return "vec4 " + functionName + "( vec4 value ) { return " + components[ 0 ] + "ToLinear" + components[ 1 ] + "; }";

    }

    private String getTexelEncodingFunction( String functionName, Texture.ENCODINGS encoding ) {

        String[] components = getEncodingComponents( encoding );
        return "vec4 " + functionName + "( vec4 value ) { return LinearTo" + components[ 0 ] + components[ 1 ] + "; }";

    }

    private String getToneMappingFunction( String functionName, Texture.TONE_MAPPING_MODE toneMapping ) {

        String toneMappingName;

        switch ( toneMapping ) {

            case LinearToneMapping:
                toneMappingName = "Linear";
                break;

            case ReinhardToneMapping:
                toneMappingName = "Reinhard";
                break;

            case Uncharted2ToneMapping:
                toneMappingName = "Uncharted2";
                break;

            case CineonToneMapping:
                toneMappingName = "OptimizedCineon";
                break;

            default:
                throw new Error( "unsupported toneMapping: " + toneMapping );

        }

        return "vec3 " + functionName + "( vec3 color ) { return " + toneMappingName + "ToneMapping( color ); }";

    }

//    private generateExtensions( extensions, parameters, rendererExtensions ) {
//
//        extensions = extensions || {};
//
//        var chunks = [
//            ( extensions.derivatives || parameters.get( "envMapCubeUV" ) || parameters.get( "bumpMap" ) || parameters.get( "normalMap" ) || parameters.get( "flatShading" ) ) ? "#extension GL_OES_standard_derivatives : enable" : "",
//            ( extensions.fragDepth || parameters.get( "logarithmicDepthBuffer" ) ) && rendererExtensions.get( "EXT_frag_depth" ) ? "#extension GL_EXT_frag_depth : enable" : "",
//            ( extensions.drawBuffers ) && rendererExtensions.get( "WEBGL_draw_buffers" ) ? "#extension GL_EXT_draw_buffers : require" : "",
//            ( extensions.shaderTextureLOD || parameters.get( "envMap" ) ) && rendererExtensions.get( "EXT_shader_texture_lod" ) ? "#extension GL_EXT_shader_texture_lod : enable" : "",
//        ];
//
//        return chunks.filter( filterEmptyLine ).join( "\n" );
//
//    }

    private String generateDefines( FastMap<Boolean> defines ) {

        List<String> chunks = new ArrayList<>();

        for ( String name : defines.keySet() ) {

            Boolean value = defines.get( name );

            if ( value == false ) continue;

            chunks.add( "#define " + name + " " + value );

        }

        return String.join("\n", chunks);

    }

    public FastMap<Integer> fetchUniformLocations(GL20 gl, int program) {

        FastMap<Integer> uniforms = new FastMap<>();

        int n = GLHelpers.getProgramParameter( gl, program, GL20.GL_ACTIVE_UNIFORMS );

        for ( int i = 0; i < n; i ++ ) {

//            String info = gl.glGetActiveUniform( program, i );
//            String name = info.name;
//            int location = gl.glGetUniformLocation( program, name );

            //console.log("WebGLProgram: ACTIVE UNIFORM:", name);

//            var matches = structRe.exec( name );
//            if ( matches ) {
//
//                var structName = matches[ 1 ];
//                var structProperty = matches[ 2 ];
//
//                var uniformsStruct = uniforms[ structName ];
//
//                if ( ! uniformsStruct ) {
//
//                    uniformsStruct = uniforms[ structName ] = {};
//
//                }
//
//                uniformsStruct[ structProperty ] = location;
//
//                continue;
//
//            }
//
//            matches = arrayStructRe.exec( name );
//
//            if ( matches ) {
//
//                var arrayName = matches[ 1 ];
//                var arrayIndex = matches[ 2 ];
//                var arrayProperty = matches[ 3 ];
//
//                var uniformsArray = uniforms[ arrayName ];
//
//                if ( ! uniformsArray ) {
//
//                    uniformsArray = uniforms[ arrayName ] = [];
//
//                }
//
//                var uniformsArrayIndex = uniformsArray[ arrayIndex ];
//
//                if ( ! uniformsArrayIndex ) {
//
//                    uniformsArrayIndex = uniformsArray[ arrayIndex ] = {};
//
//                }
//
//                uniformsArrayIndex[ arrayProperty ] = location;
//
//                continue;
//
//            }
//
//            matches = arrayRe.exec( name );
//
//            if ( matches ) {
//
//                var arrayName = matches[ 1 ];
//
//                uniforms[ arrayName ] = location;
//
//                continue;
//
//            }
//
//            uniforms[ name ] = location;

        }

        return uniforms;

    }

    public FastMap<Integer> fetchAttributeLocations( GL20 gl, int program ) {

        FastMap<Integer> attributes = new FastMap<>();

        int n = GLHelpers.getProgramParameter( gl, program, GL20.GL_ACTIVE_ATTRIBUTES );

        for ( int i = 0; i < n; i ++ ) {

//            String info = gl.glGetActiveAttrib( program, i );
//            String name = info.name;
//
//            // console.log("WebGLProgram: ACTIVE VERTEX ATTRIBUTE:", name, i );
//
//            attributes.put(  name , gl.glGetAttribLocation( program, name ) );

        }

        return attributes;

    }

    public String replaceLightNums( String string, FastMap<Object> parameters ) {

        return string
            .replaceAll( "NUM_DIR_LIGHTS",   String.valueOf( parameters.get( "numDirLights" ) ) )
            .replaceAll( "NUM_SPOT_LIGHTS",  String.valueOf( parameters.get( "numSpotLights" ) ) )
            .replaceAll( "NUM_POINT_LIGHTS", String.valueOf( parameters.get( "numPointLights" ) ) )
            .replaceAll( "NUM_HEMI_LIGHTS",  String.valueOf( parameters.get( "numHemiLights" ) ) );

    }

    private String parseIncludes( String string ) {

//        var pattern = /#include +<([\w\d.]+)>/g;
//
//        function replace( match, include ) {
//
//            var replace = ShaderChunk[ include ];
//
//            if ( replace == undefined ) {
//
//                throw new Error( "Can not resolve #include <" + include + ">" );
//
//            }
//
//            return parseIncludes( replace );
//
//        }
//
//        return string.replace( pattern, replace );

        return null;
    }

    private String unrollLoops( String string ) {

//        var pattern = /for \( int i \= (\d+)\; i < (\d+)\; i \+\+ \) \{([\s\S]+?)(?=\})\}/g;
//
//        function replace( match, start, end, snippet ) {
//
//            var unroll = "";
//
//            for ( var i = parseInt( start ); i < parseInt( end ); i ++ ) {
//
//                unroll += snippet.replace( /\[ i \]/g, "[ " + i + " ]" );
//
//            }
//
//            return unroll;
//
//        }
//
//        return string.replace( pattern, replace );

        return null;
    }

}
