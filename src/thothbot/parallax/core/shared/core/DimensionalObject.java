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

import java.util.Collection;
import java.util.List;

import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;

/**
 * Abstract Dimensional Object
 * 
 * @author thothbot
 */
public interface DimensionalObject 
{
	/**
	 * Gets object ID.
	 * 
	 * @return the object ID
	 */
	public int getId();

	/**
	 * Gets name of the current object.
	 * 
	 * @return the name of the object
	 */
	public String getName();

	/**
	 * Adds child object to the current object
	 * 
	 * @param child
	 *            the Child DimensionalObject
	 */
	public <E extends DimensionalObject> void add(E child);

	/**
	 * Removes child DimensionalObject from the current object
	 * 
	 * @param child
	 *            the Child DimensionalObject
	 */
	public <E extends DimensionalObject> void remove(E child);

	/**
	 * Gets list of children DimensionalObject associated with the current
	 * object.
	 * 
	 * @return the list of children DimensionalObject
	 */
	public List<DimensionalObject> getChildren();

	/**
	 * Gets child DimensionalObject associated with the current object by its
	 * name.
	 * 
	 * @param name
	 *            the name of child DimensionalObject
	 * @param recursive
	 *            flag to search in children objects
	 * 
	 * @return the child DimensionalObject
	 */
	public DimensionalObject getChildByName(String name, boolean recursive);
	
	public List<? extends DimensionalObject> getChildrenByClass(Class<?> clazz, boolean recursive);

	/**
	 * Gets parent DimensionalObject in which this object is included.
	 * 
	 * @return the parent DimensionalObject
	 */
	public DimensionalObject getParent();

	public boolean isDynamic();

	public boolean isMatrixAutoUpdate();

	public void setMatrixAutoUpdate(boolean autoUpdate);
	
	public boolean isMatrixWorldNeedsUpdate();

	public void setMatrixWorldNeedsUpdate(boolean needsUpdate);
	
	public boolean isRotationAutoUpdate();

	public void setRotationAutoUpdate(boolean rotationAutoUpdate);
	
	public boolean isUseQuaternion();

	public void setUseQuaternion(boolean use);

	/**
	 * Chicks if the DimensionalObject visible or not
	 */
	public boolean isVisible();
	
	/**
	 * Sets object visibility.
	 * 
	 * @param visible the visibility: true of false
	 */
	public void setVisible(boolean visible);

	public double getBoundRadius();

	public double getBoundRadiusScale();

	public Matrix4 getMatrix();

	public Matrix4 getMatrixRotationWorld();

	public Matrix4 getMatrixWorld();

	public Vector3 getPosition();

	public Quaternion getQuaternion();

	public Vector3 getRotation();

	public Vector3 getScale();

	public Vector3 getUp();

	/**
	 * Looks at specific point
	 * @param vector - the point to look at
	 */
	public void lookAt(Vector3 vector);

	public void setBoundRadius(double boundRadius);

	public void setBoundRadiusScale(double scale);

	public void setChildren(Collection<? extends DimensionalObject> children);

	public void setDynamic(boolean dynamic);

	public void setMatrix(Matrix4 matrix);

	public void setMatrixRotationWorld(Matrix4 rotation);

	public void setMatrixWorld(Matrix4 matrixWorld);
	
	public void setName(String name);

	public void setParent(DimensionalObject parent);

	public void setPosition(Vector3 position);

	public void setQuaternion(Quaternion quaternion);

	public void setRotation(Vector3 rotation);

	public void setScale(Vector3 scale);

	public void setUp(Vector3 up);

	public void applyMatrix(Matrix4 matrix);

	/**
	 * Moves object on XYZ-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 * @param axis     the axis
	 */
	public void translate(double distance, Vector3 axis);

	/**
	 * Moves object on X-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 */
	public void translateX(double distance);

	/**
	 * Moves object on Y-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 */
	public void translateY(double distance);

	/**
	 * Moves object on Z-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 */
	public void translateZ(double distance);
	
	public Vector3 localToWorld( Vector3 vector );
	
	public Vector3 worldToLocal( Vector3 vector );

	public List<DimensionalObject> getDescendants();

	/**
	 * Does update of the object's matrix
	 */
	public void updateMatrix();

	public void updateMatrixWorld(boolean force);
	
	public DimensionalObject clone();
}
