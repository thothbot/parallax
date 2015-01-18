attribute vec4 tangent;

uniform vec2 uOffset;
uniform vec2 uRepeat;

uniform bool enableDisplacement;

#ifdef VERTEX_TEXTURES

	uniform sampler2D tDisplacement;
	uniform float uDisplacementScale;
	uniform float uDisplacementBias;

#endif

varying vec3 vTangent;
varying vec3 vBinormal;
varying vec3 vNormal;
varying vec2 vUv;

varying vec3 vWorldPosition;
varying vec3 vViewPosition;

[*]

void main() {

[*]

	// normal, tangent and binormal vectors

	#ifdef USE_SKINNING

		vNormal = normalize( normalMatrix * skinnedNormal.xyz );

		vec4 skinnedTangent = skinMatrix * vec4( tangent.xyz, 0.0 );
		vTangent = normalize( normalMatrix * skinnedTangent.xyz );

	#else

		vNormal = normalize( normalMatrix * normal );
		vTangent = normalize( normalMatrix * tangent.xyz );

	#endif

	vBinormal = normalize( cross( vNormal, vTangent ) * tangent.w );

	vUv = uv * uRepeat + uOffset;

	// displacement mapping

	vec3 displacedPosition;

	#ifdef VERTEX_TEXTURES

		if ( enableDisplacement ) {

			vec3 dv = texture2D( tDisplacement, uv ).xyz;
			float df = uDisplacementScale * dv.x + uDisplacementBias;
			displacedPosition = position + normalize( normal ) * df;

		} else {

			#ifdef USE_SKINNING

				vec4 skinVertex = bindMatrix * vec4( position, 1.0 );

				vec4 skinned = vec4( 0.0 );
				skinned += boneMatX * skinVertex * skinWeight.x;
				skinned += boneMatY * skinVertex * skinWeight.y;
				skinned += boneMatZ * skinVertex * skinWeight.z;
				skinned += boneMatW * skinVertex * skinWeight.w;
				skinned  = bindMatrixInverse * skinned;

				displacedPosition = skinned.xyz;

			#else

				displacedPosition = position;

			#endif

		}

	#else

		#ifdef USE_SKINNING

			vec4 skinVertex = bindMatrix * vec4( position, 1.0 );

			vec4 skinned = vec4( 0.0 );
			skinned += boneMatX * skinVertex * skinWeight.x;
			skinned += boneMatY * skinVertex * skinWeight.y;
			skinned += boneMatZ * skinVertex * skinWeight.z;
			skinned += boneMatW * skinVertex * skinWeight.w;
			skinned  = bindMatrixInverse * skinned;

			displacedPosition = skinned.xyz;

		#else

			displacedPosition = position;

		#endif

	#endif

	//

	vec4 mvPosition = modelViewMatrix * vec4( displacedPosition, 1.0 );
	vec4 worldPosition = modelMatrix * vec4( displacedPosition, 1.0 );

	gl_Position = projectionMatrix * mvPosition;

[*]

	//

	vWorldPosition = worldPosition.xyz;
	vViewPosition = -mvPosition.xyz;

	// shadows

	#ifdef USE_SHADOWMAP

		for( int i = 0; i < MAX_SHADOWS; i ++ ) {

			vShadowCoord[ i ] = shadowMatrix[ i ] * worldPosition;

		}

	#endif

}
