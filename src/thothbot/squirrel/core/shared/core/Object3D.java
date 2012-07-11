/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * 
 * This file based on the JavaScript source file of the THREE.JS project, 
 * licensed under MIT License.
 * 
 * This file is part of Squirrel project.
 * 
 * Squirrel is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your 
 * option) any later version.
 * 
 * Squirrel is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Squirrel. If not, see http://www.gnu.org/licenses/.
 */

package thothbot.squirrel.core.shared.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thothbot.squirrel.core.client.gl2.WebGLBuffer;
import thothbot.squirrel.core.client.gl2.arrays.Float32Array;
import thothbot.squirrel.core.shared.Log;
import thothbot.squirrel.core.shared.cameras.Camera;
import thothbot.squirrel.core.shared.objects.Line;
import thothbot.squirrel.core.shared.scenes.Scene;

public class Object3D implements DimensionalObject
{
	private static int Object3DCount = 0;

	protected int id = 0;

	protected DimensionalObject parent;

	protected ArrayList<DimensionalObject> children;

	protected Vector3f position;

	protected Vector3f rotation;

	protected Boolean dynamic;

	protected Boolean rotationAutoUpdate;

	protected String name;

	protected Vector3f scale;

	protected Vector3f up;

	protected Matrix4f matrix;

	protected Matrix4f matrixWorld;

	protected Matrix4f matrixRotationWorld;

	protected Boolean matrixWorldNeedsUpdate;

	protected Boolean matrixAutoUpdate;

	protected Quaternion quaternion;

	protected Boolean useQuaternion;

	protected float boundRadius;

	protected float boundRadiusScale;

	protected Boolean visible;

	private Vector3f vector;

	// ///////////////////////////////////////////////////////////////////////////////
	// TODO: Check
	public Euler eulerOrder = Euler.XYZ;

	//public Material material;
	public boolean hasPos;
	public boolean hasNormal;
	public float renderDepth;
	public Matrix4f identityMatrix;
	public boolean castShadow = false;
	public boolean receiveShadow = false;
	public boolean frustumCulled = true;
	public boolean immediateRenderCallback;
	public Float32Array positionArray;
	public Float32Array normalArray;
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public boolean __webglActive;
	public Float32Array _objectMatrixArray;
	public Object _modelViewMatrixArray;
	public Object _normalMatrixArray;
	public Matrix3f _normalMatrix;
	public Matrix4f _modelViewMatrix;
	public boolean __webglInit;
	public int count;
	public int morphTargetBase;
	public Line.TYPE type;
	public List<Integer> morphTargetForcedOrder;
	public List<Integer> morphTargetInfluences;
	public List<Integer> __webglMorphTargetInfluences;

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

