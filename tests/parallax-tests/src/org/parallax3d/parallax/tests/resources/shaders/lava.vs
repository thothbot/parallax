uniform vec2 uvScale;
varying vec2 vUv;

void main()
{

	vUv = uvScale * uv;
	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );
	gl_Position = projectionMatrix * mvPosition;

}