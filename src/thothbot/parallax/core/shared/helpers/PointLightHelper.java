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

package thothbot.parallax.core.shared.helpers;

import thothbot.parallax.core.shared.geometries.SphereGeometry;
import thothbot.parallax.core.shared.lights.PointLight;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.objects.Mesh;

public class PointLightHelper extends Mesh {
	
	PointLight light;
	
	public PointLightHelper(PointLight light, double sphereSize) 
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
