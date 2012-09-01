varying vec3 vViewPosition;

void main() {

	vec4 mPosition = modelMatrix * vec4( position, 1.0 );
	vViewPosition = cameraPosition - mPosition.xyz;

	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );

}
