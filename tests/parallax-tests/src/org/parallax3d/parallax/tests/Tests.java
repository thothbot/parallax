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
import org.parallax3d.parallax.tests.cases.animation.*;
import org.parallax3d.parallax.tests.cases.geometries.*;
import org.parallax3d.parallax.tests.cases.materials.*;

import java.util.*;

public class Tests {

    public static final FastMap<List<? extends ParallaxTest>> DATA = new FastMap<List<? extends ParallaxTest>>(){{
        put("Geometries", Arrays.asList(
              new GeometryCube()
            , new GeometryColors()
            , new Geometries()
            , new GeometriesParametric()
            , new GeometryDynamic()
            , new GeometryHierarchy()
            , new Cameras()
            , new LinesSphere()
            , new GeometryShapes()
            , new GeometryExtrudeSplines()
            , new BufferGeometryDemo()
            , new BufferGeometryParticles()
            , new GeometryNormals()
        ));

//        put("Interactivity", Arrays.asList(
////              new InteractiveCubes()
////            , new InteractiveCubesGpu()
////            , new InteractiveDraggableCubes()
////            , new InteractiveVoxelPainter()
//        ));

        put("Materials", Arrays.asList(
              new MaterialsBumpmap()
            , new MaterialsBumpmapSkin()
            , new MaterialsLightmap()
//            , new MaterialsWireframe()
            , new MaterialsCanvas2D()
            , new MaterialsTextures()
            , new MaterialsTextureCompressed()
            , new MaterialsCubemapFresnel()
            , new MaterialsCubemapBallsReflection()
            , new MaterialsCubemapBallsRefraction()
            , new MaterialsCubemapDynamicReflection()
            , new MaterialsTextureFilter()
            , new MaterialsTextureAnisotropy()
//            , new ParticlesTrails()
//            , new ParticlesRandom()
//            , new TrackballEarth()
            , new MaterialsShaderLava()
            , new MaterialsShaderMonjori()
//            , new ShaderOcean()
            , new MaterialsRenderTarget()
        ));

        put("Animation", Arrays.asList(
//              new ClothSimulation()
              new MorphNormalsFlamingo()
            , new MorphTargetsHorse()
        ));

//        put("Custom Attributes", Arrays.asList(
////              new CustomAttributesParticles()
////            , new CustomAttributesParticles2()
//        ));

//        put("Loaders", Arrays.asList(
////              new LoaderCollada()
//        ));

//        put("Plugins", Arrays.asList(
////            , new TerrainDynamic()
////            , new HilbertCurves()
////            , new PostprocessingGodrays()
////            , new PostprocessingMulti()
////            , new EffectsLensFlares()
////            , new EffectsSprites()
////            , new Saturn()
//        ));

//        put("Miscellaneous", Arrays.asList(
////            , new PerformanceDoubleSided()
////            , new MiscLookAt()
////            , new MiscMemoryTestGeometries()
////            , new MiscMemoryTestShaders()
////            , new LoaderSTL()
////            , new Helpers()
//        ));

//        put("Raytracing Rendering", Arrays.asList(
////            , new Raytracing()
//        ));
    }};

    private static final FastMap<ParallaxTest> contentToken = new FastMap<>();

    static {
        for(Map.Entry<String, List<? extends ParallaxTest>> entry: Tests.DATA.entrySet())
        {
            for(ParallaxTest animation: entry.getValue())
            {
                contentToken.put(animation.getContentWidgetToken(), animation);
            }
        }
    }

    public static ParallaxTest getContentWidgetForToken(String token)
    {
        return contentToken.get(token);
    }
}
