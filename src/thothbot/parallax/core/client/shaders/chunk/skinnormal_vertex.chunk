#ifdef USE_SKINNING

	mat4 skinMatrix = skinWeight.x * boneMatX;
	skinMatrix 	+= skinWeight.y * boneMatY;

	#ifdef USE_MORPHNORMALS

	vec4 skinnedNormal = skinMatrix * vec4( morphedNormal, 0.0 );

	#else

	vec4 skinnedNormal = skinMatrix * vec4( normal, 0.0 );

	#endif

#endif