/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.graphics.lights;

import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.materials.MeshPhongMaterial;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 * Affects objects using {@link MeshLambertMaterial} or {@link MeshPhongMaterial}.
 * <p/>
 * <pre>
 * {@code
 * PointLight light = new PointLight( 0xff0000, 1, 100 );
 * light.getPosition().set( 50, 50, 50 );
 * getScene().add( light );
 * }
 * </pre>
 *
 * @author thothbot
 */
@ThreejsObject("THREE.PointLight")
public class PointLight extends Light implements HasShadow {
    double distance;
    int decay;

    LightShadow shadow;

    public PointLight(int hex) {
        this(hex, 1.0);
    }

    public PointLight(int hex, double intensity) {
        this(hex, intensity, 0.0, 1);
    }

    /**
     * @param hex
     * @param intensity
     * @param distance
     * @param decay     for physically correct lights, should be 2.
     */
    public PointLight(int hex, double intensity, double distance, int decay) {
        super(hex, intensity);
        this.intensity = intensity;
        this.distance = distance;
        this.decay = decay;

        this.shadow = new LightShadow(new PerspectiveCamera(90, 1, 0.5, 500));
    }

    public PointLight setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public int getDecay() {
        return decay;
    }

    public void setDecay(int decay) {
        this.decay = decay;
    }

    @Override
    public LightShadow getShadow() {
        return shadow;
    }

    @Override
    public void setShadow(LightShadow shadow) {
        this.shadow = shadow;
    }

    /**
     * intensity = power per solid angle.
     * ref: equation (17) from http://www.frostbite.com/wp-content/uploads/2014/11/course_notes_moving_frostbite_to_pbr.pdf
     *
     * @return
     */
    public double getPower() {

        return this.intensity * Math.PI;

    }

    /**
     * intensity = power per solid angle.
     * ref: equation (17) from http://www.frostbite.com/wp-content/uploads/2014/11/course_notes_moving_frostbite_to_pbr.pdf
     *
     * @return
     */
    public PointLight setPower(double power) {
        this.intensity = power / Math.PI;

        return this;
    }

    public PointLight copy(PointLight source) {

        super.copy(source);

        this.distance = source.distance;
        this.decay = source.decay;

        this.shadow = source.shadow.clone();

        return this;

    }

    @Override
    public PointLight clone() {
        return new PointLight(0x000000).copy(this);
    }
}
