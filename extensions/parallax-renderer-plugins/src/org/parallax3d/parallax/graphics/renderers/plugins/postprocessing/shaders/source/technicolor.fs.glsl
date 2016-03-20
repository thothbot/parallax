uniform sampler2D tDiffuse;
varying vec2 vUv;

void main() {

	vec4 tex = texture2D( tDiffuse, vec2( vUv.x, vUv.y ) );
	vec4 newTex = vec4(tex.r, (tex.g + tex.b) * .5, (tex.g + tex.b) * .5, 1.0);

	gl_FragColor = newTex;

}
