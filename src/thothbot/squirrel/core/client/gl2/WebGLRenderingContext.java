/**
 * Copyright 2009-2011 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package thothbot.squirrel.core.client.gl2;

import thothbot.squirrel.core.client.gl2.arrays.ArrayBuffer;
import thothbot.squirrel.core.client.gl2.arrays.ArrayBufferView;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.client.gl2.arrays.Int32Array;
import thothbot.squirrel.core.client.gl2.arrays.JsArrayUtil;
import thothbot.squirrel.core.client.gl2.arrays.TypeArray;

import com.google.gwt.canvas.dom.client.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;

/**
 * 
 */
public final class WebGLRenderingContext extends JavaScriptObject implements Context {

  /** */
  public static final int ACTIVE_ATTRIBUTES = 0x8B89;

  /** */
  public static final int ACTIVE_TEXTURE = 0x84E0;
  /** */
  public static final int ACTIVE_UNIFORMS = 0x8B86;
  /** */
  public static final int ALIASED_LINE_WIDTH_RANGE = 0x846E;

  /** */
  public static final int ALIASED_POINT_SIZE_RANGE = 0x846D;
  /** */
  public static final int ALPHA = 0x1906;
  /** */
  public static final int ALPHA_BITS = 0x0D55;
  /** */
  public static final int ALWAYS = 0x0207;
  /* Buffer Objects */
  /** */
  public static final int ARRAY_BUFFER = 0x8892;
  /** */
  public static final int ARRAY_BUFFER_BINDING = 0x8894;
  /** */
  public static final int ATTACHED_SHADERS = 0x8B85;

  /* AlphaFunction (not supported in ES20) */
  /* NEVER */
  /* LESS */
  /* EQUAL */
  /* LEQUAL */
  /* GREATER */
  /* NOTEQUAL */
  /* GEQUAL */
  /* ALWAYS */

  /** */
  public static final int BACK = 0x0405;
  /** */
  public static final int BLEND = 0x0BE2;
  /** */
  public static final int BLEND_COLOR = 0x8005;
  /** */
  public static final int BLEND_DST_ALPHA = 0x80CA;
  /* Separate Blend Functions */
  /** */
  public static final int BLEND_DST_RGB = 0x80C8;
  /** */
  public static final int BLEND_EQUATION = 0x8009;
  /** */
  public static final int BLEND_EQUATION_ALPHA = 0x883D;
  /** */
  public static final int BLEND_EQUATION_RGB = 0x8009; /*
                                                        * same as BLEND_EQUATION
                                                        */

  /** */
  public static final int BLEND_SRC_ALPHA = 0x80CB;
  /** */
  public static final int BLEND_SRC_RGB = 0x80C9;
  /** */
  public static final int BLUE_BITS = 0x0D54;

  /** */
  public static final int BOOL = 0x8B56;
  /** */
  public static final int BOOL_VEC2 = 0x8B57;
  /** */
  public static final int BOOL_VEC3 = 0x8B58;
  /** */
  public static final int BOOL_VEC4 = 0x8B59;

  /** */
  public static final int BROWSER_DEFAULT_WEBGL = 0x9244;
  /** */
  public static final int BUFFER_SIZE = 0x8764;

  /** */
  public static final int BUFFER_USAGE = 0x8765;
  /* DataType */
  /** */
  public static final int BYTE = 0x1400;
  /** */
  public static final int CCW = 0x0901;
  /** */
  public static final int CLAMP_TO_EDGE = 0x812F;
  /** */
  public static final int COLOR_ATTACHMENT0 = 0x8CE0;
  /** */
  public static final int COLOR_BUFFER_BIT = 0x00004000;
  /* SCISSOR_TEST */
  /** */
  public static final int COLOR_CLEAR_VALUE = 0x0C22;
  /** */
  public static final int COLOR_WRITEMASK = 0x0C23;
  /* Shader Source */
  /** */
  public static final int COMPILE_STATUS = 0x8B81;

  /** */
  public static final int COMPRESSED_TEXTURE_FORMATS = 0x86A3;
  /** */
  public static final int CONSTANT_ALPHA = 0x8003;
  /** */
  public static final int CONSTANT_COLOR = 0x8001;
  /** */
  public static final int CONTEXT_LOST_WEBGL = 0x9242;

  /* EnableCap */
  /* TEXTURE_2D */
  /** */
  public static final int CULL_FACE = 0x0B44;
  /** */
  public static final int CULL_FACE_MODE = 0x0B45;
  /** */
  public static final int CURRENT_PROGRAM = 0x8B8D;

  /** */
  public static final int CURRENT_VERTEX_ATTRIB = 0x8626;
  /* FrontFaceDirection */
  /** */
  public static final int CW = 0x0900;

  /** */
  public static final int DECR = 0x1E03;

  /** */
  public static final int DECR_WRAP = 0x8508;
  /** */
  public static final int DELETE_STATUS = 0x8B80;
  /** */
  public static final int DEPTH_ATTACHMENT = 0x8D00;

  /* DepthFunction */
  /* NEVER */
  /* LESS */
  /* EQUAL */
  /* LEQUAL */
  /* GREATER */
  /* NOTEQUAL */
  /* GEQUAL */
  /* ALWAYS */

  /** */
  public static final int DEPTH_BITS = 0x0D56;
  /* ClearBufferMask */
  /** */
  public static final int DEPTH_BUFFER_BIT = 0x00000100;
  /** */
  public static final int DEPTH_CLEAR_VALUE = 0x0B73;
  /* PixelFormat */
  /** */
  public static final int DEPTH_COMPONENT = 0x1902;
  /** */
  public static final int DEPTH_COMPONENT16 = 0x81A5;
  /** */
  public static final int DEPTH_FUNC = 0x0B74;
  /** */
  public static final int DEPTH_RANGE = 0x0B70;
  /** */
  public static final int DEPTH_STENCIL = 0x84F9;
  /** */
  public static final int DEPTH_STENCIL_ATTACHMENT = 0x821A;

