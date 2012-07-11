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

package thothbot.squirrel.core.shared.core;

public class Quaternion
{
	/**
	 * The x coordinate.
	 */
	public float x = 0.0f;

	/**
	 * The y coordinate.
	 */
	public float y = 0.0f;

	/**
	 * The z coordinate.
	 */
	public float z = 0.0f;

	/**
	 * The w coordinate.
	 */
	public float w = 1;

	public Quaternion() {
	}

	/**
	 * Constructs and initializes a Vector4f from the specified xyzw
	 * coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @param w
	 *            the w coordinate
	 */
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Constructs and initializes a Vector4f from the specified xyz coordinates.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public Quaternion(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void copy(Quaternion c1)
	{
		this.x = c1.x;
		this.y = c1.y;
		this.z = c1.z;
		this.w = c1.w;
	}

	public void setFromEuler(Vector3f vector)
	{

		float c = (float) Math.PI / 360, // 0.5 * Math.PI / 360, // 0.5 is an
											// optimization
		x = vector.x * c, y = vector.y * c, z = vector.z * c;

		float c1 = (float) Math.cos(y);
		float s1 = (float) Math.sin(y);
		float c2 = (float) Math.cos(-z);
		float s2 = (float) Math.sin(-z);
		float c3 = (float) Math.cos(x);
		float s3 = (float) Math.sin(x);

		float c1c2 = c1 * c2;
		float s1s2 = s1 * s2;

		this.w = c1c2 * c3 - s1s2 * s3;
		this.x = c1c2 * s3 + s1s2 * c3;
		this.y = s1 * c2 * c3 + c1 * s2 * s3;
		this.z = c1 * s2 * c3 - s1 * c2 * s3;
	}

	public void setFromAxisAngle(Vector3f axis, float angle)
	{

		// from
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/angleToQuaternion/index.htm
		// axis have to be normalized

		float halfAngle = angle / 2f;
		float s = (float) Math.sin(halfAngle);

		this.x = axis.x * s;
		this.y = axis.y * s;
		this.z = axis.z * s;
		this.w = (float) Math.cos(halfAngle);
	}

	public void setFromRotationMatrix(Matrix4f m)
	{
		// Adapted from:
		// http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/index.htm

		float absQ = (float) Math.pow(m.determinant(), 1.0f / 3.0f);

		this.w = (float) Math.sqrt(Math.max(0, absQ + m.getArray().get(0) + m.getArray().get(5)
				+ m.getArray().get(10))) / 2f;
		this.x = (float) Math.sqrt(Math.max(0, absQ + m.getArray().get(0) - m.getArray().get(5)
				- m.getArray().get(10))) / 2f;
		this.y = (float) Math.sqrt(Math.max(0, absQ - m.getArray().get(0) + m.getArray().get(5)
				- m.getArray().get(10))) / 2f;
		this.z = (float) Math.sqrt(Math.max(0, absQ - m.getArray().get(0) - m.getArray().get(5)
				+ m.getArray().get(10))) / 2f;
		this.x = copySign(this.x, (m.getArray().get(6) - m.getArray().get(9)));
		this.y = copySign(this.y, (m.getArray().get(8) - m.getArray().get(2)));
		this.z = copySign(this.z, (m.getArray().get(1) - m.getArray().get(4)));
		this.normalize();
	}

	public void calculateW()
	{
		this.w = (float) -Math.sqrt(Math.abs(1.0 - this.x * this.x - this.y * this.y - this.z
				* this.z));
	}

	public void inverse()
	{
		this.x *= -1;
		this.y *= -1;
		this.z *= -1;
	}

	public float length()
	{
		return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w
				* this.w);
	}

	public void normalize()
	{
		float l = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w
				* this.w);

