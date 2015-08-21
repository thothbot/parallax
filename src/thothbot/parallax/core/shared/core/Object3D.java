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
import thothbot.parallax.core.shared.math.Matrix3;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Quaternion.QuaternionChangeHandler;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * Base class for scene objects.
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
	
	private double renderDepth;
	
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
	

	public Matrix3 _normalMatrix;
	public Matrix4 _modelViewMatrix;
	
	public boolean __webglActive;
	public boolean __webglInit;

			
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
	
	/**
	 * Unique number for this object instance.
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Object's parent in the scene graph.
	 * 
	 * @return
	 */
	public Object3D getParent() {
		return this.parent;
	}

	public void setParent(Object3D parent) {
		this.parent = parent;
	}

	/**
	 * Array with object's children.
	 * @return
	 */
	public List<Object3D> getChildren() {
		return this.children;
	}

	public void setChildren(Collection<? extends Object3D> children) {
		this.children = new ArrayList<Object3D>(children);
	}
	
	public List<? extends Object3D> getChildrenByClass(Class<?> clazz, boolean recursive)
	{
		List<Object3D> retval = new ArrayList<Object3D>();
		
		for (int c = 0, cl = this.children.size(); c < cl; c++) 
		{
			Object3D child = this.children.get(c);

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

	/**
	 * Object's local position.
	 * @return
	 */
	public Vector3 getPosition() {
		return this.position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	/**
	 * Object's local rotation ({@link Euler} angles), in radians.
	 * @return
	 */
	public Euler getRotation() {
		return this.rotation;
	}

	public void setRotation(Euler rotation) {
		this.rotation = rotation;
	}

	/**
	 * If set to true light will cast dynamic shadows. 
	 * <p>
	 * Default � false.
	 */
	public boolean isCastShadow() {
		return isCastShadow;
	}

	public void setCastShadow(boolean isCastShadow) {
		this.isCastShadow = isCastShadow;
	}

	/**
	 * Material gets baked in shadow receiving. default – false
	 * @return
	 */
	public boolean isReceiveShadow() {
		return isReceiveShadow;
	}

	public void setReceiveShadow(boolean isReceiveShadow) {
		this.isReceiveShadow = isReceiveShadow;
	}

	/**
	 * When this is set, it checks every frame if the object is in the frustum of the camera. 
	 * Otherwise the object gets drawn every frame even if it isn't visible. default – true
	 * @return
	 */
	public boolean isFrustumCulled() {
		return isFrustumCulled;
	}

	public void setFrustumCulled(boolean isFrustumCulled) {
		this.isFrustumCulled = isFrustumCulled;
	}

	public void setRotationAutoUpdate(boolean rotationAutoUpdate) {
		this.rotationAutoUpdate = rotationAutoUpdate;

	}

	/**
	 * When this is set, then the rotationMatrix gets calculated every frame. default – true
	 * @return
	 */
	public boolean isRotationAutoUpdate() {
		return rotationAutoUpdate;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Optional name of the object (doesn't need to be unique).
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Object's local scale.
	 * @return
	 */
	public Vector3 getScale() {
		return this.scale;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}
	
	/**
	 * @return the renderDepth
	 */
	public double getRenderDepth() {
		return renderDepth;
	}

	/**
	 * @param renderDepth the renderDepth to set
	 */
	public void setRenderDepth(double renderDepth) {
		this.renderDepth = renderDepth;
	}

	/**
	 * Up direction.
	 * @return
	 */
	public Vector3 getUp() {
		return this.up;
	}

	public void setUp(Vector3 up) {
		this.up = up;
	}

	/**
	 * Local transform.
	 * @return
	 */
	public Matrix4 getMatrix() {
		return this.matrix;
	}

	public void setMatrix(Matrix4 matrix) {
		this.matrix = matrix;
	}

	/**
	 * The global transform of the object. If the Object3d has no parent, then it's identical to the local transform.
	 * @return
	 */
	public Matrix4 getMatrixWorld() {
		return this.matrixWorld;
	}

	public void setMatrixWorld(Matrix4 matrixWorld) {
		this.matrixWorld = matrixWorld;
	}

	/**
	 * When this is set, it calculates the matrixWorld in that frame and resets this property to false. default – false
	 * @return
	 */
	public boolean isMatrixWorldNeedsUpdate() {
		return this.matrixWorldNeedsUpdate;
	}

	public void setMatrixWorldNeedsUpdate(boolean needsUpdate) {
		this.matrixWorldNeedsUpdate = needsUpdate;
	}

	/**
	 * When this is set, it calculates the matrix of position, (rotation or quaternion) and scale every
	 * frame and also recalculates the matrixWorld property. default – true
	 * @return
	 */
	public boolean isMatrixAutoUpdate() {
		return this.matrixAutoUpdate;
	}

	public void setMatrixAutoUpdate(boolean autoUpdate) {
		this.matrixAutoUpdate = autoUpdate;
	}

	/**
	 * Object's local rotation as {@link Quaternion}.
	 * @return
	 */
	public Quaternion getQuaternion() {
		return this.quaternion;
	}

	public void setQuaternion(Quaternion quaternion) {
		this.quaternion = quaternion;
	}

	/**
	 * Object gets rendered if true. default – true
	 * @return
	 */
	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible)	{
		this.visible = visible;
	}
	
	/**
	 * This updates the position, rotation and scale with the matrix.
	 * @param matrix
	 */
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

	/**
	 * Rotate an object along an axis in object space. The axis is assumed to be normalized.
	 * @param axis A normalized vector in object space. 
	 * @param angle The angle in radians.
	 * @return
	 */
	public Object3D rotateOnAxis(Vector3 axis, double angle) {

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
	
	/**
	 * Translate an object by distance along an axis in object space. The axis is assumed to be normalized.
	 * @param axis A normalized vector in object space.
	 * @param distance The distance to translate.
	 * @return
	 */
	public Object3D translateOnAxis ( Vector3 axis, double distance ) {

		Vector3 v1 = new Vector3();


		v1.copy( axis ).apply( this.quaternion );

		this.position.add( v1.multiply( distance ) );

		return this;
	}

	/**
	 * Translates object along x axis by distance.
	 * @param distance
	 * @return
	 */
	public Object3D translateX(double distance) {

		Vector3 v1 = new Vector3( 1.0, 0, 0 );

		return this.translateOnAxis( v1, distance );

	}

	/**
	 * Translates object along y axis by distance.
	 * @param distance
	 * @return
	 */
	public Object3D translateY(double distance) {

		Vector3 v1 = new Vector3( 0, 1.0, 0 );

		return this.translateOnAxis( v1, distance );

	}

	/**
	 * Translates object along z axis by distance.
	 * @param distance
	 * @return
	 */
	public Object3D translateZ(double distance) {

		Vector3 v1 = new Vector3( 0, 0, 1.0 );

		return this.translateOnAxis( v1, distance );

	}

	public Vector3 localToWorld( Vector3 vector ) 
	{
		return vector.apply( this.matrixWorld );
	}

	/**
	 * Updates the vector from world space to local space.
	 * @param vector A world vector.
	 * @return
	 */
	public Vector3 worldToLocal( Vector3 vector ) 
	{
		Matrix4 m1 = new Matrix4();
		
		return vector.apply( m1.getInverse( this.matrixWorld ) );
	}

	/**
	 * Rotates object to face point in space.
	 * @param vector A world vector to look at.
	 */
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
	
	/**
	 * Adds object as child of this object.
	 * @param object An object
	 * @return
	 */
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

	/**
	 * Removes object as child of this object.
	 * @param object An object.
	 */
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
	
	/**
	 * Searches through the object's children and returns the first with a matching id, optionally recursive.
	 * @param id Unique number of the object instance
	 * @param recursive Boolean whether to search through the children's children.
	 * @return
	 */
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
	
	/**
	 * Searches through the object's children and returns the first with a matching name, optionally recursive.
	 * @param name String to match to the children's Object3d.name property. 
	 * @param recursive Boolean whether to search through the children's children.
	 * @return
	 */
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
	
	/**
	 * Executes the callback on this object and all descendants
	 * @param traverse A {@link Traverse} interface with as first argument an object3D object.
	 */
	public void traverse ( Traverse traverse ) {

		traverse.callback(this);

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			this.children.get( i ).traverse( traverse );

		}
	}

	/**
	 * Like {@link Object3D#traverse(Traverse)}, but the {@link Traverse} will only be executed for visible objects. Descendants of invisible objects are not traversed.
	 * @param traverse A {@link Traverse} interface with as first argument an object3D object.
	 */
	public void traverseVisible ( Traverse traverse ) {

		if ( this.visible == false ) return;

		traverse.callback(this);

		for ( int i = 0, l = this.children.size(); i < l; i ++ ) {

			this.children.get( i ).traverseVisible( traverse );

		}

	}
	
	/**
	 * Updates local transform.
	 */
	public void updateMatrix()
	{

		this.matrix.compose( this.position, this.quaternion, this.scale );

		this.matrixWorldNeedsUpdate = true;

	}
	
	/**
	 * Updates global transform of the object and its children.
	 * @param force
	 */
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
	
	/**
	 * Creates a new clone of this object and all descendants.
	 */
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

		object.setRenderDepth(this.getRenderDepth());

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
	
	public String toString() {
		return "{class=" + this.getClass().getSimpleName() 
				+ ", id: " + this.getId() + "}";
	}

}
