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

package org.parallax3d.parallax.graphics.extras.modifiers;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 *	@author zz85 / http://twitter.com/blurspline / http://www.lab4games.net/zz85/blog 
 *
 *	Subdivision Geometry Modifier 
 *		using Loop Subdivision Scheme
 *
 *	References:
 *		http://graphics.stanford.edu/~mdfisher/subdivision.html
 *		http://www.holmes3d.net/graphics/subdivision/
 *		http://www.cs.rutgers.edu/~decarlo/readings/subdiv-sg00c.pdf
 *
 *	Known Issues:
 *		- currently doesn't handle UVs
 *		- currently doesn't handle "Sharp Edges"
 *
 */
@ThreejsObject("SubdivisionModifier")
public class SubdivisionModifier extends Modifier {

    class Edge {
        public Vector3 a;
        public Vector3 b;

        public int newEdge;

        public List<Face3> faces = new ArrayList<>();
    }

    int subdivisions;
    
    public SubdivisionModifier() {
        this(1);
    }
    
    public SubdivisionModifier(int subdivisions) {
        this.subdivisions = subdivisions;
    }

    @Override
    public void modify(Geometry geometry) {
        int repeats = this.subdivisions;

        while ( repeats -- > 0 ) {

            this.smooth( geometry );

        }

        geometry.computeFaceNormals();
        geometry.computeVertexNormals();
    }

    // Performs one iteration of Subdivision
    public void smooth(Geometry geometry) 
    {
        Vector3 tmp = new Vector3();

        List<Vector3> oldVertices = geometry.getVertices(); // { x, y, z}
        List<Face3> oldFaces = geometry.getFaces(); // { a: oldVertex1, b: oldVertex2, c: oldVertex3 }

        /******************************************************
         *
         * Step 0: Preprocess Geometry to Generate edges Lookup
         *
         *******************************************************/

        List<List<Edge>> metaVertices = new ArrayList<>();
        FastMap<Edge> sourceEdges = new FastMap<>(); // Edge => { oldVertex1, oldVertex2, faces[]  }

        generateLookups( oldVertices, oldFaces, metaVertices, sourceEdges );


        /******************************************************
         *
         *	Step 1. 
         *	For each edge, create a new Edge Vertex,
         *	then position it.
         *
         *******************************************************/

        List<Vector3> newEdgeVertices = new ArrayList<>();
        for ( Edge currentEdge : sourceEdges.values() )
        {

            Vector3 newEdge = new Vector3();

            double edgeVertexWeight = 3. / 8.;
            double adjacentVertexWeight = 1. / 8.;

            int connectedFaces = currentEdge.faces.size();

            // check how many linked faces. 2 should be correct.
            if ( connectedFaces != 2 ) {

                // if length is not 2, handle condition
                edgeVertexWeight = 0.5;
                adjacentVertexWeight = .0;

                if ( connectedFaces != 1 ) {

                    Log.warn( "Subdivision Modifier: Number of connected faces != 2, is: " + connectedFaces );

                }

            }

            newEdge.add( currentEdge.a, currentEdge.b ).multiply( edgeVertexWeight );

            tmp.set( 0, 0, 0 );

            for ( int j = 0; j < connectedFaces; j ++ ) {

                Face3 face = currentEdge.faces.get( j );

                Vector3 other = null;
                for(int f : Arrays.asList(face.getA(), face.getB(), face.getC())) {

                    other = oldVertices.get( f );
                    if ( other != currentEdge.a && other != currentEdge.b ) break;

                }

                tmp.add( other );

            }

            tmp.multiply( adjacentVertexWeight );
            newEdge.add( tmp );

            currentEdge.newEdge = newEdgeVertices.size();
            newEdgeVertices.add( newEdge );

        }

        /******************************************************
         *
         *	Step 2. 
         *	Reposition each source vertices.
         *
         *******************************************************/

        List<Vector3> newSourceVertices = new ArrayList<>();

        for ( int i = 0, il = oldVertices.size(); i < il; i ++ ) {

            Vector3 oldVertex = oldVertices.get( i );

            // find all connecting edges (using lookupTable)
            List<Edge> connectingEdges = metaVertices.get( i );
            int n = connectingEdges.size();

            double beta = 0;
            if ( n == 3 ) {

                beta = 3. / 16.;

            } else if ( n > 3 ) {

                beta = 3. / ( 8. * n ); // Warren's modified formula

            }

            // Loop's original beta formula
            // beta = 1 / n * ( 5/8 - Math.pow( 3/8 + 1/4 * Math.cos( 2 * Math. PI / n ), 2) );

            double sourceVertexWeight = 1 - n * beta;
            double connectingVertexWeight = beta;

            if ( n <= 2 ) {

                // crease and boundary rules
                // console.warn('crease and boundary rules');

                if ( n == 2 ) {

                    Log.warn( "2 connecting edges " + connectingEdges );
                    sourceVertexWeight = 3. / 4.;
                    connectingVertexWeight = 1. / 8.;

                } else if ( n == 1 ) {

                    Log.warn( "only 1 connecting edge" );

                } else if ( n == 0 ) {

                    Log.warn( "0 connecting edges" );

                }

            }

            Vector3 newSourceVertex = oldVertex.clone().multiply( sourceVertexWeight );

            tmp.set( 0, 0, 0 );

            for ( int j = 0; j < n; j ++ ) {

                Edge connectingEdge = connectingEdges.get( j );
                Vector3 other = connectingEdge.a != oldVertex ? connectingEdge.a : connectingEdge.b;
                tmp.add( other );

            }

            tmp.multiply( connectingVertexWeight );
            newSourceVertex.add( tmp );

            newSourceVertices.add( newSourceVertex );

        }


        /******************************************************
         *
         *	Step 3. 
         *	Generate Faces between source vertecies
         *	and edge vertices.
         *
         *******************************************************/

        List<Vector3> newVertices = new ArrayList<>();
            newVertices.addAll(newSourceVertices);
            newVertices.addAll( newEdgeVertices );

        List<Face3> newFaces = new ArrayList<>();

        int sl = newSourceVertices.size();

        for ( int i = 0, il = oldFaces.size(); i < il; i ++ ) {

            Face3 face = oldFaces.get( i );

            // find the 3 new edges vertex of each old face

            int edge1 = getEdge( face.getA(), face.getB(), sourceEdges ).newEdge + sl;
            int edge2 = getEdge( face.getB(), face.getC(), sourceEdges ).newEdge + sl;
            int edge3 = getEdge( face.getC(), face.getA(), sourceEdges ).newEdge + sl;

            // create 4 faces.

            newFace( newFaces, edge1, edge2, edge3 );
            newFace( newFaces, face.getA(), edge1, edge3 );
            newFace( newFaces, face.getB(), edge2, edge1 );
            newFace( newFaces, face.getC(), edge3, edge2 );

        }

        // Overwrite old arrays
        geometry.setVertices( newVertices );
        geometry.setFaces( newFaces );
    }

