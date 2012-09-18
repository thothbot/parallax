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
import thothbot.parallax.core.shared.core.Euler;
import thothbot.parallax.core.shared.core.Matrix3;
import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Quaternion;
import thothbot.parallax.core.shared.core.Vector3;
import thothbot.parallax.core.shared.scenes.Scene;

/**
 * Implementation of DimensionalObject
 * 
 * @author thothbot
 *
 */
public class Object3D implements DimensionalObject
{
	private static Matrix4 __m1 = new Matrix4();
	private static int Object3DCount = 0;

	protected int id = 0;
	
	protected String name;

	protected DimensionalObject parent;

	protected ArrayList<DimensionalObject> children;

	protected Vector3 position;

	protected Vector3 rotation;
	
	protected Vector3 scale;

	protected Vector3 up;
	
	private Vector3 vector;
	
	protected Matrix4 matrix;

	protected Matrix4 matrixWorld;

	protected Matrix4 matrixRotationWorld;

	protected Quaternion quaternion;

	protected boolean dynamic;

	protected boolean rotationAutoUpdate;
	
	protected boolean matrixWorldNeedsUpdate;

	protected boolean matrixAutoUpdate;
	
	protected boolean useQuaternion;
	
	protected boolean visible;

	protected double boundRadius;

	protected double boundRadiusScale;
	
	protected Euler eulerOrder = Euler.XYZ;

	protected boolean isCastShadow = false;
	
	protected boolean isReceiveShadow = false;
	
	protected boolean isFrustumCulled = true;
	
	public boolean hasPos;
	public boolean hasNormal;
	public double renderDepth;
	public Matrix4 identityMatrix;
	public boolean hasImmediateRenderCallback;
	public Float32Array positionArray;
	public Float32Array normalArray;
	public WebGLBuffer __webglVertexBuffer;
	public WebGLBuffer __webglNormalBuffer;
	public boolean isWebglActive;
	public boolean isWebglInit;
	public Float32Array _modelMatrixArray;
	public Object _modelViewMatrixArray;
	public Object _normalMatrixArray;
	public Matrix3 _normalMatrix;
	public Matrix4 _modelViewMatrix;
	public int count;
	
