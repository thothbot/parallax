/*
 * Copyright 2014 Alex Usachev, thothbot@gmail.com
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

package org.parallax3d.parallax.math;

import org.parallax3d.parallax.Log;
import org.parallax3d.parallax.system.ThreejsObject;
import org.parallax3d.parallax.system.gl.arrays.Float32Array;

@ThreejsObject("THREE.Euler")
public class Euler {

	public interface EulerChangeHandler
	{
		void onChange(Euler rotation);
	}

	public final static String DefaultOrder = "XYZ";
	public final static String[] RotationOrders = { "XYZ", "YZX", "ZXY", "XZY", "YXZ", "ZYX" };

	/**
	 * The X coordinate.
	 */
	double x;

	/**
	 * The Y coordinate.
	 */
	double y;

	/**
	 * The Z coordinate.
	 */
	double z;

	String order;

	EulerChangeHandler handler;

	// Temporary variables
	static Quaternion _q = new Quaternion();

	public Euler()
	{
		this(0.0,0.0,0.0);
	}

	public Euler (double x, double y, double z)
	{
		this(x, y, z, DefaultOrder);
	}

	public Euler (double x, double y, double z, String order)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.order = order;
	}

	public void setHandler(EulerChangeHandler handler)
	{
		this.handler = handler;
	}

	private void onChange()
	{
		if(this.handler != null)
			this.handler.onChange(Euler.this);
	}


	/**
	 * This method will add specified value to X coordinate of the vector.
	 * In another words: x += value.
	 *
	 * @param x the X coordinate
	 */
	public void addX(double x)
	{
		this.x += x;
		this.onChange();
	}

	/**
	 * This method will add specified value to Y coordinate of the vector.
	 * In another words: y += value.
	 *
	 * @param y the Y coordinate
	 */
	public void addY(double y)
	{
		this.y += y;
		this.onChange();
	}

	/**
	 * This method will add specified value to Y coordinate of the vector.
	 * In another words: z += value.
	 *
	 * @param z the Z coordinate
	 */
	public void addZ(double z)
	{
		this.z += z;
		this.onChange();
	}

	public double getX () {
		return this.x;
	}

	public void setX ( double value ) {
		this.x = value;

		this.onChange();
	}

	public double getY() {
		return this.y;
	}

	public void setY ( double value )
	{
		this.y = value;

		this.onChange();
	}

	public double getZ() {
		return this.z;
	}

	public void setZ ( double value )
	{
		this.z = value;

		this.onChange();
	}

	public String getOrder ()
	{
		return this.order;
	}

	public void setOrder ( String value )
	{
		this.order = value;

		this.onChange();
	}

	public Euler set( double x, double y, double z)
	{
		return set(x, y, z, DefaultOrder);
	}

	public Euler set( double x, double y, double z, String order )
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.order = order;

		this.onChange();

		return this;
	}

	public Euler copy ( Euler euler )
	{

		this.x = euler.x;
		this.y = euler.y;
		this.z = euler.z;
		this.order = euler.order;

		this.onChange();

		return this;
	}

	public Euler setFromRotationMatrix( Matrix4 m )
	{
		return setFromRotationMatrix(m, this.order );
	}

	public Euler setFromRotationMatrix( Matrix4 m, String order )
	{
		return setFromRotationMatrix(m, order, false);
	}

	public Euler setFromRotationMatrix( Matrix4 m, String order, boolean update )
	{

		// assumes the upper 3x3 of m is a pure rotation matrix (i.e, unscaled)

		Float32Array te = m.getArray();
		double m11 = te.get(0), m12 = te.get(4), m13 = te.get(8);
		double m21 = te.get(1), m22 = te.get(5), m23 = te.get(9);
		double m31 = te.get(2), m32 = te.get(6), m33 = te.get(10);

		switch (order) {
			case "XYZ":

				this.y = Math.asin(Mathematics.clamp(m13, -1, 1));

				if (Math.abs(m13) < 0.99999) {

					this.x = Math.atan2(-m23, m33);
					this.z = Math.atan2(-m12, m11);

				} else {

					this.x = Math.atan2(m32, m22);
					this.z = 0;

				}

				break;
			case "YXZ":

				this.x = Math.asin(-Mathematics.clamp(m23, -1, 1));

				if (Math.abs(m23) < 0.99999) {

					this.y = Math.atan2(m13, m33);
					this.z = Math.atan2(m21, m22);

				} else {

					this.y = Math.atan2(-m31, m11);
					this.z = 0;

				}

				break;
			case "ZXY":

				this.x = Math.asin(Mathematics.clamp(m32, -1, 1));

				if (Math.abs(m32) < 0.99999) {

					this.y = Math.atan2(-m31, m33);
					this.z = Math.atan2(-m12, m22);

				} else {

					this.y = 0;
					this.z = Math.atan2(m21, m11);

				}

				break;
			case "ZYX":

				this.y = Math.asin(-Mathematics.clamp(m31, -1, 1));

				if (Math.abs(m31) < 0.99999) {

					this.x = Math.atan2(m32, m33);
					this.z = Math.atan2(m21, m11);

				} else {

					this.x = 0;
					this.z = Math.atan2(-m12, m22);

				}

				break;
			case "YZX":

				this.z = Math.asin(Mathematics.clamp(m21, -1, 1));

				if (Math.abs(m21) < 0.99999) {

					this.x = Math.atan2(-m23, m22);
					this.y = Math.atan2(-m31, m11);

				} else {

					this.x = 0;
					this.y = Math.atan2(m13, m33);

				}

				break;
			case "XZY":

				this.z = Math.asin(-Mathematics.clamp(m12, -1, 1));

				if (Math.abs(m12) < 0.99999) {

					this.x = Math.atan2(m32, m22);
					this.y = Math.atan2(m13, m11);

				} else {

					this.x = Math.atan2(-m23, m33);
					this.y = 0;

				}

				break;
			default:

				Log.warn("Euler: .setFromRotationMatrix() given unsupported order: " + order);

				break;
		}

		this.order = order;

		if(update) this.onChange();

		return this;
	}

	public Euler setFromQuaternion ( Quaternion q )
	{
		return setFromQuaternion(q, this.order);
	}

	public Euler setFromQuaternion ( Quaternion q, String order)
	{
		return setFromQuaternion(q, order, false);
	}

	private static Matrix4 matrix = new Matrix4();
	public Euler setFromQuaternion ( Quaternion q, String order, boolean update )
	{
		matrix.makeRotationFromQuaternion( q );
		return this.setFromRotationMatrix( matrix, order, update );
	}

	public Euler setFromVector3( Vector3 v )
	{
		return setFromVector3(v, this.order);
	}

	public Euler setFromVector3( Vector3 v, String order )
	{
		return this.set( v.x, v.y, v.z, order);
	}

	public Euler reorder(String newOrder)
	{

		// WARNING: this discards revolution information -bhouston

		_q.setFromEuler( this );
		return this.setFromQuaternion( _q, newOrder );
	}

	public Vector3 toVector3()
	{
		return new Vector3( this.x, this.y, this.z );
	}

	public Vector3 toVector3( Vector3 optionalResult )
	{
		return optionalResult.set( this.x, this.y, this.z );
	}

	public boolean equals( Euler euler )
	{
		return ( euler.x == this.x ) && ( euler.y == this.y ) && ( euler.z == this.z ) && ( euler.order.equals(this.order) );
	}

	public Euler clone() {

		return new Euler( this.x, this.y, this.z, this.order );

	}

}
