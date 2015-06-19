package thothbot.parallax.core.client.controls;
/*
 * Author Francesca Tosi, francesca.tosi@gmail.com
 * 
 * Adapted from http://threejs.org/examples/js/controls/TransformControls.js
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import thothbot.parallax.core.shared.cameras.Camera;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.GeometryObject;
import thothbot.parallax.core.shared.core.Object3D;
import thothbot.parallax.core.shared.core.Raycaster;
import thothbot.parallax.core.shared.core.Raycaster.Intersect;
import thothbot.parallax.core.shared.geometries.BoxGeometry;
import thothbot.parallax.core.shared.geometries.CylinderGeometry;
import thothbot.parallax.core.shared.geometries.OctahedronGeometry;
import thothbot.parallax.core.shared.geometries.PlaneGeometry;
import thothbot.parallax.core.shared.geometries.TorusGeometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material.SIDE;
import thothbot.parallax.core.shared.materials.MeshBasicMaterial;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Euler;
import thothbot.parallax.core.shared.math.Matrix4;
import thothbot.parallax.core.shared.math.Quaternion;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;
import thothbot.parallax.core.shared.objects.Mesh;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Widget;

public class TransformControls extends Object3D implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, MouseOverHandler /*, MouseOutHandler */ {

	Widget domElement;
	TransformGizmoTranslate gizmoTranslate;
	TransformGizmoRotate gizmoRotate;
	TransformGizmoScale gizmoScale;

	boolean _dragging = false;
	String _mode = TransformControls.Mode.TRANSLATE;
	String _plane = "XY";

	Object3D object = null;
	Double snap = null;
	String space = "world";
	int size = 1;
	String axis = null;

	LocalEvent changeEvent;
	LocalEvent mouseDownEvent;
	LocalEvent mouseUpEvent;
	LocalEvent objectChangeEvent;

	Raycaster ray = new Raycaster();
	Vector3 pointerVector = new Vector3();

	Vector3 point = new Vector3();
	Vector3 offset = new Vector3();

	Vector3 rotation = new Vector3();
	Vector3 offsetRotation = new Vector3();
	double scale = 1;
	double scaleFactor=0.2;

	Matrix4 lookAtMatrix = new Matrix4();
	Vector3 eye = new Vector3();

	Matrix4 tempMatrix = new Matrix4();
	Vector3 tempVector = new Vector3();
	Quaternion tempQuaternion = new Quaternion();
	Vector3 unitX = new Vector3( 1, 0, 0 );
	Vector3 unitY = new Vector3( 0, 1, 0 );
	Vector3 unitZ = new Vector3( 0, 0, 1 );

	Quaternion quaternionXYZ = new Quaternion();
	Quaternion quaternionX = new Quaternion();
	Quaternion quaternionY = new Quaternion();
	Quaternion quaternionZ = new Quaternion();
	Quaternion quaternionE = new Quaternion();

	Vector3 oldPosition = new Vector3();
	Vector3 oldScale = new Vector3();
	Matrix4 oldRotationMatrix = new Matrix4();

	Matrix4 parentRotationMatrix  = new Matrix4();
	Vector3 parentScale = new Vector3();

	Vector3 worldPosition = new Vector3();
	Euler worldRotation = new Euler();
	Matrix4 worldRotationMatrix  = new Matrix4();
	Vector3 camPosition = new Vector3();
	Euler camRotation = new Euler();

	Camera camera;

	Mesh geometry=new Mesh();
	
	public class Mode {
		public final static String ROTATE="rotate";
		public final static String TRANSLATE="translate";
		public final static String SCALE="scale";
	}
	
	public TransformControls(Camera camera, Widget domElement) {
		super();
		if(domElement==null)
			throw new NullPointerException("TransformControl - domElement could not be null ");

		this.camera=camera;
		this.domElement=domElement;

		this.gizmoTranslate = new TransformGizmoTranslate();
		this.gizmoRotate = new TransformGizmoRotate();
		this.gizmoScale = new TransformGizmoScale();

		this.add(this.gizmoTranslate);
		this.add(this.gizmoRotate);
		this.add(this.gizmoScale);

		this.gizmoTranslate.hide();
		this.gizmoRotate.hide();
		this.gizmoScale.hide();

		_dragging = false;
		_mode = TransformControls.Mode.TRANSLATE;
		_plane = "XY";

		changeEvent = new LocalEvent("change",null);
		mouseDownEvent = new LocalEvent("mouseDown",null);
		mouseUpEvent = new LocalEvent("mouseUp",_mode);
		objectChangeEvent = new LocalEvent("objectChange",null);

		this.domElement.addDomHandler(this, MouseDownEvent.getType());
		this.domElement.addDomHandler(this, MouseUpEvent.getType());
		this.domElement.addDomHandler(this, MouseMoveEvent.getType());
		this.domElement.addDomHandler(this, MouseOverEvent.getType());
		
	}
	public GeometryObject getGeometry() {
		return this.geometry;
	}

	/**
	 * Set the factor for scale transform.
	 * 
	 * @param scaleFactor must be between 0 and 1. Default 0.2
	 * 
	 */
	public void setScaleFactor(double scaleFactor) {
		if(scaleFactor<=0 || scaleFactor>1)
			return;
		this.scaleFactor=scaleFactor;
	}
	
	@Override
	public void onMouseOver(MouseOverEvent event) {
		
	}
	
	public void onPointerHover(MouseEvent event) {
		//OnPointerHover

		if (this.object==null || this._dragging) return;

		event.preventDefault();

		MouseEvent pointer = event;

		Intersect intersect = intersectObjects(pointer,this.getTransformGizmo(_mode).pickers.getChildren());

		String axis="";
		if(intersect!=null && intersect.object!=null) {
			axis=intersect.object.getName();
		}

		if(!axis.equals(this.axis)) {
			this.axis=axis;
			this.update();
		}		
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		//onPointerUp
		if ( _dragging && ( this.axis != null ) ) {
			mouseUpEvent.mode = _mode;
		}
		_dragging = false;
		onPointerHover( event );
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		onPointerHover(event);
		onPointerMove(event);
	}

