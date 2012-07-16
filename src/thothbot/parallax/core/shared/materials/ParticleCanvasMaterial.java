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

package thothbot.parallax.core.shared.materials;

import thothbot.parallax.core.shared.core.Color3f;

public final class ParticleCanvasMaterial extends Material
{
	public static interface Program
	{
		<T> void run(T context, Color3f color);
	}

	static class ParticleCanvasMaterialOptions extends Material.MaterialOptions
	{
		public Color3f color = new Color3f(0xffffff);
		public Program program = new Program() {

			@Override
			public void run(Object context, Color3f color)
			{
				// TODO Auto-generated method stub

			}
		};
	}

	public ParticleCanvasMaterial(ParticleCanvasMaterialOptions options) 
	{
		super(options);
		this.color = options.color;
		this.program = options.program;
	}

	private Color3f color;
	private Program program;

	public Color3f getColor()
	{
		return color;
	}

	public Program getProgram()
	{
		return program;
	}
}
