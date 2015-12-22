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

package org.parallax3d.parallax.graphics.extras.helpers;

import org.parallax3d.parallax.system.ThreeJsObject;
import org.parallax3d.parallax.graphics.extras.geometries.SphereGeometry;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;

@ThreeJsObject("THREE.PointLightHelper")
public class PointLightHelper extends Mesh {
	
	PointLight light;
	
	public PointLightHelper(PointLight light, float sphereSize) 
	{
		super(new SphereGeometry( sphereSize, 4, 2 ),  new MeshBasicMaterial());
		
		this.light = light;
		this.light.updateMatrixWorld(false);
		
		MeshBasicMaterial material = (MeshBasicMaterial) getMaterial();
		material.setWireframe(true);
		material.setFog(false);
		material.getColor().copy( this.light.getColor() ).multiply( this.light.getIntensity() );
		
		setMatrix( this.light.getMatrixWorld() );
		setMatrixAutoUpdate(false);


	}
	
	public void update () {

		((MeshBasicMaterial)getMaterial()).getColor()
			.copy( this.light.getColor() )
			.multiply( this.light.getIntensity() );
	}
}