/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.core.client.gl2.enums;

/**
 * Specifies the primitive or primitives that will be created from vertices presented
 * between glBegin and the subsequent glEnd.
 * 
 * @author thothbot
 *
 */
public enum BeginMode implements GLConstants
{
	/**
	 * Treats each vertex as a single point. Vertex n defines point n. N points are drawn.
	 */
	POINTS(GLConstants.POINTS),
	
	/**
	 * Treats each pair of vertices as an independent line segment. 
	 * Vertices 2n - 1  and 2n  define line n. N/2 lines are drawn.	
	 */
	LINES(GLConstants.LINES),
	
	/**
	 * Draws a connected group of line segments from the first vertex to 
	 * the last, then back to the first. Vertices n and n + 1  define line n. 
	 * The last line, however, is defined by vertices N and 1 . N lines are drawn.
	 */
	LINE_LOOP(GLConstants.LINE_LOOP),
	
	/**
	 * Draws a connected group of line segments from the first vertex to the last. 
	 * Vertices n and n + 1  define line n. N - 1  lines are drawn.
	 */
	LINE_STRIP(GLConstants.LINE_STRIP),
	
	/**
	 * Treats each triplet of vertices as an independent triangle. Vertices 3 n - 2, 
	 * 3 n - 1, and 3 n  define triangle n. N/3 triangles are drawn.
	 */
	TRIANGLES(GLConstants.TRIANGLES),
	
	/**
	 * Draws a connected group of triangles. One triangle is defined for each 
	 * vertex presented after the first two vertices. For odd n, vertices n, 
	 * n + 1 , and n + 2  define triangle n. For even n, vertices n + 1 , n, 
	 * and n + 2  define triangle n. N - 2 triangles are drawn.
	 */
	TRIANGLE_STRIP(GLConstants.TRIANGLE_STRIP),
	
	/**
	 * Draws a connected group of triangles. One triangle is defined for 
	 * each vertex presented after the first two vertices. Vertices 1, 
	 * n + 1 , and n + 2  define triangle n. N - 2  triangles are drawn.
	 */
	TRIANGLE_FAN(GLConstants.TRIANGLE_FAN);

	private final int value;

	private BeginMode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
