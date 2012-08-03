precision mediump float;

uniform vec3 color;
uniform sampler2D map;
uniform float opacity;

varying vec2 vUV;

void main() {

	vec4 texture = texture2D( map, vUV );
	gl_FragColor = vec4( color * texture.xyz, texture.a * opacity );

}
