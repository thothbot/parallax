varying vec2 vUv;

float PHBeckmann( float ndoth, float m ) {

	float alpha = acos( ndoth );
	float ta = tan( alpha );

	float val = 1.0 / ( m * m * pow( ndoth, 4.0 ) ) * exp( -( ta * ta ) / ( m * m ) );
	return val;

}

float KSTextureCompute( vec2 tex ) {

	// Scale the value to fit within [0,1]  invert upon lookup.

	return 0.5 * pow( PHBeckmann( tex.x, tex.y ), 0.1 );

}

void main() {

	float x = KSTextureCompute( vUv );

	gl_FragColor = vec4( x, x, x, 1.0 );

}
