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
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * @author alteredq / http://alteredqualia.com/
 */
@ThreejsObject("THREE.SpriteMaterial")
public class SpriteMaterial extends Material implements HasMap, HasColor, HasFog {

    Color color = new Color( 0xffffff );

    Texture map;

    double rotation = 0.;

    boolean fog = true;

    @Override
    public Shader getAssociatedShader() {
        return null;
    }

    @Override
    public Texture getMap() {
        return map;
    }

    @Override
    public SpriteMaterial setMap(Texture map)
    {
        this.map = map;

        return this;
    }

    /**
     * Line color in hexadecimal. Default is 0xffffff.
     */
    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public SpriteMaterial setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public SpriteMaterial setColor(int color) {
        this.color = new Color( color );
        return this;
    }

    public double getRotation() {
        return rotation;
    }

    public SpriteMaterial setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public boolean isFog() {
        return this.fog;
    }

    @Override
    public SpriteMaterial setFog(boolean fog) {
        this.fog = fog;
        return this;
    }

    @Override
    public SpriteMaterial clone() {
        return new SpriteMaterial().copy(this);
    }

    public SpriteMaterial copy(SpriteMaterial source) {
        super.copy(source);

        this.color.copy( source.color );
        this.map = source.map;

        this.rotation = source.rotation;

        this.fog = source.fog;

        return this;

    }
}
