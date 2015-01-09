#ifdef USE_MAP

	#ifdef GAMMA_INPUT

		vec4 texelColor = texture2D( map, vUv );
		texelColor.xyz *= texelColor.xyz;

		gl_FragColor = gl_FragColor * texelColor;

	#else

		gl_FragColor = gl_FragColor * texture2D( map, vUv );

	#endif

#endif