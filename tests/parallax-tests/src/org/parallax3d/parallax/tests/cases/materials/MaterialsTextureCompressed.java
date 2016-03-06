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

package org.parallax3d.parallax.tests.cases.materials;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.BoxGeometry;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshBasicMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.graphics.textures.CompressedTexture;
import org.parallax3d.parallax.system.gl.enums.TextureMagFilter;
import org.parallax3d.parallax.system.gl.enums.TextureMinFilter;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

import java.util.ArrayList;
import java.util.List;

@ThreejsExample("webgl_materials_texture_compressed")
public final class MaterialsTextureCompressed extends ParallaxTest 
{

	private static final String dxt1_nomip = "textures/compressed/disturb_dxt1_nomip.dds";
	private static final String dxt1_mip = "textures/compressed/disturb_dxt1_mip.dds";
	private static final String dxt3_mip = "textures/compressed/hepatica_dxt3_mip.dds";
	private static final String dxt5_mip = "textures/compressed/explosion_dxt5_mip.dds";

	Scene scene;
	PerspectiveCamera camera;
	List<Mesh> meshes;

	@Override
	public void onStart(RenderingContext context)
	{
		scene = new Scene();
		camera = new PerspectiveCamera(
				50, // fov
				context.getAspectRation(), // aspect
				1, // near
				2000 // far 
		); 
		
		camera.getPosition().setZ(1000);
		
		Geometry geometry = new BoxGeometry( 200, 200, 200 );

		/*
		This is how compressed textures are supposed to be used:

		DXT1 - RGB - opaque textures
		DXT3 - RGBA - transparent textures with sharp alpha transitions
		DXT5 - RGBA - transparent textures with full alpha range
		*/

		CompressedTexture map1 = new CompressedTexture( dxt1_nomip );
		map1.setMinFilter(TextureMinFilter.LINEAR);
		map1.setMagFilter(TextureMagFilter.LINEAR);
		map1.setAnisotropy(4);

		CompressedTexture map2 = new CompressedTexture( dxt1_mip );
		map2.setAnisotropy(4);

		CompressedTexture map3 = new CompressedTexture( dxt3_mip );
		map3.setAnisotropy(4);

		CompressedTexture map4 = new CompressedTexture( dxt5_mip );
		map4.setAnisotropy(4);

		MeshBasicMaterial material1 = new MeshBasicMaterial()
				.setMap(map1);
		MeshBasicMaterial material2 = new MeshBasicMaterial()
				.setMap(map2);
		MeshBasicMaterial material3 = new MeshBasicMaterial()
				.setMap(map3)
				.setAlphaTest(0.3)
				.setSide(Material.SIDE.DOUBLE);
		MeshBasicMaterial material4 = new MeshBasicMaterial()
				.setMap(map4)
				.setSide(Material.SIDE.DOUBLE)
				.setBlending(Material.BLENDING.ADDITIVE)
				.setDepthTest(false)
				.setTransparent(true);

		meshes = new ArrayList<Mesh>();
		Mesh mesh1 = new Mesh( geometry, material1 );
		mesh1.getPosition().setX( -200 );
		mesh1.getPosition().setY( -200 );
		scene.add( mesh1 );
		meshes.add( mesh1 );

		Mesh mesh2 = new Mesh( geometry, material2 );
		mesh2.getPosition().setX( 200 );
		mesh2.getPosition().setY( -200 );
		scene.add( mesh2 );
		meshes.add( mesh2 );

		Mesh mesh3 = new Mesh( geometry, material3 );
		mesh3.getPosition().setX( 200 );
		mesh3.getPosition().setY( 200 );
		scene.add( mesh3 );
		meshes.add( mesh3 );

		Mesh mesh4 = new Mesh( geometry, material4 );
		mesh4.getPosition().setX( -200 );
		mesh4.getPosition().setY( 200 );
		scene.add( mesh4 );
		meshes.add( mesh4 );

	}
	
	@Override
	public void onUpdate(RenderingContext context)
	{
		double time = context.getFrameId() * 0.01;

		for ( int i = 0; i < meshes.size(); i ++ ) 
		{
			Mesh mesh = meshes.get( i );
			mesh.getRotation().setX( time );
			mesh.getRotation().setY( time );
		}
		
		context.getRenderer().render(scene, camera);
	}
	
	@Override
	public String getName() {
		return "Compressed textures";
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getAuthor() {
		return "<a href=\"http://threejs.org\">threejs</a>";
	}
}
