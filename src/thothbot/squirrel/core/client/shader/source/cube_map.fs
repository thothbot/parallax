uniform samplerCube tCube;
uniform float tFlip;

varying vec3 vViewPosition;

void main() {

	vec3 wPos = cameraPosition - vViewPosition;
	gl_FragColor = textureCube( tCube, vec3( tFlip * wPos.x, wPos.yz ) );

}
