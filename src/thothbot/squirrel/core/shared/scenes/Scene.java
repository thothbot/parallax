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

package thothbot.squirrel.core.shared.scenes;

import java.util.ArrayList;
import java.util.List;

import thothbot.squirrel.core.shared.cameras.Camera;
import thothbot.squirrel.core.shared.core.DimentionObject;
import thothbot.squirrel.core.shared.core.Object3D;
import thothbot.squirrel.core.shared.core.WebGLObject;
import thothbot.squirrel.core.shared.lights.LensFlare;
import thothbot.squirrel.core.shared.lights.Light;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.objects.Bone;
import thothbot.squirrel.core.shared.objects.Sprite;

public final class Scene extends Object3D
{
	
	protected ArrayList<DimentionObject> objects;
	protected ArrayList<Light> lights;
	protected Fog fog;
	public boolean matrixAutoUpdate = false;


	// TODO: This should be private
	public ArrayList<DimentionObject> objectsAdded;
	public ArrayList<DimentionObject> objectsRemoved;

	/////////////////////////////////////////////
	// TODO: Check
	public Material overrideMaterial;
	public List<Sprite> __webglSprites;
	public List<LensFlare> __webglFlares;
	public List<WebGLObject> __webglObjectsImmediate;
	public List<WebGLObject> __webglObjects;
	
	public Scene()
	{
		super();
		this.fog = null;
		this.objects = new ArrayList<DimentionObject>();
		this.lights = new ArrayList<Light>();
		this.objectsAdded = new ArrayList<DimentionObject>();
		this.objectsRemoved = new ArrayList<DimentionObject>();
	}
	
	public ArrayList<Light> getLights()
	{
		return this.lights;
	}
	
	public Fog getFog()
	{
		return this.fog;
	}
	
	public void setFog(Fog fog)
	{
		this.fog = fog;
	}

	public ArrayList<DimentionObject> getObjects(){
		return this.objects;
	}

//	@Override
//	public <E extends DimentionObject> void addChild(E child)
//	{
//		super.addChild(child);
//		this.addSceneItem(child);
//	}
	
	
	public <E extends DimentionObject> void addSceneItem(E child)
	{
		if (child instanceof Light)
		{
			Light light = (Light)child;
			if (this.lights.indexOf(light) == -1)
				this.lights.add(light);
		}
		else if (!(child instanceof Camera || child instanceof Bone)) 
		{
			if (this.objects.indexOf(child) == -1) {
				this.objects.add(child);
				this.objectsAdded.add(child);
			}
		}

		for (DimentionObject item : child.getChildren())
			this.addSceneItem(item);
	}
	
//	@Override
//	public <E extends DimentionObject> void removeChild(E child)
//	{
//		super.removeChild(child);
//		this.removeSceneItem(child);
//	};
	
	public <E extends DimentionObject> void removeSceneItem(E child)
	{
		if (child instanceof Light)
		{
			Light light = (Light)child;
			if (this.lights.indexOf(light) != -1)
				this.lights.remove(light);
		}
		else if (!(child instanceof Camera || child instanceof Bone))
		{
			if (this.objects.indexOf(child) != -1){
				this.objects.remove(child);
				this.objectsRemoved.add(child);
			}
		}
		
		for (DimentionObject item : child.getChildren())
			this.removeSceneItem(item);
	}
}
