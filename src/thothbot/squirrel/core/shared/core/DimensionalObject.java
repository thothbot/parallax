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
	 * Getting object ID.
	 * 
	 * @return the object ID
	 */
	public int getId();

	public DimensionalObject getParent();

	public void setParent(DimensionalObject parent);

	public List<DimensionalObject> getChildren();

	public void setChildren(Collection<? extends DimensionalObject> children);

	public Vector3f getPosition();

	public void setPosition(Vector3f position);

	public Vector3f getRotation();

	public void setRotation(Vector3f rotation);

	public void setDynamic(Boolean dynamic);

	public Boolean getDynamic();

	public void setRotationAutoUpdate(Boolean rotationAutoUpdate);

	public Boolean getRotationAutoUpdate();

	public void setName(String name);

	public String getName();

	public Vector3f getScale();

	public void setScale(Vector3f scale);

	public Vector3f getUp();

	public void setUp(Vector3f up);

	public Matrix4f getMatrix();

	public void setMatrix(Matrix4f matrix);

	public Matrix4f getMatrixWorld();

	public void setMatrixWorld(Matrix4f matrixWorld);

	public Matrix4f getMatrixRotationWorld();

	public void setMatrixRotationWorld(Matrix4f rotation);

	public Boolean getMatrixWorldNeedsUpdate();

	public void setMatrixWorldNeedsUpdate(Boolean needsUpdate);

	public Boolean getMatrixAutoUpdate();

	public void setMatrixAutoUpdate(Boolean autoUpdate);

	public Quaternion getQuaternion();

	public void setQuaternion(Quaternion quaternion);

	public Boolean getUseQuaternion();

	public void setUseQuaternion(Boolean use);

	public double getBoundRadius();

	public void setBoundRadius(float boundRadius);

	public double getBoundRadiusScale();

	public void setBoundRadiusScale(float scale);

	public Boolean getVisible();

	public void setVisible(Boolean visible);

	// Methods
	public void translate(float distance, Vector3f axis);

	public void translateX(float distance);

	public void translateY(float distance);

	public void translateZ(float distance);

	public void lookAt(Vector3f vector);

	public <E extends DimensionalObject> void addChild(E child);

	public <E extends DimensionalObject> void removeChild(E child);
	
	public DimensionalObject getChildByName(String name, boolean recursive);

	public void updateMatrix();

	public void update(Matrix4f parentMatrixWorld, Boolean forceUpdate, Camera camera);
	
	public void updateMatrixWorld(boolean force);
	
	public void applyMatrix(Matrix4f matrix);
}
