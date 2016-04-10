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

package org.parallax3d.parallax.geometry;

import org.junit.Test;
import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.BufferAttribute;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.extras.geometries.EdgesGeometry;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsTest;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ThreejsTest("EdgesGeometry")
public class EdgesGeometryTest {

    Vector3[] vertList = new Vector3[]{
            new Vector3(0, 0, 0),
            new Vector3(1, 0, 0),
            new Vector3(1, 1, 0),
            new Vector3(0, 1, 0),
            new Vector3(1, 1, 1),
    };

    @Test
    public void testSingularity() {
        testEdges(vertList, new int[]{1, 1, 1}, 0);

    }

    @Test
    public void testNeedle() {
        testEdges(vertList, new int[]{0, 0, 1}, 0);

    }

    @Test
    public void testSingle_triangle() {
        testEdges(vertList, new int[]{0, 1, 2}, 3);

    }

    @Test
    public void testTwo_isolated_triangles() {
        Vector3[] vertList = new Vector3[]{new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(1, 1, 0), new Vector3(0, 0, 1), new Vector3(1, 0, 1), new Vector3(1, 1, 1)};
        testEdges(vertList, new int[]{0, 1, 2, 3, 4, 5}, 6);

    }

    @Test
    public void testTwo_flat_triangles() {
        testEdges(vertList, new int[]{0, 1, 2, 0, 2, 3}, 4);

    }

    @Test
    public void testTwo_flat_triangles_inverted() {
        testEdges(vertList, new int[]{0, 1, 2, 0, 3, 2}, 5);

    }

    @Test
    public void testTwo_non_coplanar_triangles() {
        testEdges(vertList, new int[]{0, 1, 2, 0, 4, 2}, 5);

    }

    @Test
    public void testThree_triangles_coplanar_first() {
        testEdges(vertList, new int[]{0, 1, 2, 0, 2, 3, 0, 4, 2}, 7);

    }

    @Test
    public void testThree_triangles_coplanar_last() {
        testEdges(vertList, new int[]{0, 1, 2, 0, 4, 2, 0, 2, 3}, 6);

    }

    @Test
    public void testTetrahedron() {
        testEdges(vertList, new int[]{0, 1, 2, 0, 1, 4, 0, 4, 2, 1, 2, 4}, 6);

    }

    static void testEdges(Vector3[] vertList, int[] idxList, int numAfter) {

        AbstractGeometry[] geoms = createGeometries(vertList, idxList);

        for (int i = 0; i < geoms.length; i++) {

            AbstractGeometry geom = geoms[i];

            int numBefore = idxList.length;
            assertEquals(countEdges(geom), numBefore);

            EdgesGeometry egeom = new EdgesGeometry(geom);

            assertEquals(countEdges(egeom), numAfter);

        }

    }

    static AbstractGeometry[] createGeometries(Vector3[] vertList, int[] idxList) {

        BufferGeometry geomIB = createIndexedBufferGeometry(vertList, idxList);
        Geometry geom = new Geometry().fromBufferGeometry(geomIB);
        BufferGeometry geomB = new BufferGeometry().fromGeometry(geom);
        BufferGeometry geomDC = addDrawCalls(geomIB.clone());
        return new AbstractGeometry[]{geom, geomB, geomIB, geomDC};

    }

    static BufferGeometry createIndexedBufferGeometry(Vector3[] vertList, int[] idxList) {

        BufferGeometry geom = new BufferGeometry();

        List<Integer> indexTable = new ArrayList<>();
        int numTris = idxList.length / 3;
        int numVerts = 0;

        Uint32Array indices = Uint32Array.create(numTris * 3);
        Float32Array vertices = Float32Array.create(vertList.length * 3);

        for (int i = 0; i < numTris; i++) {

            for (int j = 0; j < 3; j++) {

                int idx = idxList[3 * i + j];
                if (idx > indexTable.size()) {

                    Vector3 v = vertList[idx];
                    vertices.set(3 * numVerts, v.getX());
                    vertices.set(3 * numVerts + 1, v.getY());
                    vertices.set(3 * numVerts + 2, v.getZ());

                    indexTable.add(idx, numVerts);

                    numVerts++;

                }

                indices.set(3 * i + j, indexTable.get(idx));

            }

        }

//        vertices = vertices.subarray( 0, 3 * numVerts );

        geom.setIndex(new BufferAttribute(indices, 1));
        geom.addAttribute("position", new BufferAttribute(vertices, 3));

        geom.computeFaceNormals();

        return geom;

    }

    static BufferGeometry addDrawCalls(BufferGeometry geometry) {

        int numTris = geometry.getIndex().getArray().getLength() / 3;

        int offset = 0;
        for (int i = 0; i < numTris; i++) {

            int start = i * 3;
            int count = 3;

            geometry.addGroup(start, count);
        }

        return geometry;

    }

    static int countEdges(AbstractGeometry geom) {

        if (geom instanceof EdgesGeometry) {

            return ((EdgesGeometry) geom).getAttribute("position").getCount() / 2;

        }

        if (geom instanceof Geometry && ((Geometry) geom).getFaces().size() > 0) {

            return ((Geometry) geom).getFaces().size() * 3;

        }

        if (geom instanceof BufferGeometry && ((BufferGeometry) geom).getIndex() != null) {

            return ((BufferGeometry) geom).getIndex().getArray().getLength();

        }

        return ((BufferGeometry) geom).getAttribute("position").getCount();

    }
}
