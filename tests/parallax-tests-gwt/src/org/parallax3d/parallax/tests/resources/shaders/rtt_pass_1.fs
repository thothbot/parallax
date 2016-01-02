varying vec2 vUv;
uniform float time;

void main() {

	float r = vUv.x;
	if( vUv.y < 0.5 ) r = 0.0;
	float g = vUv.y;
	if( vUv.x < 0.5 ) g = 0.0;

	gl_FragColor = vec4( r, g, time, 1.0 );

}
