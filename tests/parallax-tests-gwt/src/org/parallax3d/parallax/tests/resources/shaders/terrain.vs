attribute vec4 tangent;

uniform vec2 uRepeatBase;

uniform sampler2D tNormal;

#ifdef VERTEX_TEXTURES

	uniform sampler2D tDisplacement;
	uniform float uDisplacementScale;
	uniform float uDisplacementBias;

#endif

varying vec3 vTangent;
varying vec3 vBinormal;
varying vec3 vNormal;
varying vec2 vUv;

varying vec3 vViewPosition;

[*]

void main() {

	vNormal = normalize( normalMatrix * normal );

	// tangent and binormal vectors

	vTangent = normalize( normalMatrix * tangent.xyz );

	vBinormal = cross( vNormal, vTangent ) * tangent.w;
	vBinormal = normalize( vBinormal );

	// texture coordinates

	vUv = uv;

	vec2 uvBase = uv * uRepeatBase;

	// displacement mapping

	#ifdef VERTEX_TEXTURES

		vec3 dv = texture2D( tDisplacement, uvBase ).xyz;
		float df = uDisplacementScale * dv.x + uDisplacementBias;
		vec3 displacedPosition = normal * df + position;

		vec4 worldPosition = modelMatrix * vec4( displacedPosition, 1.0 );
		vec4 mvPosition = modelViewMatrix * vec4( displacedPosition, 1.0 );

	#else

		vec4 worldPosition = modelMatrix * vec4( position, 1.0 );
		vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );

	#endif

	gl_Position = projectionMatrix * mvPosition;

	vViewPosition = -mvPosition.xyz;

	vec3 normalTex = texture2D( tNormal, uvBase ).xyz * 2.0 - 1.0;
	vNormal = normalMatrix * normalTex;

[*]

}