	public void onPointerMove(MouseEvent event) {
		//onPointerMove

		if(this.object==null || this.axis==null || !this._dragging) 
			return;

		event.preventDefault();
		event.stopPropagation();

		MouseEvent pointer = event;

		List<Object3D> tmpList=new ArrayList<Object3D>();
		tmpList.add(this.getTransformGizmo(_mode).activePlane);
		Intersect planeIntersect = intersectObjects(pointer,tmpList);

		if(planeIntersect==null ||  planeIntersect.point==null) 
			return;
		this.point.copy(planeIntersect.point);

		if(TransformControls.Mode.TRANSLATE.equals(_mode)) {
			this.point.sub(this.offset);
			this.point.multiply(this.parentScale);

			if("local".equals(this.space)) {
				this.point.apply(this.tempMatrix.getInverse(this.worldRotationMatrix));

				if(!this.axis.contains("X")) 
					this.point.setX(0); 
				if(!this.axis.contains("Y")) 
					this.point.setY(0); 
				if(!this.axis.contains("Z")) 
					this.point.setZ(0); 

				this.point.apply(this.oldRotationMatrix);

				this.object.getPosition().copy(this.oldPosition);
				this.object.getPosition().add(this.point);
			}
			if("world".equals(this.space) || (this.axis!=null && !this.axis.contains("XYZ"))) {

				if(!this.axis.contains("X")) 
					this.point.setX(0); 
				if(!this.axis.contains("Y")) 
					this.point.setY(0); 
				if(!this.axis.contains("Z")) 
					this.point.setZ(0);

				this.point.apply(this.tempMatrix.getInverse(this.parentRotationMatrix));

				this.object.getPosition().copy(this.oldPosition);
				this.object.getPosition().add(this.point);
			}
			if(this.snap!=null) {
				if(!this.axis.contains("X"))
					this.object.getPosition().setX(Math.round(this.getPosition().getX()/this.snap.doubleValue())*this.snap.doubleValue());
				if(!this.axis.contains("Y"))
					this.object.getPosition().setY(Math.round(this.getPosition().getY()/this.snap.doubleValue())*this.snap.doubleValue());
				if(!this.axis.contains("Z"))
					this.object.getPosition().setZ(Math.round(this.getPosition().getZ()/this.snap.doubleValue())*this.snap.doubleValue());
			}
		} else if(TransformControls.Mode.SCALE.equals(_mode)) {
			this.point.sub(this.offset);
			this.point.multiply(this.parentScale);

			if("local".equals(this.space)) {
				if("XYZ".equals(this.axis)) {
					this.scale = 1.+this.point.getY()*this.scaleFactor;

					this.object.getScale().setX(this.oldScale.getX()*this.scale);
					this.object.getScale().setY(this.oldScale.getY()*this.scale);
					this.object.getScale().setZ(this.oldScale.getZ()*this.scale);
				} else {
					this.point.apply(this.tempMatrix.getInverse(this.worldRotationMatrix));

					if("X".equals(this.axis)) 
						this.object. getScale().setX(this.oldScale.getX()*(1+this.point.getX()*this.scaleFactor));
					if("Y".equals(this.axis)) 
						this.object.getScale().setY(this.oldScale.getY()*(1+this.point.getY()*this.scaleFactor));
					if("Z".equals(this.axis)) 
						this.object.getScale().setZ(this.oldScale.getZ()*(1+this.point.getZ()*this.scaleFactor));
				}
			}
		} else if(TransformControls.Mode.ROTATE.equals(_mode)) {
			this.point.sub(this.worldPosition);
			this.point.multiply(this.parentScale);
			this.tempVector.copy(this.offset).sub(this.worldPosition);
			this.tempVector.multiply(this.parentScale);

			if("E".equals(this.axis)) {
				this.point.apply(this.tempMatrix.getInverse(this.lookAtMatrix));
				this.tempVector.apply(this.tempMatrix.getInverse(this.lookAtMatrix));

				this.rotation.set(Math.atan2(this.point.getZ(), this.point.getY()), Math.atan2(this.point.getX(), this.point.getZ()),Math.atan2(this.point.getY(), this.point.getX()));
				this.offsetRotation.set(Math.atan2(this.tempVector.getZ(), this.point.getY()), Math.atan2(this.tempVector.getX(), this.point.getZ()), Math.atan2(this.tempVector.getY(), this.point.getX()));

				this.tempQuaternion.setFromRotationMatrix(this.tempMatrix.getInverse(this.parentRotationMatrix));

				this.quaternionE.setFromAxisAngle(this.eye, this.rotation.getZ()-this.offsetRotation.getZ());
				this.quaternionXYZ.setFromRotationMatrix(this.worldRotationMatrix);

				this.tempQuaternion.multiply(this.tempQuaternion, this.quaternionE);
				this.tempQuaternion.multiply(this.tempQuaternion, this.quaternionXYZ);

				this.object.getQuaternion().copy(tempQuaternion);
			} else if ("XYZE".equals(this.axis)) {
				Vector3 pointLoc=this.point.clone().cross(this.tempVector).normalize();
				this.quaternionE.setFromEuler(new Euler(pointLoc.getX(), pointLoc.getY(), pointLoc.getZ())); //rotation axis

				this.tempQuaternion.setFromRotationMatrix(this.tempMatrix.getInverse(this.parentRotationMatrix));
				this.quaternionX.setFromAxisAngle(new Vector3(this.quaternionE.getX(),this.quaternionE.getY(),this.quaternionE.getZ()), -point.clone().angleTo(this.tempVector));
				this.quaternionXYZ.setFromRotationMatrix(this.worldRotationMatrix);

				this.tempQuaternion.multiply(this.tempQuaternion,this.quaternionX);
				this.tempQuaternion.multiply(this.tempQuaternion,this.quaternionXYZ);

				this.object.getQuaternion().copy(this.tempQuaternion);
			} else if ("local".equals(this.space)) {
				Matrix4 tmpMatrix4=this.tempMatrix.getInverse(this.worldRotationMatrix);

				this.point.apply(tmpMatrix4);

				this.tempVector.apply(tmpMatrix4);

				this.rotation.set(Math.atan2( point.getZ(), point.getY() ), Math.atan2( point.getX(), point.getZ() ), Math.atan2( point.getY(), point.getX() ));
				this.offsetRotation.set(Math.atan2( tempVector.getZ(), tempVector.getY() ), Math.atan2( tempVector.getX(), tempVector.getZ() ), Math.atan2( tempVector.getY(), tempVector.getX() ));

				this.quaternionXYZ.setFromRotationMatrix(this.oldRotationMatrix);
				this.quaternionX.setFromAxisAngle( this.unitX, this.rotation.getX() - this.offsetRotation.getX() );
				this.quaternionY.setFromAxisAngle( this.unitY, this.rotation.getY() - this.offsetRotation.getY() );
				this.quaternionZ.setFromAxisAngle( this.unitZ, this.rotation.getZ() - this.offsetRotation.getZ() );

				if ( "X".equals(this.axis) ) 
					this.quaternionXYZ.multiply( quaternionXYZ, quaternionX );
				if ( "Y".equals(this.axis) ) 
					this.quaternionXYZ.multiply( quaternionXYZ, quaternionY );
				if ( "Z".equals(this.axis) ) 
					this.quaternionXYZ.multiply( quaternionXYZ, quaternionZ );

				this.object.getQuaternion().copy(this.quaternionXYZ);				
			} else if ( "world".equals(this.space) ) {
				this.rotation.set( Math.atan2( point.getZ(), point.getY() ), Math.atan2( point.getX(), point.getZ() ), Math.atan2( point.getY(), point.getX() ) );
				this.offsetRotation.set( Math.atan2( tempVector.getZ(), tempVector.getY() ), Math.atan2( tempVector.getX(), tempVector.getZ() ), Math.atan2( tempVector.getY(), tempVector.getX() ) );

				this.tempQuaternion.setFromRotationMatrix( this.tempMatrix.getInverse( this.parentRotationMatrix ) );

				this.quaternionX.setFromAxisAngle( unitX, rotation.getX() - offsetRotation.getX() );
				this.quaternionY.setFromAxisAngle( unitY, rotation.getY() - offsetRotation.getY() );
				this.quaternionZ.setFromAxisAngle( unitZ, rotation.getZ() - offsetRotation.getZ() );
				this.quaternionXYZ.setFromRotationMatrix( worldRotationMatrix );

				if ( "X".equals(this.axis) ) tempQuaternion.multiply( tempQuaternion, quaternionX );
				if ( "Y".equals(this.axis) ) tempQuaternion.multiply( tempQuaternion, quaternionY );
				if ( "Z".equals(this.axis) ) tempQuaternion.multiply( tempQuaternion, quaternionZ );

				tempQuaternion.multiply( tempQuaternion, quaternionXYZ );

				this.object.getQuaternion().copy( tempQuaternion );
			}
		}
		this.update();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		//onPointerDown
		if(this.object==null || this._dragging) 
			return;

		event.preventDefault();
		event.stopPropagation();

		MouseDownEvent pointer = event;

		if(pointer.getNativeButton()==com.google.gwt.dom.client.NativeEvent.BUTTON_LEFT) {
			Intersect intersect = this.intersectObjects(pointer, this.getTransformGizmo(_mode).pickers.getChildren());
			if(intersect!=null && intersect.object!=null) {
				this.axis=intersect.object.getName();

				this.update();

				this.eye.copy(this.camPosition).sub(this.worldPosition).normalize();

				this.getTransformGizmo(_mode).setActivePlane(this.axis,this.eye);

				List<Object3D> tmpList=new ArrayList<Object3D>();
				tmpList.add(this.getTransformGizmo(_mode).activePlane);
				Intersect planeIntersect = this.intersectObjects(pointer,tmpList);

				this.oldPosition.copy(this.object.getPosition());
				this.oldScale.copy(this.object.getScale());

				this.oldRotationMatrix.extractRotation(this.object.getMatrix());
				this.worldRotationMatrix.extractRotation(this.object.getMatrixWorld());

				this.parentRotationMatrix.extractRotation(this.object.getParent().getMatrixWorld());
				this.parentScale.setFromMatrixScale(this.tempMatrix.getInverse(this.object.getParent().getMatrixWorld()));

				if(planeIntersect!=null && planeIntersect.point!=null)	
					this.offset.copy(planeIntersect.point);
			}
		}
		this._dragging=true;
	}

