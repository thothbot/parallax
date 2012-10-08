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

package thothbot.parallax.core.shared.scenes;

import java.util.ArrayList;
import java.util.List;

import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryBuffer;
import thothbot.parallax.core.shared.core.GeometryGroup;
import thothbot.parallax.core.shared.lights.Light;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.objects.Bone;
import thothbot.parallax.core.shared.objects.DimensionalObject;
import thothbot.parallax.core.shared.objects.GeometryObject;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;
import thothbot.parallax.core.shared.objects.Object3D;
import thothbot.parallax.core.shared.objects.ParticleSystem;
import thothbot.parallax.core.shared.objects.RendererObject;
import thothbot.parallax.core.shared.objects.Ribbon;

/**
 * 3D Scene. The basic class for rendering.
 * To use this Object in rendering you should add at least one {@link DimensionalObject} 
 * and {@link Camera}.
 * Also you may want to add Lights and {@link FogAbstract} to the Scene Object. 
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

	public List<RendererObject> __webglObjects;	

	public Material overrideMaterial;

	/**
	 * Here are stored lights associated with the Scene
	 */
	private ArrayList<Light> lights;
	
	/**
	 * Here is stored fogAbstract
	 */
	private FogAbstract fogAbstract;
	
	/**
	 * This default constructor will create new Scene instance.
	 */
	public Scene()
	{
		super();
		this.fogAbstract = null;
		this.objects = new ArrayList<DimensionalObject>();
		this.lights = new ArrayList<Light>();
		this.objectsAdded = new ArrayList<DimensionalObject>();
		this.objectsRemoved = new ArrayList<DimensionalObject>();
		
		this.__webglObjects = new ArrayList<RendererObject>();
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
	 * Get fogAbstract associated with the Scene.
	 * 
	 * @return the fogAbstract
	 */
	public FogAbstract getFog()
	{
		return this.fogAbstract;
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
	 * Set the fogAbstract to the Scene.
	 * 
	 * @param fogAbstract the FogAbstract
	 */
	public void setFog(FogAbstract fogAbstract)
	{
		this.fogAbstract = fogAbstract;
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
			if (this.objects.indexOf(child) == -1) 
			{
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
	
	/**
	 * Refresh Scene's objects
	 * 
	 * @param scene the Scene with child objects
	 */
	public void initWebGLObjects(WebGLRenderer renderer) 
	{
		Log.debug("initWebGLObjects() objectsAdded=" + getObjectsAdded().size() 
				+ ", objectsRemoved=" + getObjectsRemoved().size() 
				+ ", update=" + this.__webglObjects.size());
		
		while ( getObjectsAdded().size() > 0 ) 
		{
			// object.createBuffer()->initBuffer(), + __webglObject
			addObject( renderer, (Object3D) getObjectsAdded().get( 0 ) );
			getObjectsAdded().remove(0);
		}

		while ( getObjectsRemoved().size() > 0 ) 
		{
			removeObject( (Object3D) getObjectsRemoved().get( 0 ) );
			getObjectsRemoved().remove(0);
		}

		// update must be called after objects adding / removal
		for(RendererObject object: this.__webglObjects)
		{
			object.object.setBuffer(renderer);
		}			
	}
	
	/**
	 * Adds objects
	 */
	private void addObject ( WebGLRenderer renderer, Object3D object )
	{
		Log.debug("addObject() object=" + object.getClass().getName());

		if ( object instanceof GeometryObject && ! object.isWebglInit ) 
		{
			object.isWebglInit = true;

			Log.debug("addObject() initBuffer()");
			((GeometryObject)object).initBuffer(renderer);
		}

		if ( ! object.isWebglActive ) 
		{
			object.isWebglActive = true;

			Log.debug("addObject() addObjectAddBuffer()");
			addObjectAddBuffer( object );
		}
	}
	
	/*
	 * Objects removal
	 */
	private void removeObject ( Object3D object ) 
	{
		if ( object instanceof GeometryObject) 
		{
			for ( int o = this.__webglObjects.size() - 1; o >= 0; o -- )
				if ( this.__webglObjects.get( o ).object == object )
					this.__webglObjects.remove(o);
		}

		object.isWebglActive = false;
	}
	
	private void addObjectAddBuffer(Object3D object)
	{
		if(object instanceof GeometryObject)
		{
			if ( object instanceof Mesh ) 
			{
				Mesh mesh = (Mesh)object;
				Geometry geometry = mesh.getGeometry();

				if(mesh.getGeometryBuffer() != null) 
				{
					addBuffer( mesh.getGeometryBuffer(), (GeometryObject)object );
				}
				else 
				{				
					for ( GeometryGroup geometryGroup : geometry.getGeometryGroups())
					{
						addBuffer( geometryGroup, (GeometryObject)object );
					}
				}
			} 
			else if ( object instanceof Ribbon ||
					object instanceof Line ||
					object instanceof ParticleSystem 
			) {

				Geometry geometry = ((GeometryObject)object).getGeometry();
				addBuffer( geometry, (GeometryObject)object );
			}
		} 
	}
	
	private void addBuffer (GeometryBuffer buffer, GeometryObject object ) 
	{
		this.__webglObjects.add(new RendererObject(buffer, object, null, null));
	}
}