		if (l == 0) {

			this.x = 0;
			this.y = 0;
			this.z = 0;
			this.w = 0;

		} else {

			l = 1f / l;

			this.x = this.x * l;
			this.y = this.y * l;
			this.z = this.z * l;
			this.w = this.w * l;

		}
	}

	public void multiply(Quaternion a, Quaternion b)
	{

		// from
		// http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/code/index.htm

		this.x = a.x * b.w + a.y * b.z - a.z * b.y + a.w * b.x;
		this.y = -a.x * b.z + a.y * b.w + a.z * b.x + a.w * b.y;
		this.z = a.x * b.y - a.y * b.x + a.z * b.w + a.w * b.z;
		this.w = -a.x * b.x - a.y * b.y - a.z * b.z + a.w * b.w;
	}

	public void multiply(Quaternion b)
	{

		float qax = this.x, qay = this.y, qaz = this.z, qaw = this.w, qbx = b.x, qby = b.y, qbz = b.z, qbw = b.w;

		this.x = qax * qbw + qaw * qbx + qay * qbz - qaz * qby;
		this.y = qay * qbw + qaw * qby + qaz * qbx - qax * qbz;
		this.z = qaz * qbw + qaw * qbz + qax * qby - qay * qbx;
		this.w = qaw * qbw - qax * qbx - qay * qby - qaz * qbz;
	}

	public void multiply(Vector3f vector)
	{
		multiply(vector, vector);
	}

	public Vector3f multiply(Vector3f vector, Vector3f dest)
	{

		float x = vector.x, y = vector.y, z = vector.z, qx = this.x, qy = this.y, qz = this.z, qw = this.w;

		// calculate quat * vector
		float ix = qw * x + qy * z - qz * y;
		float iy = qw * y + qz * x - qx * z;
		float iz = qw * z + qx * y - qy * x;
		float iw = -qx * x - qy * y - qz * z;

		// calculate result * inverse quat

		dest.x = ix * qw + iw * -qx + iy * -qz - iz * -qy;
		dest.y = iy * qw + iw * -qy + iz * -qx - ix * -qz;
		dest.z = iz * qw + iw * -qz + ix * -qy - iy * -qx;

		return dest;
	}

	public Quaternion slerp(Quaternion qa, Quaternion qb, Quaternion qm, float t)
	{

		// http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/slerp/

		float cosHalfTheta = qa.w * qb.w + qa.x * qb.x + qa.y * qb.y + qa.z * qb.z;

		if (cosHalfTheta < 0) {
			qm.w = -qb.w;
			qm.x = -qb.x;
			qm.y = -qb.y;
			qm.z = -qb.z;
			cosHalfTheta = -cosHalfTheta;
		} else {
			qm.copy(qb);
		}

		if (Math.abs(cosHalfTheta) >= 1.0) {
			qm.w = qa.w;
			qm.x = qa.x;
			qm.y = qa.y;
			qm.z = qa.z;
			return qm;
		}

		float halfTheta = (float) Math.acos(cosHalfTheta), sinHalfTheta = (float) Math.sqrt(1.0
				- cosHalfTheta * cosHalfTheta);

		if (Math.abs(sinHalfTheta) < 0.001) {
			qm.w = 0.5f * (qa.w + qb.w);
			qm.x = 0.5f * (qa.x + qb.x);
			qm.y = 0.5f * (qa.y + qb.y);
			qm.z = 0.5f * (qa.z + qb.z);

			return qm;
		}

		float ratioA = (float) Math.sin((1f - t) * halfTheta) / sinHalfTheta, ratioB = (float) Math
				.sin(t * halfTheta) / sinHalfTheta;

		qm.w = (qa.w * ratioA + qm.w * ratioB);
		qm.x = (qa.x * ratioA + qm.x * ratioB);
		qm.y = (qa.y * ratioA + qm.y * ratioB);
		qm.z = (qa.z * ratioA + qm.z * ratioB);

		return qm;
	}

	public Quaternion clone()
	{
		return new Quaternion(this.x, this.y, this.z, this.w);
	}

	private float copySign(float a, float b)
	{
		return b < 0 ? -Math.abs(a) : Math.abs(a);
	}
}
