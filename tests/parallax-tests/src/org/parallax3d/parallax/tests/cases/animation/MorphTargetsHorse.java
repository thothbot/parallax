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

package org.parallax3d.parallax.tests.cases.animation;

import org.parallax3d.parallax.RenderingContext;
import org.parallax3d.parallax.animation.AnimationClip;
import org.parallax3d.parallax.animation.AnimationMixer;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.lights.DirectionalLight;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.materials.MeshLambertMaterial;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.graphics.scenes.Scene;
import org.parallax3d.parallax.loaders.JsonLoader;
import org.parallax3d.parallax.loaders.Loader;
import org.parallax3d.parallax.loaders.ModelLoadHandler;
import org.parallax3d.parallax.math.Mathematics;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.tests.ParallaxTest;
import org.parallax3d.parallax.tests.ThreejsExample;

@ThreejsExample("webgl_morphtargets_horse")
public final class MorphTargetsHorse extends ParallaxTest {

    private static final String model = "models/animated/horse.js";

    Scene scene;
    PerspectiveCamera camera;
    Mesh mesh;
    Vector3 target = new Vector3(0, 150, 0);

    AnimationMixer mixer;

    int radius = 600;
    double theta = 0;

    @Override
    public void onStart(RenderingContext context) {
        scene = new Scene();
        camera = new PerspectiveCamera(
                50, // fov
                context.getAspectRation(), // aspect
                1, // near
                10000 // far
        );

        camera.getPosition().setY(300);

        DirectionalLight light = new DirectionalLight(0xefefff, 1.5);
        light.getPosition().set(1, 1, 1).normalize();
        scene.add(light);

        DirectionalLight light1 = new DirectionalLight(0xffefef, 1.5);
        light1.getPosition().set(-1, -1, -1).normalize();
        scene.add(light1);

        new JsonLoader(model, new ModelLoadHandler() {

            @Override
            public void onModelLoaded(Loader loader, AbstractGeometry geometry) {

                mesh = new Mesh(geometry, new MeshLambertMaterial()
                        .setVertexColors(Material.COLORS.FACE)
                        .setMorphTargets(true));

                mesh.getScale().set(1.5, 1.5, 1.5);
                scene.add(mesh);

                mixer = new AnimationMixer(mesh);

                AnimationClip clip = AnimationClip.CreateFromMorphTargetSequence("gallop", geometry.morphTargets, 30);

                mixer.clipAction(clip).setDuration(1).play();
            }
        });

        context.getRenderer().setClearColor(0xf0f0f0);
    }

    @Override
    public void onUpdate(RenderingContext context) {
        theta += 0.1;

        camera.getPosition().setX(radius * Math.sin(Mathematics.degToRad(theta)));
        camera.getPosition().setZ(radius * Math.cos(Mathematics.degToRad(theta)));

        camera.lookAt(target);

        if (mixer != null) {

            mixer.update(context.getDeltaTime() * 0.001);

        }

        context.getRenderer().render(scene, camera);
    }

    @Override
    public String getName() {
        return "Morph targets: horse";
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
