uniform lowp int renderType;

uniform vec3 screenPosition;
uniform vec2 scale;
uniform float rotation;

uniform sampler2D occlusionMap;

attribute vec2 position;
attribute vec2 uv;

varying vec2 vUV;
varying float vVisibility;

void main() {

	vUV = uv;

	vec2 pos = position;

	if( renderType == 2 ) {

		vec4 visibility = texture2D( occlusionMap, vec2( 0.1, 0.1 ) );
		visibility += texture2D( occlusionMap, vec2( 0.5, 0.1 ) );
		visibility += texture2D( occlusionMap, vec2( 0.9, 0.1 ) );
		visibility += texture2D( occlusionMap, vec2( 0.9, 0.5 ) );
		visibility += texture2D( occlusionMap, vec2( 0.9, 0.9 ) );
		visibility += texture2D( occlusionMap, vec2( 0.5, 0.9 ) );
		visibility += texture2D( occlusionMap, vec2( 0.1, 0.9 ) );
		visibility += texture2D( occlusionMap, vec2( 0.1, 0.5 ) );
		visibility += texture2D( occlusionMap, vec2( 0.5, 0.5 ) );

		vVisibility =        visibility.r / 9.0;
		vVisibility *= 1.0 - visibility.g / 9.0;
		vVisibility *=       visibility.b / 9.0;
		vVisibility *= 1.0 - visibility.a / 9.0;

		pos.x = cos( rotation ) * position.x - sin( rotation ) * position.y;
		pos.y = sin( rotation ) * position.x + cos( rotation ) * position.y;

	}

	gl_Position = vec4( ( pos * scale + screenPosition.xy ).xy, screenPosition.z, 1.0 );

}
