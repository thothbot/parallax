/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Parallax project.
 * 
 * Parallax is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Parallax is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Parallax. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.parallax.core.shared.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thothbot.parallax.core.client.gl2.WebGLBuffer;
import thothbot.parallax.core.client.gl2.arrays.Float32Array;
import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Matrix3f;
import thothbot.parallax.core.shared.core.Matrix4f;
import thothbot.parallax.core.shared.core.Quaternion;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.scenes.Scene;

/**
 * Implementation of DimensionalObject
 * 
 * @author thothbot
 *
 */
public class Object3D implements DimensionalObject
{
	private static int Object3DCount = 0;

	protected int id = 0;
	
	protected String name;

	protected DimensionalObject parent;

	protected ArrayList<DimensionalObject> children;

	protected Vector3f position;

	protected Vector3f rotation;
	
	protected Vector3f scale;

	protected Vector3f up;
	
	private Vector3f vector;
	
	protected Matrix4f matrix;

	protected Matrix4f matrixWorld;

	protected Matrix4f matrixRotationWorld;

	protected Quaternion quaternion;

	protected boolean dynamic;

	protected boolean rotationAutoUpdate;
	
	protected boolean matrixWorldNeedsUpdate;

	protected boolean matrixAutoUpdate;
	
	protected boolean useQuaternion;
	
	protected boolean visible;

	protected float boundRadius;

	protected float boundRadiusScale;

	public boolean hasPos;
	public boolean hasNormal;
	public float renderDepth;
	public Matrix4f identityMatrix;
	public boolean isCastShadow = false;
	public boolean isReceiveShadow = false;
	public boolean isFrustumCulled = true;
	public boolean hasImmediateRenderCallback;
	public Float32Array positionArray;
	public Float32Array normalArray;
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public boolean isWebglActive;
	public boolean isWebglInit;
	public Float32Array _objectMatrixArray;
	public Object _modelViewMatrixArray;
	public Object _normalMatrixArray;
	public Matrix3f _normalMatrix;
	public Matrix4f _modelViewMatrix;
	public int count;

	public Object3D() 
	{
		this.id = Object3D.Object3DCount++;

		this.up = new Vector3f(0, 1, 0);

		this.position = new Vector3f();
		this.rotation = new Vector3f();

		this.parent = null;
		this.children = new ArrayList<DimensionalObject>();
		this.scale = new Vector3f(1, 1, 1);
		
		this.dynamic = false;
		this.rotationAutoUpdate = true;
		this.matrix = new Matrix4f();
		this.matrixWorld = new Matrix4f();
		this.matrixRotationWorld = new Matrix4f();
		this.matrixAutoUpdate = true;
		this.matrixWorldNeedsUpdate = true;
		this.quaternion = new Quaternion();
		this.useQuaternion = false;
		this.boundRadius = 0.0f;
		this.boundRadiusScale = 1.0f;
		this.visible = true;
		this.vector = new Vector3f();
		this.name = "";
	}

	@Override
	public int getId()
	{
		return this.id;
	}
	
	@Override
	public DimensionalObject getParent()
	{
		return this.parent;
	}

	@Override
	public void setParent(DimensionalObject parent)
	{
		this.parent = parent;
	}

	@Override
	public List<DimensionalObject> getChildren()
	{
		return this.children;
	}

	@Override
	public void setChildren(Collection<? extends DimensionalObject> children)
	{
		this.children = new ArrayList<DimensionalObject>(children);
	}

	@Override
	public Vector3f getPosition()
	{
		return this.position;
	}

	@Override
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	@Override
	public Vector3f getRotation()
	{
		return this.rotation;
	}

