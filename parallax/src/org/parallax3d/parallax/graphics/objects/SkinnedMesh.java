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

package org.parallax3d.parallax.graphics.objects;

import java.util.ArrayList;
import java.util.List;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector4;

@ThreejsObject("THREE.SkinnedMesh")
public class SkinnedMesh extends Mesh {

	String bindMode = "attached";
	Matrix4 bindMatrix = new Matrix4();
	Matrix4 bindMatrixInverse = new Matrix4();

    Skeleton skeleton;

	public SkinnedMesh(AbstractGeometry geometry, Material material, boolean useVertexTexture) {
		super(geometry, material);

		// TODO: remove bone creation as there is no reason (other than
		// convenience) for THREE.SkinnedMesh to do this.

		List<Bone> bones = new ArrayList<>();

		if ( this.geometry != null && ((Geometry)this.geometry).getBones() != null )
		{

			for ( int b = 0, bl = ((Geometry)this.geometry).getBones().size(); b < bl; ++ b ) {

				Geometry.GBone gbone = ((Geometry)this.geometry).getBones().get( b );

				Bone bone = new Bone( this );
				bones.add( bone );

				bone.setName( gbone.name );
				bone.getPosition().fromArray( gbone.pos );
				bone.getQuaternion().fromArray( gbone.rotq );
				if ( gbone.scl != null ) bone.getScale().fromArray( gbone.scl );

			}

			for ( int b = 0, bl = ((Geometry) this.geometry).getBones().size(); b < bl; ++ b ) {

				Geometry.GBone gbone = ((Geometry) this.geometry).getBones().get( b );

				if ( gbone.parent != -1)
				{
					bones.get( gbone.parent ).add( bones.get( b ) );

				} else {

					this.add( bones.get( b ) );

				}

			}

		}

		this.normalizeSkinWeights();

		this.updateMatrixWorld( true );
		this.bind( new Skeleton( bones, null, useVertexTexture ), this.getMatrixWorld() );


	}

	public void bind( Skeleton skeleton, Matrix4 bindMatrix ) {

		this.skeleton = skeleton;

		if ( bindMatrix == null )
		{

			this.updateMatrixWorld( true );

			this.skeleton.calculateInverses();

			bindMatrix = this.getMatrixWorld();

		}

		this.bindMatrix.copy( bindMatrix );
		this.bindMatrixInverse.getInverse( bindMatrix );

	}

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public void pose() {

		this.skeleton.pose();

	}

	public void normalizeSkinWeights () {

		if ( this.geometry instanceof Geometry ) {

			for ( int i = 0; i < ((Geometry) this.geometry).getSkinWeights().size(); i ++ ) {

				Vector4 sw = ((Geometry) this.geometry).getSkinWeights().get( i );

				double scale = 1.0 / sw.lengthManhattan();

				if ( scale != Double.POSITIVE_INFINITY ) {

					sw.multiply( scale );

				} else {

					sw.set( 1, 0, 0, 0 ); // do something reasonable

				}

			}

		} else if ( this.geometry instanceof BufferGeometry) {

			Vector4 vec = new Vector4();

			BufferAttribute skinWeight = ((BufferGeometry) this.geometry).getAttribute("skinWeight");

			for ( int i = 0; i < skinWeight.getCount(); i ++ ) {

				vec.setX( skinWeight.getX( i ) );
				vec.setY( skinWeight.getY( i ) );
				vec.setZ( skinWeight.getZ( i ) );
				vec.setW( skinWeight.getW( i ) );

				double scale = 1.0 / vec.lengthManhattan();

				if ( scale != Double.POSITIVE_INFINITY ) {

					vec.multiply( scale );

				} else {

					vec.set( 1, 0, 0, 0 ); // do something reasonable

				}

				skinWeight.setXYZW( i, vec.getX(), vec.getY(), vec.getZ(), vec.getW() );

			}

		}

	}

	public void updateMatrixWorld( boolean force ) {

		super.updateMatrixWorld( true );

		switch (this.bindMode) {
			case "attached":

				this.bindMatrixInverse.getInverse(this.getMatrixWorld());

				break;
			case "detached":

				this.bindMatrixInverse.getInverse(this.bindMatrix);

				break;
			default:

				Log.warn("SkinnedMesh unreckognized bindMode: " + this.bindMode);

				break;
		}

	}

	public SkinnedMesh clone() {
		return clone(new SkinnedMesh( getGeometry(), getMaterial(), this.skeleton.useVertexTexture ));
	}

	public SkinnedMesh clone( SkinnedMesh object ) {

		super.clone(object);

		return object;

	}
}
