#define GAMMA_FACTOR 2

uniform sampler2D tDiffuse;

varying vec2 vUv;

#include <common>

void main() {

	vec4 tex = texture2D( tDiffuse, vec2( vUv.x, vUv.y ) );

	gl_FragColor = vec4( linearToOutput( tex.rgb ), tex.a );

}
