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

package thothbot.parallax.core.shared.objects;

import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLRenderingContext;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.client.gl2.enums.BeginMode;
import thothbot.parallax.core.client.gl2.enums.BufferTarget;
import thothbot.parallax.core.client.gl2.enums.BufferUsage;
import thothbot.parallax.core.client.renderers.WebGLGeometry;
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.renderers.WebGlRendererInfo;
import thothbot.parallax.core.client.shaders.Attribute;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Raycaster;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Ray;
import thothbot.parallax.core.shared.math.Sphere;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.math.Vector4;

/**
 * A line or a series of lines.
 * 
 * @author thothbot
 *
 */
public class Line extends GeometryObject
{
	/**
	 * In OpenGL terms, LineStrip is the classic GL_LINE_STRIP and LinePieces is the equivalent to GL_LINES. 
	 */
	public static enum MODE 
	{
		/**
		 * Will draw a series of segments connecting each point 
		 * (first connected to the second, the second connected 
		 * to the third, and so on and so forth)
		 */
		STRIPS, 
		/**
		 * Will draw a series of pairs of segments (first connected 
		 * to the second, the third connected to the fourth, 
		 * and so on and so forth).
		 */
		PIECES
	};

	private MODE mode;
	
	private static LineBasicMaterial defaultMaterial = new LineBasicMaterial();
	static {
		defaultMaterial.setColor( new Color((int)(Math.random() * 0xffffff)) );
	};

	public Line() {
		this(new Geometry());
	}
	
	public Line(AbstractGeometry geometry) 
	{
		this(geometry, Line.defaultMaterial, Line.MODE.STRIPS);
	}

	public Line(AbstractGeometry geometry, LineBasicMaterial material) 
	{
		this(geometry, material, Line.MODE.STRIPS);
	}

	/**
	 * If no material is supplied, a randomized line material will be created and assigned to the object.
	 * Also, if no type is supplied, the default {@link Line.MODE}.STRIPS will be used).
	 * @param geometry  Vertices representing the line segment(s).
	 * @param material Material for the line. Default is {@link LineBasicMaterial}.
	 * @param mode Connection type between vertices. Default is {@link Line.MODE}.STRIPS.
	 */
	public Line(AbstractGeometry geometry, LineBasicMaterial material, Line.MODE mode) 
	{
		super(geometry, material);

		this.mode = mode;
	}
	
	public MODE getMode() {
		return mode;
	}

	public void setMode(Line.MODE mode)
	{
		this.mode = mode;
	}

	public Line.MODE getType()
	{
		return mode;
	}
	
	public void raycast( Raycaster raycaster, List<Raycaster.Intersect> intersects) {

		Matrix4 inverseMatrix = new Matrix4();
		Ray ray = new Ray();
		Sphere sphere = new Sphere();

		double precision = Raycaster.LINE_PRECISION;
		double precisionSq = precision * precision;

		Geometry geometry = (Geometry) this.getGeometry();

		if ( geometry.getBoundingSphere() == null ) 
			geometry.computeBoundingSphere();

		// Checking boundingSphere distance to ray

		sphere.copy( geometry.getBoundingSphere() );
		sphere.apply( this.matrixWorld );

		if ( raycaster.getRay().isIntersectionSphere( sphere ) == false ) {

			return;

		}

		inverseMatrix.getInverse( this.matrixWorld );
		ray.copy( raycaster.getRay() ).apply( inverseMatrix );

		List<Vector3> vertices = geometry.getVertices();
		int nbVertices = vertices.size();
		Vector3 interSegment = new Vector3();
		Vector3 interRay = new Vector3();
		int step = this.mode == Line.MODE.STRIPS ? 1 : 2;

		for ( int i = 0; i < nbVertices - 1; i = i + step ) {

			double distSq = ray.distanceSqToSegment( vertices.get( i ), vertices.get( i + 1 ), interRay, interSegment );

			if ( distSq > precisionSq ) continue;

			double distance = ray.getOrigin().distanceTo( interRay );

			if ( distance < raycaster.getNear() || distance > raycaster.getFar() ) continue;

			Raycaster.Intersect intersect = new Raycaster.Intersect();
			intersect.distance = distance;
			intersect.point = interSegment.clone().apply( this.matrixWorld );
			intersects.add( intersect );

		}
	}
	
	public Line clone() {
		
		return clone(new Line( (Geometry) this.getGeometry(), (LineBasicMaterial) this.getMaterial(), this.mode ));
				
	}
	
	public Line clone( Line object ) {
		
		super.clone(object);
		
		return object;
		
	}

	@Override
	public void renderBuffer(WebGLRenderer renderer, WebGLGeometry geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGlRendererInfo info = renderer.getInfo();
		
		BeginMode primitives = ( this.getType() == Line.MODE.STRIPS) 
				? BeginMode.LINE_STRIP 
				: BeginMode.LINES;

		setLineWidth( gl, ((LineBasicMaterial)getMaterial()).getLinewidth() );

		gl.drawArrays( primitives, 0, geometryBuffer.__webglLineCount );

		info.getRender().calls ++;
	}
	
//	@Override
//	public void initBuffer(WebGLRenderer renderer)
//	{
//		Geometry geometry = this.getGeometry();
//
//		if( geometry.__webglVertexBuffer == null ) 
//		{
//			createBuffers(renderer, geometry );
//			initBuffers(renderer.getGL(), geometry );
//
//			geometry.setVerticesNeedUpdate(true);
//			geometry.setColorsNeedUpdate(true);
//		}
//	}
	
