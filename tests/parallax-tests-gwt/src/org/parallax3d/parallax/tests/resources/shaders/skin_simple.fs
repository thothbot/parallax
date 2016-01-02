#define USE_BUMPMAP
#extension GL_OES_standard_derivatives : enable

uniform bool enableBump;
uniform bool enableSpecular;

uniform vec3 ambient;
uniform vec3 diffuse;
uniform vec3 specular;
uniform float opacity;

uniform float uRoughness;
uniform float uSpecularBrightness;

uniform vec3 uWrapRGB;

uniform sampler2D tDiffuse;
uniform sampler2D tBeckmann;

uniform sampler2D specularMap;

varying vec3 vNormal;
varying vec2 vUv;

uniform vec3 ambientLightColor;

#if MAX_DIR_LIGHTS > 0

	uniform vec3 directionalLightColor[ MAX_DIR_LIGHTS ];
	uniform vec3 directionalLightDirection[ MAX_DIR_LIGHTS ];

#endif

#if MAX_HEMI_LIGHTS > 0

	uniform vec3 hemisphereLightSkyColor[ MAX_HEMI_LIGHTS ];
	uniform vec3 hemisphereLightGroundColor[ MAX_HEMI_LIGHTS ];
	uniform vec3 hemisphereLightDirection[ MAX_HEMI_LIGHTS ];

#endif

#if MAX_POINT_LIGHTS > 0

	uniform vec3 pointLightColor[ MAX_POINT_LIGHTS ];
	uniform vec3 pointLightPosition[ MAX_POINT_LIGHTS ];
	uniform float pointLightDistance[ MAX_POINT_LIGHTS ];

#endif

varying vec3 vViewPosition;

[*]

			// Fresnel term

float fresnelReflectance( vec3 H, vec3 V, float F0 ) {

	float base = 1.0 - dot( V, H );
	float exponential = pow( base, 5.0 );

	return exponential + F0 * ( 1.0 - exponential );

}

			// Kelemen/Szirmay-Kalos specular BRDF

float KS_Skin_Specular( vec3 N, 		// Bumped surface normal
						vec3 L, 		// Points to light
						vec3 V, 		// Points to eye
						float m,  	// Roughness
						float rho_s 	// Specular brightness
						) {

	float result = 0.0;
	float ndotl = dot( N, L );

	if( ndotl > 0.0 ) {

		vec3 h = L + V; // Unnormalized half-way vector
		vec3 H = normalize( h );

		float ndoth = dot( N, H );

		float PH = pow( 2.0 * texture2D( tBeckmann, vec2( ndoth, m ) ).x, 10.0 );

		float F = fresnelReflectance( H, V, 0.028 );
		float frSpec = max( PH * F / dot( h, h ), 0.0 );

		result = ndotl * rho_s * frSpec; // BRDF * dot(N,L) * rho_s

	}

	return result;

}

