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
package org.parallax3d.parallax.graphics.core;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.geometry.Group;
import org.parallax3d.parallax.graphics.core.geometry.MorphNormal;
import org.parallax3d.parallax.graphics.core.geometry.MorphTarget;
import org.parallax3d.parallax.graphics.core.geometry.VertextNormal;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.system.*;
import org.parallax3d.parallax.system.events.DisposeEvent;

import java.util.ArrayList;
import java.util.List;

@ThreejsObject("THREE.DirectGeometry")
public class DirectGeometry extends EventDispatcher implements Disposable {

    static int Counter = 0;

    int id = 0;

    String name = "";

    List<Face3> indices = new ArrayList<>();
    List<Vector3> vertices = new ArrayList<>();
    List<Vector3> normals = new ArrayList<>();
    List<Color> colors = new ArrayList<>();
    List<Vector2> uvs = new ArrayList<>();
    List<Vector2> uvs2 = new ArrayList<>();

    List<Group> groups = new ArrayList<>();

    FastMap<List<List<Vector3>>> morphTargets = new FastMap<>();

    List<Vector4> skinWeights = new ArrayList<>();
    List<Vector4> skinIndices = new ArrayList<>();

    // Bounding box.
    protected Box3 boundingBox = null;

    // Bounding sphere.
    protected Sphere boundingSphere = null;

    boolean verticesNeedUpdate = false;
    boolean normalsNeedUpdate = false;
    boolean colorsNeedUpdate = false;
    boolean uvsNeedUpdate = false;
    boolean groupsNeedUpdate = false;

    public DirectGeometry() {
        this.id = Counter++;
    }

    /**
     * Computes bounding box of the geometry.
     */
    public void computeBoundingBox()
    {
        if ( this.boundingBox == null )
        {
            this.boundingBox = new Box3();
        }

        this.boundingBox.setFromPoints( this.vertices );
    }

    /**
     * Computes bounding sphere of the geometry.
     * <p>
     * Neither bounding boxes or bounding spheres are computed by default.
     * They need to be explicitly computed, otherwise they are null.
     */
    public void computeBoundingSphere()
    {
        if ( this.boundingSphere == null ) {

            this.boundingSphere = new Sphere();

        }

        this.boundingSphere.setFromPoints( this.vertices, null );

    }

    public void computeVertexNormals()
    {
        Log.warn( "DirectGeometry.computeVertexNormals() is not a method of this type of geometry." );
    }

    public void computeGroups( Geometry geometry ) {

        Group group = null;

        List<Group> groups = new ArrayList<>();
        int materialIndex = 0;

        List<Face3> faces = geometry.faces;

        for ( int i = 0; i < faces.size(); i ++ ) {

            Face3 face = faces.get(i);

            // materials

            if ( face.materialIndex != materialIndex ) {

                materialIndex = face.materialIndex;

                if ( group != null ) {

                    group.setCount((i * 3) - group.getStart());
                    groups.add( group );

                }

                group = new Group(
                    i * 3,
                    0,
                    materialIndex
                );

            }

        }

        if ( group != null ) {

            group.setCount((faces.size() * 3) - group.getStart());
            groups.add( group );

        }

        this.groups = groups;

    }

