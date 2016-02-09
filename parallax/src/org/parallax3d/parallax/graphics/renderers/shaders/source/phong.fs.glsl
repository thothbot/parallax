#define PHONG

uniform vec3 diffuse;
uniform float opacity;

uniform vec3 ambient;
uniform vec3 emissive;
uniform vec3 specular;
uniform float shininess;

[*]
			
void main() {
	gl_FragColor = vec4( vec3( 1.0 ), opacity );
				
[*]
				
}