	private Intersect intersectObjects(MouseEvent<?> pointer,List<Object3D> objects ) {
		
		double posX = (pointer.getX() / (double) this.domElement.getOffsetWidth() ) * 2.0 - 1.0; 
		double posY = - (pointer.getY() / (double) this.domElement.getOffsetHeight() ) * 2.0 + 1.0;
		
		this.pointerVector.set(posX, posY,0.5);
		this.pointerVector.unproject(this.camera);
		this.ray.set(this.camera.getPosition(), this.pointerVector.sub(this.camera.getPosition()).normalize());
		List<Intersect> intersections = this.ray.intersectObjects(objects,true);
		if(intersections!=null) {
			if(intersections.size()>0)
				return intersections.get(0);
		}
		return null;
	}

	public void update() {

		if (this.object==null) return;
		this.object.updateMatrixWorld(true);

		this.worldPosition.setFromMatrixPosition(this.object.getMatrixWorld());
		this.worldRotation.setFromRotationMatrix(this.tempMatrix.extractRotation(this.object.getMatrixWorld()));

		this.camera.updateMatrixWorld(true); 
		this.camPosition.setFromMatrixPosition(camera.getMatrixWorld());
		this.camRotation.setFromRotationMatrix(this.tempMatrix.extractRotation(camera.getMatrixWorld()));

		this.scale = (this.worldPosition.distanceTo(camPosition)/6.)*this.size;
		this.position.copy(this.worldPosition);
		this.eye.copy(this.camPosition).sub(this.worldPosition).normalize();

		TransformGizmo transformation = this.getTransformGizmo(_mode);
		if ("local".equals(this.space))
			transformation.update(this.worldRotation, this.eye);
		else if("world".equals(this.space)) 
			transformation.update(new Euler(), this.eye);
		transformation.highlight(this.axis);
	}

	public TransformGizmo getTransformGizmo(String mode) {
		if(TransformControls.Mode.TRANSLATE.equals(mode)) {
			return this.gizmoTranslate;
		} else if(TransformControls.Mode.ROTATE.equals(mode)) {
			return this.gizmoRotate;
		} else /*if("scale".equals(mode))*/ {
			return this.gizmoScale;
		}
	}

	public void setSize (int size ) {
		this.size = size;
		this.update();
	}

	public void setSpace (String space ) {
		this.space = space;
		this.update();
	}

	public void setSnap (Double snap ) {
		this.snap = snap;
	}

	public void setMode (String mode ) {
		_mode = mode!=null ? mode : _mode;
		GWT.log("mode: "+mode+"  _mode: "+_mode);
		if (TransformControls.Mode.SCALE.equals(_mode)) 
			this.space = "local";

		this.gizmoTranslate.hide();
		this.gizmoRotate.hide();
		this.gizmoScale.hide();

		this.setGeometry(_mode);

		this.showMode(_mode);

		this.update();
	}

