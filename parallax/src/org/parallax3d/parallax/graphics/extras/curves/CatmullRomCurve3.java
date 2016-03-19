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
package org.parallax3d.parallax.graphics.extras.curves;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.graphics.extras.core.CubicPoly;
import org.parallax3d.parallax.graphics.extras.core.Curve;
import org.parallax3d.parallax.math.Vector3;
import org.parallax3d.parallax.system.ThreejsObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zz85 https://github.com/zz85
 *
 * Centripetal CatmullRom Curve - which is useful for avoiding
 * cusps and self-intersections in non-uniform catmull rom curves.
 * http://www.cemyuksel.com/research/catmullrom_param/catmullrom.pdf
 *
 * curve.type accepts centripetal(default), chordal and catmullrom
 * curve.tension is used for catmullrom which defaults to 0.5
 */
@ThreejsObject("THREE.CatmullRomCurve3")
public class CatmullRomCurve3 extends Curve {

    enum Type {
        centripetal,
        chordal,
        catmullrom
    }

    List<Vector3> points;

    Type type;

    Double tension;

    boolean closed = false;

    Vector3 tmp = new Vector3();
    CubicPoly px = new CubicPoly();
    CubicPoly py = new CubicPoly();
    CubicPoly pz = new CubicPoly();

    public CatmullRomCurve3() {
        this(new ArrayList<Vector3>());
    }

    public CatmullRomCurve3(List<Vector3> points) {
        this.points = points;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTension(double tension) {
        this.tension = tension;
    }

    @Override
    public Vector3 getPoint(double t) {

        List<Vector3> points = this.points;
//                point, intPoint, weight, l;

        int l = points.size();

        if ( l < 2 ) Log.warn("CatmullRomCurve3.getPoint(): you need at least 2 points");

        double point = ( l - ( this.closed ? 0 : 1 ) ) * t;
        int intPoint = (int) Math.floor( point );
        double weight = point - intPoint;

        if ( this.closed ) {

            intPoint += intPoint > 0 ? 0 : ( Math.floor( Math.abs( intPoint ) / points.size() ) + 1 ) * points.size();

        } else if ( weight == 0 && intPoint == l - 1 ) {

            intPoint = l - 2;
            weight = 1;

        }

        Vector3 p0, p1, p2, p3; // 4 points

        if ( this.closed || intPoint > 0 ) {

            p0 = points.get((intPoint - 1) % l);

        } else {

            // extrapolate first point
            tmp.sub(points.get(0), points.get(1)).add(points.get(0));
            p0 = tmp;

        }

        p1 = points.get(intPoint % l);
        p2 = points.get((intPoint + 1) % l);

        if ( this.closed || intPoint + 2 < l ) {

            p3 = points.get((intPoint + 2) % l);

        } else {

            // extrapolate last point
            tmp.sub(points.get(l - 1), points.get(l - 2)).add(points.get(l - 1));
            p3 = tmp;

        }

        if ( this.type == null || this.type == Type.centripetal || this.type == Type.chordal ) {

            // init Centripetal / Chordal Catmull-Rom
            double pow = this.type == Type.chordal ? 0.5 : 0.25;
            double dt0 = Math.pow( p0.distanceToSquared( p1 ), pow );
            double dt1 = Math.pow( p1.distanceToSquared( p2 ), pow );
            double dt2 = Math.pow( p2.distanceToSquared( p3 ), pow );

            // safety check for repeated points
            if ( dt1 < 1e-4 ) dt1 = 1.0;
            if ( dt0 < 1e-4 ) dt0 = dt1;
            if ( dt2 < 1e-4 ) dt2 = dt1;

            px.initNonuniformCatmullRom(p0.getX(), p1.getX(), p2.getX(), p3.getX(), dt0, dt1, dt2 );
            py.initNonuniformCatmullRom(p0.getY(), p1.getY(), p2.getY(), p3.getY(), dt0, dt1, dt2 );
            pz.initNonuniformCatmullRom(p0.getZ(), p1.getZ(), p2.getZ(), p3.getZ(), dt0, dt1, dt2 );

        } else if ( this.type == Type.catmullrom ) {

            double tension = this.tension != null ? this.tension : 0.5;
            px.initCatmullRom(p0.getX(), p1.getX(), p2.getX(), p3.getX(), tension );
            py.initCatmullRom(p0.getY(), p1.getY(), p2.getY(), p3.getY(), tension );
            pz.initCatmullRom(p0.getZ(), p1.getZ(), p2.getZ(), p3.getZ(), tension );

        }

        return new Vector3(
            px.calc( weight ),
            py.calc( weight ),
            pz.calc( weight )
        );
    }
}