	public int getId()
	{
		return this.id;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getParent()
	 */
	@Override
	public DimensionalObject getParent()
	{
		return this.parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setParent(com
	 * .google.code.gwt.threejs.client.core.Object3D)
	 */
	@Override
	public void setParent(DimensionalObject parent)
	{
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getChildren()
	 */
	@Override
	public List<DimensionalObject> getChildren()
	{
		return this.children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setChildren(java
	 * .util.Collection)
	 */
	@Override
	public void setChildren(Collection<? extends DimensionalObject> children)
	{
		this.children = new ArrayList<DimensionalObject>(children);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getPosition()
	 */
	@Override
	public Vector3f getPosition()
	{
		return this.position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setPosition(com
	 * .google.code.gwt.threejs.client.core.Vector3)
	 */
	@Override
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getRotation()
	 */
	@Override
	public Vector3f getRotation()
	{
		return this.rotation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setRotation(com
	 * .google.code.gwt.threejs.client.core.Vector3)
	 */
	@Override
	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setDynamic(java
	 * .lang.Boolean)
	 */
	@Override
	public void setDynamic(Boolean dynamic)
	{
		this.dynamic = dynamic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getDynamic()
	 */
	@Override
	public Boolean getDynamic()
	{
		return dynamic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setRotationAutoUpdate
	 * (java.lang.Boolean)
	 */
	@Override
	public void setRotationAutoUpdate(Boolean rotationAutoUpdate)
	{
		this.rotationAutoUpdate = rotationAutoUpdate;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getRotationAutoUpdate ()
	 */
	@Override
	public Boolean getRotationAutoUpdate()
	{
		return rotationAutoUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setName(java. lang.String)
	 */
	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getScale()
	 */
	@Override
	public Vector3f getScale()
	{
		return this.scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setScale(com.
	 * google.code.gwt.threejs.client.core.Vector3)
	 */
	@Override
	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getUp()
	 */
	@Override
	public Vector3f getUp()
	{
		return this.up;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setUp(com.google
	 * .code.gwt.threejs.client.core.Vector3)
	 */
	@Override
	public void setUp(Vector3f up)
	{
		this.up = up;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getMatrix()
	 */
	@Override
	public Matrix4f getMatrix()
	{
		return this.matrix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setMatrix(com
	 * .google.code.gwt.threejs.client.core.Matrix4)
	 */
	@Override
	public void setMatrix(Matrix4f matrix)
	{
		this.matrix = matrix;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getMatrixWorld()
	 */
	@Override
	public Matrix4f getMatrixWorld()
	{
		return this.matrixWorld;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setMatrixWorld
	 * (com.google.code.gwt.threejs.client.core.Matrix4)
	 */
	@Override
	public void setMatrixWorld(Matrix4f matrixWorld)
	{
		this.matrixWorld = matrixWorld;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject# getMatrixRotationWorld()
	 */
	@Override
	public Matrix4f getMatrixRotationWorld()
	{
		return this.matrixRotationWorld;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#
	 * setMatrixRotationWorld(com.google.code.gwt.threejs.client.core.Matrix4)
	 */
	@Override
	public void setMatrixRotationWorld(Matrix4f rotation)
	{
		this.matrixRotationWorld = rotation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#
	 * getMatrixWorldNeedsUpdate()
	 */
	@Override
	public Boolean getMatrixWorldNeedsUpdate()
	{
		return this.matrixWorldNeedsUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#
	 * setMatrixWorldNeedsUpdate(java.lang.Boolean)
	 */
	@Override
	public void setMatrixWorldNeedsUpdate(Boolean needsUpdate)
	{
		this.matrixWorldNeedsUpdate = needsUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getMatrixAutoUpdate ()
	 */
	@Override
	public Boolean getMatrixAutoUpdate()
	{
		return this.matrixAutoUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setMatrixAutoUpdate
	 * (java.lang.Boolean)
	 */
	@Override
	public void setMatrixAutoUpdate(Boolean autoUpdate)
	{
		this.matrixAutoUpdate = autoUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getQuaternion()
	 */
	@Override
	public Quaternion getQuaternion()
	{
		return this.quaternion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setQuaternion
	 * (com.google.code.gwt.threejs.client.core.Quaternion)
	 */
	@Override
	public void setQuaternion(Quaternion quaternion)
	{
		this.quaternion = quaternion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getUseQuaternion ()
	 */
	@Override
	public Boolean getUseQuaternion()
	{
		return this.useQuaternion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setUseQuaternion
	 * (java.lang.Boolean)
	 */
	@Override
	public void setUseQuaternion(Boolean use)
	{
		this.useQuaternion = use;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getBoundRadius()
	 */
	@Override
	public double getBoundRadius()
	{
		return this.boundRadius;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setBoundRadius (float)
	 */
	@Override
	public void setBoundRadius(float boundRadius)
	{
		this.boundRadius = boundRadius;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getBoundRadiusScale ()
	 */
	@Override
	public double getBoundRadiusScale()
	{
		return this.boundRadiusScale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setBoundRadiusScale
	 * (float)
	 */
	@Override
	public void setBoundRadiusScale(float scale)
	{
		this.boundRadiusScale = scale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#getVisible()
	 */
	@Override
	public Boolean getVisible()
	{
		return this.visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alexusachev.lib.core.DimentionObject#setVisible(java
	 * .lang.Boolean)
	 */
	@Override
	public void setVisible(Boolean visible)
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
		if (index != -1) {
			child.setParent(null);
			this.children.remove(index);
		}
	}
	
	@Override
	public void updateMatrix()
	{
		this.matrix.setPosition(this.position);

		if (this.useQuaternion)  {
			this.matrix.setRotationFromQuaternion(this.quaternion);
		} else {
			this.matrix.setRotationFromEuler(this.rotation);
		}

		if ( this.scale.x != 1 || this.scale.y != 1 || this.scale.z != 1) {

			this.matrix.scale(this.scale);
			this.boundRadiusScale = Math.max(this.scale.x, Math.max( this.scale.y, this.scale.z));
		}

		this.matrixWorldNeedsUpdate = true;
	}

	@Override
	public void update(Matrix4f parentMatrixWorld, Boolean forceUpdate, Camera camera)
	{
		if (this.matrixAutoUpdate){
			this.updateMatrix();
		}
		
		// update matrixWorld
		
		if (this.matrixWorldNeedsUpdate || forceUpdate) {

			if (parentMatrixWorld != null) {
				this.matrixWorld.multiply(parentMatrixWorld, this.matrix);
			} else {
				this.matrixWorld.copy(this.matrix);
			}
			this.matrixRotationWorld.extractRotation( this.matrixWorld, this.scale );
			this.matrixWorldNeedsUpdate = false;
			forceUpdate = true;
		}

		// update children

		for (int i = 0, l = this.children.size(); i < l; i++) {
			this.children.get(i).update(this.matrixWorld, forceUpdate, camera);

		}
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

	//////////////////////////////////////////////////////////////////
	// TODO: Check

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
		if (this.matrixWorldNeedsUpdate || force) {

			if (this.parent != null) {
				this.matrixWorld.multiply(this.parent.getMatrixWorld(), this.matrix);
			} else {
				this.matrixWorld.copy(this.matrix);
			}

			this.matrixWorldNeedsUpdate = false;

			force = true;
		}

		// update children
		for (int i = 0, l = this.children.size(); i < l; i++)
			this.children.get(i).updateMatrixWorld(force);
	}
}
