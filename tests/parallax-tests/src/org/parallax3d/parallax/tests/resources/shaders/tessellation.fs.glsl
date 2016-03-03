varying vec3 vNormal;
varying vec3 vColor;

void main() {

    const float ambient = 0.4;

    vec3 light = vec3( 1.0 );
    light = normalize( light );

    float directional = max( dot( vNormal, light ), 0.0 );

    gl_FragColor = vec4( ( directional + ambient ) * vColor, 1.0 );

}