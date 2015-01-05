uniform vec3 mirrorColor;
uniform sampler2D mirrorSampler;

varying vec4 mirrorCoord;

float blendOverlay(float base, float blend) {
	return( base < 0.5 ? ( 2.0 * base * blend ) : (1.0 - 2.0 * ( 1.0 - base ) * ( 1.0 - blend ) ) );
}
		
void main() {

	vec4 color = texture2DProj(mirrorSampler, mirrorCoord);
	color = vec4(blendOverlay(mirrorColor.r, color.r), blendOverlay(mirrorColor.g, color.g), blendOverlay(mirrorColor.b, color.b), 1.0);

	gl_FragColor = color;

}