  /** */
  public static final int DEPTH_TEST = 0x0B71;
  /** */
  public static final int DEPTH_WRITEMASK = 0x0B72;
  /** */
  public static final int DITHER = 0x0BD0;
  /* HintMode */
  /** */
  public static final int DONT_CARE = 0x1100;
  /** */
  public static final int DST_ALPHA = 0x0304;

  /* BlendingFactorSrc */
  /* ZERO */
  /* ONE */
  /** */
  public static final int DST_COLOR = 0x0306;
  /** */
  public static final int DYNAMIC_DRAW = 0x88E8;

  /** */
  public static final int ELEMENT_ARRAY_BUFFER = 0x8893;
  /** */
  public static final int ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
  /** */
  public static final int EQUAL = 0x0202;
  /** */
  public static final int FASTEST = 0x1101;
  /** */
  public static final int FLOAT = 0x1406;
  /** */
  public static final int FLOAT_MAT2 = 0x8B5A;
  /** */
  public static final int FLOAT_MAT3 = 0x8B5B;
  /** */
  public static final int FLOAT_MAT4 = 0x8B5C;
  /* Uniform Types */
  /** */
  public static final int FLOAT_VEC2 = 0x8B50;
  /** */
  public static final int FLOAT_VEC3 = 0x8B51;
  /** */
  public static final int FLOAT_VEC4 = 0x8B52;
  /* Shaders */
  /** */
  public static final int FRAGMENT_SHADER = 0x8B30;
  /* Framebuffer Object. */
  /** */
  public static final int FRAMEBUFFER = 0x8D40;
  /** */
  public static final int FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1;
  /** */
  public static final int FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0;
  /** */
  public static final int FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3;
  /** */
  public static final int FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2;
  /** */
  public static final int FRAMEBUFFER_BINDING = 0x8CA6;
  /** */
  public static final int FRAMEBUFFER_COMPLETE = 0x8CD5;
  /** */
  public static final int FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
  /** */
  public static final int FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9;
  /** */
  public static final int FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
  /** */
  public static final int FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
  /* CullFaceMode */
  /** */
  public static final int FRONT = 0x0404;
  /** */
  public static final int FRONT_AND_BACK = 0x0408;
  /** */
  public static final int FRONT_FACE = 0x0B46;
  /* BlendEquationSeparate */
  /** */
  public static final int FUNC_ADD = 0x8006;
  /** */
  public static final int FUNC_REVERSE_SUBTRACT = 0x800B;
  /* BlendSubtract */
  /** */
  public static final int FUNC_SUBTRACT = 0x800A;
  /* HintTarget */
  /** */
  public static final int GENERATE_MIPMAP_HINT = 0x8192;
  /** */
  public static final int GEQUAL = 0x0206;
  /** */
  public static final int GREATER = 0x0204;
  /** */
  public static final int GREEN_BITS = 0x0D53;
  /** */
  public static final int HIGH_FLOAT = 0x8DF2;
  /** */
  public static final int HIGH_INT = 0x8DF5;
  /** */
  public static final int INCR = 0x1E02;
  /** */
  public static final int INCR_WRAP = 0x8507;
  /** */
  public static final int INT = 0x1404;
  /** */
  public static final int INT_VEC2 = 0x8B53;
  /** */
  public static final int INT_VEC3 = 0x8B54;
  /** */
  public static final int INT_VEC4 = 0x8B55;
  /** */
  public static final int INVALID_ENUM = 0x0500;
  /** */
  public static final int INVALID_FRAMEBUFFER_OPERATION = 0x0506;
  /** */
  public static final int INVALID_OPERATION = 0x0502;
  /** */
  public static final int INVALID_VALUE = 0x0501;
  /** */
  public static final int INVERT = 0x150A;

  /* GetTextureParameter */
  /* TEXTURE_MAG_FILTER */
  /* TEXTURE_MIN_FILTER */
  /* TEXTURE_WRAP_S */
  /* TEXTURE_WRAP_T */

  /* StencilOp */
  /* ZERO */
  /** */
  public static final int KEEP = 0x1E00;
  /** */
  public static final int LEQUAL = 0x0203;

  /** */
  public static final int LESS = 0x0201;
  /** */
  public static final int LINE_LOOP = 0x0002;
  /** */
  public static final int LINE_STRIP = 0x0003;

  /* GetPName */
  /** */
  public static final int LINE_WIDTH = 0x0B21;

  /** */
  public static final int LINEAR = 0x2601;
  /** */
  public static final int LINEAR_MIPMAP_LINEAR = 0x2703;
  /** */
  public static final int LINEAR_MIPMAP_NEAREST = 0x2701;
  /** */
  public static final int LINES = 0x0001;
  /** */
  public static final int LINK_STATUS = 0x8B82;
  /* Shader Precision-Specified Types */
  /** */
  public static final int LOW_FLOAT = 0x8DF0;
  /** */
  public static final int LOW_INT = 0x8DF3;

  /** */
  public static final int LUMINANCE = 0x1909;
  /** */
  public static final int LUMINANCE_ALPHA = 0x190A;
  /** */
  public static final int MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
  /** */
  public static final int MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;
  /** */
  public static final int MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD;
  /** */
  public static final int MAX_RENDERBUFFER_SIZE = 0x84E8;

  /** */
  public static final int MAX_TEXTURE_IMAGE_UNITS = 0x8872;
  /** */
  public static final int MAX_TEXTURE_SIZE = 0x0D33;
  /** */
  public static final int MAX_VARYING_VECTORS = 0x8DFC;

