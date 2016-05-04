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

package org.parallax3d.parallax.graphics.extras.core;

import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.ShapeUtils;
import org.parallax3d.parallax.graphics.extras.core.ExtrudeGeometry;
import org.parallax3d.parallax.graphics.extras.core.Shape;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Creates shape geometry from a path shape.
 * <p>
 * Based on the three.js code.
 */
public class ShapeGeometry extends Geometry {
    public static class ShapeGeometryParameters
    {
        // number of points on the curves
        public int	curveSegments	= 12;
        // material index for front and back faces
        public int	material;
    }

    public ShapeGeometry ( final ShapeGeometryParameters p_option )
    {
        this( new ArrayList< Shape >(), p_option );
    }

    public ShapeGeometry ( final Shape shape, final ShapeGeometryParameters p_option )
    {
        this( Arrays.asList(shape), p_option );
    }

    public ShapeGeometry ( final List< Shape > p_arrShape, final ShapeGeometryParameters p_option )
    {
        super();

        this.addShapeList( p_arrShape, p_option );

        this.computeFaceNormals();
    }

    /**
     * Add an array of shapes to THREE.ShapeGeometry.
     */
    public void addShapeList ( final List< Shape > shapes, final ShapeGeometryParameters options )
    {
        for ( final Shape shape : shapes )
        {
            this.addShape( shape, options );
        }
    }

    /**
     * Adds a shape to THREE.ShapeGeometry, based on THREE.ExtrudeGeometry.
     */
    public void addShape ( final Shape shape, final ShapeGeometryParameters options )
    {
        final int material = options.material;
        final int shapesOffset = this.getVertices().size();
        final List<Vector2> arrVertice = shape.getTransformedPoints();
        final List< List< Vector2 > > arrHole = shape.getPointsHoles();

        boolean reverse;
        // ------------ DECLARE ------------//

        reverse = ShapeUtils.isClockWise(arrVertice);

        if ( reverse )
        {
            Collections.reverse(arrVertice);

            // Maybe we should also check if holes are in the opposite direction, just to be safe ...
            for ( final List< Vector2 > hole : arrHole )
            {
                if ( ShapeUtils.isClockWise( hole ) )
                {
                    Collections.reverse( hole );
                }
            }
            // If vertices are in order now, we shouldn't need to worry about them again (hopefully)!
        }

        final List< List< Integer > > arrFace = ShapeUtils.triangulateShape( arrVertice, arrHole );

        // Vertices
        for ( final List< Vector2 > hole : arrHole )
        {
            arrVertice.addAll( hole );
        }

        for ( final Vector2 vert : arrVertice )
        {
            this.getVertices().add( new Vector3( vert.getX(), vert.getY(), 0.d ) );
        }

        for ( final List< Integer > face : arrFace )
        {

            final int a = face.get( 0 ) + shapesOffset;
            final int b = face.get( 1 ) + shapesOffset;
            final int c = face.get( 2 ) + shapesOffset;

            this.getFaces().add( new Face3( a, b, c, material ) );
            this.getFaceVertexUvs().get( 0 ).add( ExtrudeGeometry.WorldUVGenerator.generateTopUV(this, a, b, c) );
        }
    }
}
