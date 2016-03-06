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

package org.parallax3d.parallax.tests.cases.misc;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.extras.helpers.*;
import org.parallax3d.parallax.graphics.lights.PointLight;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_helpers")
public final class Helpers extends ParallaxTest {

    private static final String model = "models/obj/leeperrysmith/LeePerrySmith.js";

    Scene scene;
    PerspectiveCamera camera;
    PointLight light;

    @Override
    public void onStart(RenderingContext context) {
        scene = new Scene();
        camera = new PerspectiveCamera(
                70, // fov
                context.getAspectRation(), // aspect
                1, // near
                1000 // far 
        );

        camera.getPosition().setZ(400);

        light = new PointLight(0xffffff);
        light.getPosition().set(200, 100, 150);
        scene.add(light);

        scene.add(new PointLightHelper(light, 5.0));

        GridHelper helper = new GridHelper(200, 10);
        helper.setColors(0x0000ff, 0x808080);
        helper.getPosition().setY(-150);
        scene.add(helper);

        new JsonLoader(model, new ModelLoadHandler() {

            @Override
            public void onModelLoaded(Loader loader, AbstractGeometry geometry) {
                MeshLambertMaterial material = new MeshLambertMaterial();

                Mesh mesh = new Mesh(geometry, material);
                mesh.getScale().multiply(50);
                scene.add(mesh);

                scene.add(new FaceNormalsHelper(mesh, 10));
                scene.add(new VertexNormalsHelper(mesh, 10));

                WireframeHelper helper = new WireframeHelper(mesh);
                helper.getMaterial().setDepthTest(false);
                helper.getMaterial().setOpacity(0.25);
                helper.getMaterial().setTransparent(true);
                scene.add(helper);

                scene.add(new BoxHelper(mesh));

            }
        });

    }

    @Override
    public void onUpdate(RenderingContext context) {

        double time = context.getFrameId() * 0.003;
        camera.getPosition().setX(400 * Math.cos(time));
        camera.getPosition().setZ(400 * Math.sin(time));
        camera.lookAt(scene.getPosition());

        light.getPosition().setX(Math.sin(time * 1.7) * 300);
        light.getPosition().setY(Math.cos(time * 1.5) * 400);
        light.getPosition().setZ(Math.cos(time * 1.3) * 300);

        context.getRenderer().render(scene, camera);
    }

    @Override
    public String getName() {
        return "Helpers";
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