  /** */
  public static final int MAX_VERTEX_ATTRIBS = 0x8869;
  /** */
  public static final int MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
  /** */
  public static final int MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB;
  /** */
  public static final int MAX_VIEWPORT_DIMS = 0x0D3A;
  /** */
  public static final int MEDIUM_FLOAT = 0x8DF1;
  /** */
  public static final int MEDIUM_INT = 0x8DF4;
  /** */
  public static final int MIRRORED_REPEAT = 0x8370;
  /* TextureMagFilter */
  /** */
  public static final int NEAREST = 0x2600;
  /** */
  public static final int NEAREST_MIPMAP_LINEAR = 0x2702;
  /* TextureMinFilter */
  /* NEAREST */
  /* LINEAR */
  /** */
  public static final int NEAREST_MIPMAP_NEAREST = 0x2700;
  /* StencilFunction */
  /** */
  public static final int NEVER = 0x0200;
  /** */
  public static final int NICEST = 0x1102;
  /* ErrorCode */
  /** */
  public static final int NO_ERROR = 0;
  /** */
  public static final int NONE = 0;
  /** */
  public static final int NOTEQUAL = 0x0205;
  /** */
  public static final int NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2;
  /** */
  public static final int ONE = 1;
  /** */
  public static final int ONE_MINUS_CONSTANT_ALPHA = 0x8004;

  /** */
  public static final int ONE_MINUS_CONSTANT_COLOR = 0x8002;
  /** */
  public static final int ONE_MINUS_DST_ALPHA = 0x0305;
  /** */
  public static final int ONE_MINUS_DST_COLOR = 0x0307;
  /** */
  public static final int ONE_MINUS_SRC_ALPHA = 0x0303;
  /** */
  public static final int ONE_MINUS_SRC_COLOR = 0x0301;
  /** */
  public static final int OUT_OF_MEMORY = 0x0505;
  /** */
  public static final int PACK_ALIGNMENT = 0x0D05;
  /* BeginMode */
  /** */
  public static final int POINTS = 0x0000;

  /* POLYGON_OFFSET_FILL */
  /** */
  public static final int POLYGON_OFFSET_FACTOR = 0x8038;
  /** */
  public static final int POLYGON_OFFSET_FILL = 0x8037;
  /** */
  public static final int POLYGON_OFFSET_UNITS = 0x2A00;
  /** */
  public static final int RED_BITS = 0x0D52;
  /** */
  public static final int RENDERBUFFER = 0x8D41;
  /** */
  public static final int RENDERBUFFER_ALPHA_SIZE = 0x8D53;
  /** */
  public static final int RENDERBUFFER_BINDING = 0x8CA7;

  /** */
  public static final int RENDERBUFFER_BLUE_SIZE = 0x8D52;
  /** */
  public static final int RENDERBUFFER_DEPTH_SIZE = 0x8D54;
  /** */
  public static final int RENDERBUFFER_GREEN_SIZE = 0x8D51;

  /** */
  public static final int RENDERBUFFER_HEIGHT = 0x8D43;
  /** */
  public static final int RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;

  /** */
  public static final int RENDERBUFFER_RED_SIZE = 0x8D50;
  /** */
  public static final int RENDERBUFFER_STENCIL_SIZE = 0x8D55;
  /** */
  public static final int RENDERBUFFER_WIDTH = 0x8D42;
  /** */
  public static final int RENDERER = 0x1F01;

  /* TextureWrapMode */
  /** */
  public static final int REPEAT = 0x2901;
  /** */
  public static final int REPLACE = 0x1E01;
  /** */
  public static final int RGB = 0x1907;
  /** */
  public static final int RGB5_A1 = 0x8057;

  /** */
  public static final int RGB565 = 0x8D62;
  /** */
  public static final int RGBA = 0x1908;

  /** */
  public static final int RGBA4 = 0x8056;
  /** */
  public static final int SAMPLE_ALPHA_TO_COVERAGE = 0x809E;
  /** */
  public static final int SAMPLE_BUFFERS = 0x80A8;
  /** */
  public static final int SAMPLE_COVERAGE = 0x80A0;
  /** */
  public static final int SAMPLE_COVERAGE_INVERT = 0x80AB;
  /** */
  public static final int SAMPLE_COVERAGE_VALUE = 0x80AA;
  /** */
  public static final int SAMPLER_2D = 0x8B5E;
  /** */
  public static final int SAMPLER_CUBE = 0x8B60;
  /** */
  public static final int SAMPLES = 0x80A9;

  /** */
  public static final int SCISSOR_BOX = 0x0C10;
  /** */
  public static final int SCISSOR_TEST = 0x0C11;
  /** */
  public static final int SHADER_TYPE = 0x8B4F;
  /** */
  public static final int SHADING_LANGUAGE_VERSION = 0x8B8C;
  /** */
  public static final int SHORT = 0x1402;
  /** */
  public static final int SRC_ALPHA = 0x0302;
  /** */
  public static final int SRC_ALPHA_SATURATE = 0x0308;
  /* SRC_ALPHA */
  /* ONE_MINUS_SRC_ALPHA */
  /* DST_ALPHA */
  /* ONE_MINUS_DST_ALPHA */
  /** */
  public static final int SRC_COLOR = 0x0300;
  /** */
  public static final int STATIC_DRAW = 0x88E4;
  /** */
  public static final int STENCIL_ATTACHMENT = 0x8D20;
  /** */
  public static final int STENCIL_BACK_FAIL = 0x8801;
  /** */
  public static final int STENCIL_BACK_FUNC = 0x8800;
  /** */
  public static final int STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
  /** */
  public static final int STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
  /** */
  public static final int STENCIL_BACK_REF = 0x8CA3;
  /** */
  public static final int STENCIL_BACK_VALUE_MASK = 0x8CA4;
  /** */
  public static final int STENCIL_BACK_WRITEMASK = 0x8CA5;
  /** */
  public static final int STENCIL_BITS = 0x0D57;
  /** */
  public static final int STENCIL_BUFFER_BIT = 0x00000400;
  /** */
  public static final int STENCIL_CLEAR_VALUE = 0x0B91;
  /** */
  public static final int STENCIL_FAIL = 0x0B94;
  /** */
  public static final int STENCIL_FUNC = 0x0B92;
  /** */
  public static final int STENCIL_INDEX = 0x1901;
  /** */
  public static final int STENCIL_INDEX8 = 0x8D48;
  /** */
  public static final int STENCIL_PASS_DEPTH_FAIL = 0x0B95;
  /** */
  public static final int STENCIL_PASS_DEPTH_PASS = 0x0B96;
  /** */
  public static final int STENCIL_REF = 0x0B97;
  /** */
  public static final int STENCIL_TEST = 0x0B90;
  /** */
  public static final int STENCIL_VALUE_MASK = 0x0B93;
  /** */
  public static final int STENCIL_WRITEMASK = 0x0B98;
  /** */
  public static final int STREAM_DRAW = 0x88E0;
  /** */
  public static final int SUBPIXEL_BITS = 0x0D50;
  /** */
  public static final int TEXTURE = 0x1702;

