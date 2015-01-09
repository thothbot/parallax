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

package thothbot.parallax.core.shared.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.math.Euler;
import thothbot.parallax.core.shared.math.Euler.EulerChangeHandler;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Quaternion.QuaternionChangeHandler;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * Implementation of DimensionalObject
 * 
 * @author thothbot
 *
 */
public class Object3D 
{
	public static final Vector3 DefaultUp = new Vector3( 0, 1, 0 );
	
	public static interface ObjectHandler 
	{
		void onAdd(Object3D object);
		void onRemove(Object3D object);
	}
	
	public static interface Traverse {
		void callback(Object3D object);
	}
	
	private static int Object3DCount = 0;
	
	protected int id = 0;
	
	protected String name;

	protected Object3D parent;

	protected ArrayList<Object3D> children;
	
	protected Vector3 up = Object3D.DefaultUp.clone();

	protected Vector3 position;

	protected Euler rotation;
	
	protected Quaternion quaternion;
	
	protected Vector3 scale;
	
	public double renderDepth;
	
	protected boolean rotationAutoUpdate;

	protected Matrix4 matrix;

	protected Matrix4 matrixWorld;

	protected boolean matrixAutoUpdate;

	protected boolean matrixWorldNeedsUpdate;
	
	protected boolean visible;

	protected boolean isCastShadow = false;
	
	protected boolean isReceiveShadow = false;

	protected boolean isFrustumCulled = true;
	
	protected ObjectHandler handler;
		
	public Object3D() 
	{
		this.id = Object3D.Object3DCount++;
		
		this.name = "";
		
		this.parent = null;

		this.children = new ArrayList<Object3D>();

		this.up = Object3D.DefaultUp.clone();

		this.position = new Vector3();
		this.scale = new Vector3(1, 1, 1);	
		
		this.quaternion = new Quaternion();
		this.quaternion.setHandler(new QuaternionChangeHandler() {
			
			@Override
			public void onChange(Quaternion quaternion) {
				rotation.setFromQuaternion( quaternion );
			}
		});
		
		this.rotation = new Euler();
		this.rotation.setHandler(new EulerChangeHandler() {
			
			@Override
			public void onChange(Euler rotation) {
				quaternion.setFromEuler( rotation );
			}
		});
		
		this.rotationAutoUpdate = true;

		this.matrix = new Matrix4();

		this.matrixWorld = new Matrix4();

		this.matrixAutoUpdate = true;
		
		this.matrixWorldNeedsUpdate = false;
		
		this.visible = true;
		
		this.isFrustumCulled = true;
	}
	
	public void setHandler(ObjectHandler handler) {
		this.handler = handler;
	}
	
	private void onAdd() {
		if(this.handler != null)
			this.handler.onAdd(Object3D.this);
	}
	
	private void onRemove() {
		if(this.handler != null)
			this.handler.onRemove(Object3D.this);
	}
	
	public int getId() {
		return this.id;
	}
	
	public Object3D getParent() {
		return this.parent;
	}

	public void setParent(Object3D parent) {
		this.parent = parent;
	}

	public List<Object3D> getChildren() {
		return this.children;
	}

	public void setChildren(Collection<? extends Object3D> children) {
		this.children = new ArrayList<Object3D>(children);
	}