	private void setGeometry(String mode) {
		if(MyTransformControls.Mode.TRANSLATE.equals(mode)) {
//			this.gizmoTranslate.show();
		} else if(MyTransformControls.Mode.ROTATE.equals(mode)) {
//			this.gizmoRotate.show();
		} else if(MyTransformControls.Mode.SCALE.equals(mode)) {
			this.geometry=this.gizmoScale.getGeometry();
		}
	}

	private void showMode(String mode) {
		if(TransformControls.Mode.TRANSLATE.equals(mode)) {
			this.gizmoTranslate.show();
		} else if(TransformControls.Mode.ROTATE.equals(mode)) {
			this.gizmoRotate.show();
		} else if(TransformControls.Mode.SCALE.equals(mode)) {
			this.gizmoScale.show();
		}
	}

	public void detach (Object3D object ) {
		this.object=null;
		this.axis = null;

		this.gizmoTranslate.hide();
		this.gizmoRotate.hide();
		this.gizmoScale.hide();
	}

	public void attach(Object3D object ) {
		this.object=object;
		this.gizmoTranslate.hide();
		this.gizmoRotate.hide();
		this.gizmoScale.hide();
		this.showMode(_mode);

		this.update();
	}

	public class LocalEvent {
		public String type;
		public String mode;

		public LocalEvent(String type, String mode) {
			this.type=type;
			this.mode=mode;
		}
	}

	public class TransformGizmoScale extends TransformGizmo {

		Mesh geometryScale = new Mesh();
		
		public TransformGizmoScale() {
			super();
			
			Geometry arrowGeometry = new Geometry();
			Mesh mesh = new Mesh(new BoxGeometry(0.125,0.125,0.125));
			mesh.getPosition().setY(0.5);
			mesh.updateMatrix();

			arrowGeometry.merge((Geometry)mesh.getGeometry(),mesh.getMatrix());

			Geometry lineXGeometry = new Geometry();
			lineXGeometry.getVertices().add(new Vector3(0,0,0));
			lineXGeometry.getVertices().add(new Vector3( 1,0,0));

			Geometry lineYGeometry = new Geometry();
			lineYGeometry.getVertices().add(new Vector3(0,0,0));
			lineYGeometry.getVertices().add(new Vector3(0,1,0));

			Geometry lineZGeometry = new Geometry();
			lineZGeometry.getVertices().add(new Vector3(0,0,0));
			lineZGeometry.getVertices().add(new Vector3(0,0,1));

			this.handleGizmos=new HashMap<String, ArrayList<HandleGizmos>>();
			{
				//X
				ArrayList<HandleGizmos> xList=new ArrayList<HandleGizmos>();
				xList.add(new HandleGizmos(new Mesh( arrowGeometry, new GizmoMaterial(new Color(0xff0000),null)), new Vector3(0.5,0,0), new Vector3(0,0,-Math.PI/2.)));
				xList.add(new HandleGizmos(new Line( lineXGeometry, new GizmoLineMaterial(new Color(0xff0000))), null, null));
				this.handleGizmos.put("X",xList);
				//Y
				ArrayList<HandleGizmos> yList=new ArrayList<HandleGizmos>();
				yList.add(new HandleGizmos(new Mesh( arrowGeometry, new GizmoMaterial(new Color(0x00ff00),null)), new Vector3(0,0.5,0), null));
				yList.add(new HandleGizmos(new Line( lineYGeometry, new GizmoLineMaterial(new Color(0x00ff00))), null, null));
				this.handleGizmos.put("Y",yList);
				//Z
				ArrayList<HandleGizmos> zList=new ArrayList<HandleGizmos>();
				zList.add(new HandleGizmos(new Mesh( arrowGeometry, new GizmoMaterial(new Color(0x0000ff),null)), new Vector3(0,0,0.5), new Vector3(Math.PI/2.,0,0)));
				zList.add(new HandleGizmos(new Line( lineZGeometry, new GizmoLineMaterial(new Color(0x0000ff))), null, null));
				this.handleGizmos.put("Z",zList);
				//XYZ
				ArrayList<HandleGizmos> xyzList=new ArrayList<HandleGizmos>();
				xyzList.add(new HandleGizmos(new Mesh( new BoxGeometry(0.125,0.125,0.125), new GizmoMaterial(new Color(0xffffff),0.25)), null, null));
				this.handleGizmos.put("XYZ",xyzList);

			}
			this.pickerGizmos=new HashMap<String, ArrayList<HandleGizmos>>();
			{
				//X
				ArrayList<HandleGizmos> xList=new ArrayList<HandleGizmos>();
				CylinderGeometry obj_xx=new CylinderGeometry(0.2,0.,1,4,1);
				Object3D obj_x=new Mesh(obj_xx , new GizmoMaterial(new Color(0xff0000),.25));
				xList.add(new HandleGizmos(obj_x, new Vector3(0.6,0,0), new Vector3(0,0,-Math.PI/2.)));
				this.pickerGizmos.put("X",xList);
				obj_x.setName("X");
				geometryScale.add (obj_x);
				GWT.log("SIZE: "+geometryScale.getChildren().size());
				//Y
				ArrayList<HandleGizmos> yList=new ArrayList<HandleGizmos>();
				Object3D obj_y=new Mesh( new CylinderGeometry(0.2,0.,1,4,1), new GizmoMaterial(new Color(0x00ff00),.25));
				yList.add(new HandleGizmos(obj_y, new Vector3(0,0.6,0), null));
				this.pickerGizmos.put("Y",yList);
				obj_x.setName("Y");
				geometryScale.add(obj_y);
				GWT.log("SIZE: "+geometryScale.getChildren().size());
				//Z
				ArrayList<HandleGizmos> zList=new ArrayList<HandleGizmos>();
				Object3D obj_z=new Mesh( new CylinderGeometry(0.2,0.,1,4,1), new GizmoMaterial(new Color(0x0000ff),0.25));
				zList.add(new HandleGizmos(obj_z, new Vector3(0,0,0.6), new Vector3(Math.PI/2.,0,0)));
				this.pickerGizmos.put("Z",zList);
				obj_x.setName("Z");
				geometryScale.add(obj_z);
				GWT.log("SIZE: "+geometryScale.getChildren().size());
				//XYZ
				ArrayList<HandleGizmos> xyzList=new ArrayList<HandleGizmos>();
				Object3D obj_xyz=new Mesh( new BoxGeometry(0.4,0.4,0.4), new GizmoMaterial(new Color(0x000000),0.25));
				xyzList.add(new HandleGizmos(obj_xyz, null, null));
				this.pickerGizmos.put("XYZ",xyzList);
				obj_x.setName("XYZ");
				geometryScale.add(obj_xyz);
				GWT.log("SIZE: "+geometryScale.getChildren().size());
			}

			this.init();
		}