  /* TextureTarget */
  /** */
  public static final int TEXTURE_2D = 0x0DE1;
  /** */
  public static final int TEXTURE_BINDING_2D = 0x8069;
  /** */
  public static final int TEXTURE_BINDING_CUBE_MAP = 0x8514;

  /** */
  public static final int TEXTURE_CUBE_MAP = 0x8513;
  /** */
  public static final int TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
  /** */
  public static final int TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
  /** */
  public static final int TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A;
  /** */
  public static final int TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
  /** */
  public static final int TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
  /** */
  public static final int TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
  /* TextureParameterName */
  /** */
  public static final int TEXTURE_MAG_FILTER = 0x2800;
  /** */
  public static final int TEXTURE_MIN_FILTER = 0x2801;
  /** */
  public static final int TEXTURE_WRAP_S = 0x2802;
  /** */
  public static final int TEXTURE_WRAP_T = 0x2803;
  /* TextureUnit */
  /** */
  public static final int TEXTURE0 = 0x84C0;
  /** */
  public static final int TEXTURE1 = 0x84C1;
  /** */
  public static final int TEXTURE10 = 0x84CA;
  /** */
  public static final int TEXTURE11 = 0x84CB;

  /** */
  public static final int TEXTURE12 = 0x84CC;
  /** */
  public static final int TEXTURE13 = 0x84CD;
  /** */
  public static final int TEXTURE14 = 0x84CE;
  /** */
  public static final int TEXTURE15 = 0x84CF;
  /** */
  public static final int TEXTURE16 = 0x84D0;
  /** */
  public static final int TEXTURE17 = 0x84D1;
  /** */
  public static final int TEXTURE18 = 0x84D2;

  /** */
  public static final int TEXTURE19 = 0x84D3;

  /** */
  public static final int TEXTURE2 = 0x84C2;
  /** */
  public static final int TEXTURE20 = 0x84D4;
  /** */
  public static final int TEXTURE21 = 0x84D5;
  /** */
  public static final int TEXTURE22 = 0x84D6;
  /** */
  public static final int TEXTURE23 = 0x84D7;
  /** */
  public static final int TEXTURE24 = 0x84D8;

  /** */
  public static final int TEXTURE25 = 0x84D9;
  /** */
  public static final int TEXTURE26 = 0x84DA;

  /** */
  public static final int TEXTURE27 = 0x84DB;
  /** */
  public static final int TEXTURE28 = 0x84DC;
  /** */
  public static final int TEXTURE29 = 0x84DD;
  /** */
  public static final int TEXTURE3 = 0x84C3;
  /** */
  public static final int TEXTURE30 = 0x84DE;
  /** */
  public static final int TEXTURE31 = 0x84DF;
  /** */
  public static final int TEXTURE4 = 0x84C4;

  /** */
  public static final int TEXTURE5 = 0x84C5;
  /** */
  public static final int TEXTURE6 = 0x84C6;
  /** */
  public static final int TEXTURE7 = 0x84C7;
  /** */
  public static final int TEXTURE8 = 0x84C8;
  /** */
  public static final int TEXTURE9 = 0x84C9;
  /** */
  public static final int TRIANGLE_FAN = 0x0006;
  /** */
  public static final int TRIANGLE_STRIP = 0x0005;
  /** */
  public static final int TRIANGLES = 0x0004;
  /** */
  public static final int UNPACK_ALIGNMENT = 0x0CF5;

  /** */
  public static final int UNPACK_COLORSPACE_CONVERSION_WEBGL = 0x9243;
  /* WebGL-specific enums */
  /** */
  public static final int UNPACK_FLIP_Y_WEBGL = 0x9240;
  /** */
  public static final int UNPACK_PREMULTIPLY_ALPHA_WEBGL = 0x9241;
  /** */
  public static final int UNSIGNED_BYTE = 0x1401;

  /** */
  public static final int UNSIGNED_INT = 0x1405;
  /** */
  public static final int UNSIGNED_SHORT = 0x1403;
  /* PixelType */
  /* UNSIGNED_BYTE */
  /** */
  public static final int UNSIGNED_SHORT_4_4_4_4 = 0x8033;
  /** */
  public static final int UNSIGNED_SHORT_5_5_5_1 = 0x8034;

  /** */
  public static final int UNSIGNED_SHORT_5_6_5 = 0x8363;

  /** */
  public static final int VALIDATE_STATUS = 0x8B83;
  /* StringName */
  /** */
  public static final int VENDOR = 0x1F00;
  /** */
  public static final int VERSION = 0x1F02;
  /** */
  public static final int VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;
  /* Vertex Arrays */
  /** */
  public static final int VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;

