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

package thothbot.parallax.core.shared.cameras;

/**
 * A general perpose camera, for setting FOV, Lens Focal Length,  
 * and switching between perspective and orthographic views easily.
 * <p>
 * Use this only if you do not wish to manage 
 * both an Orthographic and Perspective Camera
 * <p>
 * The code based on three.js code.
 * 
 * @author thothbot
 *
 */
public class CombinedCamera extends Camera
{

	public double fov;

	public double left;
	public double right;
	public double top;
	public double bottom;

	public double near;
	public double far;

	public double zoom = 1.0;

	private OrthographicCamera cameraO;
	private PerspectiveCamera cameraP;

	public boolean inPersepectiveMode = true;
	public boolean inOrthographicMode = false;

	public CombinedCamera(int width, int height, double fov, double near, double far, double orthonear,
			double orthofar) 
	{
		this.fov = fov;

		this.left = -width / 2.0;
		this.right = width / 2.0;
		this.top = height / 2.0;
		this.bottom = -height / 2.0;

		// We could also handle the projectionMatrix internally, but just wanted
		// to test nested camera objects
		this.cameraO = new OrthographicCamera(width / -2.0, width / 2.0, height / 2.0, height / -2.0,
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

	/**
	 * Switches to the Orthographic camera estimating viewport from Perspective
	 */
	public void toOrthographic()
	{
		double aspect = this.cameraP.getAspectRation();
		double near = this.cameraP.near;
		double far = this.cameraP.far;

		// The size that we set is the mid plane of the viewing frustum
		double hyperfocus = (near + far) / 2.0;

		double halfHeight = Math.tan(this.fov / 2.0) * hyperfocus;
		double planeHeight = 2.0 * halfHeight;
		double planeWidth = planeHeight * aspect;
		double halfWidth = planeWidth / 2.0;

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
		this.cameraP.setAspectRatio(width / height * 1.0);
		this.left = -width / 2.0;
		this.right = width / 2.0;
		this.top = height / 2.0;
		this.bottom = -height / 2.0;
	}

	public void setFov(double fov)
	{
		this.fov = fov;

		if (this.inPersepectiveMode) 
		{
			this.toPerspective();
		} 
		else 
		{
			this.toOrthographic();
		}
	}

	/**
	 * For mantaining similar API with PerspectiveCamera
	 */
	public void updateProjectionMatrix()
	{
		if (this.inPersepectiveMode) 
		{
			this.toPerspective();
		} 
		else 
		{
			this.toPerspective();
			this.toOrthographic();
		}
	}

	/**
	 * Uses Focal Length (in mm) to estimate and set FOV 35mm (fullframe) camera
	 * is used if frame size is not specified; Formula based on
	 * <a href="http://www.bobatkins.com/photography/technical/field_of_view.html">http://www.bobatkins.com</a>
	 */
	public void setLens(int focalLength)
	{
		setLens(focalLength, 24);
	}

	public void setLens(int focalLength, int frameHeight)
	{
		double fov = 2.0 * Math.atan(frameHeight / (focalLength * 2.0)) * (180.0 / Math.PI);

		this.setFov(fov);
	};

	public void setZoom(double zoom)
	{

		this.zoom = zoom;

		if (this.inPersepectiveMode) 
		{
			this.toPerspective();
		} 
		else 
		{
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
		this.rotation.setY(Math.PI);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}
		
	public void toLeftView() {
		this.rotation.setX(0);
		this.rotation.setY(- Math.PI / 2.0);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}

	public void toRightView() {
		this.rotation.setX(0);
		this.rotation.setY(Math.PI / 2.0);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}

	public void toTopView() {
		this.rotation.setX(- Math.PI / 2.0);
		this.rotation.setY(0);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}

	public void toBottomView() {
		this.rotation.setX(Math.PI / 2.0);
		this.rotation.setY(0);
		this.rotation.setZ(0);
		this.rotationAutoUpdate = false;
	}
}
