#define PHONG

varying vec3 vViewPosition;
varying vec3 vNormal;

[*]
			
void main() {
			
[*]
			
vNormal = transformedNormal;

[*]

vViewPosition = -mvPosition.xyz;

[*]

}