		public Mesh getGeometry() {
			return geometryScale;
		}
		
		@Override
		public void setActivePlane(String axis, Vector3 eye) {

			Matrix4 tempMatrix = new Matrix4();
			eye.apply(tempMatrix.getInverse( tempMatrix.extractRotation( this.planes.getObjectByName("XY",true).getMatrixWorld())));

			if ("X".equals(axis)) {
				this.activePlane = this.planes.getObjectByName("XY",true);
				if(Math.abs(eye.getY()) > Math.abs(eye.getZ())) 
					this.activePlane = this.planes.getObjectByName("XZ",true);
			}
			if ("Y".equals(axis)){
				this.activePlane = this.planes.getObjectByName("XY",true);
				if ( Math.abs(eye.getX()) > Math.abs(eye.getZ()) ) 
					this.activePlane = this.planes.getObjectByName("YZ",true);
			}
			if ("Z".equals(axis)){
				this.activePlane = this.planes.getObjectByName("XZ",true);
				if ( Math.abs(eye.getX()) > Math.abs(eye.getY()) ) 
					this.activePlane = this.planes.getObjectByName("YZ",true);
			}
			if ("XYZ".equals(axis)) 
				this.activePlane = this.planes.getObjectByName("XYZE",true);

			this.hide();
			this.show();
		}		
	}

	public class TransformGizmoRotate extends TransformGizmo {

		public TransformGizmoRotate() {
			super();

			this.handleGizmos=new HashMap<String, ArrayList<HandleGizmos>>();
			{
				//X
				ArrayList<HandleGizmos> xList=new ArrayList<HandleGizmos>();
				xList.add(new HandleGizmos(new Line( CircleGeometry(1.,"x",0.5), new GizmoLineMaterial(new Color(0xff0000))), null, null));
				this.handleGizmos.put("X",xList);
				//Y
				ArrayList<HandleGizmos> yList=new ArrayList<HandleGizmos>();
				yList.add(new HandleGizmos(new Line( CircleGeometry(1.,"y",0.5), new GizmoLineMaterial(new Color(0x00ff00))), null, null));
				this.handleGizmos.put("Y",yList);
				//Z
				ArrayList<HandleGizmos> zList=new ArrayList<HandleGizmos>();
				zList.add(new HandleGizmos(new Line( CircleGeometry(1.,"z",0.5), new GizmoLineMaterial(new Color(0x0000ff))), null, null));
				this.handleGizmos.put("Z",zList);
				//E
				ArrayList<HandleGizmos> eList=new ArrayList<HandleGizmos>();
				eList.add(new HandleGizmos(new Line( CircleGeometry(1.25,"z",1.), new GizmoLineMaterial(new Color(0xcccc00))), null, null));
				this.handleGizmos.put("E",eList);
				//XYZE
				ArrayList<HandleGizmos> xyzeList=new ArrayList<HandleGizmos>();
				xyzeList.add(new HandleGizmos(new Line( CircleGeometry(1.,"z",1.), new GizmoLineMaterial(new Color(0x787878))), null, null));
				this.handleGizmos.put("XYZE",xyzeList);
			}

			this.pickerGizmos = new HashMap<String, ArrayList<HandleGizmos>>();
			{
				//X
				ArrayList<HandleGizmos> xList=new ArrayList<HandleGizmos>();
				xList.add(new HandleGizmos(new Mesh( new TorusGeometry(1.,0.12,4,12,Math.PI), new GizmoMaterial(new Color(0xff0000),0.25)), new Vector3(0,0,0), new Vector3(0,-Math.PI/2.,-Math.PI/2.)));
				this.pickerGizmos.put("X",xList);
				//Y
				ArrayList<HandleGizmos> yList=new ArrayList<HandleGizmos>();
				yList.add(new HandleGizmos(new Mesh( new TorusGeometry(1.,0.12,4,12,Math.PI), new GizmoMaterial(new Color(0x00ff00),0.25)), new Vector3(0,0,0), new Vector3(Math.PI/2.,0,0)));
				this.pickerGizmos.put("Y",yList);
				//Z
				ArrayList<HandleGizmos> zList=new ArrayList<HandleGizmos>();
				zList.add(new HandleGizmos(new Mesh( new TorusGeometry(1., 0.12, 4,12,Math.PI), new GizmoMaterial(new Color(0x0000ff),0.25)), new Vector3(0,0,0), new Vector3(0,0,-Math.PI/2.)));
				this.pickerGizmos.put("Z",zList);
				//E
				ArrayList<HandleGizmos> eList=new ArrayList<HandleGizmos>();
				eList.add(new HandleGizmos(new Mesh( new TorusGeometry(1.25,0.12,2,24), new GizmoMaterial(new Color(0xcccc00),0.25)), null, null));
				this.pickerGizmos.put("E",eList);
				//XYZE
				ArrayList<HandleGizmos> xyzeList=new ArrayList<HandleGizmos>();
				xyzeList.add(new HandleGizmos(new Mesh(new Geometry()), null, null));
				this.pickerGizmos.put("XYZE",xyzeList);
			}
			this.init();
		}

		public class Models {
			public Map<String,ArrayList<HandleGizmos>> handles ;
			public Map<String,ArrayList<HandleGizmos>> pickers ;
		}

