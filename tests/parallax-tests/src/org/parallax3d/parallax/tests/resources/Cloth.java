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

package org.parallax3d.parallax.tests.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.parallax3d.parallax.graphics.core.Face3;
import org.parallax3d.parallax.graphics.core.Geometry;
import org.parallax3d.parallax.graphics.objects.Mesh;
import org.parallax3d.parallax.math.Vector3;

/**
 * Suggested Readings
 * <p>
 * Advanced Character Physics by Thomas Jakobsen Character<br>
 * <a href="http://freespace.virgin.net/hugo.elias/models/m_cloth.htm">freespace.virgin.net</a><br>
 * <a href="http://en.wikipedia.org/wiki/Cloth_modeling">wikipedia.org</a><br>
 * <a href="http://cg.alexandra.dk/tag/spring-mass-system/">cg.alexandra.dk</a><br>
 * Real-time Cloth Animation <a href="http://www.darwin3d.com/gamedev/articles/col0599.pdf">www.darwin3d.com</a>
 * <p>
 * Based on three.js code
 * 
 * @author thothbot
 *
 */
public class Cloth 
{
//	public class ClothPlane extends ParametricGeometry
//	{
//
//		public ClothPlane(final double width, final double height, int slices, int stacks)
//		{
//			super(new ParametricFunction() {
//				
//				@Override
//				public Vector3 run(double u, double v)
//				{
//					double x = (u-0.5) * width;
//					double y = (v+0.5) * height;
//
//					return new Vector3(x, y, 0);
//				}
//			}, slices, stacks, true);
//		}
//
//	}
	
	class Particle
	{
		private Vector3 position;
		private Vector3 previous;
		private Vector3 original;
		private Vector3 a;
		private double mass;
		private double invMass;
		
		private Vector3 tmp;
		private Vector3 tmp2;
		
		public Particle(Vector3 position, double mass)
		{
			this.position = position; // position
			this.previous = position; // previous
			this.original = position; 
			this.a = new Vector3(0, 0, 0); // acceleration
			this.mass = mass;
			this.invMass = 1 / mass;
			this.tmp = new Vector3();
			this.tmp2 = new Vector3();
		}
		
		/**
		 * Force -> Acceleration
		 */
		public void addForce(Vector3 force) 
		{
			this.a.add(
				this.tmp2.copy(force).multiply(this.invMass)
			);
		}
		
		/**
		 * Performs verlet integration
		 */
		public void integrate(double timesq) 
		{
			Vector3 newPos = this.tmp.sub(this.position, this.previous);
			newPos.multiply(drag).add(this.position);
			newPos.add(this.a.multiply(timesq));
			
			this.tmp = this.previous;
			this.previous = this.position;
			this.position = newPos;

			this.a.set(0, 0, 0);
		}
		
		public String toString()
		{
			return this.position.toString();
		}
	}

	public static final double damping = 0.03;
	public static final double drag = 1 - damping;
	public static final double mass = 0.1;
	public static final double restDistance = 25;
	
	public static final int xSegs = 10; //
	public static final int ySegs = 10; //

	private static final double GRAVITY = 981 * 1.4; // 
	private static final Vector3 gravity = new Vector3( 0, -GRAVITY, 0 ).multiply(Cloth.mass);

	private static final double TIMESTEP = 18 / 1000;
	private static final double TIMESTEP_SQ = TIMESTEP * TIMESTEP;
	
	Geometry clothGeometry;

	private int width;
	private int height;

	private boolean isWindEnabled = true;
	private double windStrength = 2;
	private Vector3 windForce = new Vector3(0,0,0);

	private Mesh ball;
	private Vector3 ballPosition = new Vector3(0, -45, 0);
	private double ballSize = 60;

	private List<Particle> particles;
	private List<List<Particle>> constrains;

	private Vector3 tmpForce = new Vector3();
	
	private double lastTime;
	
	public List<Integer> pins = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	public Cloth()
	{
		this(xSegs, ySegs);
	}