	public Vector3 getPosition() {
		return this.position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Euler getRotation() {
		return this.rotation;
	}

	public void setRotation(Euler rotation) {
		this.rotation = rotation;
	}

	/**
	 * If set to true light will cast dynamic shadows. 
	 * Warning: This is expensive and requires tweaking to get shadows looking right.
	 * <p>
	 * Default � false.
	 */
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

	public void setRotationAutoUpdate(boolean rotationAutoUpdate) {
		this.rotationAutoUpdate = rotationAutoUpdate;

	}

	public boolean isRotationAutoUpdate() {
		return rotationAutoUpdate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Vector3 getScale() {
		return this.scale;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	public Vector3 getUp() {
		return this.up;
	}

	public void setUp(Vector3 up) {
		this.up = up;
	}

	public Matrix4 getMatrix() {
		return this.matrix;
	}

	public void setMatrix(Matrix4 matrix) {
		this.matrix = matrix;
	};

	public Matrix4 getMatrixWorld() {
		return this.matrixWorld;
	}

	public void setMatrixWorld(Matrix4 matrixWorld) {
		this.matrixWorld = matrixWorld;
	}

	public boolean isMatrixWorldNeedsUpdate() {
		return this.matrixWorldNeedsUpdate;
	}

	public void setMatrixWorldNeedsUpdate(boolean needsUpdate) {
		this.matrixWorldNeedsUpdate = needsUpdate;
	}

	public boolean isMatrixAutoUpdate() {
		return this.matrixAutoUpdate;
	}

	public void setMatrixAutoUpdate(boolean autoUpdate) {
		this.matrixAutoUpdate = autoUpdate;
	}

	public Quaternion getQuaternion() {
		return this.quaternion;
	}

	public void setQuaternion(Quaternion quaternion) {
		this.quaternion = quaternion;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible)	{
		this.visible = visible;
	}
	
	public void applyMatrix(Matrix4 matrix)
	{
		this.matrix.multiply( matrix, this.matrix );

		this.matrix.decompose( this.position, this.quaternion, this.scale );
	}
	
	public void setRotationFromAxisAngle ( Vector3 axis, double angle ) {

		// assumes axis is normalized

		this.quaternion.setFromAxisAngle( axis, angle );

	}

	public void setRotationFromEuler ( Euler euler ) {

		this.quaternion.setFromEuler( euler );

	}

	public void setRotationFromMatrix ( Matrix4 m ) {

		// assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)

		this.quaternion.setFromRotationMatrix( m );

	}

	public void setRotationFromQuaternion ( Quaternion q ) {

		// assumes q is normalized

		this.quaternion.copy( q );

	}

	public Object3D rotateOnAxis(Vector3 axis, double angle) {

		// rotate object on axis in object space
		// axis is assumed to be normalized

		Quaternion q1 = new Quaternion();

		q1.setFromAxisAngle( axis, angle );

		this.quaternion.multiply( q1 );

		return this;

	}

	public Object3D rotateX(double angle) {

		Vector3 v1 = new Vector3( 1.0, 0, 0 );

		return this.rotateOnAxis( v1, angle );

	}

	public Object3D rotateY(double angle) {

		Vector3 v1 = new Vector3( 0, 1.0, 0 );

		return this.rotateOnAxis( v1, angle );

	}

	public Object3D rotateZ(double angle) {

		Vector3 v1 = new Vector3( 0, 0, 1.0 );

		return this.rotateOnAxis( v1, angle );

	}
	
	public Object3D translateOnAxis ( Vector3 axis, double distance ) {

		// translate object by distance along axis in object space
		// axis is assumed to be normalized

		Vector3 v1 = new Vector3();


		v1.copy( axis ).apply( this.quaternion );

		this.position.add( v1.multiply( distance ) );

		return this;
	}

	public Object3D translateX(double distance) {

		Vector3 v1 = new Vector3( 1.0, 0, 0 );

		return this.translateOnAxis( v1, distance );

	}

	public Object3D translateY(double distance) {

		Vector3 v1 = new Vector3( 0, 1.0, 0 );

		return this.translateOnAxis( v1, distance );

	}

	public Object3D translateZ(double distance) {

		Vector3 v1 = new Vector3( 0, 0, 1.0 );

		return this.translateOnAxis( v1, distance );

	}

	public Vector3 localToWorld( Vector3 vector ) 
	{
		return vector.apply( this.matrixWorld );
	}

	public Vector3 worldToLocal( Vector3 vector ) 
	{
		Matrix4 m1 = new Matrix4();
		
		return vector.apply( m1.getInverse( this.matrixWorld ) );
	}

	public void lookAt(Vector3 vector)
	{
		// This routine does not support objects with rotated and/or translated parent(s)

		Matrix4 m1 = new Matrix4();


		m1.lookAt( vector, this.position, this.up );

		this.quaternion.setFromRotationMatrix( m1 );

	}
	
	public Object3D add(List<? extends Object3D> objects) {
		for (int i = 0; i < objects.size(); i++)
			this.add(objects.get(i));
		
		return this;
	}
	
	public Object3D add(Object3D object)
	{
		if(object.equals(this)) {
			Log.error("Object3D.add: " + object + " can't be added as a child of itself.");
			return this;
		}
			  
		Log.info("Adding for ID " + this.getId() + " = " + this.getClass().getName() 
				+ " child ID " + object.getId() + " = " + object.getClass().getName());
				

		if ( object.parent != null ) {

			object.parent.remove( object );

		}

		object.parent = this;
		
		object.onAdd();

		this.children.add( object );

		return this;
	}
	
	public void remove(List<? extends Object3D> objects) {
		for (int i = 0; i < objects.size(); i++)
			this.remove(objects.get(i));
	}

	public void remove(Object3D object)
	{
		int index = this.children.indexOf(object);

		if (index != -1) 
		{
			object.parent = null;

			object.onRemove();

			this.children.remove( index );
		}
	}
	
	public Object3D getObjectById(int id, boolean recursive ) {

		if ( this.id == id ) return this;

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			Object3D child = this.children.get( i );
			Object3D object = child.getObjectById( id, recursive );

			if ( object != null ) {

				return object;

			}

		}

		return null;
	}
	
	public Object3D getObjectByName( String name, boolean recursive ) {

		if ( this.name.equals( name ) ) return this;

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			Object3D child = this.children.get( i );
			Object3D object = child.getObjectByName( name, recursive );

			if ( object != null ) {

				return object;

			}

		}

		return null;
	}

	public Vector3 getWorldPosition() {
		return getWorldPosition(new Vector3());
	}
	
	public Vector3 getWorldPosition( Vector3 optionalTarget ) {

		this.updateMatrixWorld( true );

		return optionalTarget.setFromMatrixPosition( this.matrixWorld );
	}
	
	public Quaternion getWorldQuaternion(Vector3 optionalTarget) {
		return getWorldQuaternion(new Quaternion());
	}

	public Quaternion getWorldQuaternion(Quaternion optionalTarget) {

		Vector3 position = new Vector3();
		Vector3 scale = new Vector3();


		this.updateMatrixWorld( true );

		this.matrixWorld.decompose( position, optionalTarget, scale );

		return optionalTarget;
	}
	
	public Euler getWorldRotation() {
		return getWorldRotation(new Euler());
	}

	public Euler getWorldRotation(Euler optionalTarget) {

		Quaternion quaternion = new Quaternion();

		this.getWorldQuaternion( quaternion );

		return optionalTarget.setFromQuaternion( quaternion, this.rotation.getOrder() );
			
	}
	
	public Vector3 getWorldScale() {
		return getWorldScale(new Vector3());
	}

	public Vector3 getWorldScale(Vector3 optionalTarget) {

		Vector3 position = new Vector3();
		Quaternion quaternion = new Quaternion();

		this.updateMatrixWorld( true );

		this.matrixWorld.decompose( position, quaternion, optionalTarget );

		return optionalTarget;
	}
	
	public Vector3 getWorldDirection() {
		return getWorldDirection(new Vector3());
	}

	public Vector3 getWorldDirection(Vector3 optionalTarget) {

		Quaternion quaternion = new Quaternion();

		this.getWorldQuaternion( quaternion );

		return optionalTarget.set( 0, 0, 1.0 ).apply( quaternion );

	}
	
	public void traverse ( Traverse traverse ) {

		traverse.callback(this);

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			this.children.get( i ).traverse( traverse );

		}
	}

