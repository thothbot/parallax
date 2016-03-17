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
package org.parallax3d.parallax.math.interpolants;

import org.parallax3d.parallax.math.Interpolant;
import org.parallax3d.parallax.system.ThreejsObject;

/**
 *
 * Interpolant that evaluates to the sample value at the position preceeding
 * the parameter.
 *
 * @author tschw
 */
@ThreejsObject("THREE.DiscreteInterpolant")
public class DiscreteInterpolant extends Interpolant {

    public DiscreteInterpolant(double[] parameterPositions, double[] sampleValues, int sampleSize, double[] resultBuffer)
    {
        super(parameterPositions, sampleValues, sampleSize, resultBuffer);
    }

    @Override
    protected double[] interpolate(int i1, double t0, double t, double t1)
    {
        return this.copySampleValue( i1 - 1 );
    }
}
