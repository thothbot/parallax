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

package org.parallax3d.parallax.graphics.objects;

import com.google.gwt.dom.client.Element;
import org.parallax3d.parallax.graphics.core.Object3D;

/**
 * gwt version of tree.js CSS3DObject
 *
 * @author svv2014
 */
public class CSS3DObject extends Object3D {

    protected Element element;

    public CSS3DObject(final Element element) {
        super();
        this.element = element;

        this.element.getStyle().setProperty("position", "absolute");

        this.setHandler(new ObjectHandler() {
            @Override
            public void onAdd(Object3D object) {

            }

            @Override
            public void onRemove(Object3D object) {
                if (element.getParentNode() != null) {
                    element.getParentNode().removeChild(element);
                }
            }
        });

    }

    public Element getElement(){
        return element;
    }
}
