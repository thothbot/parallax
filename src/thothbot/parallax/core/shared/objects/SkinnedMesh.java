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

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.textures.Texture;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;

public class SkinnedMesh extends Mesh {
	
	public Texture boneTexture;
	public Float32Array boneMatrices = (Float32Array) Float32Array.createArray();
	
	private String bindMode = "attached";
	private Matrix4 bindMatrix = new Matrix4();
	private Matrix4 bindMatrixInverse = new Matrix4();
	private boolean useVertexTexture;
	List<Bone> bones = new ArrayList<Bone>();	
	
	public SkinnedMesh(AbstractGeometry geometry, Material material, boolean useVertexTexture) {
		super(geometry, material);
		
		// init bones

		// TODO: remove bone creation as there is no reason (other than
		// convenience) for THREE.SkinnedMesh to do this.

		if ( getGeometry() != null && ((Geometry)getGeometry()).getBones() != null ) {

			for ( int b = 0, bl = ((Geometry)getGeometry()).getBones().size(); b < bl; ++b ) {

				Bone gbone = ((Geometry)getGeometry()).getBones().get( b );

				Vector3 p = gbone.getPosition();
				Quaternion q = gbone.getQuaternion();
				Vector3 s = gbone.getScale();
 
				Bone bone = new Bone( this );
				bones.add( bone );

				bone.setName( gbone.getName());
				bone.getPosition().set( p.getX(), p.getY(), p.getZ() );
				bone.getQuaternion().set( q.getX(), q.getY(), q.getZ(), q.getW() );

				if ( s != null ) {

					bone.getScale().set( s.getX(), s.getY(), s.getZ() );

				} else {

					bone.getScale().set( 1, 1, 1 );

				}

			}

			for ( int b = 0, bl = ((Geometry)getGeometry()).getBones().size(); b < bl; ++b ) {

				Bone gbone = ((Geometry)getGeometry()).getBones().get( b );

				if ( gbone.getParent() != null ) {

					// TODO
//					bones[ gbone.getParent() ].add( bones.get( b ) );

				} else {

					this.add( bones.get( b ) );

				}

			}

		}

		this.normalizeSkinWeights();

		this.updateMatrixWorld( true );
//		this.bind( new THREE.Skeleton( bones, undefined, useVertexTexture ) );

	}
		
	public List<Bone> getBones() {
		return bones;
	}

	public void setBones(List<Bone> bones) {
		this.bones = bones;
	}

	public boolean isUseVertexTexture() {
		return useVertexTexture;
	}

	public void setUseVertexTexture(boolean useVertexTexture) {
		this.useVertexTexture = useVertexTexture;
	}
	
	public Texture getBoneTexture() {
		return boneTexture;
	}

	public void setBoneTexture(Texture boneTexture) {
		this.boneTexture = boneTexture;
	}

	public Float32Array getBoneMatrices() {
		return boneMatrices;
	}

	public void setBoneMatrices(Float32Array boneMatrices) {
		this.boneMatrices = boneMatrices;
	}

	public void normalizeSkinWeights () {

		if ( getGeometry() instanceof Geometry ) {

			for ( int i = 0; i < ((Geometry)getGeometry()).getSkinIndices().size(); i ++ ) {

				Vector4 sw = ((Geometry)getGeometry()).getSkinWeights().get( i );

				double scale = 1.0 / sw.lengthManhattan();

				if ( scale != Double.POSITIVE_INFINITY ) {

					sw.multiply( scale );

				} else {

					sw.set( 1 ); // this will be normalized by the shader anyway

				}

			}

		} else {

			// skinning weights assumed to be normalized for THREE.BufferGeometry

		}

	}
	
	public void updateMatrixWorld( boolean force ) {

		super.updateMatrixWorld( true );

		if ( this.bindMode == "attached" ) {

			this.bindMatrixInverse.getInverse( this.matrixWorld );

		} else if ( this.bindMode == "detached" ) {

			this.bindMatrixInverse.getInverse( this.bindMatrix );

		} else {

			Log.warn( "SkinnedMesh unreckognized bindMode: " + this.bindMode );

		}

	}
	
	public SkinnedMesh clone() {
		return clone(new SkinnedMesh( getGeometry(), getMaterial(), this.useVertexTexture ));
	}
	
	public SkinnedMesh clone( SkinnedMesh object ) {

		super.clone(object);

		return object;

	}

}
