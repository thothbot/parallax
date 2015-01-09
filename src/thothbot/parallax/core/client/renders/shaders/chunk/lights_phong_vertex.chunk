#ifndef PHONG_PER_PIXEL

#if MAX_POINT_LIGHTS > 0

	for( int i = 0; i < MAX_POINT_LIGHTS; i ++ ) {

		vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );
		vec3 lVector = lPosition.xyz - mvPosition.xyz;

		float lDistance = 1.0;
		if ( pointLightDistance[ i ] > 0.0 )
			lDistance = 1.0 - min( ( length( lVector ) / pointLightDistance[ i ] ), 1.0 );

		vPointLight[ i ] = vec4( lVector, lDistance );

	}

#endif

#if MAX_SPOT_LIGHTS > 0

	for( int i = 0; i < MAX_SPOT_LIGHTS; i ++ ) {

		vec4 lPosition = viewMatrix * vec4( spotLightPosition[ i ], 1.0 );
		vec3 lVector = lPosition.xyz - mvPosition.xyz;

		float lDistance = 1.0;
		if ( spotLightDistance[ i ] > 0.0 )
			lDistance = 1.0 - min( ( length( lVector ) / spotLightDistance[ i ] ), 1.0 );

		vSpotLight[ i ] = vec4( lVector, lDistance );

	}

#endif

#endif

#if MAX_SPOT_LIGHTS > 0 || defined( USE_BUMPMAP )

	vWorldPosition = mPosition.xyz;

#endif