    private void newFace( List<Face3> newFaces, int a, int b, int c ) {

        newFaces.add( new Face3( a, b, c ) );

    }

    private Edge getEdge( int a, int b, FastMap<Edge> map )
    {
        int vertexIndexA = Math.min( a, b );
        int vertexIndexB = Math.max( a, b );

        String key = vertexIndexA + "_" + vertexIndexB;

        return map.containsKey(key) ? map.get( key ) : null;

    }

    private void processEdge(int a, int b, List<Vector3> vertices, FastMap<Edge> map, Face3 face, List<List<Edge>> metaVertices)
    {
        int vertexIndexA = Math.min( a, b );
        int vertexIndexB = Math.max( a, b );

        String key = vertexIndexA + "_" + vertexIndexB;

        Edge edge;

        if ( map.containsKey( key ) ) {

            edge = map.get( key );

        } else {

            Vector3 vertexA = vertices.get( vertexIndexA );
            Vector3 vertexB = vertices.get( vertexIndexB );

            edge = new Edge();
            edge.a = vertexA;
            edge.b = vertexB;

            map.put( key, edge );

        }

        edge.faces.add( face );

        metaVertices.get( a ).add( edge );
        metaVertices.get( b ).add( edge );

    }

    private void generateLookups( List<Vector3> vertices, List<Face3> faces, List<List<Edge>> metaVertices,  FastMap<Edge> edges ) {

        for ( int i = 0, il = vertices.size(); i < il; i ++ ) {

            metaVertices.add(new ArrayList<Edge>());

        }

        for ( int i = 0, il = faces.size(); i < il; i ++ ) {

            Face3 face = faces.get( i );

            processEdge( face.getA(), face.getB(), vertices, edges, face, metaVertices );
            processEdge( face.getB(), face.getC(), vertices, edges, face, metaVertices );
            processEdge( face.getC(), face.getA(), vertices, edges, face, metaVertices );

        }

    }

}
