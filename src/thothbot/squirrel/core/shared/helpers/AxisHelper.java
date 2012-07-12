/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.helpers;

import thothbot.squirrel.core.shared.core.Color3f;
import thothbot.squirrel.core.shared.core.Geometry;
import thothbot.squirrel.core.shared.core.Vector3f;
import thothbot.squirrel.core.shared.geometries.Cylinder;
import thothbot.squirrel.core.shared.materials.LineBasicMaterial;
import thothbot.squirrel.core.shared.materials.MeshBasicMaterial;
import thothbot.squirrel.core.shared.objects.Line;
import thothbot.squirrel.core.shared.objects.Mesh;
import thothbot.squirrel.core.shared.objects.Object3D;

public class AxisHelper extends Object3D
{

	public AxisHelper()
	{
		super();

		Geometry lineGeometry = new Geometry();
		lineGeometry.getVertices().add( new Vector3f() );
		lineGeometry.getVertices().add( new Vector3f( 0, 100, 0 ) );

		Cylinder coneGeometry = new Cylinder( 0f, 5f, 25f, 5, 1 );

		// x
		LineBasicMaterial.LineBasicMaterialOptions lOptions = new LineBasicMaterial.LineBasicMaterialOptions();
		lOptions.color = new Color3f(0xff0000);

		MeshBasicMaterial.MeshBasicMaterialOptions mOptions = new MeshBasicMaterial.MeshBasicMaterialOptions();
		mOptions.color = new Color3f(0xff0000);
		
		Line line1 = new Line( lineGeometry, new LineBasicMaterial( lOptions ) );
		line1.getRotation().setZ( (float) (- Math.PI / 2f));
		this.addChild(line1 );
		
		Mesh cone1 = new Mesh( coneGeometry, new MeshBasicMaterial( mOptions ) );
		cone1.getPosition().setX(100);
		cone1.getRotation().setZ((float) (- Math.PI / 2f));
		this.addChild( cone1 );

		// y

		lOptions.color = new Color3f(0x00ff00);
		Line line2 = new Line( lineGeometry, new LineBasicMaterial( lOptions ) );
		this.addChild( line2 );

		mOptions.color = new Color3f(0x00ff00);
		Mesh cone2 = new Mesh( coneGeometry, new MeshBasicMaterial( mOptions ) );
		cone2.getPosition().setY(100);
		this.addChild( cone2 );

		// z
		lOptions.color = new Color3f(0x0000ff);
		Line line3 = new Line( lineGeometry, new LineBasicMaterial( lOptions ) );
		line3.getRotation().setX((float) (Math.PI / 2f));
		this.addChild( line3 );

		mOptions.color = new Color3f(0x0000ff);
		Mesh cone3 = new Mesh( coneGeometry, new MeshBasicMaterial( mOptions ) );
		cone3.getPosition().setZ(100);
		cone3.getRotation().setX((float) (Math.PI / 2f));
		this.addChild( cone3 );
	}
}
