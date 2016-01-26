precision mediump float;

uniform lowp int renderType;

uniform sampler2D map;
uniform sampler2D occlusionMap;
uniform float opacity;
uniform vec3 color;

varying vec2 vUV;

void main() {

						// pink square

	if( renderType == 0 ) {

		gl_FragColor = vec4( texture2D( map, vUV ).rgb, 0.0 );

						// restore

	} else if( renderType == 1 ) {

		gl_FragColor = texture2D( map, vUV );

						// flare

	} else {

		float visibility = texture2D( occlusionMap, vec2( 0.5, 0.1 ) ).a;
		visibility += texture2D( occlusionMap, vec2( 0.9, 0.5 ) ).a;
		visibility += texture2D( occlusionMap, vec2( 0.5, 0.9 ) ).a;
		visibility += texture2D( occlusionMap, vec2( 0.1, 0.5 ) ).a;
		visibility = ( 1.0 - visibility / 4.0 );

		vec4 texture = texture2D( map, vUV );
		texture.a *= opacity * visibility;
		gl_FragColor = texture;
		gl_FragColor.rgb *= color;

	}

}
