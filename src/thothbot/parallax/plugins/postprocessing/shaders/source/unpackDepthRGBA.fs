uniform float opacity;

uniform sampler2D tDiffuse;

varying vec2 vUv;

// RGBA depth

float unpackDepth( const in vec4 rgba_depth ) {

	const vec4 bit_shift = vec4( 1.0 / ( 256.0 * 256.0 * 256.0 ), 1.0 / ( 256.0 * 256.0 ), 1.0 / 256.0, 1.0 );
	float depth = dot( rgba_depth, bit_shift );
	return depth;

}

void main() {

	float depth = 1.0 - unpackDepth( texture2D( tDiffuse, vUv ) );
	gl_FragColor = opacity * vec4( vec3( depth ), 1.0 );

}