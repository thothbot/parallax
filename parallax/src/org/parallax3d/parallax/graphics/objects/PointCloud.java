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

package org.parallax3d.parallax.graphics.objects;

import java.util.List;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.graphics.materials.PointCloudMaterial;
import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.graphics.renderers.shaders.Attribute;
import org.parallax3d.parallax.math.Ray;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.graphics.renderers.GLGeometry;
import org.parallax3d.parallax.graphics.renderers.GLRenderer;
import org.parallax3d.parallax.graphics.core.BufferGeometry.DrawCall;
import org.parallax3d.parallax.graphics.materials.Material;
import org.parallax3d.parallax.math.*;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.math.Vector2;
import org.parallax3d.parallax.math.Color;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;
import org.parallax3d.parallax.system.gl.arrays.Uint16Array;
import org.parallax3d.parallax.system.gl.enums.BeginMode;
import org.parallax3d.parallax.system.gl.enums.BufferTarget;
import org.parallax3d.parallax.system.gl.enums.BufferUsage;

public class PointCloud extends GeometryObject
{
	public static double RAYCASTER_THRESHOLD = 1.0;

	private boolean sortParticles = false;

	private static PointCloudMaterial defaultMaterial = new PointCloudMaterial();
	static {
		defaultMaterial.setColor( new Color((int)(Math.random() * 0xffffff)) );
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

	public boolean isSortParticles() {
		return sortParticles;
	}

	public void setSortParticles(boolean sortParticles) {
		this.sortParticles = sortParticles;
	}

	@Override
	public void raycast(Raycaster raycaster, List<Raycaster.Intersect> intersects) {
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
			Float32Array positions = (Float32Array)bGeometry.getAttribute("position").getArray();

			if ( bGeometry.getAttribute("index") != null ) {

				Uint16Array indices = (Uint16Array)bGeometry.getAttribute("index").getArray();
				List<DrawCall> offsets = bGeometry.getDrawcalls();

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

	private void testPoint(Raycaster raycaster, List<Raycaster.Intersect> intersects, Ray ray, Vector3 point, int index ) {

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
		return clone(new PointCloud( this.getGeometry(), this.getMaterial() ));
	}

	public PointCloud clone( PointCloud object ) {

		super.clone(object);

		object.sortParticles = this.sortParticles;

		return object;

	}

	@Override
	public void renderBuffer(GLRenderer renderer, GLGeometry geometryBuffer, boolean updateBuffers)
	{
		GLRendererInfo info = renderer.getInfo();

		renderer.gl.glDrawArrays(BeginMode.POINTS.getValue(), 0, geometryBuffer.__webglParticleCount);

		info.getRender().calls ++;
		info.getRender().points += geometryBuffer.__webglParticleCount;
	}

	public void initBuffers(GL20 gl)
	{
		Geometry geometry = (Geometry)getGeometry();
		int nvertices = geometry.getVertices().size();

		geometry.__vertexArray = Float32Array.create(nvertices * 3);
		geometry.__colorArray = Float32Array.create(nvertices * 3);

		geometry.__webglParticleCount = nvertices;

		initCustomAttributes ( gl, geometry );
	}

	public void createBuffers ( GLRenderer renderer)
	{
		Geometry geometry = (Geometry)getGeometry();
		GLRendererInfo info = renderer.getInfo();

		geometry.__webglVertexBuffer = renderer.gl.glGenBuffer();
		geometry.__webglColorBuffer = renderer.gl.glGenBuffer();

		info.getMemory().geometries ++;
	}

	public void setBuffers(GLRenderer renderer, BufferUsage hint)
	{
		Geometry geometry = (Geometry)getGeometry();

		List<Vector3> vertices = geometry.getVertices();
		int vl = vertices.size();

		List<Color> colors = geometry.getColors();
		int cl = colors.size();

		Float32Array vertexArray = geometry.__vertexArray;
		Float32Array colorArray = geometry.__colorArray;

//		Float32Array sortArray = geometry.__sortArray;

		boolean dirtyVertices = geometry.isVerticesNeedUpdate();
		boolean dirtyElements = geometry.isElementsNeedUpdate();
		boolean dirtyColors = geometry.isColorsNeedUpdate();

		List<Attribute> customAttributes = geometry.__webglCustomAttributesList;

		if ( this.sortParticles ) {

			renderer._projScreenMatrixPS.copy( renderer._projScreenMatrix );
			renderer._projScreenMatrixPS.multiply( getMatrixWorld() );

			for ( int v = 0; v < vl; v ++ ) {

				Vector3 vertex = vertices.get( v );

				renderer._vector3.copy( vertex );
				renderer._vector3.applyProjection( renderer._projScreenMatrixPS );

//				sort, v ] = [ _vector3.z, v ];

			}

//			sortArray.sort( numericalSort );

			for ( int v = 0; v < vl; v ++ ) {

//				vertex = vertices[ sortArray[ v ][ 1 ] ];
				Vector3 vertex = vertices.get( v );

				int offset = v * 3;

				vertexArray.set(offset, vertex.getX());
				vertexArray.set(offset + 1, vertex.getY());
				vertexArray.set(offset + 2, vertex.getZ());

			}

			for ( int c = 0; c < cl; c ++ ) {

				int offset = c * 3;

//				Color color = colors[ sortArray[ c ][ 1 ] ];
				Color color = colors.get(c);

				colorArray.set(offset, color.getR());
				colorArray.set(offset + 1, color.getG());
				colorArray.set(offset + 2, color.getB());

			}

			if ( customAttributes != null ) {

				for ( int i = 0, il = customAttributes.size(); i < il; i ++ ) {

					Attribute customAttribute = customAttributes.get( i );

					if ( ! ( customAttribute.getBoundTo() == null || customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES ) ) continue;

					int offset = 0;

					int cal = customAttribute.getValue().size();

					if ( customAttribute.size == 1 ) {

						for ( int ca = 0; ca < cal; ca ++ ) {

//							index = sortArray[ ca ][ 1 ];
							int index = ca;

							customAttribute.array.set(ca, (Double) customAttribute.getValue().get(index));

						}

					} else if ( customAttribute.size == 2 ) {

						for ( int ca = 0; ca < cal; ca ++ ) {

//							index = sortArray[ ca ][ 1 ];
							int index = ca;

							Vector2 value = (Vector2)customAttribute.getValue().get( index );

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());

							offset += 2;

						}

					} else if ( customAttribute.size == 3 ) {

						if ( customAttribute.type == Attribute.TYPE.C) {

							for ( int ca = 0; ca < cal; ca ++ ) {

//								index = sortArray[ ca ][ 1 ];
								int index = ca;

								Color value = (Color)customAttribute.getValue().get( index );

								customAttribute.array.set(offset, value.getR());
								customAttribute.array.set(offset + 1, value.getG());
								customAttribute.array.set(offset + 2, value.getB());

								offset += 3;

							}

						} else {

							for ( int ca = 0; ca < cal; ca ++ ) {

//								index = sortArray[ ca ][ 1 ];
								int index = ca;

								Vector3 value = (Vector3)customAttribute.getValue().get( index );

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());

								offset += 3;

							}

						}

					} else if ( customAttribute.size == 4 ) {

						for ( int ca = 0; ca < cal; ca ++ ) {

//							index = sortArray[ ca ][ 1 ];
							int index = ca;

							Vector4 value = (Vector4)customAttribute.getValue().get( index );

							customAttribute.array.set(offset, value.getX());
							customAttribute.array.set(offset + 1, value.getY());
							customAttribute.array.set(offset + 2, value.getZ());
							customAttribute.array.set(offset + 3, value.getW());

							offset += 4;

						}

					}

				}

			}

		} else {

			if ( dirtyVertices ) {

				for ( int v = 0; v < vl; v ++ ) {

					Vector3 vertex = vertices.get( v );

					int offset = v * 3;

					vertexArray.set(offset, vertex.getX());
					vertexArray.set(offset + 1, vertex.getY());
					vertexArray.set(offset + 2, vertex.getZ());

				}

			}

			if ( dirtyColors ) {

				for ( int c = 0; c < cl; c ++ ) {

					Color color = colors.get( c );

					int offset = c * 3;

					colorArray.set(offset, color.getR());
					colorArray.set(offset + 1, color.getG());
					colorArray.set(offset + 2, color.getB());

				}

			}

			if ( customAttributes != null ) {

				for ( int i = 0, il = customAttributes.size(); i < il; i ++ ) {

					Attribute customAttribute = customAttributes.get( i );

					if ( customAttribute.needsUpdate &&
						 ( customAttribute.getBoundTo() == null ||
							 customAttribute.getBoundTo() == Attribute.BOUND_TO.VERTICES ) ) {

						int cal = customAttribute.getValue().size();

						int offset = 0;

						if ( customAttribute.size == 1 ) {

							for ( int ca = 0; ca < cal; ca ++ ) {

								customAttribute.array.set(ca, (Double) customAttribute.getValue().get(ca));

							}

						} else if ( customAttribute.size == 2 ) {

							for ( int ca = 0; ca < cal; ca ++ ) {

								Vector2 value = (Vector2)customAttribute.getValue().get( ca );

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());

								offset += 2;

							}

						} else if ( customAttribute.size == 3 ) {

							if ( customAttribute.type == Attribute.TYPE.C ) {

								for ( int ca = 0; ca < cal; ca ++ ) {

									Color value = (Color)customAttribute.getValue().get( ca );

									customAttribute.array.set(offset, value.getR());
									customAttribute.array.set(offset + 1, value.getG());
									customAttribute.array.set(offset + 2, value.getB());

									offset += 3;

								}

							} else {

								for ( int ca = 0; ca < cal; ca ++ ) {

									Vector3 value = (Vector3)customAttribute.getValue().get( ca );

									customAttribute.array.set(offset, value.getX());
									customAttribute.array.set(offset + 1, value.getY());
									customAttribute.array.set(offset + 2, value.getZ());

									offset += 3;

								}

							}

						} else if ( customAttribute.size == 4 ) {

							for ( int ca = 0; ca < cal; ca ++ ) {

								Vector4 value = (Vector4)customAttribute.getValue().get( ca );

								customAttribute.array.set(offset, value.getX());
								customAttribute.array.set(offset + 1, value.getY());
								customAttribute.array.set(offset + 2, value.getZ());
								customAttribute.array.set(offset + 3, value.getW());

								offset += 4;

							}

						}

					}

				}

			}

		}

		if ( dirtyVertices || this.sortParticles ) {

			renderer.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglVertexBuffer);
			renderer.gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), vertexArray.getByteLength(), vertexArray.getTypedBuffer(), hint.getValue());

		}

		if ( dirtyColors || this.sortParticles ) {

			renderer.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), geometry.__webglColorBuffer);
			renderer.gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), colorArray.getByteLength(), colorArray.getTypedBuffer(), hint.getValue());

		}

		if ( customAttributes != null ) {

			for (Attribute customAttribute : customAttributes) {

				if (customAttribute.needsUpdate || this.sortParticles) {

					renderer.gl.glBindBuffer(BufferTarget.ARRAY_BUFFER.getValue(), customAttribute.buffer);
					renderer.gl.glBufferData(BufferTarget.ARRAY_BUFFER.getValue(), customAttribute.array.getByteLength(), customAttribute.array.getTypedBuffer(), hint.getValue());

				}

			}

		}

	}

}