		public void update(Euler rotation, Vector3 eyeP) {
			super.update(rotation, eyeP); 

			Matrix4 tempMatrix = new Matrix4();
			final Euler worldRotation = new Euler(0,0,1);
			final Quaternion tempQuaternion = new Quaternion();
			final Vector3 unitX = new Vector3(1,0,0);
			final Vector3 unitY = new Vector3(0,1,0);
			final Vector3 unitZ = new Vector3(0,0,1);

			final Quaternion quaternionX = new Quaternion();
			final Quaternion quaternionY = new Quaternion();
			final Quaternion quaternionZ = new Quaternion();

			final Vector3 eye = eyeP.clone();

			worldRotation.copy(this.planes.getObjectByName("XY",true).getRotation());
			tempQuaternion.setFromEuler(worldRotation);

			tempMatrix.makeRotationFromQuaternion(tempQuaternion).getInverse(tempMatrix);
			eye.apply(tempMatrix);

			this.traverse(new Traverse() {
				@Override
				public void callback(Object3D child) {
					tempQuaternion.setFromEuler(worldRotation);
					
					if ("X".equals(child.getName())) {
						quaternionX.setFromAxisAngle( unitX, Math.atan2(-eye.getY(),eye.getZ()));
						tempQuaternion.multiply(tempQuaternion,quaternionX);
						child.getQuaternion().copy(tempQuaternion);
					}
					if ("Y".equals(child.getName())) {
						quaternionY.setFromAxisAngle(unitY,Math.atan2(eye.getX(),eye.getZ()));
						tempQuaternion.multiply(tempQuaternion,quaternionY);
						child.getQuaternion().copy(tempQuaternion);
					}
					if ("Z".equals(child.getName())) {
						quaternionZ.setFromAxisAngle(unitZ,Math.atan2(eye.getY(),eye.getX()));
						tempQuaternion.multiply(tempQuaternion,quaternionZ);
						child.getQuaternion().copy(tempQuaternion);
					}					
				}
			});
		}

		//Mettiamo il secondo parametro anche se non utilizzato
		@Override
		public void setActivePlane(String  axis, Vector3 eye) {
			if ( "E".equals(axis) ) this.activePlane = this.planes.getObjectByName("XYZE",true);
			if ( "X".equals(axis) ) this.activePlane = this.planes.getObjectByName("YZ",true);
			if ( "Y".equals(axis) ) this.activePlane = this.planes.getObjectByName("XZ",true);
			if ( "Z".equals(axis) ) this.activePlane = this.planes.getObjectByName("XY",true);
			
			this.hide();
			this.show();
		}

		public Geometry CircleGeometry(Double radius, String facing, Double arc ) {
			Geometry geometry = new Geometry();
			arc = (arc!=null) ? arc : 1;
			for ( int i = 0; i <= 64 * arc; ++i ) {
				if ( "x".equals(facing) ) 
					geometry.getVertices().add( new Vector3( 0, Math.cos( (double)i / 32. * Math.PI ), Math.sin( (double)i / 32. * Math.PI ) ).multiply(radius) );
				if ( "y".equals(facing) ) 
					geometry.getVertices().add( new Vector3( Math.cos( (double)i / 32. * Math.PI ), 0, Math.sin( (double)i / 32. * Math.PI ) ).multiply(radius) );
				if ( "z".equals(facing) ) 
					geometry.getVertices().add( new Vector3( Math.sin( (double)i / 32. * Math.PI ), Math.cos( (double)i / 32. * Math.PI ), 0 ).multiply(radius) );
			}
			return geometry;
		}
	}

	public class TransformGizmoTranslate extends TransformGizmo {

