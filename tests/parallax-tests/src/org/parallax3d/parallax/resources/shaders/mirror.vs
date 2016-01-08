uniform mat4 textureMatrix;

varying vec4 mirrorCoord;

void main() {

	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );
	vec4 worldPosition = modelMatrix * vec4( position, 1.0 );
	mirrorCoord = textureMatrix * worldPosition;

	gl_Position = projectionMatrix * mvPosition;

}
