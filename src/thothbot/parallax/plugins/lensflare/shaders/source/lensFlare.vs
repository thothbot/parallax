uniform lowp int renderType;

uniform vec3 screenPosition;
uniform vec2 scale;
uniform float rotation;

attribute vec2 position;
attribute vec2 uv;

varying vec2 vUV;

void main() {

	vUV = uv;

	vec2 pos = position;

	if( renderType == 2 ) {

		pos.x = cos( rotation ) * position.x - sin( rotation ) * position.y;
		pos.y = sin( rotation ) * position.x + cos( rotation ) * position.y;

	}

	gl_Position = vec4( ( pos * scale + screenPosition.xy ).xy, screenPosition.z, 1.0 );

}
