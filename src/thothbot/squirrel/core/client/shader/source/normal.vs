varying vec3 vNormal;

void main() {
	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );
	vNormal = normalMatrix * normal;

	gl_Position = projectionMatrix * mvPosition;
}
			