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

package org.parallax3d.parallax.graphics.renderers.plugins.sprite;

import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;

import java.util.List;

public class Sprite extends GeometryObject implements Comparable<Sprite>
{
	private static SpriteMaterial defaultMaterial = new SpriteMaterial();

	private static BufferGeometry defaultGeometry = new BufferGeometry();
	static {
		Uint16Array indices = Uint16Array.create( new int[]{
				0, 1, 2,  
				0, 2, 3 });
		Float32Array vertices = Float32Array.create( new double[] {
			    - 0.5, - 0.5, 0,  
			 	  0.5, - 0.5, 0,   
				  0.5, 0.5, 0,   
				- 0.5, 0.5, 0});
		Float32Array uvs = Float32Array.create( new double[] {
				0, 0,   
				1, 0,   
				1, 1,   
				0, 1});
		
		defaultGeometry.addAttribute( "index", new BufferAttribute( indices, 1 ) );
		defaultGeometry.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
		defaultGeometry.addAttribute( "uv", new BufferAttribute( uvs, 2 ) );
	};
	
	private double z;
	
	public Sprite() 
	{
		this(defaultMaterial);
	}
	
	public Sprite(SpriteMaterial material) 
	{
		this(defaultGeometry, material);
	}
	
	public Sprite(AbstractGeometry geometry, Material material)
	{
		super(geometry, material);
	}
	
	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
		
	@Override
	public void raycast(Raycaster raycaster, List<Raycaster.Intersect> intersects) {
		Vector3 matrixPosition = new Vector3();
		
		matrixPosition.setFromMatrixPosition( this.matrixWorld );

		double distance = raycaster.getRay().distanceToPoint( matrixPosition );

		if ( distance > this.scale.getX() ) {

			return;

		}

		Raycaster.Intersect intersect = new Raycaster.Intersect();
		intersect.distance = distance;
		intersect.point = this.position;
		intersect.object = this;
		intersects.add( intersect );
		
	}
	
	@Override
	public void renderBuffer(GLRenderer renderer,
							 GLGeometry geometryBuffer, boolean updateBuffers) {
		// TODO Auto-generated method stub
		
	}

	public Sprite clone() 
	{

		Sprite object = new Sprite( (SpriteMaterial) this.material );

		super.clone( object );

		return object;

	}
	
	@Override
	public int compareTo(Sprite b)
	{
		Sprite a = this;
		if ( a.z != b.z ) {

			return (int) (b.z - a.z);

		} else {

			return b.id - a.id;

		}

	}

}
