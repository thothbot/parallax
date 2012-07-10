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

package thothbot.squirrel.core.shared.curves;

import thothbot.squirrel.core.shared.core.Vector2f;

public class CurveLine extends Curve
{

	private Vector2f v1;
	private Vector2f v2;

	public CurveLine(Vector2f v1, Vector2f v2) 
	{
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public Vector2f getPoint(float t)
	{
		Vector2f point = this.v2.clone();
		point.sub(this.v1);
		point.multiply(t);
		point.add(this.v1);

		return point;
	}

	/*
	 * Line curve is linear, so we can overwrite default getPointAt
	 */
	@Override
	public Vector2f getPointAt(float u)
	{
		return this.getPoint(u);
	}

	@Override
	public Vector2f getTangent(float t)
	{
		Vector2f tangent = this.v2.clone();
		tangent.sub(this.v1);
		tangent.normalize();
		return tangent;
	}
}
