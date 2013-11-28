uniform vec3 diffuse;
uniform float opacity;

uniform float dashSize;
uniform float totalSize;

varying float vLineDistance;

[*]

void main() {

	if ( mod( vLineDistance, totalSize ) > dashSize ) {

		discard;

	}

	gl_FragColor = vec4( diffuse, opacity );

[*]

}