	public Object3D() 
	{
		this.id = Object3D.Object3DCount++;

		this.up = new Vector3(0, 1, 0);

		this.position = new Vector3();
		this.rotation = new Vector3();

		this.parent = null;
		this.children = new ArrayList<DimensionalObject>();
		this.scale = new Vector3(1, 1, 1);
		
		this.dynamic = false;
		this.rotationAutoUpdate = true;
		this.matrix = new Matrix4();
		this.matrixWorld = new Matrix4();
		this.matrixRotationWorld = new Matrix4();
		this.matrixAutoUpdate = true;
		this.matrixWorldNeedsUpdate = true;
		this.quaternion = new Quaternion();
		this.useQuaternion = false;
		this.boundRadius = 0.0;
		this.boundRadiusScale = 1.0;
		this.visible = true;
		this.vector = new Vector3();
		this.name = "";
		
		this._modelViewMatrix = new Matrix4();
		this._normalMatrix = new Matrix3();
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public DimensionalObject getParent() {
		return this.parent;
	}

	@Override
	public void setParent(DimensionalObject parent) {
		this.parent = parent;
	}

	@Override
	public List<DimensionalObject> getChildren() {
		return this.children;
	}

	@Override
	public void setChildren(Collection<? extends DimensionalObject> children) {
		this.children = new ArrayList<DimensionalObject>(children);
	}

	@Override
	public Vector3 getPosition() {
		return this.position;
	}

	@Override
	public void setPosition(Vector3 position) {
		this.position = position;
	}

	@Override
	public Vector3 getRotation() {
		return this.rotation;
	}

	@Override
	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	@Override
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	public boolean isCastShadow() {
		return isCastShadow;
	}

	public void setCastShadow(boolean isCastShadow) {
		this.isCastShadow = isCastShadow;
	}

	public boolean isReceiveShadow() {
		return isReceiveShadow;
	}

	public void setReceiveShadow(boolean isReceiveShadow) {
		this.isReceiveShadow = isReceiveShadow;
	}

	public boolean isFrustumCulled() {
		return isFrustumCulled;
	}

	public void setFrustumCulled(boolean isFrustumCulled) {
		this.isFrustumCulled = isFrustumCulled;
	}

	@Override
	public void setRotationAutoUpdate(boolean rotationAutoUpdate) {
		this.rotationAutoUpdate = rotationAutoUpdate;

	}

	@Override
	public boolean isRotationAutoUpdate() {
		return rotationAutoUpdate;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Vector3 getScale() {
		return this.scale;
	}

	@Override
	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	@Override
	public Vector3 getUp() {
		return this.up;
	}

	@Override
	public void setUp(Vector3 up) {
		this.up = up;
	}

	@Override
	public Matrix4 getMatrix() {
		return this.matrix;
	}

	@Override
	public void setMatrix(Matrix4 matrix) {
		this.matrix = matrix;
	};

	@Override
	public Matrix4 getMatrixWorld() {
		return this.matrixWorld;
	}

	@Override
	public void setMatrixWorld(Matrix4 matrixWorld) {
		this.matrixWorld = matrixWorld;
	}

	@Override
	public Matrix4 getMatrixRotationWorld() {
		return this.matrixRotationWorld;
	}

	@Override
	public void setMatrixRotationWorld(Matrix4 rotation) {
		this.matrixRotationWorld = rotation;
	}

	@Override
	public boolean isMatrixWorldNeedsUpdate() {
		return this.matrixWorldNeedsUpdate;
	}

	@Override
	public void setMatrixWorldNeedsUpdate(boolean needsUpdate) {
		this.matrixWorldNeedsUpdate = needsUpdate;
	}

	@Override
	public boolean isMatrixAutoUpdate() {
		return this.matrixAutoUpdate;
	}

	@Override
	public void setMatrixAutoUpdate(boolean autoUpdate) {
		this.matrixAutoUpdate = autoUpdate;
	}

	@Override
	public Quaternion getQuaternion() {
		return this.quaternion;
	}

	@Override
	public void setQuaternion(Quaternion quaternion) {
		this.quaternion = quaternion;
	}

	@Override
	public boolean isUseQuaternion() {
		return this.useQuaternion;
	}

	@Override
	public void setUseQuaternion(boolean use) {
		this.useQuaternion = use;
	}

	@Override
	public double getBoundRadius() {
		return this.boundRadius;
	}

	@Override
	public void setBoundRadius(double boundRadius) {
		this.boundRadius = boundRadius;
	}

	@Override
	public double getBoundRadiusScale() {
		return this.boundRadiusScale;
	}

	@Override
	public void setBoundRadiusScale(double scale) {
		this.boundRadiusScale = scale;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public void setVisible(boolean visible)	{
		this.visible = visible;
	}
	
	public Euler getEulerOrder() {
		return eulerOrder;
	}

	public void setEulerOrder(Euler eulerOrder) {
		this.eulerOrder = eulerOrder;
	}

	@Override
	public void translate(double distance, Vector3 axis)
	{
		this.matrix.rotateAxis(axis);
		axis.multiply(distance);
		this.position.add(axis);
	}
	
	@Override
	public void translateX(double distance)
	{
		this.vector.set(1, 0, 0);
		this.translate(distance, this.vector);
	}
	
	@Override
	public void translateY(double distance)
	{
		this.vector.set(0, 1, 0);
		this.translate(distance, this.vector);
	}
	
	@Override
	public void translateZ(double distance)
	{
		this.vector.set(0, 0, 1);
		this.translate(distance, this.vector);
	}

	@Override
	public Vector3 localToWorld( Vector3 vector ) 
	{
		return this.matrixWorld.multiplyVector3( vector );
	}

	@Override
	public Vector3 worldToLocal( Vector3 vector ) 
	{
		return Object3D.__m1.getInverse( this.matrixWorld ).multiplyVector3( vector );
	}

	@Override
	public void lookAt(Vector3 vector)
	{
		// TODO: Add hierarchy support.
		this.matrix.lookAt(vector, this.position, this.up);

		if (this.rotationAutoUpdate)
		{
			this.rotation.setEulerFromRotationMatrix( this.matrix, this.eulerOrder );
		}
	}
	
	@Override
	public <E extends DimensionalObject> void add(E child)
	{
		Log.info("Adding for ID " + this.getId() + " = " + this.getClass().getName() 
				+ " child ID " + child.getId() + " = " + child.getClass().getName());

		if (this.children.indexOf(child) == -1) 
		{
			if(child.getParent() != null )
				child.getParent().remove(child);

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
	public <E extends DimensionalObject> void remove(E child)
	{
		int index = this.children.indexOf(child);

		if (index != -1) 
		{
			child.setParent(null);
			this.children.remove(index);
			
			// remove from scene
			DimensionalObject scene = this;

			while ( scene.getParent() != null ) 
				scene = scene.getParent();

			if ( scene != null && scene.getClass() == Scene.class) 
				((Scene)scene).removeSceneItem( child );
		}
	}
	
	@Override
	public List<DimensionalObject> getDescendants() 
	{
		List<DimensionalObject> retval = new ArrayList<DimensionalObject>();
		retval.addAll(getChildren());

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) 
		{
			retval.addAll(this.children.get( i ).getDescendants());
		}

		return retval;
	}
	
	@Override
	public void updateMatrix()
	{

		this.matrix.setPosition( this.position );

		if ( this.useQuaternion == false )  
		{
			this.matrix.setRotationFromEuler( this.rotation, this.eulerOrder );
		}
		else
		{
			this.matrix.setRotationFromQuaternion( this.quaternion );
		}

		if ( this.scale.getX() != 1 || this.scale.getY() != 1 || this.scale.getZ() != 1 ) 
		{
			this.matrix.scale( this.scale );
			this.boundRadiusScale = Math.max( this.scale.getX(), Math.max( this.scale.getY(), this.scale.getZ() ) );
		}

		this.matrixWorldNeedsUpdate = true;
	}
	
	public void updateMatrixWorld(boolean force)
	{
		if ( this.matrixAutoUpdate ) 
			this.updateMatrix();

		if ( this.matrixWorldNeedsUpdate || force ) 
		{
			if ( this.parent == null ) 
			{
				this.matrixWorld.copy( this.matrix );
			} 
			else 
			{
				this.matrixWorld.multiply( this.parent.getMatrixWorld(), this.matrix );
			}

			this.matrixWorldNeedsUpdate = false;

			force = true;
		}

		// update children

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) 
		{
			this.children.get( i ).updateMatrixWorld( force );
		}
	}

	@Override
	public List<? extends DimensionalObject> getChildrenByClass(Class clazz, boolean recursive)
	{
		List<DimensionalObject> retval = new ArrayList<DimensionalObject>();
		
		for (int c = 0, cl = this.children.size(); c < cl; c++) 
		{
			DimensionalObject child = this.children.get(c);

			if (child.getClass() == clazz) 
			{
				retval.add(child);
			}

			if (recursive) 
			{
				retval.addAll(child.getChildrenByClass(clazz, recursive));
			}
		}

		return retval;
	}
	
	@Override
	public DimensionalObject getChildByName(String name, boolean recursive)
	{
		for (int c = 0, cl = this.children.size(); c < cl; c++) 
		{
			DimensionalObject child = this.children.get(c);

			if (child.getName().equals(name)) 
			{
				return child;
			}

			if (recursive) 
			{
				child = child.getChildByName(name, recursive);
				if (child != null) 
				{
					return child;
				}

			}
		}

		return null;
	}

	public void applyMatrix(Matrix4 matrix)
	{
		this.matrix.multiply(matrix, this.matrix);

		this.scale.getScaleFromMatrix(this.matrix);
		
		Matrix4 mat = new Matrix4().extractRotation( this.matrix );
		this.rotation.setEulerFromRotationMatrix( mat, this.eulerOrder );

		this.position.getPositionFromMatrix(this.matrix);
	}
}
