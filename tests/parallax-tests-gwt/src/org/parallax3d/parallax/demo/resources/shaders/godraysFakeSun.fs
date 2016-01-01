varying vec2 vUv;

uniform vec2 vSunPositionScreenSpace;
uniform float fAspect;

uniform vec3 sunColor;
uniform vec3 bgColor;

void main() {

	vec2 diff = vUv - vSunPositionScreenSpace;

	// Correct for aspect ratio

	diff.x *= fAspect;

	float prop = clamp( length( diff ) / 0.5, 0.0, 1.0 );
	prop = 0.35 * pow( 1.0 - prop, 3.0 );

	gl_FragColor.xyz = mix( sunColor, bgColor, 1.0 - prop );
	gl_FragColor.w = 1.0;

}
