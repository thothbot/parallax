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

package org.parallax3d.parallax.system.gl.enums;

import org.parallax3d.parallax.system.gl.GL20;

/**
 * Specifies the primitive or primitives that will be created from vertices presented
 * between glBegin and the subsequent glEnd.
 * 
 * @author thothbot
 *
 */
public enum BeginMode implements GLEnum
{
	/**
	 * Treats each vertex as a single point. Vertex n defines point n. N points are drawn.
	 */
	POINTS(GL20.GL_POINTS),
	
	/**
	 * Treats each pair of vertices as an independent line segment. 
	 * Vertices 2n - 1  and 2n  define line n. N/2 lines are drawn.	
	 */
	LINES(GL20.GL_LINES),
	
	/**
	 * Draws a connected group of line segments from the first vertex to 
	 * the last, then back to the first. Vertices n and n + 1  define line n. 
	 * The last line, however, is defined by vertices N and 1 . N lines are drawn.
	 */
	LINE_LOOP(GL20.GL_LINE_LOOP),
	
	/**
	 * Draws a connected group of line segments from the first vertex to the last. 
	 * Vertices n and n + 1  define line n. N - 1  lines are drawn.
	 */
	LINE_STRIP(GL20.GL_LINE_STRIP),
	
	/**
	 * Treats each triplet of vertices as an independent triangle. Vertices 3 n - 2, 
	 * 3 n - 1, and 3 n  define triangle n. N/3 triangles are drawn.
	 */
	TRIANGLES(GL20.GL_TRIANGLES),
	
	/**
	 * Draws a connected group of triangles. One triangle is defined for each 
	 * vertex presented after the first two vertices. For odd n, vertices n, 
	 * n + 1 , and n + 2  define triangle n. For even n, vertices n + 1 , n, 
	 * and n + 2  define triangle n. N - 2 triangles are drawn.
	 */
	TRIANGLE_STRIP(GL20.GL_TRIANGLE_STRIP),
	
	/**
	 * Draws a connected group of triangles. One triangle is defined for 
	 * each vertex presented after the first two vertices. Vertices 1, 
	 * n + 1 , and n + 2  define triangle n. N - 2  triangles are drawn.
	 */
	TRIANGLE_FAN(GL20.GL_TRIANGLE_FAN);

	private final int value;

	private BeginMode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
