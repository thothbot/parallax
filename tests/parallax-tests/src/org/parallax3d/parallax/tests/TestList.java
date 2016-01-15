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

package org.parallax3d.parallax.tests;

import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.tests.geometries.GeometryCube;

import java.util.*;

public class TestList {

    public static final FastMap<List<? extends TestAnimation>> DATA = new FastMap<List<? extends TestAnimation>>(){{
        put("Geometries", Arrays.asList(
                new GeometryCube()
                ,new GeometryCube()
                ,new GeometryCube()
                ,new GeometryCube()
                ,new GeometryCube()
//            , new CopyOfGeometryCube()
//            , new GeometryColors()
//            , new Geometries()
//            , new GeometriesParametric()
//            , new GeometryDynamic()
//            , new GeometryHierarchy()
//            , new Cameras()
//            , new LinesSphere()
//            , new GeometryShapes()
//            , new GeometryExtrudeSplines()
//            , new BufferGeometryDemo()
//            , new BufferGeometryParticles()
//            , new GeometryNormals()
        ));

        put("Interactivity", Arrays.asList(
                new GeometryCube()
//              new InteractiveCubes()
//            , new InteractiveCubesGpu()
//            , new InteractiveDraggableCubes()
//            , new InteractiveVoxelPainter()
        ));

        put("Materials", Arrays.asList(
                new GeometryCube()
//              new MaterialsBumpmap()
//            , new MaterialsBumpmapSkin()
//            , new MaterialsLightmap()
//            , new MaterialsWireframe()
//            , new MaterialsCanvas2D()
//            , new MaterialsTextures()
//            , new MaterialsTextureCompressed()
//            , new MaterialsCubemapFresnel()
//            , new MaterialsCubemapBallsReflection()
//            , new MaterialsCubemapBallsRefraction()
//            , new MaterialsCubemapDynamicReflection()
//            , new MaterialsTextureFilter()
//            , new MaterialsTextureAnisotropy()
//            , new ParticlesTrails()
//            , new ParticlesRandom()
//            , new TrackballEarth()
//            , new MaterialsShaderLava()
//            , new MaterialsShaderMonjori()
//            , new ShaderOcean()
//            , new MaterialsRenderTarget()
        ));

        put("Custom Attributes", Arrays.asList(
                new GeometryCube()
//              new CustomAttributesParticles()
//            , new CustomAttributesParticles2()
        ));

        put("Animation", Arrays.asList(
                new GeometryCube()
//              new ClothSimulation()
//            , new MorphNormalsFlamingo()
//            , new MorphTargetsHorse()
        ));

        put("Loaders", Arrays.asList(
                new GeometryCube()
//              new LoaderCollada()
        ));

        put("Plugins", Arrays.asList(
                new GeometryCube()
//            , new TerrainDynamic()
//            , new HilbertCurves()
//            , new PostprocessingGodrays()
//            , new PostprocessingMulti()
//            , new EffectsLensFlares()
//            , new EffectsSprites()
//            , new Saturn()
        ));

        put("Miscellaneous", Arrays.asList(
                new GeometryCube()
//            , new PerformanceDoubleSided()
//            , new MiscLookAt()
//            , new MiscMemoryTestGeometries()
//            , new MiscMemoryTestShaders()
//            , new LoaderSTL()
//            , new Helpers()
        ));

        put("Raytracing Rendering", Arrays.asList(
                new GeometryCube()
//            , new Raytracing()
        ));
    }};

    private static final FastMap<TestAnimation> contentToken = new FastMap<>();

    static {
        for(Map.Entry<String, List<? extends TestAnimation>> entry: TestList.DATA.entrySet())
        {
            for(TestAnimation animation: entry.getValue())
            {
                contentToken.put(animation.getContentWidgetToken(), animation);
            }
        }
    }

    public static TestAnimation getContentWidgetForToken(String token)
    {
        return contentToken.get(token);
    }
}
