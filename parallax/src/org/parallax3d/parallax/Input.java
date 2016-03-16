/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * This file is part of Parallax project.
 * Parallax is free software: you can redistribute it and/or modify it
 * under the terms of the Creative Commons Attribution 3.0 Unported License.
 * Parallax is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Creative Commons Attribution
 * 3.0 Unported License. for more details.
 * You should have received a copy of the the Creative Commons Attribution
 * 3.0 Unported License along with Parallax.
 * If not, see http://creativecommons.org/licenses/by/3.0/.
 */

package org.parallax3d.parallax;

import org.parallax3d.parallax.input.InputHandler;

public interface Input
{

	enum Orientation
	{
		Landscape, Portrait
	}

	enum Peripheral
	{
		HardwareKeyboard, OnscreenKeyboard, MultitouchScreen, Accelerometer, Compass, Vibrator
	}

	class Buttons
	{
		public static final int	LEFT	= 0;
		public static final int	RIGHT	= 1;
		public static final int	MIDDLE	= 2;
		public static final int	BACK	= 3;
		public static final int	FORWARD	= 4;
	}

	float getAccelerometerX ();

	float getAccelerometerY ();

	float getAccelerometerZ ();

	int getX ();

	int getX ( int pointer );

	int getDeltaX ();

	int getDeltaX ( int pointer );

	int getY ();

	int getY ( int pointer );

	int getDeltaY ();

	int getDeltaY ( int pointer );

	boolean isTouched ();

	boolean isButtonPressed ( int button );

	boolean isKeyPressed ( int key );

	Orientation getNativeOrientation ();

	boolean isCursorCatched ();

	void addInputHandler ( InputHandler processor );

	void removeInputHandler ( InputHandler processor );

	boolean isPeripheralAvailable ( Peripheral peripheral );

}