	public void traverseVisible ( Traverse traverse ) {

		if ( this.visible == false ) return;

		traverse.callback(this);

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			this.children.get( i ).traverseVisible( traverse );

		}

	}
	
	public void updateMatrix()
	{

		this.matrix.compose( this.position, this.quaternion, this.scale );

		this.matrixWorldNeedsUpdate = true;

	}
	
	public void updateMatrixWorld(boolean force)
	{
		if ( this.matrixAutoUpdate == true ) this.updateMatrix();

		if ( this.matrixWorldNeedsUpdate == true || force == true ) {

			if ( this.parent == null ) {

				this.matrixWorld.copy( this.matrix );

			} else {

				this.matrixWorld.multiply( this.parent.matrixWorld, this.matrix );

			}

			this.matrixWorldNeedsUpdate = false;

			force = true;

		}

		// update children

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			this.children.get( i ).updateMatrixWorld( force );

		}

	}
	
	public Object3D clone() {
		return clone(new Object3D());
	}

	public Object3D clone( Object3D object) {
		return clone(object, true);
	}
	
	public Object3D clone( Object3D object, boolean recursive ) {

		object.name = this.name;

		object.up.copy( this.up );

		object.position.copy( this.position );
		object.quaternion.copy( this.quaternion );
		object.scale.copy( this.scale );

		object.renderDepth = this.renderDepth;

		object.rotationAutoUpdate = this.rotationAutoUpdate;

		object.matrix.copy( this.matrix );
		object.matrixWorld.copy( this.matrixWorld );

		object.matrixAutoUpdate = this.matrixAutoUpdate;
		object.matrixWorldNeedsUpdate = this.matrixWorldNeedsUpdate;

		object.visible = this.visible;

		object.isCastShadow = this.isCastShadow;
		object.isReceiveShadow = this.isReceiveShadow;

		object.isFrustumCulled = this.isFrustumCulled;

		if ( recursive == true ) {

			for ( int i = 0; i < this.children.size(); i ++ ) {

				Object3D child = this.children.get( i );
				object.add( child.clone() );

			}

		}

		return object;

	}
}
