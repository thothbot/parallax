varying vec3 vLightFront;
#ifdef DOUBLE_SIDED
	varying vec3 vLightBack;
#endif
			
[*]

void main() {
	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );

[*]
			
#ifndef USE_ENVMAP
	vec4 mPosition = modelMatrix * vec4( position, 1.0 );
#endif
			
[*]
			
}
			