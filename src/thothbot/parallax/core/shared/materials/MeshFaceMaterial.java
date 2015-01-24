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

package thothbot.parallax.core.shared.materials;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.shaders.Shader;

/**
 * A Material to define multiple materials for the same geometry.
 * The geometry decides which material is used for which faces by the faces materialindex. 
 * The materialindex corresponds with the index of the material in the materials array.
 *
 */

public final class MeshFaceMaterial extends Material 
{
	List<Material> materials;
	
	public MeshFaceMaterial() 
	{
		this.materials = new ArrayList<Material>();
	}
	
	/**
	 * Creates a MeshFaceMaterial with the correct materials.
	 * @param materials  The materials for the geometry.
	 */
	public MeshFaceMaterial(List<Material> materials) 
	{
		this.materials = materials;
	}
	
	/**
	 * Get or set the materials for the geometry.
	 * @return
	 */
	public List<Material> getMaterials() 
	{
		return this.materials;
	}

	@Override
	public Shader getAssociatedShader() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public MeshFaceMaterial clone() {

		MeshFaceMaterial material = new MeshFaceMaterial();

		for ( int i = 0; i < this.materials.size(); i ++ ) {

			material.materials.add( this.materials.get( i ).clone() );

		}

		return material;

	}

}