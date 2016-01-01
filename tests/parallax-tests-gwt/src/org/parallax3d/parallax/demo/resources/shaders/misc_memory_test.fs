void main() {

	if ( mod ( gl_FragCoord.x, 4.0001 ) < 1.0 || mod ( gl_FragCoord.y, 4.0001 ) < 1.0 )

		gl_FragColor = vec4( [*], 1.0 );

	else

		gl_FragColor = vec4( 1.0 );

}