	public void createBuffers ( WebGLRenderer renderer ) 
	{
		Geometry geometry = (Geometry)getGeometry();
		
		WebGLRenderingContext gl = renderer.getGL();
		WebGlRendererInfo info = renderer.getInfo();
		
		geometry.__webglVertexBuffer = gl.createBuffer();
		geometry.__webglColorBuffer = gl.createBuffer();
		geometry.__webglLineDistanceBuffer = gl.createBuffer();

		info.getMemory().geometries ++;
	}

	public void initBuffers (WebGLRenderingContext gl) 
	{
		Geometry geometry = (Geometry)getGeometry();
		
		int nvertices = geometry.getVertices().size();

		geometry.__vertexArray = Float32Array.create( nvertices * 3 );
		geometry.__colorArray = Float32Array.create( nvertices * 3 );
		geometry.__lineDistanceArray = Float32Array.create( nvertices * 3 );

		geometry.__webglLineCount = nvertices;

		initCustomAttributes ( gl, geometry );
	}

//	@Override
//	public void setBuffer(WebGLRenderer renderer)
//	{
//		WebGLRenderingContext gl = renderer.getGL();
//
//		this.material = Material.getBufferMaterial( this, null );
//
//		boolean areCustomAttributesDirty = material.getShader().areCustomAttributesDirty();
//		if ( this.geometry.isVerticesNeedUpdate() 
//				|| this.geometry.isColorsNeedUpdate() 
//				|| areCustomAttributesDirty 
//		) {
//			this.setBuffers( gl, BufferUsage.DYNAMIC_DRAW );
//
//			this.material.getShader().clearCustomAttributes();
//		}
//
//		this.geometry.setVerticesNeedUpdate(false);
//		this.geometry.setColorsNeedUpdate(false);
//	}

	// setLineBuffers
	public void setBuffers(WebGLRenderingContext gl, BufferUsage hint)
	{		
		Geometry geometry = (Geometry)this.getGeometry();
		
		List<Vector3> vertices = geometry.getVertices();
		List<Color> colors = geometry.getColors();
		List<Double> lineDistances = geometry.getLineDistances();
		
		int vl = vertices.size();
		int cl = colors.size();
		int dl = lineDistances.size();

		Float32Array vertexArray = geometry.__vertexArray;
		Float32Array colorArray = geometry.__colorArray;
		Float32Array lineDistanceArray = geometry.__lineDistanceArray;

		boolean dirtyVertices = geometry.isVerticesNeedUpdate();
		boolean dirtyColors = geometry.isColorsNeedUpdate();
		boolean dirtyLineDistances = geometry.isLineDistancesNeedUpdate();

		List<Attribute> customAttributes = geometry.__webglCustomAttributesList;

		if (dirtyVertices) 
		{
			for (int v = 0; v < vl; v++) 
			{
				Vector3 vertex = vertices.get(v);
				int offset = v * 3;
				vertexArray.set(offset, vertex.getX());
				vertexArray.set(offset + 1, vertex.getY());
				vertexArray.set(offset + 2, vertex.getZ());
			}

			gl.bindBuffer(BufferTarget.ARRAY_BUFFER, geometry.__webglVertexBuffer);
			gl.bufferData(BufferTarget.ARRAY_BUFFER, vertexArray, hint);
		}

		if (dirtyColors) 
		{
			for (int c = 0; c < colors.size(); c++) 
			{
				Color color = colors.get(c);
				int offset = c * 3;

				colorArray.set(offset, color.getR());
				colorArray.set(offset + 1, color.getG());
				colorArray.set(offset + 2, color.getB());
			}

			gl.bindBuffer(BufferTarget.ARRAY_BUFFER, geometry.__webglColorBuffer);
			gl.bufferData(BufferTarget.ARRAY_BUFFER, colorArray, hint);
		}
		
		if ( dirtyLineDistances ) {

			for ( int d = 0; d < dl; d ++ ) {

				lineDistanceArray.set( d, lineDistances.get( d ));

			}

			gl.bindBuffer( BufferTarget.ARRAY_BUFFER, geometry.__webglLineDistanceBuffer );
			gl.bufferData( BufferTarget.ARRAY_BUFFER, lineDistanceArray, hint );

		}

		if (customAttributes != null) 
		{
			for (int i = 0; i < customAttributes.size(); i++) 
			{
				Attribute customAttribute = customAttributes.get(i);

				if (customAttribute.needsUpdate
						&& (customAttribute.getBoundTo() == null 
						|| customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES)) 
				{

					int offset = 0;

					if (customAttribute.size == 1) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++)
							customAttribute.array.set(ca, (Double)customAttribute.getValue().get(ca));

					}
					else if (customAttribute.size == 2) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{

							Vector2 value = (Vector2) customAttribute.getValue().get(ca);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());

							offset += 2;
						}

					} 
					else if (customAttribute.size == 3) 
					{
						if (customAttribute.type == Attribute.TYPE.C) 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{

								Color value = (Color) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getR());
								customAttribute.array.set(offset + 1, value.getG());
								customAttribute.array.set(offset + 2, value.getB());

								offset += 3;
							}

						} 
						else 
						{
							for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
							{
								Vector3 value = (Vector3) customAttribute.getValue().get(ca);

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());

								offset += 3;
							}
						}

					} 
					else if (customAttribute.size == 4) 
					{
						for (int ca = 0; ca < customAttribute.getValue().size(); ca++) 
						{
							Vector4 value = (Vector4) customAttribute.getValue().get(ca);

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());
							customAttribute.array.set(offset + 2, value.getZ());
							customAttribute.array.set(offset + 3, value.getW());

							offset += 4;
						}
					}

					gl.bindBuffer(BufferTarget.ARRAY_BUFFER, customAttribute.buffer);
					gl.bufferData(BufferTarget.ARRAY_BUFFER, customAttribute.array, hint);
				}
			}
		}
	}
}