	@Override
	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}

	@Override
	public void setDynamic(boolean dynamic)
	{
		this.dynamic = dynamic;
	}

	@Override
	public boolean isDynamic()
	{
		return dynamic;
	}

	@Override
	public void setRotationAutoUpdate(boolean rotationAutoUpdate)
	{
		this.rotationAutoUpdate = rotationAutoUpdate;

	}

	@Override
	public boolean isRotationAutoUpdate()
	{
		return rotationAutoUpdate;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Vector3f getScale()
	{
		return this.scale;
	}

	@Override
	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}

	@Override
	public Vector3f getUp()
	{
		return this.up;
	}

	@Override
	public void setUp(Vector3f up)
	{
		this.up = up;
	}

	@Override
	public Matrix4f getMatrix()
	{
		return this.matrix;
	}

	@Override
	public void setMatrix(Matrix4f matrix)
	{
		this.matrix = matrix;
	};

	@Override
	public Matrix4f getMatrixWorld()
	{
		return this.matrixWorld;
	}

	@Override
	public void setMatrixWorld(Matrix4f matrixWorld)
	{
		this.matrixWorld = matrixWorld;
	}

	@Override
	public Matrix4f getMatrixRotationWorld()
	{
		return this.matrixRotationWorld;
	}

	@Override
	public void setMatrixRotationWorld(Matrix4f rotation)
	{
		this.matrixRotationWorld = rotation;
	}

	@Override
	public boolean isMatrixWorldNeedsUpdate()
	{
		return this.matrixWorldNeedsUpdate;
	}

	@Override
	public void setMatrixWorldNeedsUpdate(boolean needsUpdate)
	{
		this.matrixWorldNeedsUpdate = needsUpdate;
	}

	@Override
	public boolean isMatrixAutoUpdate()
	{
		return this.matrixAutoUpdate;
	}

	@Override
	public void setMatrixAutoUpdate(boolean autoUpdate)
	{
		this.matrixAutoUpdate = autoUpdate;
	}

	@Override
	public Quaternion getQuaternion()
	{
		return this.quaternion;
	}

	@Override
	public void setQuaternion(Quaternion quaternion)
	{
		this.quaternion = quaternion;
	}

	@Override
	public boolean isUseQuaternion()
	{
		return this.useQuaternion;
	}

	@Override
	public void setUseQuaternion(boolean use)
	{
		this.useQuaternion = use;
	}

	@Override
	public float getBoundRadius()
	{
		return this.boundRadius;
	}

	@Override
	public void setBoundRadius(float boundRadius)
	{
		this.boundRadius = boundRadius;
	}

	@Override
	public float getBoundRadiusScale()
	{
		return this.boundRadiusScale;
	}

	@Override
	public void setBoundRadiusScale(float scale)
	{
		this.boundRadiusScale = scale;
	}

	@Override
	public boolean isVisible()
	{
		return this.visible;
	}

	@Override
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	@Override
	public void translate(float distance, Vector3f axis)
	{
		this.matrix.rotateAxis(axis);
		axis.multiply(distance);
		this.position.add(axis);
	}
	
	@Override
	public void translateX(float distance)
	{
		this.vector.set(1,0,0);
		this.translate(distance, this.vector);
	}
	
	@Override
	public void translateY(float distance)
	{
		this.vector.set(0, 1, 0);
		this.translate(distance, this.vector);
	}
	
	@Override
	public void translateZ(float distance)
	{
		this.vector.set(0,0,1);
		this.translate(distance, this.vector);
	}
	
	@Override
	public void lookAt(Vector3f vector)
	{
		// TODO: Add hierarchy support.
		this.matrix.lookAt(vector, this.position, this.up);

		if (this.rotationAutoUpdate){
			this.rotation.getRotationFromMatrix(this.matrix);
		}
	}
	
	@Override
	public <E extends DimensionalObject> void addChild(E child)
	{
		Log.info("Adding for ID " + this.getId() + " = " + this.getClass().getName() 
				+ " child ID " + child.getId() + " = " + child.getClass().getName());

		if (this.children.indexOf(child) == -1) 
		{
			if(child.getParent() != null )
				child.getParent().removeChild(child);

			child.setParent(this);
			this.children.add(child);

			// add to scene

			DimensionalObject scene = this;

			while (scene.getParent() != null)
				scene = scene.getParent();

			if (scene != null && scene.getClass() == Scene.class)
				((Scene)scene).addSceneItem(child);
		}
	}

	@Override
	public <E extends DimensionalObject> void removeChild(E child)
	{
		int index = this.children.indexOf(child);
		if (index != -1) 
		{
			child.setParent(null);
			this.children.remove(index);
		}
	}
	
	@Override
	public void updateMatrix()
	{
		getMatrix().setPosition(this.position);

		if (isUseQuaternion())
			getMatrix().setRotationFromQuaternion(getQuaternion());
		else
			getMatrix().setRotationFromEuler(getRotation());

		if ( getScale().getX() != 1 || getScale().getY() != 1 || getScale().getZ() != 1) 
		{

			getMatrix().scale(this.scale);
			this.boundRadiusScale = Math.max(getScale().getX(), Math.max( getScale().getY(), getScale().getZ()));
		}

		this.matrixWorldNeedsUpdate = true;
	}

	@Override
	public DimensionalObject getChildByName(String name, boolean recursive)
	{
		for (int c = 0, cl = this.children.size(); c < cl; c++) {

			DimensionalObject child = this.children.get(c);

			if (child.getName().equals(name)) {
				return child;
			}

			if (recursive) {
				child = child.getChildByName(name, recursive);
				if (child != null) {
					return child;
				}

			}
		}

		return null;
	}

	public void applyMatrix(Matrix4f matrix)
	{
		this.matrix.multiply(matrix, this.matrix);

		this.scale.getScaleFromMatrix(this.matrix);
		this.rotation.getRotationFromMatrix(this.matrix, this.scale);
		this.position.getPositionFromMatrix(this.matrix);
	}

	public void updateMatrixWorld(boolean force)
	{
		if (this.matrixAutoUpdate)
			this.updateMatrix();

		// update matrixWorld
		if (this.matrixWorldNeedsUpdate || force) 
		{
			
			if (this.parent != null)
				this.matrixWorld.multiply(this.parent.getMatrixWorld(), this.matrix);
			
			else
				this.matrixWorld.copy(this.matrix);

			this.matrixWorldNeedsUpdate = false;

			force = true;
		}

		// update children
		for (int i = 0, l = this.children.size(); i < l; i++)
			this.children.get(i).updateMatrixWorld(force);
	}
}
