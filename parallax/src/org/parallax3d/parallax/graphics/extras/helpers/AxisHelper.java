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

import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.objects.LineSegments;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.extras.geometries.CylinderGeometry;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.materials.LineBasicMaterial;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.graphics.objects.Line;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

@ThreejsObject("THREE.AxisHelper")
public class AxisHelper extends LineSegments {

	public AxisHelper() {
		this(1.);
	}

	public AxisHelper(double size) {
		super(intDefaultGeometry(size), new LineBasicMaterial().setVertexColors(Material.COLORS.VERTEX));
	}

	private static BufferGeometry intDefaultGeometry(double size) {

		Float32Array vertices = Float32Array.create(new double[]{
				0, 0, 0, size, 0, 0,
				0, 0, 0, 0, size, 0,
				0, 0, 0, 0, 0, size
		});

		Float32Array colors = Float32Array.create(new double[]{
				1, 0, 0, 1, 0.6, 0,
				0, 1, 0, 0.6, 1, 0,
				0, 0, 1, 0, 0.6, 1
		});

		BufferGeometry geometry = new BufferGeometry();
		geometry.addAttribute( "position", new BufferAttribute( vertices, 3 ) );
		geometry.addAttribute( "color", new BufferAttribute( colors, 3 ) );

		return geometry;
	}
}