	public Cloth(int width, int height)
	{	
		this.width = width;
		this.height = height;
		
		particles = new ArrayList<Particle>();
		constrains = new ArrayList<List<Particle>>();

		createParticles();
		createStructure();
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isWindEnabled() {
		return isWindEnabled;
	}

	public void setWindEnabled(boolean isWindEnabled) {
		this.isWindEnabled = isWindEnabled;
	}

	public double getWindStrength() {
		return windStrength;
	}

	public void setWindStrength(double windStrength) {
		this.windStrength = windStrength;
	}

	public Vector3 getWindForce() {
		return windForce;
	}

	public void setWindForce(Vector3 windForce) {
		this.windForce = windForce;
	}
	
	public Mesh getBall() {
		return ball;
	}

	public void setBall(Mesh ball) {
		this.ball = ball;
	}

	public double getBallSize() {
		return ballSize;
	}

	public void setBallSize(double ballSize) {
		this.ballSize = ballSize;
	}
	
	public Geometry getGeometry() {
		return clothGeometry;
	}

	public void setGeometry(Geometry clothGeometry) {
		this.clothGeometry = clothGeometry;
	}

	public void simulate() 
	{
		clothGeometry.computeFaceNormals();
		clothGeometry.computeVertexNormals();
		
		// Aerodynamics forces
		if (isWindEnabled)
		{
			List<Face3> faces = clothGeometry.getFaces();

			for (int i = 0, il = faces.size(); i < il; i++)
			{
				Face3 face = faces.get(i);
				Vector3 normal = face.getNormal();

				tmpForce.copy(normal).normalize().multiply(normal.dot(windForce));
				particles.get(face.getA()).addForce(tmpForce);
				particles.get(face.getB()).addForce(tmpForce);
				particles.get(face.getC()).addForce(tmpForce);
			}
		}
		
		for (int i = 0, il = particles.size(); i < il; i++) 
		{
			Particle particle = particles.get(i);
			particle.addForce(gravity);
			particle.integrate(TIMESTEP_SQ);
		}

		// Start Constrains
		for (int i = 0, il = constrains.size(); i < il; i++) 
		{
			List<Particle> constrain = constrains.get(i);
			satisifyConstrains(constrain.get(0), constrain.get(1));
		}

		// Ball Constrains

//		double time = Duration.currentTimeMillis();
//		ballPosition.setZ( -Math.sin(time / 600) * 90 ) ; //+ 40;
//		ballPosition.setX( Math.cos(time / 400) * 70 );

		if (ball != null && ball.isVisible())
		{
			for (int i = 0, il = particles.size(); i < il; i++) 
			{
				Particle particle = particles.get(i);
				Vector3 pos = particle.position;
				Vector3 diff = new Vector3();
				diff.sub(pos, ballPosition);
				if (diff.length() < ballSize) 
				{
					// collided
					diff.normalize().multiply(ballSize);
					pos.copy(ballPosition).add(diff);
				}
			}
		}

		// Floor Constains
		for (int i = 0, il = particles.size(); i<il; i++) 
		{
			Particle particle = particles.get(i);
			Vector3 pos = particle.position;
			if (pos.getY() < -250) 
			{
				pos.setY( -250 );
			}
		}

		// Pin Constrains
		for (int i = 0, il = pins.size(); i<il; i++) 
		{
			int xy = pins.get(i);
			Particle p = particles.get(xy);
			p.position.copy(p.original);
			p.previous.copy(p.original);
		}
				
		for ( int i = 0, il = particles.size(); i < il; i ++ ) 
		{
			clothGeometry.getVertices().get( i ).copy( particles.get( i ).position );
		}
		
		clothGeometry.setNormalsNeedUpdate( true );
		clothGeometry.setVerticesNeedUpdate( true );

		ball.getPosition().copy( ballPosition );
	}

	private void createParticles()
	{
		for (int v = 0; v <= height; v++) 
		{
			for (int u = 0; u <= width; u++) 
			{
				particles.add(
					new Particle(clothFunction( u, v ), Cloth.mass)
				);
			}
		}
	}
	
	private Vector3 clothFunction(double u, double v)
	{
		double x = (u / this.width - 0.5) * restDistance * this.width;
		double y = (v / this.height + 0.5) * restDistance * this.height;

		return new Vector3(x, y, 0);
	}
	
	private void createStructure()
	{
		for (int v = 0; v < height; v++) 
		{
			for (int u = 0; u < width; u++) 
			{
				constrains.add(Arrays.asList(
					particles.get( index(u, v) ),
					particles.get( index(u, v + 1) )
				));

				constrains.add(Arrays.asList(
					particles.get( index(u, v) ),
					particles.get( index(u + 1, v) )
				));

			}
		}

		for (int u = width, v=0; v < height; v++) 
		{
			constrains.add(Arrays.asList(
				particles.get( index(u, v) ),
				particles.get( index(u, v + 1) )
			));
		}

		for (int v = height, u = 0; u < width; u++) 
		{
			constrains.add(Arrays.asList(
				particles.get( index(u, v) ),
				particles.get( index(u + 1, v) )
			));
		}
	}
	
	private void satisifyConstrains(Particle p1, Particle p2)
	{
		Vector3 diff = new Vector3();
		
		diff.sub(p2.position, p1.position);
		double currentDist = diff.length();
	
		if (currentDist == 0) 
			return; // prevents division by 0
		
		Vector3 correction = diff.multiply(1 - restDistance/currentDist);
		Vector3 correctionHalf = correction.multiply(0.5);

		p1.position.add(correctionHalf);
		p2.position.sub(correctionHalf);
	}
	
	private int index(int u, int v) 
	{
		return u + v * (width + 1);
	}
}
