uniform float mNear;
uniform float mFar;
uniform float opacity;

[*]

void main() {

[*]

	#ifdef USE_LOGDEPTHBUF_EXT

		float depth = gl_FragDepthEXT / gl_FragCoord.w;

	#else

		float depth = gl_FragCoord.z / gl_FragCoord.w;

	#endif

	float color = 1.0 - smoothstep( mNear, mFar, depth );
	gl_FragColor = vec4( vec3( color ), opacity );

}