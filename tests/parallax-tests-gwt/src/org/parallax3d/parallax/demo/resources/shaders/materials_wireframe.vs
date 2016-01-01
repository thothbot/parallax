attribute vec3 center;
varying vec3 vCenter;

void main() {

	vCenter = center;
	gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );

}
