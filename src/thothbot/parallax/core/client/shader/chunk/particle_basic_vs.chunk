uniform float size;
uniform float scale;
			
[*]			

void main() {
			
[*]
			
vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );

#ifdef USE_SIZEATTENUATION
	gl_PointSize = size * ( scale / length( mvPosition.xyz ) );
#else
	gl_PointSize = size;
#endif

gl_Position = projectionMatrix * mvPosition;
			
[*]

}