void main() {

	gl_FragColor = vec4( vec3( 1.0 ), opacity );

	vec4 colDiffuse = texture2D( tDiffuse, vUv );
	colDiffuse.rgb *= colDiffuse.rgb;

	gl_FragColor = gl_FragColor * colDiffuse;

	vec3 normal = normalize( vNormal );
	vec3 viewPosition = normalize( vViewPosition );

	float specularStrength;

	if ( enableSpecular ) {

		vec4 texelSpecular = texture2D( specularMap, vUv );
		specularStrength = texelSpecular.r;

	} else {

		specularStrength = 1.0;

	}

	#ifdef USE_BUMPMAP

		if ( enableBump ) normal = perturbNormalArb( -vViewPosition, normal, dHdxy_fwd() );

	#endif

				// point lights

	vec3 specularTotal = vec3( 0.0 );

	#if MAX_POINT_LIGHTS > 0

		vec3 pointTotal = vec3( 0.0 );

		for ( int i = 0; i < MAX_POINT_LIGHTS; i ++ ) {

			vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );

			vec3 lVector = lPosition.xyz + vViewPosition.xyz;

			float lDistance = 1.0;

			if ( pointLightDistance[ i ] > 0.0 )
				lDistance = 1.0 - min( ( length( lVector ) / pointLightDistance[ i ] ), 1.0 );

			lVector = normalize( lVector );

			float pointDiffuseWeightFull = max( dot( normal, lVector ), 0.0 );
			float pointDiffuseWeightHalf = max( 0.5 * dot( normal, lVector ) + 0.5, 0.0 );
			vec3 pointDiffuseWeight = mix( vec3 ( pointDiffuseWeightFull ), vec3( pointDiffuseWeightHalf ), uWrapRGB );

			float pointSpecularWeight = KS_Skin_Specular( normal, lVector, viewPosition, uRoughness, uSpecularBrightness );

			pointTotal    += lDistance * diffuse * pointLightColor[ i ] * pointDiffuseWeight;
			specularTotal += lDistance * specular * pointLightColor[ i ] * pointSpecularWeight * specularStrength;

		}

	#endif

				// directional lights

	#if MAX_DIR_LIGHTS > 0

		vec3 dirTotal = vec3( 0.0 );

		for( int i = 0; i < MAX_DIR_LIGHTS; i++ ) {

			vec4 lDirection = viewMatrix * vec4( directionalLightDirection[ i ], 0.0 );

			vec3 dirVector = normalize( lDirection.xyz );

			float dirDiffuseWeightFull = max( dot( normal, dirVector ), 0.0 );
			float dirDiffuseWeightHalf = max( 0.5 * dot( normal, dirVector ) + 0.5, 0.0 );
			vec3 dirDiffuseWeight = mix( vec3 ( dirDiffuseWeightFull ), vec3( dirDiffuseWeightHalf ), uWrapRGB );

			float dirSpecularWeight =  KS_Skin_Specular( normal, dirVector, viewPosition, uRoughness, uSpecularBrightness );

			dirTotal 	   += diffuse * directionalLightColor[ i ] * dirDiffuseWeight;
			specularTotal += specular * directionalLightColor[ i ] * dirSpecularWeight * specularStrength;

		}

	#endif

				// hemisphere lights

	#if MAX_HEMI_LIGHTS > 0

		vec3 hemiTotal = vec3( 0.0 );

		for ( int i = 0; i < MAX_HEMI_LIGHTS; i ++ ) {

			vec4 lDirection = viewMatrix * vec4( hemisphereLightDirection[ i ], 0.0 );
			vec3 lVector = normalize( lDirection.xyz );

			float dotProduct = dot( normal, lVector );
			float hemiDiffuseWeight = 0.5 * dotProduct + 0.5;

			hemiTotal += diffuse * mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight );

						// specular (sky light)

			float hemiSpecularWeight = 0.0;
			hemiSpecularWeight += KS_Skin_Specular( normal, lVector, viewPosition, uRoughness, uSpecularBrightness );

						// specular (ground light)

			vec3 lVectorGround = -lVector;
			hemiSpecularWeight += KS_Skin_Specular( normal, lVectorGround, viewPosition, uRoughness, uSpecularBrightness );

			specularTotal += specular * mix( hemisphereLightGroundColor[ i ], hemisphereLightSkyColor[ i ], hemiDiffuseWeight ) * hemiSpecularWeight * specularStrength;

		}

	#endif

				// all lights contribution summation

	vec3 totalLight = vec3( 0.0 );

	#if MAX_DIR_LIGHTS > 0
		totalLight += dirTotal;
	#endif

	#if MAX_POINT_LIGHTS > 0
		totalLight += pointTotal;
	#endif

	#if MAX_HEMI_LIGHTS > 0
		totalLight += hemiTotal;
	#endif

	gl_FragColor.xyz = gl_FragColor.xyz * ( totalLight + ambientLightColor * ambient ) + specularTotal;

[*]

}
