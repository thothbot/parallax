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

package thothbot.parallax.plugins.postprocessing;

public abstract class Pass
{
	private boolean enabled = true;
	private boolean needsSwap = false;

	public Pass(){}
		
	public abstract void render( Postprocessing postprocessing, double delta, boolean maskActive );
		
	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public boolean isNeedsSwap()
	{
		return needsSwap;
	}

	public void setNeedsSwap(boolean needsSwap)
	{
		this.needsSwap = needsSwap;
	}
	
	public boolean isMaskActive()
	{
		return false;
	}
}