		public TransformGizmoTranslate() {
			super();

			Geometry arrowGeometry = new Geometry();
			Mesh mesh = new Mesh(new CylinderGeometry( 0, 0.05, 0.2, 12, 1));
			mesh.getPosition().setY(0.5);
			mesh.updateMatrix();

			arrowGeometry.merge((Geometry)mesh.getGeometry(),mesh.getMatrix());

			Geometry lineXGeometry = new Geometry();
			lineXGeometry.getVertices().add(new Vector3( 0, 0, 0 ));
			lineXGeometry.getVertices().add(new Vector3( 1, 0, 0 ));

			Geometry lineYGeometry = new Geometry();
			lineYGeometry.getVertices().add(new Vector3( 0, 0, 0 ));
			lineYGeometry.getVertices().add(new Vector3( 0, 1, 0 ));

			Geometry lineZGeometry = new Geometry();
			lineZGeometry.getVertices().add(new Vector3( 0, 0, 0 ));
			lineZGeometry.getVertices().add(new Vector3( 0, 0, 1 ));

			this.handleGizmos=new HashMap<String, ArrayList<HandleGizmos>>();
			{
				//X
				ArrayList<HandleGizmos> xList=new ArrayList<HandleGizmos>();
				xList.add(new HandleGizmos(new Mesh( arrowGeometry.clone(), new GizmoMaterial(new Color(0xff0000),null)), new Vector3(0.5,0,0), new Vector3(0, 0, -Math.PI/2.)));
				xList.add(new HandleGizmos(new Line( lineXGeometry, new GizmoLineMaterial(new Color(0xff0000))), null, null));
				this.handleGizmos.put("X",xList);
				//Y
				ArrayList<HandleGizmos> yList=new ArrayList<HandleGizmos>();
				yList.add(new HandleGizmos(new Mesh( arrowGeometry.clone(), new GizmoMaterial(new Color(0x00ff00),null)), new Vector3(0,0.5,0), null));
				yList.add(new HandleGizmos(new Line( lineYGeometry, new GizmoLineMaterial(new Color(0x00ff00))), null, null));
				this.handleGizmos.put("Y",yList);
				//Z
				ArrayList<HandleGizmos> zList=new ArrayList<HandleGizmos>();
				zList.add(new HandleGizmos(new Mesh( arrowGeometry.clone(), new GizmoMaterial(new Color(0x0000ff),null)), new Vector3(0,0,0.5), new Vector3(Math.PI/2.,0,0)));
				zList.add(new HandleGizmos(new Line( lineZGeometry, new GizmoLineMaterial(new Color(0x0000ff))), null, null));
				this.handleGizmos.put("Z",zList);
				//XYZ
				ArrayList<HandleGizmos> xyzList=new ArrayList<HandleGizmos>();
				xyzList.add(new HandleGizmos(new Mesh(new OctahedronGeometry( 0.1, 0), new GizmoMaterial(new Color(0xffffff),0.25)), new Vector3(0,0,0), new Vector3(0,0,0)));
				this.handleGizmos.put("XYZ",xyzList);
				//XY
				ArrayList<HandleGizmos> xyList=new ArrayList<HandleGizmos>();
				xyList.add(new HandleGizmos(new Mesh(new PlaneGeometry( 0.29, 0.29), new GizmoMaterial(new Color(0xffff00),0.25)), new Vector3(0.15,0.15,0), null));
				this.handleGizmos.put("XY",xyList);
				//YZ
				ArrayList<HandleGizmos> yzList=new ArrayList<HandleGizmos>();
				yzList.add(new HandleGizmos(new Mesh(new PlaneGeometry(0.29, 0.29), new GizmoMaterial(new Color(0x00ffff),0.25)), new Vector3(0,0.15,0.15), new Vector3(0,Math.PI/2.,0)));
				this.handleGizmos.put("YZ",yzList);
				//XZ
				ArrayList<HandleGizmos> xzList=new ArrayList<HandleGizmos>();
				xzList.add(new HandleGizmos(new Mesh(new PlaneGeometry(0.29, 0.29), new GizmoMaterial(new Color(0xff00ff),0.25)), new Vector3(0.15,0,0.15), new Vector3(-Math.PI/2.,0,0)));
				this.handleGizmos.put("XZ",xzList);
			}
			this.pickerGizmos=new HashMap<String, ArrayList<HandleGizmos>>();
			{
				//X
				ArrayList<HandleGizmos> xList=new ArrayList<HandleGizmos>();
				xList.add(new HandleGizmos(new Mesh( new CylinderGeometry( 0.2, 0., 1., 4, 1 ), new GizmoMaterial(new Color(0xff0000),0.25)), new Vector3(0.6,0,0), new Vector3(0, 0, -Math.PI/2.)));
				this.pickerGizmos.put("X",xList);
				//Y
				ArrayList<HandleGizmos> yList=new ArrayList<HandleGizmos>();
				yList.add(new HandleGizmos(new Mesh( new CylinderGeometry( 0.2, 0., 1., 4, 1 ), new GizmoMaterial(new Color(0x00ff00),0.25)), new Vector3(0,0.6,0), null));
				this.pickerGizmos.put("Y",yList);
				//Z
				ArrayList<HandleGizmos> zList=new ArrayList<HandleGizmos>();
				zList.add(new HandleGizmos(new Mesh( new CylinderGeometry( 0.2, 0., 1., 4, 1 ), new GizmoMaterial(new Color(0x0000ff),0.25)), new Vector3(0,0,0.6), new Vector3(Math.PI/2.,0,0)));
				this.pickerGizmos.put("Z",zList);
				//XYZ
				ArrayList<HandleGizmos> xyzList=new ArrayList<HandleGizmos>();
				xyzList.add(new HandleGizmos(new Mesh( new OctahedronGeometry( 0.2, 0 ), new GizmoMaterial(new Color(0xffffff),0.25)), null, null));
				this.pickerGizmos.put("XYZ",xyzList);
				//XY
				ArrayList<HandleGizmos> xyList=new ArrayList<HandleGizmos>();
				xyList.add(new HandleGizmos(new Mesh( new PlaneGeometry( 0.4, 0.4 ), new GizmoMaterial(new Color(0xffff00),0.25)), new Vector3(0.2,0.2,0), null));
				this.pickerGizmos.put("XY",xyList);
				//YZ
				ArrayList<HandleGizmos> yzList=new ArrayList<HandleGizmos>();
				yzList.add(new HandleGizmos(new Mesh( new PlaneGeometry( 0.4, 0.4 ), new GizmoMaterial(new Color(0x00ffff),0.25)), new Vector3(0,0.2,0.2), new Vector3(0,Math.PI/2.,0)));
				this.pickerGizmos.put("YZ",yzList);
				//XZ
				ArrayList<HandleGizmos> xzList=new ArrayList<HandleGizmos>();
				xzList.add(new HandleGizmos(new Mesh( new PlaneGeometry( 0.4, 0.4 ), new GizmoMaterial(new Color(0xff00ff),0.25)), new Vector3(0.2,0,0.2), new Vector3(-Math.PI/2.,0,0)));
				this.pickerGizmos.put("XZ",xzList);
			}

			this.init();
		}

		@Override
		public void setActivePlane(String axis, Vector3 eye) {

			Matrix4 tempMatrix = new Matrix4();

			eye.apply(tempMatrix.getInverse(tempMatrix.extractRotation( this.planes.getObjectByName("XY", true).getMatrixWorld() ) ));

			if ( "X".equals(axis) ) {
				this.activePlane = this.planes.getObjectByName("XY", true);
				if(Math.abs(eye.getY()) > Math.abs(eye.getZ())) 
					this.activePlane = this.planes.getObjectByName("XZ", true);
			}
			if ( "Y".equals(axis) ){
				this.activePlane = this.planes.getObjectByName("XY", true);
				if(Math.abs(eye.getX()) > Math.abs(eye.getZ())) 
					this.activePlane = this.planes.getObjectByName("YZ", true);
			}
			if ( "Z".equals(axis) ){
				this.activePlane = this.planes.getObjectByName("XZ", true);
				if(Math.abs(eye.getX()) > Math.abs(eye.getY())) 
					this.activePlane = this.planes.getObjectByName("YZ", true);
			}

			if ( "XYZ".equals(axis) ) 
				this.activePlane = this.planes.getObjectByName("XYZE",true);
			if ( "XY".equals(axis) ) 
				this.activePlane = this.planes.getObjectByName("XY",true);
			if ("YZ".equals(axis) )
				this.activePlane = this.planes.getObjectByName("YZ",true);
			if ( "XZ".equals(axis) )
				this.activePlane =this.planes.getObjectByName("XZ",true);

			this.hide();
			this.show();
		}
	}

	abstract class TransformGizmo extends Object3D {
		boolean showPickers = false; //debug
		boolean showActivePlane = false; //debug

		Object3D handles;
		Object3D pickers;
		Object3D planes;

		Map<String,ArrayList<HandleGizmos>> handleGizmos;
		Map<String,ArrayList<HandleGizmos>> pickerGizmos;

		public Object3D activePlane;

		public TransformGizmo() {
			super();
		}

		abstract public void setActivePlane(String axis, Vector3 eye);

