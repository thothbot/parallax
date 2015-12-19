uniform samplerCube tCube;
uniform float tFlip;

varying vec3 vWorldPosition;

[*]

void main() {

	gl_FragColor = textureCube( tCube, vec3( tFlip * vWorldPosition.x, vWorldPosition.yz ) );

[*]

}
