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

package org.parallax3d.parallax.tests.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.prefetch.RunAsyncCode;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import org.parallax3d.parallax.tests.TestAnimation;
import org.parallax3d.parallax.tests.geometries.GeometryCube;

/**
 * The {@link TreeViewModel} used by the main menu.
 */
public class DataModel implements TreeViewModel 
{
	/**
	 * The cell used to render categories.
	 */
	private static class CategoryCell extends AbstractCell<Category> 
	{
		@Override
		public void render(Context context, Category value, SafeHtmlBuilder sb) 
		{
			if (value != null)
				sb.appendEscaped(value.getName());
		}
	}

	/**
	 * The cell used to render examples.
	 */
	private static class ContentWidgetCell extends AbstractCell<TestAnimation>
	{    
		@Override
		public void render(Context context, TestAnimation value, SafeHtmlBuilder sb)
		{
			if (value != null) 
			{
				sb.appendHtmlConstant("<img class='menuIcon' src='" + value.getIconUrl() + "'/>");
				sb.appendHtmlConstant("<span>" + value.getName() + "</span>");
			}
		}
	}

	/**
	 * A top level category in the tree.
	 */
	public class Category 
	{
		private final ListDataProvider<TestAnimation> examples = new ListDataProvider<TestAnimation>();
		private final String name;
		private NodeInfo<TestAnimation> nodeInfo;

		public Category(String name) 
		{
			this.name = name;
		}

		public void addExample(TestAnimation example)
		{
			examples.getList().add(example);

			contentCategory.put(example, this);
			contentToken.put(example.getContentWidgetToken(), example);
		}
		
		public ListDataProvider<TestAnimation> getExamples()
		{
			return this.examples;
		}

		public String getName() 
		{
			return name;
		}

		/**
		 * Get the node info for examples under this category.
		 * 
		 * @return the node info
		 */
		public NodeInfo<TestAnimation> getNodeInfo()
		{
			if (nodeInfo == null)
				nodeInfo = new DefaultNodeInfo<TestAnimation>(getExamples(), contentWidgetCell, selectionModel, null);
			
			return nodeInfo;
		}
	}

	/**
	 * The top level categories.
	 */
	private final ListDataProvider<Category> categories = new ListDataProvider<Category>();

	/**
	 * A mapping of {@link ContentWidget}s to their associated categories.
	 */
	private final Map<TestAnimation, Category> contentCategory = new HashMap<TestAnimation, Category>();

	/**
	 * The cell used to render examples.
	 */
	private final ContentWidgetCell contentWidgetCell = new ContentWidgetCell();

	/**
	 * A mapping of history tokens to their associated {@link ContentWidget}.
	 */
	private final Map<String, TestAnimation> contentToken = new HashMap<String, TestAnimation>();

	/**
	 * The selection model used to select examples.
	 */
	private final SelectionModel<TestAnimation> selectionModel;

	public DataModel(SelectionModel<TestAnimation> selectionModel)
	{
		this.selectionModel = selectionModel;
		initializeTree();
	}

	public  SelectionModel<TestAnimation> getSelectionModel()
	{
		return this.selectionModel;
	}

	/**
	 * Get the {@link Category} associated with a widget.
	 * 
	 * @param widget the {@link ContentWidget}
	 * @return the associated {@link Category}
	 */
	public Category getCategoryForContentWidget(ContentWidget widget) 
	{
		return contentCategory.get(widget);
	}

	/**
	 * Get the content widget associated with the specified history token.
	 * 
	 * @param token the history token
	 * @return the associated {@link ContentWidget}
	 */
	public TestAnimation getContentWidgetForToken(String token)
	{
		return contentToken.get(token);
	}

	public <T> NodeInfo<?> getNodeInfo(T value) 
	{
		if (value == null) 
		{
			// Return the top level categories.
			return new DefaultNodeInfo<Category>(categories, new CategoryCell());
		} 
		else if (value instanceof Category) 
		{
			// Return the examples within the category.
			Category category = (Category) value;
			return category.getNodeInfo();
		}
		return null;
	}

	public boolean isLeaf(Object value) 
	{
		return value != null && !(value instanceof Category);
	}

	public List<Category> getCategories()
	{
		return categories.getList();
	}
	/**
	 * Get the set of all {@link ContentWidget}s used in the model.
	 * 
	 * @return the {@link ContentWidget}s
	 */
	public Set<TestAnimation> getAllContentWidgets()
	{
		Set<TestAnimation> widgets = new HashSet<TestAnimation>();
		for (Category category : getCategories()) 
		{
			for (TestAnimation example : category.examples.getList())
				widgets.add(example);
		}

		return widgets;
	}

