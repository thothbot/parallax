/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
 *
 * This file is part of Parallax project.
 *
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 *
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 *
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */
package org.parallax3d.parallax.graphics.materials;

import org.parallax3d.parallax.graphics.renderers.shaders.Shader;
import org.parallax3d.parallax.graphics.textures.Texture;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.system.ThreejsObject;

@ThreejsObject("THREE.MeshStandardMaterial")
public class MeshStandardMaterial extends Material implements HasColor, HasMap, HasLightMap, HasAoMap,
        HasEmissiveMap, HasBumpMap, HasNormalMap, HasDisplacementMap, HasRoughnessMap, HasAlphaMap, HasEnvMap, HasFog,
        HasShading, HasWireframe, HasVertexColors, HasSkinning
{
    Color color = new Color( 0xffffff ); // diffuse
    double roughness = 0.5;
    double metalness = 0.5;

    Texture map;

    Texture lightMap;
    double lightMapIntensity = 1.0;

    Texture aoMap;
    double aoMapIntensity = 1.0;

    Color emissive = new Color( 0x000000 );
    double emissiveIntensity = 1.0;
    Texture emissiveMap;

    Texture bumpMap;
    double bumpScale = 1.0;

    Texture normalMap;
    Vector2 normalScale = new Vector2( 1, 1 );

    Texture displacementMap;
    double displacementScale = 1.0;
    double displacementBias = 0.0;

    Texture roughnessMap;

    Texture metalnessMap;

    Texture alphaMap;

    Texture envMap;
    double envMapIntensity = 1.0;
    Texture.OPERATIONS combine = Texture.OPERATIONS.MULTIPLY;
    double reflectivity = 1.0;
    double refractionRatio = 0.98;

    boolean fog = true;

    Material.SHADING shading = SHADING.SMOOTH;

    boolean wireframe = false;
    double wireframeLinewidth = 1.0;
    String wireframeLinecap = "round";
    String wireframeLinejoin = "round";

    Material.COLORS vertexColors = COLORS.NO;

    boolean skinning = false;
    boolean morphTargets = false;
    boolean morphNormals = false;


    @Override
    public Texture getAlphaMap() {
        return alphaMap;
    }

    @Override
    public MeshStandardMaterial setAlphaMap(Texture map) {
        this.map = map;
        return this;
    }

    @Override
    public Texture getAoMap() {
        return aoMap;
    }

    @Override
    public MeshStandardMaterial setAoMap(Texture aoMap) {
        this.aoMap = aoMap;
        return this;
    }

    @Override
    public double getAoMapIntensity() {
        return aoMapIntensity;
    }

    @Override
    public MeshStandardMaterial setAoMapIntensity(double aoMapIntensity) {
        this.aoMapIntensity = aoMapIntensity;
        return this;
    }

    @Override
    public Texture getBumpMap() {
        return bumpMap;
    }

    @Override
    public MeshStandardMaterial setBumpMap(Texture bumpMap) {
        this.bumpMap = bumpMap;
        return this;
    }

    @Override
    public double getBumpScale() {
        return bumpScale;
    }

    @Override
    public MeshStandardMaterial setBumpScale(double bumpScale) {
        this.bumpScale = bumpScale;
        return this;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public MeshStandardMaterial setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public MeshStandardMaterial setColor(int color) {
        this.color = new Color( color );
        return this;
    }

    @Override
    public Texture getDisplacementMap() {
        return displacementMap;
    }

    @Override
    public MeshStandardMaterial setDisplacementMap(Texture displacementMap) {
        this.displacementMap = displacementMap;
        return this;
    }

    @Override
    public Texture getEmissiveMap() {
        return emissiveMap;
    }

    @Override
    public MeshStandardMaterial setEmissiveMap(Texture emissiveMap) {
        this.emissiveMap = emissiveMap;
        return this;
    }

    @Override
    public Texture getEnvMap() {
        return envMap;
    }

    @Override
    public MeshStandardMaterial setEnvMap(Texture envMap) {
        this.envMap = envMap;
        return this;
    }

    @Override
    public Texture.OPERATIONS getCombine() {
        return combine;
    }

    @Override
    public MeshStandardMaterial setCombine(Texture.OPERATIONS combine) {
        this.combine = combine;
        return this;
    }

    @Override
    public double getReflectivity() {
        return reflectivity;
    }

    @Override
    public MeshStandardMaterial setReflectivity(double reflectivity) {
        this.reflectivity = reflectivity;
        return this;
    }

    @Override
    public double getRefractionRatio() {
        return refractionRatio;
    }

    @Override
    public MeshStandardMaterial setRefractionRatio(double refractionRatio) {
        this.refractionRatio = refractionRatio;
        return this;
    }

    @Override
    public boolean isFog() {
        return fog;
    }

    @Override
    public MeshStandardMaterial setFog(boolean fog) {
        this.fog = fog;
        return this;
    }

    @Override
    public Texture getLightMap() {
        return lightMap;
    }

    @Override
    public MeshStandardMaterial setLightMap(Texture lightMap) {
        this.lightMap = lightMap;
        return this;
    }

    @Override
    public double getLightMapIntensity() {
        return lightMapIntensity;
    }

    @Override
    public MeshStandardMaterial setLightMapIntensity(double intensity) {
        this.lightMapIntensity = intensity;
        return this;
    }

    @Override
    public Texture getMap() {
        return map;
    }

    @Override
    public MeshStandardMaterial setMap(Texture map) {
        this.map = map;
        return this;
    }

    @Override
    public Texture getNormalMap() {
        return normalMap;
    }

    @Override
    public MeshStandardMaterial setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
        return this;
    }

    @Override
    public Vector2 getNormalScale() {
        return normalScale;
    }

    @Override
    public MeshStandardMaterial setNormalScale(Vector2 normalScale) {
        this.normalScale = normalScale;
        return this;
    }

    @Override
    public Texture getRoughnessMap() {
        return roughnessMap;
    }

    @Override
    public MeshStandardMaterial setRoughnessMap(Texture displacementMap) {
        this.displacementMap = displacementMap;
        return this;
    }

    @Override
    public SHADING getShading() {
        return shading;
    }

    @Override
    public MeshStandardMaterial setShading(SHADING shading) {
        this.shading = shading;
        return this;
    }

    @Override
    public boolean isSkinning() {
        return skinning;
    }

    @Override
    public MeshStandardMaterial setSkinning(boolean isSkinning) {
        this.skinning = skinning;
        return this;
    }

    @Override
    public boolean isMorphTargets() {
        return morphTargets;
    }

    @Override
    public MeshStandardMaterial setMorphTargets(boolean isMorphTargets) {
        this.morphTargets = isMorphTargets;
        return this;
    }

    @Override
    public boolean isMorphNormals() {
        return morphNormals;
    }

    @Override
    public MeshStandardMaterial setMorphNormals(boolean isMorphNormals) {
        this.morphNormals = isMorphNormals;
        return this;
    }

    @Override
    public COLORS isVertexColors() {
        return vertexColors;
    }

    @Override
    public MeshStandardMaterial setVertexColors(COLORS vertexColors) {
        this.vertexColors = vertexColors;
        return this;
    }

    @Override
    public boolean isWireframe() {
        return wireframe;
    }

    @Override
    public MeshStandardMaterial setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
        return this;
    }

    @Override
    public double getWireframeLineWidth() {
        return wireframeLinewidth;
    }

    @Override
    public MeshStandardMaterial setWireframeLineWidth(double wireframeLineWidth) {
        this.wireframeLinewidth = wireframeLineWidth;
        return this;
    }

    @Override
    public Shader getAssociatedShader() {
        return null;
    }

    @Override
    public Material clone() {
        return new MeshStandardMaterial().copy(this);
    }

    public MeshStandardMaterial copy(MeshStandardMaterial source)
    {
        super.copy( source );

        this.color.copy( source.color );
        this.roughness = source.roughness;
        this.metalness = source.metalness;

        this.map = source.map;

        this.lightMap = source.lightMap;
        this.lightMapIntensity = source.lightMapIntensity;

        this.aoMap = source.aoMap;
        this.aoMapIntensity = source.aoMapIntensity;

        this.emissive.copy( source.emissive );
        this.emissiveMap = source.emissiveMap;
        this.emissiveIntensity = source.emissiveIntensity;

        this.bumpMap = source.bumpMap;
        this.bumpScale = source.bumpScale;

        this.normalMap = source.normalMap;
        this.normalScale.copy( source.normalScale );

        this.displacementMap = source.displacementMap;
        this.displacementScale = source.displacementScale;
        this.displacementBias = source.displacementBias;

        this.roughnessMap = source.roughnessMap;

        this.metalnessMap = source.metalnessMap;

        this.alphaMap = source.alphaMap;

        this.envMap = source.envMap;
        this.envMapIntensity = source.envMapIntensity;

        this.refractionRatio = source.refractionRatio;

        this.fog = source.fog;

        this.shading = source.shading;

        this.wireframe = source.wireframe;
        this.wireframeLinewidth = source.wireframeLinewidth;
        this.wireframeLinecap = source.wireframeLinecap;
        this.wireframeLinejoin = source.wireframeLinejoin;

        this.vertexColors = source.vertexColors;

        this.skinning = source.skinning;
        this.morphTargets = source.morphTargets;
        this.morphNormals = source.morphNormals;

        return this;
    }
}
