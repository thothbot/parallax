varying vec2 vUv;

uniform sampler2D tColors;
uniform sampler2D tGodRays;

uniform vec2 vSunPositionScreenSpace;
uniform float fGodRayIntensity;

void main() {

	// Since THREE.MeshDepthMaterial renders foreground objects white and background
	// objects black, the god-rays will be white streaks. Therefore value is inverted
	// before being combined with tColors

	gl_FragColor = texture2D( tColors, vUv ) + fGodRayIntensity * vec4( 1.0 - texture2D( tGodRays, vUv ).r );
	gl_FragColor.a = 1.0;

}
