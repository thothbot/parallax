uniform mat4 textureMatrix;
uniform float time;

varying vec4 mirrorCoord;
varying vec3 worldPosition;
		
void main()
{
	mirrorCoord = modelMatrix * vec4( position, 1.0 );
	worldPosition = mirrorCoord.xyz;
	mirrorCoord = textureMatrix * mirrorCoord;
	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );
}
