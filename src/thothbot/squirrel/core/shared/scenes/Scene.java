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
import thothbot.squirrel.core.shared.core.DimensionalObject;
import thothbot.squirrel.core.shared.core.Object3D;
import thothbot.squirrel.core.shared.core.WebGLObject;
import thothbot.squirrel.core.shared.lights.LensFlare;
import thothbot.squirrel.core.shared.lights.Light;
import thothbot.squirrel.core.shared.materials.Material;
import thothbot.squirrel.core.shared.objects.Bone;
import thothbot.squirrel.core.shared.objects.Sprite;

/**
 * 3D Scene. The basic class for rendering.
 * To use this Object in rendering you should add at least one {@link DimensionalObject} 
 * and {@link Camera}.
 * Also you may want to add Lights and {@link Fog} to the Scene Object. 
 * 
 * @author thothbot
 *
 */
public final class Scene extends Object3D
{
	/**
	 * Here are stored cameras, geometries and other DimensionalObject
	 */
	private ArrayList<DimensionalObject> objects;
	
	/**
	 * Here are stored lights associated with the Scene
	 */
	private ArrayList<Light> lights;
	
	/**
	 * Here is stored fog
	 */
	private Fog fog;
	
	/**
	 * History list of added DimensionalObject to the Scene.
	 * This list needed to recalculate buffers from 
	 * the renderer
	 */
	private ArrayList<DimensionalObject> objectsAdded;
	
	/**
	 * History list of removed DimensionalObject from the Scene.
	 * This list needed to recalculate buffers from 
	 * the renderer
	 */
	private ArrayList<DimensionalObject> objectsRemoved;

	/////////////////////////////////////////////
	// TODO: Check
	public Material overrideMaterial;
	public List<Sprite> __webglSprites;
	public List<LensFlare> __webglFlares;
	public List<WebGLObject> __webglObjectsImmediate;
	public List<WebGLObject> __webglObjects;
	
	/**
	 * This default constructor will create new Scene instance.
	 */
	public Scene()
	{
		super();
		this.fog = null;
		this.objects = new ArrayList<DimensionalObject>();
		this.lights = new ArrayList<Light>();
		this.objectsAdded = new ArrayList<DimensionalObject>();
		this.objectsRemoved = new ArrayList<DimensionalObject>();
	}
	
	/**
	 * Get DimensionalObject associated with the Scene.
	 * 
	 * @return the list of DimensionalObject
	 */
	public ArrayList<DimensionalObject> getObjects()
	{
		return this.objects;
	}
	
	/**
	 * Get list of lights associated with the Scene.
	 * 
	 * @return the list of lights.
	 */
	public ArrayList<Light> getLights()
	{
		return this.lights;
	}
	
	/**
	 * Get fog associated with the Scene.
	 * 
	 * @return the fog
	 */
	public Fog getFog()
	{
		return this.fog;
	}
	
	/**
	 * Return the history list of Dimensional objects added to the list.
	 * 
	 * @return the list of DimensionalObject
	 */
	public ArrayList<DimensionalObject> getObjectsAdded()
	{
		return this.objectsAdded;
	}
	
	/**
	 * Return the history list of Dimensional objects removed to the list.
	 * 
	 * @return the list of DimensionalObject
	 */
	public ArrayList<DimensionalObject> getObjectsRemoved()
	{
		return this.objectsRemoved;
	}
	
	/**
	 * Set the fog to the Scene.
	 * 
	 * @param fog the Fog
	 */
	public void setFog(Fog fog)
	{
		this.fog = fog;
	}
	
	/** 
	 * Adding child DimensionalObject to the Scene
	 * 
	 * @param child the DimensionalObject
	 */
	public <E extends DimensionalObject> void addSceneItem(E child)
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

		for (DimensionalObject item : child.getChildren())
			this.addSceneItem(item);
	}
	
	/** 
	 * Removing child DimensionalObject from the Scene
	 * 
	 * @param child the DimensionalObject
	 */
	public <E extends DimensionalObject> void removeSceneItem(E child)
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
		
		for (DimensionalObject item : child.getChildren())
			this.removeSceneItem(item);
	}
}