  /** */
  public static final int VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A;
  /** */
  public static final int VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;
  /** */
  public static final int VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;

  /** */
  public static final int VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;

  /** */
  public static final int VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
  /** */
  public static final int VERTEX_SHADER = 0x8B31;
  /** */
  public static final int VIEWPORT = 0x0BA2;
  /* BlendingFactorDest */
  /** */
  public static final int ZERO = 0;
  /**
   * Protected standard constructor as specified by
   * {@link com.google.gwt.core.client.JavaScriptObject}.
   */
  protected WebGLRenderingContext() {
  }

  public native void activeTexture(int texture) /*-{
		this.activeTexture(texture);
  }-*/;

  public native void attachShader(WebGLProgram program, WebGLShader shader) /*-{
		this.attachShader(program, shader);
  }-*/;

  public native void bindAttribLocation(WebGLProgram program, int index, String name) /*-{
		this.bindAttribLocation(program, index, name);
  }-*/;

  public native void bindBuffer(int target, WebGLBuffer buffer) /*-{
		this.bindBuffer(target, buffer);
  }-*/;

  public native void bindFramebuffer(int target, WebGLFramebuffer buffer) /*-{
		this.bindFramebuffer(target, buffer);
  }-*/;

  public native void bindRenderbuffer(int target, WebGLRenderbuffer buffer) /*-{
		this.bindRenderbuffer(target, buffer);
  }-*/;

  public native void bindTexture(int target, WebGLTexture texture) /*-{
		this.bindTexture(target, texture);
  }-*/;

  public native void blendColor(float red, float green, float blue, float alpha) /*-{
		this.blendColor(red, green, blue, alpha);
  }-*/;

  public native void blendEquation(int mode) /*-{
		this.blendEquation(mode);
  }-*/;

  public native void blendEquationSeparate(int modeRGB, int modeAlpha) /*-{
		this.blendEquationSeparate(modeRGB, modeAlpha);
  }-*/;

  public native void blendFunc(int sfactor, int dfactor) /*-{
		this.blendFunc(sfactor, dfactor);
  }-*/;

