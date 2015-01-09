varying vec3 vNormal;

[*]

void main() {

	vNormal = normalize( normalMatrix * normal );

[*]

}