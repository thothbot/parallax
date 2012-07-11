/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

import java.util.Collection;
import java.util.List;

import thothbot.squirrel.core.shared.cameras.Camera;

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
	 * @param child the Child DimensionalObject 
	 */
	public <E extends DimensionalObject> void addChild(E child);
	
	/**
	 * Remove child DimensionalObject from the current object
	 * 
	 * @param child the Child DimensionalObject
	 */
	public <E extends DimensionalObject> void removeChild(E child);

	/**
	 * Get list of children DimensionalObject asictiated with the
	 * current object.
	 * 
	 * @return the list of children DimensionalObject
	 */
	public List<DimensionalObject> getChildren();
	
	/**
	 * Get child DimensionalObject associated with the current
	 * object by its name.
	 * 
	 * @param name      the name of child DimensionalObject
	 * @param recursive flag to search in children objects
	 * 
	 * @return the child DimensionalObject
	 */
	public DimensionalObject getChildByName(String name, boolean recursive);
	
	/**
	 * Get parent DimensionalObject in which this object is included.
	 * 
	 * @return the parent DimensionalObject
	 */
	public DimensionalObject getParent();

	public boolean isDynamic();

	public boolean isMatrixAutoUpdate();
	
	public boolean isMatrixWorldNeedsUpdate();
	
	public boolean isRotationAutoUpdate();
	
	public boolean isUseQuaternion();

	public boolean isVisible();
	
	public double getBoundRadius();

	public double getBoundRadiusScale();

	public Matrix4f getMatrix();

	public Matrix4f getMatrixRotationWorld();

	public Matrix4f getMatrixWorld();

	public Vector3f getPosition();

	public Quaternion getQuaternion();

	public Vector3f getRotation();

	public Vector3f getScale();

	public Vector3f getUp();

	public void lookAt(Vector3f vector);

	public void setBoundRadius(float boundRadius);

	public void setBoundRadiusScale(float scale);

	public void setChildren(Collection<? extends DimensionalObject> children);

	public void setDynamic(boolean dynamic);

	public void setMatrix(Matrix4f matrix);

	public void setMatrixAutoUpdate(boolean autoUpdate);

	public void setMatrixRotationWorld(Matrix4f rotation);

	public void setMatrixWorld(Matrix4f matrixWorld);

	public void setMatrixWorldNeedsUpdate(boolean needsUpdate);

	public void setName(String name);

	public void setParent(DimensionalObject parent);

	public void setPosition(Vector3f position);

	public void setQuaternion(Quaternion quaternion);

	public void setRotation(Vector3f rotation);

	public void setRotationAutoUpdate(boolean rotationAutoUpdate);

	public void setScale(Vector3f scale);

	public void setUp(Vector3f up);

	public void setUseQuaternion(boolean use);

	public void setVisible(boolean visible);
	
	public void applyMatrix(Matrix4f matrix);

	public void translate(float distance, Vector3f axis);

	public void translateX(float distance);
	
	public void translateY(float distance);

	public void translateZ(float distance);

	public void update(Matrix4f parentMatrixWorld, boolean forceUpdate, Camera camera);
	
	public void updateMatrix();
	
	public void updateMatrixWorld(boolean force);
}
