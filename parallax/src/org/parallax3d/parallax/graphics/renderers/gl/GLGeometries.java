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

import org.parallax3d.parallax.graphics.core.AbstractGeometry;
import org.parallax3d.parallax.graphics.core.BufferGeometry;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.core.GeometryObject;
import org.parallax3d.parallax.graphics.renderers.GLRendererInfo;
import org.parallax3d.parallax.system.FastMap;
import org.parallax3d.parallax.system.gl.GL20;

public class GLGeometries {

    GL20 gl;
    GLRendererInfo info;

    FastMap<BufferGeometry> geometries = new FastMap<>();

    public GLGeometries(GL20 gl, GLProperties properties, GLRendererInfo info) {
        this.gl = gl;
        this.info = info;
    }

    public BufferGeometry get( GeometryObject object ) {

        AbstractGeometry geometry = object.getGeometry();

        if ( geometries.containsKey( geometry.getId() )) {

            return geometries.get( geometry.getId() );

        }

//        geometry.addEventListener( "dispose", onGeometryDispose );

        BufferGeometry buffergeometry = null;

        if ( geometry instanceof BufferGeometry) {

            buffergeometry = (BufferGeometry) geometry;

        } else if ( geometry instanceof Geometry) {

            if ( geometry._bufferGeometry == null ) {

                geometry._bufferGeometry = new BufferGeometry().setFromObject( object );

            }

            buffergeometry = geometry._bufferGeometry;

        }

        geometries.put(String.valueOf(geometry.getId()), buffergeometry );

        info.getMemory().geometries ++;

        return buffergeometry;

    }

//    public void onGeometryDispose( event ) {
//
//        var geometry = event.target;
//        var buffergeometry = geometries[ geometry.id ];
//
//        if ( buffergeometry.index != null ) {
//
//            deleteAttribute( buffergeometry.index );
//
//        }
//
//        deleteAttributes( buffergeometry.attributes );
//
//        geometry.removeEventListener( "dispose", onGeometryDispose );
//
//        delete geometries[ geometry.id ];
//
//        // TODO
//
//        var property = properties.get( geometry );
//
//        if ( property.wireframe ) {
//
//            deleteAttribute( property.wireframe );
//
//        }
//
//        properties.delete( geometry );
//
//        var bufferproperty = properties.get( buffergeometry );
//
//        if ( bufferproperty.wireframe ) {
//
//            deleteAttribute( bufferproperty.wireframe );
//
//        }
//
//        properties.delete( buffergeometry );
//
//        //
//
//        info.memory.geometries --;
//
//    }

//    public void getAttributeBuffer( attribute ) {
//
//        if ( attribute instanceof InterleavedBufferAttribute) {
//
//            return properties.get( attribute.data ).__webglBuffer;
//
//        }
//
//        return properties.get( attribute ).__webglBuffer;
//
//    }
//
//    public void deleteAttribute( attribute ) {
//
//        var buffer = getAttributeBuffer( attribute );
//
//        if ( buffer !== undefined ) {
//
//            gl.deleteBuffer( buffer );
//            removeAttributeBuffer( attribute );
//
//        }
//
//    }
//
//    public void deleteAttributes( attributes ) {
//
//        for ( var name in attributes ) {
//
//            deleteAttribute( attributes[ name ] );
//
//        }
//
//    }
//
//    public void removeAttributeBuffer( attribute ) {
//
//        if ( attribute instanceof InterleavedBufferAttribute ) {
//
//            properties.delete( attribute.data );
//
//        } else {
//
//            properties.delete( attribute );
//
//        }
//
//    }
}
