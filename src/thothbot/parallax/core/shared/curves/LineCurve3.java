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

package thothbot.parallax.core.shared.curves;

import thothbot.parallax.core.shared.core.Vector3;

public class LineCurve3 extends Curve
{

	public Vector3 v1;
	public Vector3 v2;

	public LineCurve3(Vector3 v1, Vector3 v2) 
	{
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vector3 getPoint(double t)
	{
		Vector3 point = this.v2.clone();
		point.sub(this.v1);
		point.multiply(t);
		point.add(this.v1);

		return point;
	}

}