    public DirectGeometry fromGeometry( Geometry geometry ) {

        List<Face3> faces = geometry.faces;
        List<Vector3> vertices = geometry.vertices;
        List<List<List<Vector2>>> faceVertexUvs = geometry.faceVertexUvs;

        boolean hasFaceVertexUv = faceVertexUvs.size() > 0 && faceVertexUvs.get(0) != null && faceVertexUvs.get(0).size() > 0;
        boolean hasFaceVertexUv2 = faceVertexUvs.size() > 1 && faceVertexUvs.get(1) != null && faceVertexUvs.get(1).size() > 0;

        // morphs

        List<MorphTarget> morphTargets = geometry.morphTargets;
        int morphTargetsLength = morphTargets.size();

        List<List<Vector3>> morphTargetsPosition = null;

        if ( morphTargetsLength > 0 ) {

            morphTargetsPosition = new ArrayList<>();

            for ( int i = 0; i < morphTargetsLength; i ++ ) {

                morphTargetsPosition.add( i , new ArrayList<Vector3>());

            }

            this.morphTargets.put("position", morphTargetsPosition);

        }

        List<MorphNormal> morphNormals = geometry.morphNormals;
        int morphNormalsLength = morphNormals.size();

        List<List<Vector3>>  morphTargetsNormal = null;

        if ( morphNormalsLength > 0 ) {

            morphTargetsNormal = new ArrayList<>();

            for ( int i = 0; i < morphNormalsLength; i ++ ) {

                morphTargetsNormal.add( i , new ArrayList<Vector3>());

            }

            this.morphTargets.put("normal", morphTargetsNormal);

        }

        // skins

        List<Vector4> skinIndices = geometry.skinIndices;
        List<Vector4> skinWeights = geometry.skinWeights;

        boolean hasSkinIndices = skinIndices.size() == vertices.size();
        boolean hasSkinWeights = skinWeights.size() == vertices.size();

        //

        for ( int i = 0; i < faces.size(); i ++ ) {

            Face3 face = faces.get(i);

            this.vertices.add(vertices.get(face.a));
            this.vertices.add(vertices.get(face.b));
            this.vertices.add(vertices.get(face.c));

            List<Vector3> vertexNormals = face.vertexNormals;

            if ( vertexNormals.size() == 3 ) {

                this.normals.add(vertexNormals.get(0));
                this.normals.add(vertexNormals.get(1));
                this.normals.add(vertexNormals.get(2));

            } else {

                Vector3 normal = face.normal;

                this.normals.add( normal );
                this.normals.add( normal );
                this.normals.add( normal );

            }

            List<Color> vertexColors = face.vertexColors;

            if ( vertexColors.size() == 3 ) {

                this.colors.add(vertexColors.get(0));
                this.colors.add(vertexColors.get(1));
                this.colors.add(vertexColors.get(2));

            } else {

                Color color = face.color;

                this.colors.add( color );
                this.colors.add( color );
                this.colors.add( color );

            }

            if ( hasFaceVertexUv ) {

                List<Vector2> vertexUvs = faceVertexUvs.get(0).get(i);

                if ( vertexUvs != null ) {

                    this.uvs.add(vertexUvs.get(0));
                    this.uvs.add(vertexUvs.get(1));
                    this.uvs.add(vertexUvs.get(2));

                } else {

                    Log.warn( "DirectGeometry.fromGeometry(): Undefined vertexUv " + i );

                    this.uvs.add( new Vector2() );
                    this.uvs.add( new Vector2() );
                    this.uvs.add( new Vector2() );

                }

            }

            if ( hasFaceVertexUv2 ) {

                List<Vector2> vertexUvs = faceVertexUvs.get(1).get(i);

                if ( vertexUvs != null ) {

                    this.uvs2.add(vertexUvs.get(0));
                    this.uvs2.add(vertexUvs.get(1));
                    this.uvs2.add(vertexUvs.get(2));

                } else {

                    Log.warn( "DirectGeometry.fromGeometry(): Undefined vertexUv2 " + i );

                    this.uvs2.add( new Vector2() );
                    this.uvs2.add( new Vector2() );
                    this.uvs2.add( new Vector2() );

                }

            }

            // morphs

            for ( int j = 0; j < morphTargetsLength; j ++ ) {

                List<Vector3> morphTarget = morphTargets.get(j).getVertices();

                morphTargetsPosition.get( j ).add(morphTarget.get(face.a));
                morphTargetsPosition.get( j ).add(morphTarget.get(face.b));
                morphTargetsPosition.get( j ).add(morphTarget.get(face.c));

            }

            for ( int j = 0; j < morphNormalsLength; j ++ ) {

                VertextNormal morphNormal = morphNormals.get(j).getVertexNormals().get(i);

                morphTargetsNormal.get(j).add(morphNormal.getA());
                morphTargetsNormal.get(j).add(morphNormal.getB());
                morphTargetsNormal.get(j).add(morphNormal.getC());

            }

            // skins

            if ( hasSkinIndices ) {

                this.skinIndices.add(skinIndices.get(face.a));
                this.skinIndices.add(skinIndices.get(face.b));
                this.skinIndices.add(skinIndices.get(face.c));

            }

            if ( hasSkinWeights ) {

                this.skinWeights.add(skinWeights.get(face.a));
                this.skinWeights.add(skinWeights.get(face.b));
                this.skinWeights.add(skinWeights.get(face.c));

            }

        }

        this.computeGroups( geometry );

        this.verticesNeedUpdate = geometry.verticesNeedUpdate;
        this.normalsNeedUpdate = geometry.normalsNeedUpdate;
        this.colorsNeedUpdate = geometry.colorsNeedUpdate;
        this.uvsNeedUpdate = geometry.uvsNeedUpdate;
        this.groupsNeedUpdate = geometry.groupsNeedUpdate;

        return this;

    }

    @Override
    public void dispose() {

        dispatchEvent( new DisposeEvent( this) );

    }

}
