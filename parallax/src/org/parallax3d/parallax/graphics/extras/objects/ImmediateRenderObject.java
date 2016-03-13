/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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
package org.parallax3d.parallax.graphics.extras.objects;

import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.core.Raycaster;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.List;

/**
 * @author alteredq / http://alteredqualia.com/
 */
@ThreejsObject("THREE.ImmediateRenderObject")
public class ImmediateRenderObject extends Object3D {

    Material material;

    public ImmediateRenderObject(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
