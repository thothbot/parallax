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

package thothbot.squirrel.core.shared.cameras;

public class CombinedCamera extends Camera
{

	public float fov;

	public float left;
	public float right;
	public float top;
	public float bottom;

	public float near;
	public float far;

	public float zoom = 1f;

	private OrthographicCamera cameraO;
	private PerspectiveCamera cameraP;

	public boolean inPersepectiveMode = true;
	public boolean inOrthographicMode = false;

	public CombinedCamera(int width, int height, float fov, float near, float far, float orthonear,
			float orthofar) {
		this.fov = fov;

		this.left = -width / 2f;
		this.right = width / 2f;
		this.top = height / 2f;
		this.bottom = -height / 2f;

		// We could also handle the projectionMatrix internally, but just wanted
		// to test nested camera objects
		this.cameraO = new OrthographicCamera(width / -2, width / 2, height / 2, height / -2,
				orthonear, orthofar);
		this.cameraP = new PerspectiveCamera(fov, width / height, near, far);

		toPerspective();
	}

	/*
	 * Switches to the Perspective Camera
	 */
	public void toPerspective()
	{
		this.near = this.cameraP.near;
		this.far = this.cameraP.far;
		this.cameraP.setFieldOfView(this.fov / this.zoom);
		this.cameraP.updateProjectionMatrix();
		this.projectionMatrix = this.cameraP.projectionMatrix;

		this.inPersepectiveMode = true;
		this.inOrthographicMode = false;
	}

	/*
	 * Switches to the Orthographic camera estimating viewport from Perspective
	 */
	public void toOrthographic()
	{
		float aspect = this.cameraP.getAspectRation();
		float near = this.cameraP.near;
		float far = this.cameraP.far;

		// The size that we set is the mid plane of the viewing frustum
		float hyperfocus = (near + far) / 2;

		float halfHeight = (float) (Math.tan(this.fov / 2) * hyperfocus);
		float planeHeight = 2 * halfHeight;
		float planeWidth = planeHeight * aspect;
		float halfWidth = planeWidth / 2;

		halfHeight /= this.zoom;
		halfWidth /= this.zoom;

		this.cameraO.left = -halfWidth;
		this.cameraO.right = halfWidth;
		this.cameraO.top = halfHeight;
		this.cameraO.bottom = -halfHeight;

		// this.cameraO.left = -farHalfWidth;
		// this.cameraO.right = farHalfWidth;
		// this.cameraO.top = farHalfHeight;
		// this.cameraO.bottom = -farHalfHeight;

		// this.cameraO.left = this.left / this.zoom;
		// this.cameraO.right = this.right / this.zoom;
		// this.cameraO.top = this.top / this.zoom;
		// this.cameraO.bottom = this.bottom / this.zoom;

		this.cameraO.updateProjectionMatrix();

		this.near = this.cameraO.near;
		this.far = this.cameraO.far;
		this.projectionMatrix = this.cameraO.projectionMatrix;

		this.inPersepectiveMode = false;
		this.inOrthographicMode = true;
	}

	public void setSize(int width, int height)
	{
		this.cameraP.setAspectRatio((float)width / height);
		this.left = -width / 2f;
		this.right = width / 2f;
		this.top = height / 2f;
		this.bottom = -height / 2f;
	}

	public void setFov(float fov)
	{
		this.fov = fov;

		if (this.inPersepectiveMode) {
			this.toPerspective();
		} else {
			this.toOrthographic();
		}
	}

	/*
	 * For mantaining similar API with PerspectiveCamera
	 */
	public void updateProjectionMatrix()
	{
		if (this.inPersepectiveMode) {
			this.toPerspective();
		} else {
			this.toPerspective();
			this.toOrthographic();
		}
	}

	/*
	 * Uses Focal Length (in mm) to estimate and set FOV 35mm (fullframe) camera
	 * is used if frame size is not specified; Formula based on
	 * http://www.bobatkins.com/photography/technical/field_of_view.html
	 */
	public void setLens(int focalLength)
	{
		setLens(focalLength, 24);
	}

	public void setLens(int focalLength, int frameHeight)
	{
		float fov = (float) (2f * Math.atan(frameHeight / (focalLength * 2f)) * (180f / Math.PI));

		this.setFov(fov);
	};

	public void setZoom(float zoom)
	{

		this.zoom = zoom;

		if (this.inPersepectiveMode) {
			this.toPerspective();
		} else {
			this.toOrthographic();
		}
	}
	
	public void toFrontView() {
		this.rotation.set(0, 0, 0);
		// should we be modifing the matrix instead?
		this.rotationAutoUpdate = false;
	}

	public void toBackView() {
		this.rotation.setX(0);
		this.rotation.setY((float) Math.PI);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}
		
	public void toLeftView() {
		this.rotation.setX(0);
		this.rotation.setY((float) (- Math.PI / 2.0));
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}

	public void toRightView() {
		this.rotation.setX(0);
		this.rotation.setY((float) (Math.PI / 2.0));
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}

	public void toTopView() {
		this.rotation.setX((float) (- Math.PI / 2.0));
		this.rotation.setY(0);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}

	public void toBottomView() {
		this.rotation.setX((float) (Math.PI / 2.0));
		this.rotation.setY(0);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}
}