	/**
	 * Initialize the tree.
	 */
	private void initializeTree() 
	{
		List<Category> categoriesList = categories.getList();

		// Geometries.
		{
			Category category = new Category("Geometries");
			categoriesList.add(category);
			category.addExample(new GeometryCube());

//			category.addExample(new CopyOfGeometryCube(), 
//					RunAsyncCode.runAsyncCode(CopyOfGeometryCube.class));
//			category.addExample(new GeometryCube(),
//					RunAsyncCode.runAsyncCode(GeometryCube.class));
//			category.addExample(new GeometryColors(),
//					RunAsyncCode.runAsyncCode(GeometryColors.class));
//			category.addExample(new Geometries(),
//					RunAsyncCode.runAsyncCode(Geometries.class));
//			category.addExample(new GeometriesParametric(),
//					RunAsyncCode.runAsyncCode(GeometriesParametric.class));
//			category.addExample(new GeometryDynamic(),
//					RunAsyncCode.runAsyncCode(GeometryDynamic.class));
//			category.addExample(new GeometryHierarchy(),
//					RunAsyncCode.runAsyncCode(GeometryHierarchy.class));
//			category.addExample(new Cameras(),
//					RunAsyncCode.runAsyncCode(Cameras.class));
//			category.addExample(new LinesSphere(),
//					RunAsyncCode.runAsyncCode(LinesSphere.class));
//			category.addExample(new GeometryShapes(),
//					RunAsyncCode.runAsyncCode(GeometryShapes.class));
//			category.addExample(new GeometryExtrudeSplines(),
//					RunAsyncCode.runAsyncCode(GeometryExtrudeSplines.class));
//			category.addExample(new BufferGeometryDemo(),
//					RunAsyncCode.runAsyncCode(BufferGeometryDemo.class));
//			category.addExample(new BufferGeometryParticles(),
//					RunAsyncCode.runAsyncCode(BufferGeometryParticles.class));
//			category.addExample(new GeometryNormals(),
//					RunAsyncCode.runAsyncCode(GeometryNormals.class));
		}
		
		// Interactivity 
		{
			Category category = new Category("Interactivity");
			categoriesList.add(category);
//			category.addExample(new InteractiveCubes(),
//					RunAsyncCode.runAsyncCode(InteractiveCubes.class));
//			category.addExample(new InteractiveCubesGpu(),
//					RunAsyncCode.runAsyncCode(InteractiveCubesGpu.class));
//			category.addExample(new InteractiveDraggableCubes(),
//					RunAsyncCode.runAsyncCode(InteractiveDraggableCubes.class));
//			category.addExample(new InteractiveVoxelPainter(),
//					RunAsyncCode.runAsyncCode(InteractiveVoxelPainter.class));
		}

		// Materials
		{
			Category category = new Category("Materials");
			categoriesList.add(category);
//			category.addExample(new MaterialsBumpmap(),
//					RunAsyncCode.runAsyncCode(MaterialsBumpmap.class));
//			category.addExample(new MaterialsBumpmapSkin(),
//					RunAsyncCode.runAsyncCode(MaterialsBumpmapSkin.class));
//			category.addExample(new MaterialsLightmap(),
//					RunAsyncCode.runAsyncCode(MaterialsLightmap.class));
//			category.addExample(new MaterialsWireframe(),
//					RunAsyncCode.runAsyncCode(MaterialsWireframe.class));
//			category.addExample(new MaterialsCanvas2D(),
//					RunAsyncCode.runAsyncCode(MaterialsCanvas2D.class));
//			category.addExample(new MaterialsTextures(),
//					RunAsyncCode.runAsyncCode(MaterialsTextures.class));
//			category.addExample(new MaterialsTextureCompressed(),
//					RunAsyncCode.runAsyncCode(MaterialsTextureCompressed.class));
//			category.addExample(new MaterialsCubemapFresnel(),
//					RunAsyncCode.runAsyncCode(MaterialsCubemapFresnel.class));
//			category.addExample(new MaterialsCubemapBallsReflection(),
//					RunAsyncCode.runAsyncCode(MaterialsCubemapBallsReflection.class));
//			category.addExample(new MaterialsCubemapBallsRefraction(),
//					RunAsyncCode.runAsyncCode(MaterialsCubemapBallsRefraction.class));
//			category.addExample(new MaterialsCubemapDynamicReflection(),
//					RunAsyncCode.runAsyncCode(MaterialsCubemapDynamicReflection.class));
//			category.addExample(new MaterialsTextureFilter(),
//					RunAsyncCode.runAsyncCode(MaterialsTextureFilter.class));
//			category.addExample(new MaterialsTextureAnisotropy(),
//					RunAsyncCode.runAsyncCode(MaterialsTextureAnisotropy.class));
//			category.addExample(new ParticlesTrails(),
//					RunAsyncCode.runAsyncCode(ParticlesTrails.class));
//			category.addExample(new ParticlesRandom(),
//					RunAsyncCode.runAsyncCode(ParticlesRandom.class));
//			category.addExample(new TrackballEarth(),
//					RunAsyncCode.runAsyncCode(TrackballEarth.class));
//			category.addExample(new MaterialsShaderLava(),
//					RunAsyncCode.runAsyncCode(MaterialsShaderLava.class));
//			category.addExample(new MaterialsShaderMonjori(),
//					RunAsyncCode.runAsyncCode(MaterialsShaderMonjori.class));
//			category.addExample(new ShaderOcean(),
//					RunAsyncCode.runAsyncCode(ShaderOcean.class));
//			category.addExample(new MaterialsRenderTarget(),
//					RunAsyncCode.runAsyncCode(MaterialsRenderTarget.class));
		}

		// Custom Attributes
		{
			Category category = new Category("Custom Attributes");
			categoriesList.add(category);
//			category.addExample(new CustomAttributesParticles(),
//					RunAsyncCode.runAsyncCode(CustomAttributesParticles.class));
//			category.addExample(new CustomAttributesParticles2(),
//					RunAsyncCode.runAsyncCode(CustomAttributesParticles2.class));
		}
		
		// Animation
		{
			Category category = new Category("Animation");
			categoriesList.add(category);
//			category.addExample(new ClothSimulation(),
//					RunAsyncCode.runAsyncCode(ClothSimulation.class));
//			category.addExample(new MorphNormalsFlamingo(),
//					RunAsyncCode.runAsyncCode(MorphNormalsFlamingo.class));
//			category.addExample(new MorphTargetsHorse(),
//					RunAsyncCode.runAsyncCode(MorphTargetsHorse.class));
		}
		
		// Loaders
//		{
//			Category category = new Category("Loaders");
//			categoriesList.add(category);
//			category.addExample(new LoaderCollada(),
//					RunAsyncCode.runAsyncCode(LoaderCollada.class));
//		}
		
		// Plugins
		{
			Category category = new Category("Plugins");
			categoriesList.add(category);
//			category.addExample(new TerrainDynamic(),
//					RunAsyncCode.runAsyncCode(TerrainDynamic.class));
//			category.addExample(new HilbertCurves(),
//					RunAsyncCode.runAsyncCode(HilbertCurves.class));
//			category.addExample(new PostprocessingGodrays(),
//					RunAsyncCode.runAsyncCode(PostprocessingGodrays.class));
//			category.addExample(new PostprocessingMulti(),
//					RunAsyncCode.runAsyncCode(PostprocessingMulti.class));
//			category.addExample(new EffectsLensFlares(),
//					RunAsyncCode.runAsyncCode(EffectsLensFlares.class));
//			category.addExample(new EffectsSprites(),
//					RunAsyncCode.runAsyncCode(EffectsSprites.class));
//			category.addExample(new Saturn(),
//					RunAsyncCode.runAsyncCode(Saturn.class));

		}

		// Miscellaneous
		{
			Category category = new Category("Miscellaneous");
			categoriesList.add(category);
//			category.addExample(new PerformanceDoubleSided(),
//					RunAsyncCode.runAsyncCode(PerformanceDoubleSided.class));
//			category.addExample(new MiscLookAt(),
//					RunAsyncCode.runAsyncCode(MiscLookAt.class));
//			category.addExample(new MiscMemoryTestGeometries(),
//					RunAsyncCode.runAsyncCode(MiscMemoryTestGeometries.class));
//			category.addExample(new MiscMemoryTestShaders(),
//					RunAsyncCode.runAsyncCode(MiscMemoryTestShaders.class));
//			category.addExample(new LoaderSTL(),
//					RunAsyncCode.runAsyncCode(LoaderSTL.class));
//			category.addExample(new Helpers(),
//					RunAsyncCode.runAsyncCode(Helpers.class));
		}
		
		// Raytracing Rendering
		{
			Category category = new Category("Raytracing Rendering");
			categoriesList.add(category);
//			category.addExample(new Raytracing(),
//					RunAsyncCode.runAsyncCode(Raytracing.class));
		}
	}
}