  public native void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) /*-{
		this.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
  }-*/;

  public native void bufferData(int target, ArrayBuffer dta, int usage) /*-{
		this.bufferData(target, dta, usage);
  }-*/;

  public native void bufferData(int target, int size, int usage) /*-{
		this.bufferData(target, size, usage);
  }-*/;

  public native void bufferData(int target, TypeArray dta, int usage) /*-{
		this.bufferData(target, dta, usage);
  }-*/;

  public native void bufferSubData(int target, int offset, ArrayBuffer data) /*-{
		this.bufferSubData(target, offset, data);
  }-*/;

  public native void bufferSubData(int target, int offset, TypeArray data) /*-{
		this.bufferSubData(target, offset, data);
  }-*/;

  public native int checkFramebufferStatus(int target) /*-{
		return this.checkFramebufferStatus(target);
  }-*/;

  public native void clear(int mask) /*-{
		this.clear(mask);
  }-*/;

  public native void clearColor(float red, float green, float blue, float alpha) /*-{
		this.clearColor(red, green, blue, alpha);
  }-*/;

  public native void clearDepth(float depth) /*-{
		this.clearDepth(depth);
  }-*/;

  public native void clearStencil(int s) /*-{
		this.clearStencil(s);
  }-*/;

  public native void colorMask(boolean red, boolean green, boolean blue, boolean alpha) /*-{
		this.colorMask(red, green, blue, alpha);
  }-*/;

  public native void compileShader(WebGLShader shader) /*-{
		this.compileShader(shader);
  }-*/;

  public native void copyTexImage2D(int target, int level, int intformat, int x, int y, int width,
      int height, int border) /*-{
		this.copyTexImage2D(target, level, intformat, x, y, width, height,
				border);
  }-*/;

  public native void copyTexSubImage2D(int target, int level, int intformat, int xoffset,
      int yoffset, int x, int y, int width, int height) /*-{
		this.copyTexSubImage2D(target, level, intformat, xoffset, yoffset, x,
				y, width, height);
  }-*/;

  public native WebGLBuffer createBuffer() /*-{
		return this.createBuffer();
  }-*/;

  public native WebGLFramebuffer createFramebuffer() /*-{
		return this.createFramebuffer();
  }-*/;

  public native WebGLProgram createProgram() /*-{
		return this.createProgram();
  }-*/;

  public native WebGLRenderbuffer createRenderbuffer() /*-{
		return this.createRenderbuffer();
  }-*/;

  public native WebGLShader createShader(int shaderType) /*-{
		return this.createShader(shaderType);
  }-*/;

  public native WebGLTexture createTexture() /*-{
		return this.createTexture();
  }-*/;

  public native void cullFace(int mode) /*-{
		this.cullFace(mode);
  }-*/;

  public native void deleteBuffer(WebGLBuffer buffer) /*-{
		this.deleteBuffer(buffer);
  }-*/;

  public native void deleteFramebuffer(WebGLFramebuffer buffer) /*-{
		this.deleteFramebuffer(buffer);
  }-*/;

  public native void deleteProgram(WebGLProgram program) /*-{
		this.deleteProgram(program);
  }-*/;

  public native void deleteRenderbuffer(WebGLRenderbuffer buffer) /*-{
		this.deleteRenderbuffer(buffer);
  }-*/;

  public native void deleteShader(WebGLShader shader) /*-{
		this.deleteShader(shader);
  }-*/;

  public native void deleteTexture(WebGLTexture texture) /*-{
		this.deleteTexture(texture);
  }-*/;

  public native void depthFunc(int func) /*-{
		this.depthFunc(func);
  }-*/;

  public native void depthMask(boolean flag) /*-{
		this.depthMask(flag);
  }-*/;

  public native void depthRange(float nearVal, float farVal) /*-{
		this.depthRange(nearVal, farVal);
  }-*/;

  public native void detachShader(WebGLProgram program, WebGLShader shader) /*-{
		this.detachShader(program, shader);
  }-*/;

  public native void disable(int param) /*-{
		this.disable(param);
  }-*/;

  public native void disableVertexAttribArray(int index) /*-{
		this.disableVertexAttribArray(index);
  }-*/;

  public native void drawArrays(int mode, int first, int count) /*-{
		this.drawArrays(mode, first, count);
  }-*/;

  public native void drawElements(int mode, int count, int type, int offset) /*-{
		this.drawElements(mode, count, type, offset);
  }-*/;

  public native void enable(int param) /*-{
		this.enable(param);
  }-*/;

  public native void enableVertexAttribArray(int index) /*-{
		this.enableVertexAttribArray(index);
  }-*/;

  public native void finish() /*-{
		this.finish();
  }-*/;

  public native void flush() /*-{
		this.flush();
  }-*/;

  public native void framebufferRenderbuffer(int target, int attachment, int rbtarget,
      WebGLRenderbuffer rbuffer) /*-{
		this.framebufferRenderbuffer(target, attachment, rbtarget, rbuffer);
  }-*/;

  public native void framebufferTexture2D(int target, int att, int textarget, WebGLTexture tex,
      int level) /*-{
		this.framebufferTexture2D(target, att, textarget, tex, level);
  }-*/;

  public native void frontFace(int mode) /*-{
		this.frontFace(mode);
  }-*/;

  public native void generateMipmap(int target) /*-{
		this.generateMipmap(target);
  }-*/;

  public native WebGLActiveInfo getActiveAttrib(WebGLProgram program, int index) /*-{
		return this.getActiveAttrib(program, index);
  }-*/;

  public native WebGLActiveInfo getActiveUniform(WebGLProgram program, int idx) /*-{
		return this.getActiveUniform(program, idx);
  }-*/;

  /**
   * Return the list of {@link WebGLShader}s attached to the passed {@link WebGLProgram}.
   * 
   * @param program {@link WebGLProgram} object to be queried.
   * @return array of {@link WebGLShader}s attached to the passed {@link WebGLProgram}
   * @see "http://www.khronos.org/opengles/sdk/docs/man/glGetAttachedShaders.xml"
   */
  public WebGLShader[] getAttachedShaders(WebGLProgram program) {
    // TODO implement this in the generator
    try {
      if (GWT.isProdMode()) {
        return getAttachedShadersProd(program);
      }
      JsArray<WebGLShader> shaders = getAttachedShadersDev(program);

      WebGLShader[] result = new WebGLShader[shaders.length()];
      for (int i = 0; i < shaders.length(); i++) {
        result[i] = shaders.get(i);
      }
      return result;
    } catch (Exception e) {
      return new WebGLShader[0];
    }
  }

  public native int getAttribLocation(WebGLProgram program, String name) /*-{
		return this.getAttribLocation(program, name);
  }-*/;

  public native int getBufferParameteri(int target, int pname) /*-{
		return this.getBufferParameter(target, pname);
  }-*/;

  public native int getError() /*-{
		return this.getError();
  }-*/;

  public native JavaScriptObject getExtension(String name) /*-{
		return this.getExtension(name);
  }-*/;

  public native int getExtensioni(String name) /*-{
		return this.getExtension(name);
  }-*/;

  public native JavaScriptObject getFramebufferAttachmentParameter(int target, int attachment,
      int pname) /*-{
		return this
				.getFramebufferAttachmentParameter(target, attachment, pname);
  }-*/;

  public native int getFramebufferAttachmentParameteri(int target, int attachment, int pname) /*-{
		return this
				.getFramebufferAttachmentParameter(target, attachment, pname);
  }-*/;

  public native <T extends JavaScriptObject> T getParameter(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native boolean getParameterb(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native float getParameterf(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native int getParameteri(int pname) /*-{
		return this.getParameter(pname);
  }-*/;

  public native String getProgramInfoLog(WebGLProgram program) /*-{
		return this.getProgramInfoLog(program);
  }-*/;

  public native boolean getProgramParameterb(WebGLProgram program, int pname) /*-{
		return this.getProgramParameter(program, pname);
  }-*/;

  public native int getProgramParameteri(WebGLProgram program, int pname) /*-{
		return this.getProgramParameter(program, pname);
  }-*/;

  public native int getRenderbufferParameteri(int target, int pname) /*-{
		return this.getRenderbufferParameter(target, pname);
  }-*/;

  public native String getShaderInfoLog(WebGLShader shader) /*-{
		return this.getShaderInfoLog(shader);
  }-*/;

  public native boolean getShaderParameterb(WebGLShader shader, int pname) /*-{
		return this.getShaderParameter(shader, pname);
  }-*/;

  public native int getShaderParameteri(WebGLShader shader, int pname) /*-{
		return this.getShaderParameter(shader, pname);
  }-*/;

  public native String getShaderSource(WebGLShader shader) /*-{
		return this.getShaderSource(shader);
  }-*/;

  /**
   * Determines the extensions supported by the WebGL implementation.
   * 
   * @return an array containing the names of the supported extensions.
   */
   public String[] getSupportedExtensions() {
		JsArrayString supportedExts = getSupportedExtensionsAsJsArray();
		String[] outSupportedExts = new String[supportedExts.length()];
		for (int i = 0; i < outSupportedExts.length; i++) {
			outSupportedExts[i] = supportedExts.get(i);
		}
		return outSupportedExts;
  }

  public native JsArrayString getSupportedExtensionsAsJsArray() /*-{
		return this.getSupportedExtensions();
  }-*/;

  public native int getTexParameteri(int target, int pname) /*-{
		return this.getTexParameter(target, pname);
  }-*/;

  public native <T extends thothbot.squirrel.core.client.gl2.arrays.TypeArray> T getUniforma(
      WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native boolean getUniformb(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native float getUniformf(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native int getUniformi(WebGLProgram program, WebGLUniformLocation location) /*-{
		return this.getUniform(program, location);
  }-*/;

  public native WebGLUniformLocation getUniformLocation(WebGLProgram program, String name) /*-{
		return this.getUniformLocation(program, name);
  }-*/;

  public native <T extends JavaScriptObject> T getVertexAttrib(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native boolean getVertexAttribb(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native int getVertexAttribi(int index, int pname) /*-{
		return this.getVertexAttrib(index, pname);
  }-*/;

  public native int getVertexAttribOffset(int index, String pname) /*-{
		return this.getVertexAttribOffset(index, pname);
  }-*/;

  public native boolean isBuffer(WebGLBuffer buffer) /*-{
		return this.isBuffer(buffer);
  }-*/;

  public native boolean isFramebuffer(JavaScriptObject buffer) /*-{
		return this.isFramebuffer(buffer);
  }-*/;

  public native boolean isProgram(WebGLProgram program) /*-{
		return this.isProgram(program);
  }-*/;

  public native boolean isRenderbuffer(WebGLRenderbuffer buffer) /*-{
		return this.isRenderbuffer(buffer);
  }-*/;

  public native boolean isShader(JavaScriptObject shader) /*-{
		return this.isShader(shader);
  }-*/;

  public native boolean isTexture(WebGLTexture texture) /*-{
		return this.isTexture(texture);
  }-*/;

  public native void lineWidth(float width) /*-{
		this.lineWidth(width);
  }-*/;

  public native void linkProgram(WebGLProgram program) /*-{
		this.linkProgram(program);
  }-*/;

  public native void pixelStorei(int pname, int param) /*-{
		this.pixelStorei(pname, param);
  }-*/;

  public native void polygonOffset(float factor, float units) /*-{
		this.polygonOffset(factor, units);
  }-*/;

  public native void readPixels(int x, int y, int width, int height, int format, int type,
      ArrayBufferView pixels) /*-{
		this.readPixels(x, y, width, height, format, type, pixels);
  }-*/;

  public native void renderbufferStorage(int target, int format, int width, int height) /*-{
		this.renderbufferStorage(target, format, width, height);
  }-*/;

  public native void sampleCoverage(float value, boolean invert) /*-{
		this.sampleCoverage(value, invert);
  }-*/;

  public native void scissor(int x, int y, int width, int height) /*-{
		this.scissor(x, y, width, height);
  }-*/;

  public native void shaderSource(WebGLShader shader, String shaderSrc) /*-{
		this.shaderSource(shader, shaderSrc);
  }-*/;

  public native void stencilFunc(int func, int ref, int mask) /*-{
		this.stencilFunc(func, ref, mask);
  }-*/;

  public native void stencilFuncSeparate(int face, int func, int ref, int mask) /*-{
		this.stencilFuncSeparate(face, func, ref, mask);
  }-*/;

  public native void stencilMask(int mask) /*-{
		this.stencilMask(mask);
  }-*/;

  public native void stencilMaskSeparate(int face, int mask) /*-{
		this.stencilMaskSeparate(face, mask);
  }-*/;

  public native void stencilOp(int sfail, int dpfail, int dppass) /*-{
		this.stencilOp(sfail, dpfail, dppass);
  }-*/;

  public native void stencilOpSeparate(int face, int sfail, int dpfail, int dppass) /*-{
		this.stencilOpSeparate(face, sfail, dpfail, dppass);
  }-*/;

  public native void texImage2D(int target, int level, int internalformat, int width, int height,
      int border, int format, int type, ArrayBufferView pixels) /*-{
		this.texImage2D(target, level, internalformat, width, height, border,
				format, type, pixels);
  }-*/;

  public native void texImage2D(int target, int level, int internalformat, int format, int type,
      JavaScriptObject data) /*-{
		this.texImage2D(target, level, internalformat, format, type, data);
  }-*/;

  public native void texParameterf(int target, int pname, float value) /*-{
		this.texParameterf(target, pname, value);
  }-*/;

  public native void texParameteri(int target, int pname, int value) /*-{
		this.texParameteri(target, pname, value);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset, int width,
      int height, int format, int type, TypeArray data) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, width, height,
				format, type, data);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset,
      JavaScriptObject data) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, data);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset,
      JavaScriptObject data, boolean flipY) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, data, flipY);
  }-*/;

  public native void texSubImage2D(int target, int level, int xoffset, int yoffset,
      JavaScriptObject data, boolean flipY, boolean asPremultipliedAlpha) /*-{
		this.texSubImage2D(target, level, xoffset, yoffset, data, flipY,
				asPremultipliedAlpha);
  }-*/;

  public native void uniform1f(WebGLUniformLocation location, float v0) /*-{
		this.uniform1f(location, v0);
  }-*/;

  public void uniform1fv(WebGLUniformLocation location, float[] values) {
    uniform1fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform1fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform1fv(location, values);
  }-*/;

  public native void uniform1fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform1fv(location, v);
  }-*/;

  public native void uniform1i(WebGLUniformLocation location, int v0) /*-{
		this.uniform1i(location, v0);
  }-*/;

  public void uniform1iv(WebGLUniformLocation location, int[] values) {
    uniform1iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform1iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform1iv(location, v);
  }-*/;

  public native void uniform1iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform1iv(location, values);
  }-*/;

  public native void uniform2f(WebGLUniformLocation location, float v0, float v1) /*-{
		this.uniform2f(location, v0, v1);
  }-*/;

  public void uniform2fv(WebGLUniformLocation location, float[] values) {
    uniform2fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform2fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform2fv(location, v);
  }-*/;

  public native void uniform2fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform2fv(location, values);
  }-*/;

  public native void uniform2i(WebGLUniformLocation location, int v0, int v1) /*-{
		this.uniform2i(location, v0, v1);
  }-*/;

  public void uniform2iv(WebGLUniformLocation location, int[] values) {
    uniform2iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform2iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform2iv(location, v);
  }-*/;

  public native void uniform2iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform2iv(location, values);
  }-*/;

  public native void uniform3f(WebGLUniformLocation location, float v0, float v1, float v2) /*-{
		this.uniform3f(location, v0, v1, v2);
  }-*/;

  public void uniform3fv(WebGLUniformLocation location, float[] values) {
    uniform3fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform3fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform3fv(location, v);
  }-*/;

  public native void uniform3fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform3fv(location, values);
  }-*/;

  public native void uniform3i(WebGLUniformLocation location, int v0, int v1, int v2) /*-{
		this.uniform3i(location, v0, v1, v2);
  }-*/;

  public void uniform3iv(WebGLUniformLocation location, int[] values) {
    uniform3iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform3iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform3iv(location, values);
  }-*/;

  public native void uniform3iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform3iv(location, v);
  }-*/;

  public native void uniform4f(WebGLUniformLocation location, float v0, float v1, float v2, float v3) /*-{
		this.uniform4f(location, v0, v1, v2, v3);
  }-*/;

  public void uniform4fv(WebGLUniformLocation location, float[] values) {
    uniform4fv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform4fv(WebGLUniformLocation location, Float32Array v) /*-{
		this.uniform4fv(location, v);
  }-*/;

  public native void uniform4fv(WebGLUniformLocation location, JsArrayNumber values) /*-{
		this.uniform4fv(location, values);
  }-*/;

  public native void uniform4i(WebGLUniformLocation location, int v0, int v1, int v2, int v3) /*-{
		this.uniform4i(location, v0, v1, v2, v3);
  }-*/;

  public void uniform4iv(WebGLUniformLocation location, int[] values) {
    uniform4iv(location, JsArrayUtil.wrapArray(values));
  }

  public native void uniform4iv(WebGLUniformLocation location, Int32Array v) /*-{
		this.uniform4iv(location, v);
  }-*/;

  public native void uniform4iv(WebGLUniformLocation location, JsArrayInteger values) /*-{
		this.uniform4iv(location, values);
  }-*/;

  public void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose, float[] value) {
    uniformMatrix2fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  public native void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix2fv(location, transpose, value);
  }-*/;

  public native void uniformMatrix2fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix2fv(location, transpose, value);
  }-*/;

  public void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose, float[] value) {
    uniformMatrix3fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  public native void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix3fv(location, transpose, value);
  }-*/;

  public native void uniformMatrix3fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix3fv(location, transpose, value);
  }-*/;

  public void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose, float[] value) {
    uniformMatrix4fv(location, transpose, JsArrayUtil.wrapArray(value));
  }

  public native void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose,
      Float32Array value) /*-{
		this.uniformMatrix4fv(location, transpose, value);
  }-*/;

  public native void uniformMatrix4fv(WebGLUniformLocation location, boolean transpose,
      JsArrayNumber value) /*-{
		this.uniformMatrix4fv(location, transpose, value);
  }-*/;

  public native void useProgram(WebGLProgram program) /*-{
		this.useProgram(program);
  }-*/;

  public native void validateProgram(WebGLProgram program) /*-{
		this.validateProgram(program);
  }-*/;

  public native void vertexAttrib1f(int index, float x) /*-{
		this.vertexAttrib1f(index, x);
  }-*/;

  public void vertexAttrib1fv(int index, float[] values) {
    vertexAttrib1fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib1fv(int index, Float32Array values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  public native void vertexAttrib1fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib1fv(index, values);
  }-*/;

  public native void vertexAttrib2f(int index, float x, float y) /*-{
		this.vertexAttrib2f(index, x, y);
  }-*/;

  public void vertexAttrib2fv(int index, float[] values) {
    vertexAttrib2fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib2fv(int index, Float32Array values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  public native void vertexAttrib2fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib2fv(index, values);
  }-*/;

  public native void vertexAttrib3f(int index, float x, float y, float z) /*-{
		this.vertexAttrib3f(index, x, y, z);
  }-*/;

  public void vertexAttrib3fv(int index, float[] values) {
    vertexAttrib3fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib3fv(int index, Float32Array values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  public native void vertexAttrib3fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib3fv(index, values);
  }-*/;

  public native void vertexAttrib4f(int index, float x, float y, float z, float w) /*-{
		this.vertexAttrib4f(index, x, y, z, w);
  }-*/;

  public void vertexAttrib4fv(int index, float[] values) {
    vertexAttrib4fv(index, JsArrayUtil.wrapArray(values));
  }

  public native void vertexAttrib4fv(int index, Float32Array values) /*-{
		this.vertexAttrib4fv(index, values);
  }-*/;

  public native void vertexAttrib4fv(int index, JsArrayNumber values) /*-{
		this.vertexAttrib4fv(index, values);
  }-*/;

  public native void vertexAttribPointer(int index, int size, int type, boolean normalized,
      int stride, int offset) /*-{
		this.vertexAttribPointer(index, size, type, normalized, stride, offset);
  }-*/;

  public native void viewport(int x, int y, int w, int h) /*-{
		this.viewport(x, y, w, h);
  }-*/;

  protected native com.google.gwt.core.client.JsArray<WebGLShader> getAttachedShadersDev(
      WebGLProgram program) /*-{
		return this.getAttachedShaders(program);
  }-*/;

  protected native WebGLShader[] getAttachedShadersProd(WebGLProgram program) /*-{
		return this.getAttachedShaders(program);
  }-*/;

}
