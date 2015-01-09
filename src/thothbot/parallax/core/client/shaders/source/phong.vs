#define PHONG

varying vec3 vViewPosition;
varying vec3 vNormal;

[*]
			
void main() {
			
[*]
			
vNormal = normalize( transformedNormal );

[*]

vViewPosition = -mvPosition.xyz;

[*]

}
