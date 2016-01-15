#define TAPS_PER_PASS 6.0

varying vec2 vUv;

uniform sampler2D tInput;

uniform vec2 vSunPositionScreenSpace;
uniform float fStepSize; // filter step size

void main() {

	// delta from current pixel to "sun" position

	vec2 delta = vSunPositionScreenSpace - vUv;
	float dist = length( delta );

	// Step vector (uv space)

	vec2 stepv = fStepSize * delta / dist;

	// Number of iterations between pixel and sun

	float iters = dist/fStepSize;

	vec2 uv = vUv.xy;
	float col = 0.0;

	// This breaks ANGLE in Chrome 22
	//	- see http://code.google.com/p/chromium/issues/detail?id=153105

/*
	// Unrolling didnt do much on my hardware (ATI Mobility Radeon 3450),
	// so i've just left the loop

	for ( float i = 0.0; i < TAPS_PER_PASS; i += 1.0 ) {

					// Accumulate samples, making sure we dont walk past the light source.

					// The check for uv.y < 1 would not be necessary with "border" UV wrap
					// mode, with a black border colour. I don't think this is currently
					// exposed by three.js. As a result there might be artifacts when the
					// sun is to the left, right or bottom of screen as these cases are
					// not specifically handled.

		col += ( i <= iters && uv.y < 1.0 ? texture2D( tInput, uv ).r : 0.0 );
		uv += stepv;

	}
*/

	// Unrolling loop manually makes it work in ANGLE

	if ( 0.0 <= iters && uv.y < 1.0 ) col += texture2D( tInput, uv ).r;
	uv += stepv;

	if ( 1.0 <= iters && uv.y < 1.0 ) col += texture2D( tInput, uv ).r;
	uv += stepv;

	if ( 2.0 <= iters && uv.y < 1.0 ) col += texture2D( tInput, uv ).r;
	uv += stepv;

	if ( 3.0 <= iters && uv.y < 1.0 ) col += texture2D( tInput, uv ).r;
	uv += stepv;

	if ( 4.0 <= iters && uv.y < 1.0 ) col += texture2D( tInput, uv ).r;
	uv += stepv;

	if ( 5.0 <= iters && uv.y < 1.0 ) col += texture2D( tInput, uv ).r;
	uv += stepv;

	// Should technically be dividing by 'iters', but 'TAPS_PER_PASS' smooths out
	// objectionable artifacts, in particular near the sun position. The side
	// effect is that the result is darker than it should be around the sun, as
	// TAPS_PER_PASS is greater than the number of samples actually accumulated.
	// When the result is inverted (in the shader 'godrays_combine', this produces
	// a slight bright spot at the position of the sun, even when it is occluded.

	gl_FragColor = vec4( col/TAPS_PER_PASS );
	gl_FragColor.a = 1.0;

}
