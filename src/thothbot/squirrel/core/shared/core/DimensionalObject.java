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


public interface DimensionalObject
{
	public abstract int getId();

	public abstract DimensionalObject getParent();

	public abstract void setParent(DimensionalObject parent);

	public abstract List<DimensionalObject> getChildren();

	public abstract void setChildren(Collection<? extends DimensionalObject> children);

	public abstract Vector3f getPosition();

	public abstract void setPosition(Vector3f position);

	public abstract Vector3f getRotation();

	public abstract void setRotation(Vector3f rotation);

	public abstract void setDynamic(Boolean dynamic);

	public abstract Boolean getDynamic();

	public abstract void setRotationAutoUpdate(Boolean rotationAutoUpdate);

	public abstract Boolean getRotationAutoUpdate();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract Vector3f getScale();

	public abstract void setScale(Vector3f scale);

	public abstract Vector3f getUp();

	public abstract void setUp(Vector3f up);

	public abstract Matrix4f getMatrix();

	public abstract void setMatrix(Matrix4f matrix);

	public abstract Matrix4f getMatrixWorld();

	public abstract void setMatrixWorld(Matrix4f matrixWorld);

	public abstract Matrix4f getMatrixRotationWorld();

	public abstract void setMatrixRotationWorld(Matrix4f rotation);

	public abstract Boolean getMatrixWorldNeedsUpdate();

	public abstract void setMatrixWorldNeedsUpdate(Boolean needsUpdate);

	public abstract Boolean getMatrixAutoUpdate();

	public abstract void setMatrixAutoUpdate(Boolean autoUpdate);

	public abstract Quaternion getQuaternion();

	public abstract void setQuaternion(Quaternion quaternion);

	public abstract Boolean getUseQuaternion();

	public abstract void setUseQuaternion(Boolean use);

	public abstract double getBoundRadius();

	public abstract void setBoundRadius(float boundRadius);

	public abstract double getBoundRadiusScale();

	public abstract void setBoundRadiusScale(float scale);

	public abstract Boolean getVisible();

	public abstract void setVisible(Boolean visible);

	// Methods
	public abstract void translate(float distance, Vector3f axis);

	public abstract void translateX(float distance);

	public abstract void translateY(float distance);

	public abstract void translateZ(float distance);

	public abstract void lookAt(Vector3f vector);

	public abstract <E extends DimensionalObject> void addChild(E child);

	public abstract <E extends DimensionalObject> void removeChild(E child);
	
	public abstract DimensionalObject getChildByName(String name, boolean recursive);

	public abstract void updateMatrix();

	public abstract void update(Matrix4f parentMatrixWorld, Boolean forceUpdate, Camera camera);
	
	public abstract void updateMatrixWorld(boolean force);
	
	public abstract void applyMatrix(Matrix4f matrix);

}
