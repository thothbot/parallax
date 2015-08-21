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

package thothbot.parallax.core.shared.geometries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thothbot.parallax.core.shared.core.Face3;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.math.Sphere;
import thothbot.parallax.core.shared.math.Vector2;
import thothbot.parallax.core.shared.math.Vector3;

public class RingGeometry extends Geometry {
	
	public RingGeometry(double innerRadius, double outerRadius) {
		this(innerRadius, outerRadius, 8, 8, 0, Math.PI * 2.0);
	}

	public RingGeometry( double innerRadius, double outerRadius, int thetaSegments, int phiSegments, double thetaStart, double thetaLength ) {

		super();

		thetaSegments = Math.max( 3, thetaSegments );
		phiSegments = Math.max( 1, phiSegments );

		double radius = innerRadius;
		double radiusStep = ( ( outerRadius - innerRadius ) / phiSegments );
		
		List<Vector2> uvs = new ArrayList<Vector2>();

		for ( int i = 0; i < phiSegments + 1; i ++ ) { // concentric circles inside ring

			for ( int o = 0; o < thetaSegments + 1; o ++ ) { // number of segments per circle

				Vector3 vertex = new Vector3();
				double segment = thetaStart + (double)o / thetaSegments * thetaLength;
				vertex.setX( radius * Math.cos( segment ) );
				vertex.setY( radius * Math.sin( segment ) );

				this.getVertices().add( vertex );
				uvs.add( new Vector2( ( vertex.getX() / outerRadius + 1.0 ) / 2.0, ( vertex.getY() / outerRadius + 1.0 ) / 2.0 ) );
			}

			radius += radiusStep;

		}

		Vector3 n = new Vector3( 0, 0, 1 );

		for ( int i = 0; i < phiSegments; i ++ ) { // concentric circles inside ring

			int thetaSegment = i * (thetaSegments + 1);

			for ( int o = 0; o < thetaSegments ; o ++ ) { // number of segments per circle

				int segment = o + thetaSegment;

				int v1 = segment;
				int v2 = segment + thetaSegments + 1;
				int v3 = segment + thetaSegments + 2;

				this.getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n.clone(), n.clone(), n.clone() ) ) );
				this.getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( v1 ).clone(), uvs.get( v2 ).clone(), uvs.get( v3 ).clone() ));

				v1 = segment;
				v2 = segment + thetaSegments + 2;
				v3 = segment + 1;

				this.getFaces().add( new Face3( v1, v2, v3, Arrays.asList( n.clone(), n.clone(), n.clone() ) ) );
				this.getFaceVertexUvs().get( 0 ).add( Arrays.asList( uvs.get( v1 ).clone(), uvs.get( v2 ).clone(), uvs.get( v3 ).clone() ));

			}
		}

		this.computeFaceNormals();

		this.boundingSphere = new Sphere( new Vector3(), radius );

	}
}
