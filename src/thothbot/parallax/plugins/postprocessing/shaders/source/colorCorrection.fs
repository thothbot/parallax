uniform sampler2D tDiffuse;
uniform vec3 powRGB;
uniform vec3 mulRGB;

varying vec2 vUv;

void main() {

	gl_FragColor = texture2D( tDiffuse, vUv );
	gl_FragColor.rgb = mulRGB * pow( gl_FragColor.rgb, powRGB );

}
