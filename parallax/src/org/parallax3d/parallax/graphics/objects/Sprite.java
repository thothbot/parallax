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
package org.parallax3d.parallax.graphics.objects;

import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.graphics.materials.SpriteMaterial;
import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;

import java.util.List;

/**
 * @author mikael emtinger / http://gomo.se/
 * @author alteredq / http://alteredqualia.com/
 */
@ThreejsObject("THREE.Sprite")
public class Sprite extends GeometryObject {

    static final BufferGeometry defaultGeometry = new BufferGeometry();
    static {
        defaultGeometry.setIndex( new BufferAttribute(Uint16Array.create(new int[]{ 0, 1, 2,  0, 2, 3 } ), 1 ) );
        defaultGeometry.addAttribute( "position", new BufferAttribute( Float32Array.create( new double[]{ - 0.5, - 0.5, 0,   0.5, - 0.5, 0,   0.5, 0.5, 0,   - 0.5, 0.5, 0 } ), 3 ) );
        defaultGeometry.addAttribute( "uv", new BufferAttribute( Float32Array.create( new double[]{ 0, 0,   1, 0,   1, 1,   0, 1 } ), 2 ) );
    }

    public Sprite() {
        this(new SpriteMaterial());
    }

    public Sprite(SpriteMaterial material)
    {
        super(defaultGeometry, material);
    }

    @Override
    public void raycast(Raycaster raycaster, List<Raycaster.Intersect> intersects)
    {
        Vector3 matrixPosition = new Vector3();

        matrixPosition.setFromMatrixPosition( this.getMatrixWorld() );

        double distanceSq = raycaster.getRay().distanceSqToPoint( matrixPosition );
        double guessSizeSq = this.getScale().getX() * this.getScale().getY();

        if ( distanceSq > guessSizeSq ) {

            return;

        }

        Raycaster.Intersect intersect = new Raycaster.Intersect();
        intersect.distance = Math.sqrt( distanceSq );
        intersect.point = this.getPosition();

        intersects.add( intersect );

    }

    @Override
    public void renderBuffer(GLRenderer renderer, GLGeometry geometryBuffer, boolean updateBuffers) {

    }

    @Override
    public Sprite clone() {

        return (Sprite) super.clone(new Sprite( (SpriteMaterial) this.getMaterial()) );

    }
}
