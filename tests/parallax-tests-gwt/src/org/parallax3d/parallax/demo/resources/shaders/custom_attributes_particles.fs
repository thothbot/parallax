uniform vec3 color;
uniform sampler2D texture;

varying vec3 vColor;

void main() {

	vec4 color = vec4( color * vColor, 1.0 ) * texture2D( texture, gl_PointCoord );

	if ( color.w < 0.5 ) discard;

	gl_FragColor = color;

}
