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
import thothbot.parallax.core.client.renderers.WebGLRenderer;
import thothbot.parallax.core.client.renderers.WebGlRendererInfo;
import thothbot.parallax.core.shared.core.AbstractGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry;
import thothbot.parallax.core.shared.core.BufferGeometry.DrawCall;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.core.Raycaster;
import thothbot.parallax.core.shared.core.Raycaster.Intersect;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.materials.PointCloudMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Ray;
import thothbot.parallax.core.shared.math.Vector3;

public class PointCloud extends GeometryObject
{
	public static double RAYCASTER_THRESHOLD = 1.0;
	
	private boolean sortParticles = false;
	
	private static PointCloudMaterial defaultMaterial = new PointCloudMaterial();
	static {
		defaultMaterial.setColor( new Color((int)Math.random() * 0xffffff) );
	};
	
	public PointCloud() {
		this(new Geometry());
	}
	
	public PointCloud(AbstractGeometry geometry) 
	{
		this(geometry, PointCloud.defaultMaterial);
	}

	public PointCloud(AbstractGeometry geometry, Material material) 
	{
		super(geometry, material);
	}
	
	@Override
	public void raycast(Raycaster raycaster, List<Intersect> intersects) {
		Matrix4 inverseMatrix = new Matrix4();
		Ray ray = new Ray();

		AbstractGeometry geometry = getGeometry();

		inverseMatrix.getInverse( this.matrixWorld );
		ray.copy( raycaster.getRay() ).apply( inverseMatrix );

		if ( geometry.getBoundingBox() != null ) {

			if ( ray.isIntersectionBox( geometry.getBoundingBox() ) == false ) {

				return;

			}

		}

		Vector3 position = new Vector3();

		if ( geometry instanceof BufferGeometry ) {

			BufferGeometry bGeometry = (BufferGeometry)geometry;
			Float32Array positions = bGeometry.getAttribute("position").getArray();

			if ( bGeometry.getAttribute("index") != null ) {

				Float32Array indices = bGeometry.getAttribute("index").getArray();
				List<DrawCall> offsets = bGeometry.getOffsets();

				if ( offsets.size() == 0 ) {

					BufferGeometry.DrawCall offset = new BufferGeometry.DrawCall(0, indices.getLength(), 0 );

					offsets.add( offset );

				}

				for ( int oi = 0, ol = offsets.size(); oi < ol; ++oi ) {

					int start = offsets.get( oi ).start;
					int count = offsets.get( oi ).count;
					int index = offsets.get( oi ).index;

					for ( int i = start, il = start + count; i < il; i ++ ) {

						int a = index + (int)indices.get( i );

						position.fromArray( positions, a * 3 );

						testPoint( raycaster, intersects, ray, position, a );

					}

				}

			} else {

				int pointCount = positions.getLength() / 3;

				for ( int i = 0; i < pointCount; i ++ ) {

					position.set(
						positions.get( 3 * i ),
						positions.get( 3 * i + 1 ),
						positions.get( 3 * i + 2 )
					);

					testPoint( raycaster, intersects, ray, position, i );

				}

			}

		} else {

			List<Vector3> vertices = ((Geometry)this.getGeometry()).getVertices();

			for ( int i = 0; i < vertices.size(); i ++ ) {

				testPoint( raycaster, intersects, ray, vertices.get( i ), i );

			}

		}
	}
	
	private void testPoint(Raycaster raycaster, List<Intersect> intersects, Ray ray, Vector3 point, int index ) {

		double rayPointDistance = ray.distanceToPoint( point );
		double localThreshold = RAYCASTER_THRESHOLD / ( ( this.scale.getX() + this.scale.getY() + this.scale.getZ() ) / 3.0 );

		if ( rayPointDistance < localThreshold ) {

			Vector3 intersectPoint = ray.closestPointToPoint( point );
			intersectPoint.apply( this.getMatrixWorld() );

			double distance = raycaster.getRay().getOrigin().distanceTo( intersectPoint );

			Raycaster.Intersect intersect = new Raycaster.Intersect();
			intersect.distance = distance;
			intersect.distanceToRay = rayPointDistance;
			intersect.point = intersectPoint.clone();
			intersect.object = this;
			intersects.add( intersect );			
		}
	}
	
	public PointCloud clone() {
		return clone(new PointCloud( (Geometry) this.getGeometry(), (PointCloudMaterial) this.getMaterial() ));
	}
	
	public PointCloud clone( PointCloud object ) {
		
		super.clone(object);

		object.sortParticles = this.sortParticles;

		return object;

	}
	
	@Override
	public void renderBuffer(WebGLRenderer renderer, Geometry geometryBuffer, boolean updateBuffers)
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGlRendererInfo info = renderer.getInfo();
		

		gl.drawArrays( BeginMode.POINTS, 0, geometryBuffer.__webglParticleCount );

		info.getRender().calls ++;
		info.getRender().points += geometryBuffer.__webglParticleCount;
	}
	
	private void initBuffers (WebGLRenderingContext gl, Geometry geometry) 
	{
		int nvertices = geometry.getVertices().size();

		geometry.__vertexArray = Float32Array.create( nvertices * 3 );
		geometry.__colorArray = Float32Array.create( nvertices * 3 );
	

		geometry.__webglLineCount = nvertices;

		initCustomAttributes ( gl, geometry );
	}
	
	private void createBuffers ( WebGLRenderer renderer, Geometry geometry ) 
	{
		WebGLRenderingContext gl = renderer.getGL();
		WebGlRendererInfo info = renderer.getInfo();
		
		geometry.__webglVertexBuffer = gl.createBuffer();
		geometry.__webglColorBuffer = gl.createBuffer();

		info.getMemory().geometries ++;
	}

}
