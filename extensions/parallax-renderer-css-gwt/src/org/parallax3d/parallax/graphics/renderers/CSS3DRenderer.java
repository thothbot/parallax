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
package org.parallax3d.parallax.graphics.renderers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.parallax3d.parallax.graphics.cameras.Camera;
import org.parallax3d.parallax.graphics.cameras.PerspectiveCamera;
import org.parallax3d.parallax.graphics.core.Object3D;
import org.parallax3d.parallax.graphics.objects.CSS3DObject;
import org.parallax3d.parallax.graphics.objects.CSS3DSprite;
import org.parallax3d.parallax.math.Matrix4;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

import java.util.HashMap;
import java.util.Map;

/**
 * gwt version of tree.js CSS3DRenderer
 *
 * @author svv2014
 */
public class CSS3DRenderer extends HTMLPanel {

    private final Element cameraElement;

    private Map<Integer, String> objects = new HashMap<Integer, String>();

    private int _width, _height;
    private int _widthHalf, _heightHalf;
    private Matrix4 matrix = new Matrix4();
    private double cacheCameraFov;
    private String cacheCameraStyle = "";

    public CSS3DRenderer() {
        super("");
        getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        getElement().getStyle().setProperty("webkitTransformStyle", "preserve-3d");
        getElement().getStyle().setProperty("mozTransformStyle", "preserve-3d");
        getElement().getStyle().setProperty("oTransformStyle", "preserve-3d");
        getElement().getStyle().setProperty("transformStyle", "preserve-3d");

        cameraElement = Document.get().createElement("div");

        cameraElement.getStyle().setProperty("webkitTransformStyle", "preserve-3d");
        cameraElement.getStyle().setProperty("mozTransformStyle", "preserve-3d");
        cameraElement.getStyle().setProperty("oTransformStyle", "preserve-3d");
        cameraElement.getStyle().setProperty("transformStyle", "preserve-3d");

        getElement().appendChild(cameraElement);
    }

    //TODO: setClearColor
    public void setClearColor() {

    }

    public void setSize(int width, int height) {
        _width = width;
        _height = height;

        _widthHalf = _width / 2;
        _heightHalf = _height / 2;

        getElement().getStyle().setProperty("width", width + "px");
        getElement().getStyle().setProperty("height", height + "px");

        cameraElement.getStyle().setProperty("width", width + "px");
        cameraElement.getStyle().setProperty("height", height + "px");
    }

    private double epsilon(double value) {
        return Math.abs(value) < 0.000001 ? 0 : value;
    }

    private String getCameraCSSMatrix(Matrix4 matrix) {

        Float32Array elements = matrix.getArray();

        return " matrix3d(" +
                epsilon(elements.get(0)) + ',' +
                epsilon(-elements.get(1)) + ',' +
                epsilon(elements.get(2)) + ',' +
                epsilon(elements.get(3)) + ',' +
                epsilon(elements.get(4)) + ',' +
                epsilon(-elements.get(5)) + ',' +
                epsilon(elements.get(6)) + ',' +
                epsilon(elements.get(7)) + ',' +
                epsilon(elements.get(8)) + ',' +
                epsilon(-elements.get(9)) + ',' +
                epsilon(elements.get(10)) + ',' +
                epsilon(elements.get(11)) + ',' +
                epsilon(elements.get(12)) + ',' +
                epsilon(-elements.get(13)) + ',' +
                epsilon(elements.get(14)) + ',' +
                epsilon(elements.get(15)) +
                ")";
    }


    private String getObjectCSSMatrix(Matrix4 matrix) {

        Float32Array elements = matrix.getArray();

        return "translate3d(-50%,-50%,0) matrix3d(" +
                epsilon(elements.get(0)) + ',' +
                epsilon(elements.get(1)) + ',' +
                epsilon(elements.get(2)) + ',' +
                epsilon(elements.get(3)) + ',' +
                epsilon(-elements.get(4)) + ',' +
                epsilon(-elements.get(5)) + ',' +
                epsilon(-elements.get(6)) + ',' +
                epsilon(-elements.get(7)) + ',' +
                epsilon(elements.get(8)) + ',' +
                epsilon(elements.get(9)) + ',' +
                epsilon(elements.get(10)) + ',' +
                epsilon(elements.get(11)) + ',' +
                epsilon(elements.get(12)) + ',' +
                epsilon(elements.get(13)) + ',' +
                epsilon(elements.get(14)) + ',' +
                epsilon(elements.get(15)) +
                ")";
    }

    public void renderObject(Object3D object, Camera camera) {

        if (object instanceof CSS3DObject) {
            String style;

            if (object instanceof CSS3DSprite) {

                matrix.copy(camera.getMatrixWorldInverse());
                matrix.transpose();
                matrix.copyPosition(object.getMatrixWorld());
                matrix.scale(object.getScale());

                matrix.getArray().set(3, 0);
                matrix.getArray().set(7, 0);
                matrix.getArray().set(11, 0);
                matrix.getArray().set(15, 1);

                style = getObjectCSSMatrix(matrix);

            } else {

                style = getObjectCSSMatrix(object.getMatrixWorld());

            }

            Element element = ((CSS3DObject) object).getElement();
            String cachedStyle = objects.containsKey(object.getId()) ? objects.get(object.getId()) : null;

            if (cachedStyle == null || !cachedStyle.equals(style)) {

                element.getStyle().setProperty("webkitTransform", style);
                element.getStyle().setProperty("mozTransform", style);
                element.getStyle().setProperty("oTransform", style);
                element.getStyle().setProperty("transform", style);
                objects.put(object.getId(), style);

            }

            if (!element.getParentElement().equals(cameraElement)) {
                cameraElement.appendChild(element);
            }

        }

        for (int i = 0, l = object.getChildren().size(); i < l; i++) {

            renderObject(object.getChildren().get(i), camera);

        }

    }

    public void render(Object3D scene, PerspectiveCamera camera) {

        double fov = 0.5 / Math.tan(Math.toRadians(camera.getFov() * 0.5)) * _height;

        if (cacheCameraFov != fov) {
            getElement().getStyle().setProperty("webkitPerspective", fov + "px");
            getElement().getStyle().setProperty("mozPerspective", fov + "px");
            getElement().getStyle().setProperty("oPerspective", fov + "px");
            getElement().getStyle().setProperty("perspective", fov + "px");
            cacheCameraFov = fov;
        }

        scene.updateMatrixWorld(false);

        if (camera.getParent() == null) camera.updateMatrixWorld(false);

        camera.getMatrixWorldInverse().getInverse(camera.getMatrixWorld());

        String style = "translate3d(0,0," + fov + "px)" + getCameraCSSMatrix(camera.getMatrixWorldInverse()) +
                " translate3d(" + _widthHalf + "px," + _heightHalf + "px, 0)";

        if (!cacheCameraStyle.equals(style)) {
            cameraElement.getStyle().setProperty("webkitTransform", style);
            cameraElement.getStyle().setProperty("mozTransform", style);
            cameraElement.getStyle().setProperty("oTransform", style);
            cameraElement.getStyle().setProperty("transform", style);
            cacheCameraStyle = style;
        }
        renderObject(scene, camera);
    }


    /**
     * to be consistent with tree.js CSS3Renderer
     *
     * @return
     */
    public HTMLPanel getDomElement() {
        return this;
    }
}
