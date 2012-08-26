/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

import java.util.Collection;
import java.util.List;

import thothbot.parallax.core.shared.core.Matrix4;
import thothbot.parallax.core.shared.core.Quaternion;
import thothbot.parallax.core.shared.core.Vector3;

/**
 * Abstract Dimensional Object
 * 
 * @author thothbot
 */
public interface DimensionalObject 
{
	/**
	 * Get object ID.
	 * 
	 * @return the object ID
	 */
	public int getId();

	/**
	 * Get name of the current object.
	 * 
	 * @return the name of the object
	 */
	public String getName();

	/**
	 * Adding child object to the current object
	 * 
	 * @param child
	 *            the Child DimensionalObject
	 */
	public <E extends DimensionalObject> void addChild(E child);

	/**
	 * Remove child DimensionalObject from the current object
	 * 
	 * @param child
	 *            the Child DimensionalObject
	 */
	public <E extends DimensionalObject> void removeChild(E child);

	/**
	 * Get list of children DimensionalObject asictiated with the current
	 * object.
	 * 
	 * @return the list of children DimensionalObject
	 */
	public List<DimensionalObject> getChildren();

	/**
	 * Get child DimensionalObject associated with the current object by its
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
	
	public List<? extends DimensionalObject> getChildrenByClass(Class clazz, boolean recursive);

	/**
	 * Get parent DimensionalObject in which this object is included.
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
	 * Chick if the DimensionalObject visible or not
	 */
	public boolean isVisible();
	
	/**
	 * Set object visibility.
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
	 * Move object on XYZ-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 * @param axis     the axis
	 */
	public void translate(double distance, Vector3 axis);

	/**
	 * Move object on X-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 */
	public void translateX(double distance);

	/**
	 * Move object on Y-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 */
	public void translateY(double distance);

	/**
	 * Move object on Z-axis by defined distance.
	 * 
	 * @param distance the moving distance
	 */
	public void translateZ(double distance);

	/**
	 * Do update of the object's matrix
	 */
	public void updateMatrix();

	public void updateMatrixWorld(boolean force);
}
