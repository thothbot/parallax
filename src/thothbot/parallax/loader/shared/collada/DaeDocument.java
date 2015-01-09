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

package thothbot.parallax.loader.shared.collada;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Object3D;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;

public class DaeDocument
{
	private Document document;
	private DaeAsset asset;
	
	private List<DaeVisualScene> scenes;
	private Object3D scene;
	
	// Libraries
	private Map<String, DaeImage> images;
	private Map<String, DaeMaterial> materials;
	private Map<String, DaeEffect> effects;
	private Map<String, DaeGeometry> geometries;
	private Map<String, DaeController> controllers;
	private Map<String, DaeVisualScene> visualScenes;

	public DaeDocument(Document document) 
	{
		this.document = document;
		
		this.asset        = DaeAsset.parse(this);
		
		this.images       = DaeImage.parse(this);
		this.materials    = DaeMaterial.parse(this);
		this.effects      = DaeEffect.parse(this);
		this.geometries   = DaeGeometry.parse(this);
		this.controllers  = DaeController.parse(this);
		this.visualScenes = DaeVisualScene.parse(this);
		
		parseScene();
	}
	
	public Document getDocument() {
		return this.document;
	}
	
	public Object3D getScene() 
	{
		if(this.scene == null)
		{
			this.scene = new Object3D();
			for ( int i = 0; i < scenes.size(); i ++ ) 
			{
				scene.add( getSceneObject( scenes.get(i) ) );
			}
		}
		
		return this.scene;
	}
	
	private Object3D getSceneObject(DaeVisualScene visualScene)
	{
		Object3D object = new Object3D();
		
		return object;
	}
	
	private void parseScene()
	{
		scenes = new ArrayList<DaeVisualScene>();
		
		NodeList list = document.getElementsByTagName("instance_visual_scene");
		for (int i = 0; i < list.getLength(); i++) 
		{
			DaeDummyElement scene = new DaeDummyElement(list.item(i));
			String sceneName = scene.readAttribute("url", true);
			Log.debug("DaeDocument() adding scene: " + sceneName);
			scenes.add(visualScenes.get(sceneName));
		}
	}
}
