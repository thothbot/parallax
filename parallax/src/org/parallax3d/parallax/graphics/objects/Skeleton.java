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

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.textures.DataTexture;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mikael emtinger / http://gomo.se/
 * @author alteredq / http://alteredqualia.com/
 * @author michael guerrero / http://realitymeltdown.com
 * @author ikerr / http://verold.com
 */
@ThreejsObject("THREE.Skeleton")
public class Skeleton {

    boolean useVertexTexture;
    Matrix4 identityMatrix = new Matrix4();

    List<Bone> bones;
    List<Matrix4> boneInverses;

    int boneTextureWidth;
    int boneTextureHeight;

    Float32Array boneMatrices;

    DataTexture boneTexture;

    public Skeleton(List<Bone> bones, List<Matrix4> boneInverses)
    {
        this(bones, boneInverses, true);
    }

    public Skeleton(List<Bone> bones, List<Matrix4> boneInverses, boolean useVertexTexture)
    {
        this.useVertexTexture = useVertexTexture;

        // copy the bone array

        this.bones = new ArrayList<>(bones);

        // create a bone texture or an array of floats

        if ( this.useVertexTexture )
        {

            // layout (1 matrix = 4 pixels)
            //      RGBA RGBA RGBA RGBA (=> column1, column2, column3, column4)
            //  with  8x8  pixel texture max   16 bones * 4 pixels =  (8 * 8)
            //       16x16 pixel texture max   64 bones * 4 pixels = (16 * 16)
            //       32x32 pixel texture max  256 bones * 4 pixels = (32 * 32)
            //       64x64 pixel texture max 1024 bones * 4 pixels = (64 * 64)


            double size = Math.sqrt( this.bones.size() * 4 ); // 4 pixels needed for 1 matrix
            size = Mathematics.nextPowerOfTwo((int) Math.ceil( size ));
            size = Math.max( size, 4 );

            this.boneTextureWidth = (int) size;
            this.boneTextureHeight = (int) size;

            this.boneMatrices = Float32Array.create( this.boneTextureWidth * this.boneTextureHeight * 4 ); // 4 floats per RGBA pixel
//            this.boneTexture = new DataTexture( this.boneMatrices, this.boneTextureWidth, this.boneTextureHeight, RGBAFormat, THREE.FloatType );

        } else {

            this.boneMatrices = Float32Array.create( 16 * this.bones.size() );

        }

        // use the supplied bone inverses or calculate the inverses

        if ( boneInverses == null ) {

            this.calculateInverses();

        } else {

            if ( this.bones.size() == boneInverses.size() ) {

                this.boneInverses = new ArrayList<>( boneInverses );

            } else {

                Log.warn( "Skeleton bonInverses is the wrong length." );

                this.boneInverses = new ArrayList<>();

                for ( int b = 0, bl = this.bones.size(); b < bl; b ++ ) {

                    this.boneInverses.add( new Matrix4() );

                }

            }

        }
    }

    public boolean isUseVertexTexture() {
        return useVertexTexture;
    }

    public void setUseVertexTexture(boolean useVertexTexture) {
        this.useVertexTexture = useVertexTexture;
    }

    public List<Bone> getBones() {
        return bones;
    }

    public void setBones(List<Bone> bones) {
        this.bones = bones;
    }

    public List<Matrix4> getBoneInverses() {
        return boneInverses;
    }

    public void setBoneInverses(List<Matrix4> boneInverses) {
        this.boneInverses = boneInverses;
    }

    public int getBoneTextureWidth() {
        return boneTextureWidth;
    }

    public void setBoneTextureWidth(int boneTextureWidth) {
        this.boneTextureWidth = boneTextureWidth;
    }

    public int getBoneTextureHeight() {
        return boneTextureHeight;
    }

    public void setBoneTextureHeight(int boneTextureHeight) {
        this.boneTextureHeight = boneTextureHeight;
    }

    public Float32Array getBoneMatrices() {
        return boneMatrices;
    }

    public void setBoneMatrices(Float32Array boneMatrices) {
        this.boneMatrices = boneMatrices;
    }

    public DataTexture getBoneTexture() {
        return boneTexture;
    }

    public void setBoneTexture(DataTexture boneTexture) {
        this.boneTexture = boneTexture;
    }

    public Matrix4 getIdentityMatrix() {
        return identityMatrix;
    }

    public void setIdentityMatrix(Matrix4 identityMatrix) {
        this.identityMatrix = identityMatrix;
    }

    public void calculateInverses()
    {

        this.boneInverses = new ArrayList<>();

        for ( int b = 0, bl = this.bones.size(); b < bl; b ++ ) {

            Matrix4 inverse = new Matrix4();

            if ( this.bones.get( b ) != null )
            {

                inverse.getInverse( this.bones.get( b ).getMatrixWorld() );

            }

            this.boneInverses.add( inverse );

        }

    }

    public void pose()
    {

        Bone bone;

        // recover the bind-time world matrices

        for ( int b = 0, bl = this.bones.size(); b < bl; b ++ ) {

            bone = this.bones.get( b );

            if ( bone != null ) {

                bone.getMatrixWorld().getInverse( this.boneInverses.get( b ) );

            }

        }

        // compute the local matrices, positions, rotations and scales

        for ( int b = 0, bl = this.bones.size(); b < bl; b ++ ) {

            bone = this.bones.get( b );

            if ( bone != null) {

                if ( bone.getParent() != null ) {

                    bone.getMatrix().getInverse( bone.getParent().getMatrixWorld() );
                    bone.getMatrix().multiply( bone.getMatrixWorld() );

                } else {

                    bone.getMatrix().copy( bone.getMatrixWorld() );

                }

                bone.getMatrix().decompose( bone.getPosition(), bone.getQuaternion(), bone.getScale() );

            }

        }

    }

    Matrix4 _offsetMatrix = new Matrix4();
    public void update()
    {

        // flatten bone matrices to array

        for ( int b = 0, bl = this.bones.size(); b < bl; b ++ ) {

            // compute the offset between the current and the original transform

            Matrix4 matrix = this.bones.get( b ) != null ? this.bones.get( b ).getMatrixWorld() : this.identityMatrix;

            _offsetMatrix.multiply( matrix, this.boneInverses.get( b ) );
            _offsetMatrix.flattenToArrayOffset( this.boneMatrices, b * 16 );

        }

        if ( this.useVertexTexture ) {

            this.boneTexture.setNeedsUpdate(true);

        }

    }

    public Skeleton clone()
    {
        return new Skeleton( this.bones, this.boneInverses, this.useVertexTexture );
    }
}
