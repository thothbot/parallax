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
package org.parallax3d.parallax.graphics.core.geometry;

import org.parallax3d.parallax.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class MorphNormal {

    List<Vector3> faceNormals = new ArrayList<>();
    List<VertextNormal> vertexNormals = new ArrayList<>();

    public List<Vector3> getFaceNormals() {
        return faceNormals;
    }

    public void setFaceNormals(List<Vector3> faceNormals) {
        this.faceNormals = faceNormals;
    }

    public List<VertextNormal> getVertexNormals() {
        return vertexNormals;
    }

    public void setVertexNormals(List<VertextNormal> vertexNormals) {
        this.vertexNormals = vertexNormals;
    }
}
