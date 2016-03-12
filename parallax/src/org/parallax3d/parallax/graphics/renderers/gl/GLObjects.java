/*
 * Copyright 2016 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.graphics.renderers.gl;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.core.*;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.GL20;
import org.parallax3d.parallax.system.gl.arrays.TypeArray;
import org.parallax3d.parallax.system.gl.arrays.Uint32Array;
import org.parallax3d.parallax.system.gl.enums.BufferTarget;
import org.parallax3d.parallax.system.gl.enums.BufferUsage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GLObjects {

    GLGeometries geometries;

    GL20 gl;
    GLProperties properties;

    public GLObjects(GL20 gl, GLProperties properties) {

        this.gl = gl;
        this.properties = properties;
        this.geometries = new GLGeometries(gl, properties);

    }

    public BufferAttribute getWireframeAttribute( BufferGeometry geometry )
    {

        FastMap<Object> property = properties.get( geometry );

        if ( property.containsKey("wireframe") ) {

            return (BufferAttribute) property.get("wireframe");

        }

        int[] indices = new int[0];

        AttributeData index = geometry.getIndex();
        FastMap<BufferAttribute> attributes = geometry.getAttributes();
        BufferAttribute position = attributes.get("position");

        if ( index != null ) {

            Map<Integer, List<Integer>> edges = new HashMap<>();
            Uint32Array array = (Uint32Array) index.getArray();

            for ( int i = 0, l = array.getLength(); i < l; i += 3 ) {

                int a = array.get( i + 0 );
                int b = array.get( i + 1 );
                int c = array.get( i + 2 );

                if ( checkEdge( edges, a, b ) ) indices = new int[]{ a, b };
                if ( checkEdge( edges, b, c ) ) indices = new int[]{ b, c };
                if ( checkEdge( edges, c, a ) ) indices = new int[]{ c, a };

            }

        } else {

            TypeArray array = position.getArray();

            for ( int i = 0, l = ( array.getLength() / 3 ) - 1; i < l; i += 3 ) {

                int a = i;
                int b = i + 1;
                int c = i + 2;

                indices = new int[]{a, b, b, c, c, a};

            }

        }

        BufferAttribute attribute = new BufferAttribute(Uint32Array.create(indices), 1 );

        updateAttribute( attribute, BufferTarget.ELEMENT_ARRAY_BUFFER );

        property.put("wireframe", attribute);

        return attribute;

    }

    public boolean checkEdge( Map<Integer, List<Integer>> edges, int a, int b )
    {

        if ( a > b ) {

            int tmp = a;
            a = b;
            b = tmp;

        }

        List<Integer> list = edges.get( a );

        if ( list == null ) {

            edges.put( a , Arrays.asList(b));
            return true;

        } else if ( list.indexOf( b ) == -1 ) {

            list.add( b );
            return true;

        }

        return false;

    }

    public void updateAttribute(InterleavedBufferAttribute attribute, BufferTarget bufferType )
    {
        updateAttribute(attribute.getData(), bufferType );
    }

    public void updateAttribute(AttributeData data, BufferTarget bufferType ) {

        FastMap<Object> attributeProperties = properties.get( data );

        if ( !attributeProperties.containsKey("__webglBuffer")) {

            createBuffer( attributeProperties, data, bufferType );

        } else if ( attributeProperties.get("version") != data.getVersion() ) {

            updateBuffer( attributeProperties, data, bufferType );

        }

    }

    private void createBuffer( FastMap<Object> attributeProperties, AttributeData data, BufferTarget bufferType )
    {
        attributeProperties.put("__webglBuffer", gl.glGenBuffer());
        gl.glBindBuffer( bufferType.getValue(), (Integer) attributeProperties.get("__webglBuffer"));

        BufferUsage usage = data.isDynamic() ? BufferUsage.DYNAMIC_DRAW : BufferUsage.STATIC_DRAW;

        gl.glBufferData( bufferType.getValue(), data.getArray().getByteLength(), data.getArray().getTypedBuffer(), usage.getValue() );

        attributeProperties.put("version", data.getVersion());

    }

    private void updateBuffer( FastMap<Object> attributeProperties, AttributeData data, BufferTarget bufferType )
    {

        gl.glBindBuffer( bufferType.getValue(), (Integer) attributeProperties.get("__webglBuffer"));

        if ( !data.isDynamic() || data.getUpdateRange().count == - 1 ) {

            // Not using update ranges

            gl.glBufferSubData( bufferType.getValue(), 0, data.getArray().getByteLength(), data.getArray().getTypedBuffer() );

        } else if ( data.getUpdateRange().count == 0 ) {

            Log.error("GLObjects.updateBuffer: dynamic BufferAttribute marked as needsUpdate but updateRange.count is 0, ensure you are using set methods or updating manually.");

        } else {

            TypeArray array = data.getArray().getSubarray( data.getUpdateRange().offset, data.getUpdateRange().offset + data.getUpdateRange().count );
            gl.glBufferSubData( bufferType.getValue(), data.getUpdateRange().offset * data.getArray().getBytesPerElement(), array.getByteLength(), array.getTypedBuffer());

            data.getUpdateRange().count = 0; // reset range

        }

        attributeProperties.put("version", data.getVersion());

    }

    public int getAttributeBuffer( InterleavedBufferAttribute attribute )
    {
        return (int) properties.get( attribute.getData() ).get("__webglBuffer");
    }

    public int getAttributeBuffer( AttributeData attribute )
    {
        return (int) properties.get( attribute ).get("__webglBuffer");
    }

}
