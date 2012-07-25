/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.materials.Material;

public class SkinnedMesh extends Mesh
{

	public List<Bone> bones;
	public Float32Array boneMatrices;
	
	public SkinnedMesh(Geometry geometry, Material material) 
	{
		super(geometry, material);

		// init bones
		this.identityMatrix = new Matrix4f();

		this.bones = new ArrayList<Bone>();
		this.boneMatrices = (Float32Array) Float32Array.createArray();

		// TODO: FIX
//		if ( this.geometry.bones != null ) {
//
//			for ( int b = 0; b < this.geometry.bones.size(); b ++ ) {
//
//				Bone gbone = this.geometry.bones.get( b );
//
//				p = gbone.pos;
//				q = gbone.rotq;
//				s = gbone.scl;
//
//				bone = this.addBone();
//
//				bone.name = gbone.name;
//				bone.position.set( p[0], p[1], p[2] );
//				bone.quaternion.set( q[0], q[1], q[2], q[3] );
//				bone.useQuaternion = true;
//
//				if ( s !== undefined ) {
//
//					bone.scale.set( s[0], s[1], s[2] );
//
//				} else {
//
//					bone.scale.set( 1, 1, 1 );
//
//				}
//
//			}
//
//			for ( b = 0; b < this.bones.length; b ++ ) {
//
//				gbone = this.geometry.bones[ b ];
//				bone = this.bones[ b ];
//
//				if ( gbone.parent === -1 ) {
//
//					this.add( bone );
//
//				} else {
//
//					this.bones[ gbone.parent ].add( bone );
//
//				}
//
//			}
//
//			this.boneMatrices = new Float32Array( 16 * this.bones.length );
//
//			this.pose();
//
//		}
	}

	public void addBone() 
	{
		addBone(new Bone());
	}

	public void addBone( Bone bone ) 
	{
		this.bones.add( bone );
	}
	
	public void update( Matrix4f parentSkinMatrix, boolean forceUpdate ) {
		updateMatrixWorld( true );
	}

	public void updateMatrixWorld( boolean force ) 
	{
		if(this.matrixAutoUpdate)
			this.updateMatrix();

		// update matrixWorld

		if ( this.matrixWorldNeedsUpdate || force ) {

			if ( this.parent != null)
				this.matrixWorld.multiply( this.parent.getMatrixWorld(), this.matrix );
			else 
				this.matrixWorld.copy( this.matrix );

			this.matrixWorldNeedsUpdate = false;

			force = true;
		}

		// update children
		for(DimensionalObject child: this.getChildren()) {
			SkinnedMesh mesh = (SkinnedMesh) child;
			mesh.update( this.identityMatrix, false );
		}

		// flatten bone matrices to array
		for ( int b = 0; b < this.bones.size(); b ++ )
			this.bones.get( b ).skinMatrix.flattenToArray( this.boneMatrices, b * 16 );
	}
	/*
	 * Pose
	 */
	public void pose() 
	{
		updateMatrixWorld( true );
		
		List<Matrix4f> boneInverses = null;

		for (int b = 0; b < this.bones.size(); b ++ ) {

			Bone bone = this.bones.get(b);

			Matrix4f inverseMatrix = new Matrix4f();
			inverseMatrix.getInverse( bone.skinMatrix );

			boneInverses.add( inverseMatrix );

			bone.skinMatrix.flattenToArray( this.boneMatrices, b * 16 );
		}

		// project vertices to local
		if ( this.geometry.getSkinVerticesA() == null ) 
		{
			this.geometry.setSkinVerticesA( new ArrayList<Vector3f>() );
			this.geometry.setSkinVerticesB( new ArrayList<Vector3f>() );

			for ( int i = 0; i < this.geometry.getSkinIndices().size(); i ++ ) {

				Vector3f orgVertex = this.geometry.getVertices().get( i );

				int indexA = (int) this.geometry.getSkinIndices().get( i ).getX();
				int indexB = (int) this.geometry.getSkinIndices().get( i ).getY();

				Vector3f vertex = new Vector3f( orgVertex.getX(), orgVertex.getY(), orgVertex.getZ() );
				this.geometry.getSkinVerticesA().add( (Vector3f) boneInverses.get( indexA ).multiplyVector3( vertex ) );

				Vector3f vertex2 = new Vector3f( orgVertex.getX(), orgVertex.getY(), orgVertex.getZ() );
				this.geometry.getSkinVerticesB().add( (Vector3f) boneInverses.get( indexB ).multiplyVector3( vertex2 ) );

				// todo: add more influences

				// normalize weights

				if ( this.geometry.getSkinWeights().get( i ).getX() + this.geometry.getSkinWeights().get( i ).getY() != 1 ) 
				{
					float len = ( 1.0f - ( this.geometry.getSkinWeights().get( i ).getX() 
							+ this.geometry.getSkinWeights().get( i ).getY() ) ) * 0.5f;
					this.geometry.getSkinWeights().get( i ).setX(this.geometry.getSkinWeights().get( i ).getX() + len);
					this.geometry.getSkinWeights().get( i ).setY(this.geometry.getSkinWeights().get( i ).getY() + len);
				}
			}
		}
	}
}
