attribute vec4 tangent;

uniform vec2 uOffset;
uniform vec2 uRepeat;

#ifdef VERTEX_TEXTURES

	uniform sampler2D tDisplacement;
	uniform float uDisplacementScale;
	uniform float uDisplacementBias;

#endif

varying vec3 vTangent;
varying vec3 vBinormal;
varying vec3 vNormal;
varying vec2 vUv;

#if MAX_POINT_LIGHTS > 0

	uniform vec3 pointLightPosition[ MAX_POINT_LIGHTS ];
	uniform float pointLightDistance[ MAX_POINT_LIGHTS ];

	varying vec4 vPointLight[ MAX_POINT_LIGHTS ];

#endif

varying vec3 vViewPosition;

[*]

void main() {

	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );

	vViewPosition = -mvPosition.xyz;

	// normal, tangent and binormal vectors

	vNormal = normalMatrix * normal;
	vTangent = normalMatrix * tangent.xyz;
	vBinormal = cross( vNormal, vTangent ) * tangent.w;

	vUv = uv * uRepeat + uOffset;

	// point lights

	#if MAX_POINT_LIGHTS > 0

		for( int i = 0; i < MAX_POINT_LIGHTS; i++ ) {

			vec4 lPosition = viewMatrix * vec4( pointLightPosition[ i ], 1.0 );
			vec3 lVector = lPosition.xyz - mvPosition.xyz;

			float lDistance = 1.0;
			if ( pointLightDistance[ i ] > 0.0 )
				lDistance = 1.0 - min( ( length( lVector ) / pointLightDistance[ i ] ), 1.0 );

			lVector = normalize( lVector );

			vPointLight[ i ] = vec4( lVector, lDistance );

		}

	#endif

	// displacement mapping

	#ifdef VERTEX_TEXTURES

		vec3 dv = texture2D( tDisplacement, uv ).xyz;
		float df = uDisplacementScale * dv.x + uDisplacementBias;
		vec4 displacedPosition = vec4( normalize( vNormal.xyz ) * df, 0.0 ) + mvPosition;
		gl_Position = projectionMatrix * displacedPosition;

	#else

		gl_Position = projectionMatrix * mvPosition;

	#endif

[*]

}