		public void init() {
			this.handles = new Object3D();
			this.pickers = new Object3D();
			this.planes = new Object3D();

			this.add(this.handles);
			this.add(this.pickers);
			this.add(this.planes);

			//// PLANES
			PlaneGeometry planeGeometry = new PlaneGeometry( 50, 50, 2, 2 );
			MeshBasicMaterial planeMaterial = new MeshBasicMaterial();
			planeMaterial.setWireframe(true);
			planeMaterial.setSide(SIDE.DOUBLE);

			Map<String,Mesh> planesLocal = new HashMap<String,Mesh>();

			planesLocal.put("XY",new Mesh(planeGeometry.clone(),planeMaterial));
			planesLocal.put("YZ",new Mesh(planeGeometry.clone(),planeMaterial));
			planesLocal.put("XZ",new Mesh(planeGeometry.clone(),planeMaterial));
			planesLocal.put("XYZE",new Mesh(planeGeometry,planeMaterial));

			this.activePlane = planesLocal.get("XYZE");

			planesLocal.get("YZ").getRotation().set( 0, Math.PI/2., 0 );
			planesLocal.get("XZ").getRotation().set( -Math.PI/2., 0, 0 );

			for(Entry<String, Mesh> entry:planesLocal.entrySet()) { 
				Mesh mesh = entry.getValue();
				mesh.setName(entry.getKey()+"");
				this.planes.add(mesh);
				mesh.setVisible(false);
			}

			this.setupGizmos(handleGizmos, this.handles);
			this.setupGizmos(pickerGizmos, this.pickers);

			this.traverse(new Traverse() {

				@Override
				public void callback(Object3D child) {
					if (child instanceof Mesh) {
						Mesh localChild=(Mesh) child;
						localChild.updateMatrix();

						Geometry tempGeometry = new Geometry();
						tempGeometry.merge( (Geometry)localChild.getGeometry(), child.getMatrix() );

						((Mesh) child).setGeometry(tempGeometry);
						child.setPosition(new Vector3(0, 0, 0));
						child.setRotation(new Euler(0, 0, 0));
						child.setScale(new Vector3( 1, 1, 1));
					}
				}
			});
		}

		public void update(final Euler rotation, final Vector3 eye ) {

			final Vector3 vec1 = new Vector3( 0, 0, 0 );
			final Vector3 vec2 = new Vector3( 0, 1, 0 );
			final Matrix4 lookAtMatrix = new Matrix4();

			this.traverse(new Traverse() {
				@Override
				public void callback(Object3D child) {
					if(child.getName().contains("E")) {
						child.getQuaternion().setFromRotationMatrix(lookAtMatrix.lookAt(eye, vec1, vec2));
					} else if(child.getName().contains("X") || child.getName().contains("Y") || child.getName().contains("Z")) {
						child.getQuaternion().setFromEuler(rotation);
					}
				}
			});
		};

		public void show() {
			this.traverse(new Traverse() {
				@Override
				public void callback(Object3D child) {
					child.setVisible(true);
					if (child.getParent().equals(pickers)) 
						child.setVisible(showPickers);
					if (child.getParent().equals(planes))
						child.setVisible(false);
				}
			});
			this.activePlane.setVisible(showActivePlane);
		}

		public void highlight(final String axis) {
			this.traverse(new Traverse() {
				@Override
				public void callback(Object3D child) {
					if(child instanceof GeometryObject) {
						GeometryObject geoLoc=(GeometryObject)child;
						if(geoLoc.getMaterial() instanceof HasHighlight) {
							HasHighlight material = (HasHighlight) geoLoc.getMaterial();
							if ( geoLoc.getName().equals(axis) ) {
								material.highlight(true);
							} else {
								material.highlight(false);
							}
						}
					}
				}
			});
		}

		public void hide() {
			this.traverse(new Traverse() {
				@Override
				public void callback(Object3D child) {
					child.setVisible(false);
				}
			});
		}

		private void setupGizmos(Map<String,ArrayList<HandleGizmos>> gizmoMap, Object3D parent) {
			for (String key : gizmoMap.keySet()) {
				ArrayList<HandleGizmos> gizmoList=gizmoMap.get(key);
				for(HandleGizmos gizmo:gizmoList) {
					Object3D object=gizmo.getGeometry();
					Vector3 position=gizmo.getPos();
					Vector3 rotation=gizmo.getRot();

					object.setName(key);
					if(position!=null) { 
						object.getPosition().set(position.getX(), position.getY(), position.getZ());
					}
					if(rotation!=null) {  
						object.getRotation().set(rotation. getX(), rotation. getY(), rotation. getZ());
					}
					parent.add(object);
				}
			}
		}
	}

	public class HandleGizmos {
		//private GeometryObject geometry;
		private Object3D geometry;
		//private AbstractGeometry geometry;
		private Vector3 pos=new Vector3();
		private Vector3 rot=new Vector3();

		public HandleGizmos(Object3D geometry, Vector3 pos, Vector3 rot) {
			this.geometry=geometry;
			if(pos!=null)
				this.pos=pos;
			if(rot!=null)
				this.rot=rot;
		}
		public Object3D getGeometry() {
			return this.geometry;
		}
		public Vector3 getPos() {
			return this.pos;
		}
		public Vector3 getRot() {
			return this.rot;
		}		
	}

	public class GizmoLineMaterial extends LineBasicMaterial implements HasHighlight {

		public Color oldColor;
		private double opacity;
		private double oldOpacity;

		public GizmoLineMaterial(Color color) {
			super();

			this.setDepthTest(false);
			this.setDepthWrite(false);
			this.setTransparent(true);
			this.setLinewidth(1.);

			this.setColor(color);
			this.oldColor=this.getColor().clone();
			this.oldOpacity = this.opacity;
		}

		@Override
		public void highlight(boolean highlighted) {
			if ( highlighted ) {
				this.setColor(new Color().setRGB(1,1,0));
				this.opacity = 1;
			} else {
				this.getColor().copy(this.oldColor);
				this.opacity = this.oldOpacity;
			}
		}
	}

	public interface HasHighlight {
		public void highlight(boolean highlighted);
	}
	
	public class GizmoMaterial extends MeshBasicMaterial implements HasHighlight {

		private Color oldColor;
		private double oldOpacity;

		public GizmoMaterial(Color color, Double opacity/*vedremo in seguito i parametri utilizzati*/) {
			super();

			this.setDepthTest(false);
			this.setDepthWrite(false);
			this.setSide(SIDE.FRONT);
			this.setTransparent(true);

			if(color!=null)
				this.setColor(color);
			if(opacity!=null) {
				super.setOpacity(opacity);
			}

			this.oldColor = this.getColor().clone();
			this.oldOpacity = super.getOpacity();
		}

		@Override
		public void highlight(boolean highlighted) {
			if ( highlighted ) {
				this.setColor(new Color().setRGB(1,1,0));
				super.setOpacity(1);
			} else {
				this.getColor().copy(this.oldColor);
				super.setOpacity(this.oldOpacity);
			}
		}
	}
}

