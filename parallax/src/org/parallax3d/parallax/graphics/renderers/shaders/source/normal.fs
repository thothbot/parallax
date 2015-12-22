uniform float opacity;
varying vec3 vNormal;

[*]

void main() {

	gl_FragColor = vec4( 0.5 * normalize( vNormal ) + 0.5, opacity );

[*]

}