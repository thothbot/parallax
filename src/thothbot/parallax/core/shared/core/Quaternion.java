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

package thothbot.parallax.core.shared.core;

/**
 * Implementation of Quaternion which provide a convenient mathematical notation 
 * for representing orientations and rotations of objects in three dimensions.
 * 
 * Quaternion represented by four coordinates: X, Y, Z, W
 * 
 * @author thothbot
 *
 */
public class Quaternion
{
	/**
	 * The X coordinate.
	 */
	public double x;

	/**
	 * The Y coordinate.
	 */
	public double y;

	/**
	 * The Z coordinate.
	 */
	public double z;

	/**
	 * The W coordinate.
	 */
	public double w;

	/**
	 * Default constructor will make Quaternion (0.0, 0.0, 0.0, 1.0)
	 */
	public Quaternion() 
	{
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
		this.w = 1.0;
	}

	/**
	 * Constructs and initializes a Quaternion from the specified X, Y, Z, W
	 * coordinates.
	 * 
	 * Will make Quaternion (X, Y, Z, W)
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @param z the Z coordinate
	 * @param w the W coordinate
	 */
	public Quaternion(double x, double y, double z, double w) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Constructs and initializes a Quaternion from the specified X, Y, Z coordinates.
	 * Will make Quaternion (X, Y, Z, 1.0)
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public Quaternion(double x, double y, double z) 
	{
		this();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * get X coordinate from the Quaternion
	 * 
	 * @return a X coordinate
	 */
	public double getX()
	{
		return this.x;
	}
	
	/**
	 * get Y coordinate from the Quaternion
	 * 
	 * @return a Y coordinate
	 */
	public double getY()
	{
		return this.y;
	}
	
	/**
	 * get Z coordinate from the Quaternion
	 * 
	 * @return a Z coordinate
	 */
	public double getZ()
	{
		return this.z;
	}
	
	/**
	 * get W coordinate from the Quaternion
	 * 
	 * @return a W coordinate
	 */
	public double getW()
	{
		return this.w;
	}
	
	/**
	 * Copy values from input Quaternion to the values of current Quaternion.
	 * 
	 * @param c1 the input Quaternion
	 */
	public void copy(Quaternion c1)
	{
		this.x = c1.x;
		this.y = c1.y;
		this.z = c1.z;
		this.w = c1.w;
	}
	
	public Quaternion set(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public void setFromEuler(Vector3 vector)
	{

		double c = Math.PI / 360.0, // 0.5 * Math.PI / 360, // 0.5 is an
											// optimization
		x = vector.x * c, y = vector.y * c, z = vector.z * c;

		double c1 = Math.cos(y);
		double s1 = Math.sin(y);
		double c2 = Math.cos(-z);
		double s2 = Math.sin(-z);
		double c3 = Math.cos(x);
		double s3 = Math.sin(x);

		double c1c2 = c1 * c2;
		double s1s2 = s1 * s2;

		this.w = c1c2 * c3 - s1s2 * s3;
		this.x = c1c2 * s3 + s1s2 * c3;
		this.y = s1 * c2 * c3 + c1 * s2 * s3;
		this.z = c1 * s2 * c3 - s1 * c2 * s3;
	}

	public void setFromAxisAngle(Vector3 axis, double angle)
	{
		// from
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToQuaternion/index.htm
		// axis have to be normalized

		double halfAngle = angle / 2.0;
		double s = Math.sin(halfAngle);

		this.x = axis.x * s;
		this.y = axis.y * s;
		this.z = axis.z * s;
		this.w = Math.cos(halfAngle);
	}

	public void setFromRotationMatrix(Matrix4 m)
	{
		// Adapted from:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm

		double absQ = Math.pow(m.determinant(), 1.0 / 3.0);

		this.w = Math.sqrt(Math.max(0, absQ + m.getArray().get(0) + m.getArray().get(5)	+ m.getArray().get(10))) / 2.0;
		this.x = Math.sqrt(Math.max(0, absQ + m.getArray().get(0) - m.getArray().get(5)	- m.getArray().get(10))) / 2.0;
		this.y = Math.sqrt(Math.max(0, absQ - m.getArray().get(0) + m.getArray().get(5)	- m.getArray().get(10))) / 2.0;
		this.z = Math.sqrt(Math.max(0, absQ - m.getArray().get(0) - m.getArray().get(5)	+ m.getArray().get(10))) / 2.0;
		this.x = copySign(this.x, (m.getArray().get(6) - m.getArray().get(9)));
		this.y = copySign(this.y, (m.getArray().get(8) - m.getArray().get(2)));
		this.z = copySign(this.z, (m.getArray().get(1) - m.getArray().get(4)));
		this.normalize();
	}

	public void calculateW()
	{
		this.w = -Math.sqrt(Math.abs(1.0 - this.x * this.x - this.y * this.y - this.z * this.z));
	}

	/**
	 * Negates the value of this Quaternion in place.
	 */
	public void inverse()
	{
		this.x *= -1;
		this.y *= -1;
		this.z *= -1;
	}

	public double length()
	{
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
	}

	/**
	 * Normalize the current Quaternion
	 */
	public void normalize()
	{
		double l = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);

		if (l == 0) 
		{
			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.w = 0;
		} 
		else 
		{
			l = 1.0 / l;

			this.x = this.x * l;
			this.y = this.y * l;
			this.z = this.z * l;
			this.w = this.w * l;
		}
	}

	/**
	 * Sets the value of this Quaternion to the vector multiplication of Quaternion a and
	 * Quaternion v2.
	 * 
	 * Based on <a href="http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/code/index.htm">http://www.euclideanspace.com</a>
	 * 
	 * @param a the first Quaternion
	 * @param b the second Quaternion
	 * 
	 */
	public void multiply(Quaternion a, Quaternion b)
	{
		this.x = a.x * b.w  + a.y * b.z - a.z * b.y + a.w * b.x;
		this.y = -a.x * b.z + a.y * b.w + a.z * b.x + a.w * b.y;
		this.z = a.x * b.y  - a.y * b.x + a.z * b.w + a.w * b.z;
		this.w = -a.x * b.x - a.y * b.y - a.z * b.z + a.w * b.w;
	}

	/**
	 * Sets the value of this Quaternion to the vector multiplication of itself and
	 * Quaternion b.
	 * (this = this * b)
	 * 
	 * @param b the other Quaternion
	 * 
	 */
	public void multiply(Quaternion b)
	{
		double qax = this.x, qay = this.y, qaz = this.z, qaw = this.w, qbx = b.x, qby = b.y, qbz = b.z, qbw = b.w;

		this.x = qax * qbw + qaw * qbx + qay * qbz - qaz * qby;
		this.y = qay * qbw + qaw * qby + qaz * qbx - qax * qbz;
		this.z = qaz * qbw + qaw * qbz + qax * qby - qay * qbx;
		this.w = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;
	}

	/**
	 * Sets the value of the input vector to the vector multiplication of input vector and
	 * the current Quaternion.
	 * 
	 * @param vector the input vector
	 * 
	 * @return the modified input vector
	 */
	public Vector3 multiply(Vector3 vector)
	{
		return multiply(vector, vector);
	}

	/**
	 * Sets the value of the destination vector to the vector multiplication of vector and
	 * the current Quaternion.
	 * 
	 * @param vector the input vector
	 * @param dest   the destination vector
	 * 
	 * @return the modified destination vector
	 */
	public Vector3 multiply(Vector3 vector, Vector3 dest)
	{
		double x = vector.getX(), 
			   y = vector.getY(), 
			   z = vector.getZ();
		double qx = this.getX(), 
			   qy = this.getY(), 
			   qz = this.getZ(), 
			   qw = this.getW();

		// calculate quat * vector
		double ix = qw * x + qy * z - qz * y;
		double iy = qw * y + qz * x - qx * z;
		double iz = qw * z + qx * y - qy * x;
		double iw = -qx * x - qy * y - qz * z;

		// calculate result * inverse quat
		dest.set( 
				(ix * qw + iw * -qx + iy * -qz - iz * -qy), // x
				(iy * qw + iw * -qy + iz * -qx - ix * -qz), // y
				(iz * qw + iw * -qz + ix * -qy - iy * -qx)  // z 
			);

		return dest;
	}

	/**
	 * Quaternion Interpolation
	 * 
	 * Based on <a href="http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/">http://www.euclideanspace.com</a>
	 * 
	 * @param qa  the quaternion a (first quaternion to be interpolated between)
	 * @param qb  the quaternion b (second quaternion to be interpolated between)
	 * @param qm  the interpolated quaternion
	 * @param t   a scalar between 0.0 (at qa) and 1.0 (at qb)
	 * 
	 * @return the interpolated quaternion
	 */
	public static Quaternion slerp(Quaternion qa, Quaternion qb, Quaternion qm, double t)
	{
		double cosHalfTheta = qa.w * qb.w + qa.x * qb.x + qa.y * qb.y + qa.z * qb.z;

		if (cosHalfTheta < 0) 
		{
			qm.w = -qb.w;
			qm.x = -qb.x;
			qm.y = -qb.y;
			qm.z = -qb.z;
			cosHalfTheta = -cosHalfTheta;
		} 
		else 
		{
			qm.copy(qb);
		}

		if (Math.abs(cosHalfTheta) >= 1.0) 
		{
			qm.w = qa.w;
			qm.x = qa.x;
			qm.y = qa.y;
			qm.z = qa.z;
			return qm;
		}

		double halfTheta = Math.acos(cosHalfTheta), 
				sinHalfTheta = Math.sqrt(1.0	- cosHalfTheta * cosHalfTheta);

		if (Math.abs(sinHalfTheta) < 0.001) 
		{
			qm.w = 0.5 * (qa.w + qb.w);
			qm.x = 0.5 * (qa.x + qb.x);
			qm.y = 0.5 * (qa.y + qb.y);
			qm.z = 0.5 * (qa.z + qb.z);

			return qm;
		}

		double ratioA = Math.sin((1.0 - t) * halfTheta) / sinHalfTheta, 
				ratioB = Math.sin(t * halfTheta) / sinHalfTheta;

		qm.w = (qa.w * ratioA + qm.w * ratioB);
		qm.x = (qa.x * ratioA + qm.x * ratioB);
		qm.y = (qa.y * ratioA + qm.y * ratioB);
		qm.z = (qa.z * ratioA + qm.z * ratioB);

		return qm;
	}

	/**
	 * Clone the current Quaternion
	 * quaternion.clone() != quaternion;
	 * 
	 * @return the new instance of Quaternion
	 */
	public Quaternion clone()
	{
		return new Quaternion(this.x, this.y, this.z, this.w);
	}

	private double copySign(double a, double b)
	{
		return b < 0 ? -Math.abs(a) : Math.abs(a);
	}
	
	@Override
	public String toString() 
	{
		return "(" + this.x + ", " + this.y + ", " + this.z +  ", " + this.w + ")";
	}
}
