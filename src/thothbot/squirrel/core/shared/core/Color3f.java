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

public final class Color3f
{
	/*
	 * The Color in HEX.
	 */
	private int hex;

	/*
	 * The R color.
	 */
	private float r = 1.0f;

	/*
	 * The G color.
	 */
	private float g = 1.0f;

	/*
	 * The B color.
	 */
	private float b = 1.0f;

	private Boolean autoUpdate;

	public Color3f() {		
	}

	public Color3f(int hex) {
		setHex(hex);
	}

	public float getR()
	{
		return r;
	}

	public void setR(float r)
	{
		this.r = r;
	}

	public float getG()
	{
		return g;
	}

	public void setG(float g)
	{
		this.g = g;
	}

	public float getB()
	{
		return b;
	}

	public void setB(float b)
	{
		this.b = b;
	}

	public void setAutoUpdate(boolean autoUpdate)
	{
		this.autoUpdate = autoUpdate;
	}

	public boolean getAutoUpdate()
	{
		return this.autoUpdate;
	}

	public void setHex(int hex)
	{
		this.hex = (~~hex) & 0xffffff;
		this.updateRGB();
	}

	public int getHex()
	{
		return this.hex;
	}

	public void copy(Color3f color)
	{
		this.setRGB(color.getR(), color.getG(), color.getB());
	}

	public Color3f setRGB(float r, float g, float b)
	{
		this.setR(r);
		this.setG(g);
		this.setB(b);
		this.updateHex();
		
		return this;
	}

	public Color3f setHSV(float h, float s, float v)
	{
		// based on MochiKit implementation by Bob Ippolito
		// h,s,v ranges are < 0.0 - 1.0 >

		float r = 0, g = 0, b = 0, f, p, q, t;

		if (v == 0.0) {

			r = g = b = 0.0f;

		} else {

			int i = (int) Math.floor(h * 6);
			f = (h * 6) - i;
			p = v * (1 - s);
			q = v * (1 - (s * f));
			t = v * (1 - (s * (1 - f)));

			switch (i) {

			case 1:
				r = q;
				g = v;
				b = p;
				break;
			case 2:
				r = p;
				g = v;
				b = t;
				break;
			case 3:
				r = p;
				g = q;
				b = v;
				break;
			case 4:
				r = t;
				g = p;
				b = v;
				break;
			case 5:
				r = v;
				g = p;
				b = q;
				break;
			case 6: // fall through
			case 0:
				r = v;
				g = t;
				b = p;
				break;

			}

		}

		return this.setRGB(r, g, b);
	}

	public void updateHex()
	{
		this.hex = ~~((int) Math.floor(this.r * 255)) << 16
				^ ~~((int) Math.floor(this.g * 255)) << 8 ^ ~~((int) Math.floor(this.b * 255));
	}

	public void updateRGB()
	{
		this.setR(((float)(this.hex >> 16 & 255) / 255));
		this.setG(((float)(this.hex >> 8 & 255) / 255));
		this.setB(((float)(this.hex & 255) / 255));
	}

	public Color3f copyGammaToLinear(Color3f c)
	{
		this.setR(c.getR() * c.getR());
		this.setG(c.getG() * c.getG());
		this.setB(c.getB() * c.getB());
		return this;
	}

	public Color3f copyLinearToGamma(Color3f c)
	{

		this.setR((int) Math.sqrt(c.getR()));
		this.setG((int) Math.sqrt(c.getG()));
		this.setB((int) Math.sqrt(c.getB()));
		return this;
	}

	public Color3f convertGammaToLinear()
	{
		this.setR(this.getR() * this.getR());
		this.setG(this.getG() * this.getG());
		this.setB(this.getB() * this.getB());
		return this;
	}

	public Color3f convertLinearToGamma()
	{
		this.setR((int) Math.sqrt(this.getR()));
		this.setG((int) Math.sqrt(this.getG()));
		this.setB((int) Math.sqrt(this.getB()));
		return this;
	}

	public void lerp(Color3f c, float alpha)
	{
		this.setR((int) (this.getR() + (c.getR() - this.getR()) * alpha));
		this.setG((int) (this.getG() + (c.getG() - this.getG()) * alpha));
		this.setB((int) (this.getB() + (c.getB() - this.getB()) * alpha));
	}

	public String toString()
	{
		return "rgb(" + Math.floor(this.r * 255) + ',' + Math.floor(this.g * 255) + ','
				+ Math.floor(this.b * 255) + ")";
	}

	public Color3f clone()
	{
		return new Color3f(this.hex);
	}
}
