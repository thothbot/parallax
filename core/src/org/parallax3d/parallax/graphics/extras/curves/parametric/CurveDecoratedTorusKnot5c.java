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

package org.parallax3d.parallax.graphics.extras.curves.parametric;

import org.parallax3d.parallax.math.Vector3;

public final class CurveDecoratedTorusKnot5c extends CurveDecoratedTorusKnot4a 
{
	@Override
	public Vector3 getPoint(float t)
	{
		float fi = (float)(t * Math.PI * 2);
		float x = (float)(Math.cos(4.0 * fi) * (1.0 + 0.5 * (Math.cos(5.0 * fi) + 0.4 * Math.cos(20.0 * fi))));
		float y = (float)(Math.sin(4.0 * fi) * (1.0 + 0.5 * (Math.cos(5.0 * fi) + 0.4 * Math.cos(20.0 * fi))));
		float z = (float)(0.35 * Math.sin(15.0 * fi));

		return new Vector3(x, y, z).multiply(this.scale);
	}
}
