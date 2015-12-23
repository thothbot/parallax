uniform float mRefractionRatio;
uniform float mFresnelBias;
uniform float mFresnelScale;
uniform float mFresnelPower;

varying vec3 vReflect;
varying vec3 vRefract[3];
varying float vReflectionFactor;

void main() {

	vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );
	vec4 mPosition = modelMatrix * vec4( position, 1.0 );

	vec3 nWorld = normalize ( mat3( modelMatrix[0].xyz, modelMatrix[1].xyz, modelMatrix[2].xyz ) * normal );

	vec3 I = mPosition.xyz - cameraPosition;

	vReflect = reflect( I, nWorld );
	vRefract[0] = refract( normalize( I ), nWorld, mRefractionRatio );
	vRefract[1] = refract( normalize( I ), nWorld, mRefractionRatio * 0.99 );
	vRefract[2] = refract( normalize( I ), nWorld, mRefractionRatio * 0.98 );
	vReflectionFactor = mFresnelBias + mFresnelScale * pow( 1.0 + dot( normalize( I ), nWorld ), mFresnelPower );

	gl_Position = projectionMatrix * mvPosition;

}
