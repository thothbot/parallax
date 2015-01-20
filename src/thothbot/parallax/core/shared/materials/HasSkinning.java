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

package thothbot.parallax.core.shared.materials;

public interface HasSkinning 
{
	public boolean isSkinning();
	public void setSkinning(boolean isSkinning);
	
	public boolean isMorphTargets();
	public void setMorphTargets(boolean isMorphTargets);
	
	public boolean isMorphNormals();
	public void setMorphNormals(boolean isMorphNormals);

	public int getNumSupportedMorphTargets();
	public void setNumSupportedMorphTargets(int num);
	
	public int getNumSupportedMorphNormals();
	public void setNumSupportedMorphNormals(int num);
}
