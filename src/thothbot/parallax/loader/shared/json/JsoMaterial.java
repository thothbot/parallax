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

package thothbot.parallax.loader.shared.json;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface JsoMaterial 
{
	@PropertyName("DbgColor")
	int getDbgColor();
	@PropertyName("DbgIndex")
	int getDbgIndex();
	@PropertyName("DbgName")
	String getDbgName();
	
	JsoBlending getBlending();
	
	/**
	 * List of R, G, B
	 */
	List<Double> getColorAmbient();

	/**
	 * List of R, G, B
	 */
	List<Double> getColorDiffuse();
	
	/**
	 * List of R, G, B
	 */
	List<Double> getColorSpecular();
	
	/**
	 * List of R, G, B
	 */
	List<Double> getColorEmissive();
	
	double getIllumination();
	
	double getOpticalDensity();
	
	boolean getDepthTest();
	
	boolean getDepthWrite();

	String getShading();
	
	double getSpecularCoef();
	
	double getTransparency();
	
	boolean getTransparent();
	
	boolean getVertexColors();

	///////////////////////////////////////////////
	// Textures
	
	// Diffuse
	String getMapDiffuse();
	List<Integer> getMapDiffuseRepeat();
	List<JsoTextureWrapMode> getMapDiffuseWrap();
	int getMapDiffuseAnisotropy();
	List<Double> getMapDiffuseOffset();

	// Light
	String getMapLight();
	List<Integer> getMapLightRepeat();
	List<JsoTextureWrapMode> getMapLightWrap();
	int getMapLightAnisotropy();
	List<Double> getMapLightOffset();
	
	// Bump
	String getMapBump();
	List<Integer> getMapBumpRepeat();
	List<JsoTextureWrapMode> getMapBumpWrap();
	double getMapBumpScale();
	int getMapBumpAnisotropy();
	List<Double> getMapBumpOffset();
	
	// Normal
	String getMapNormal();
	List<Integer> getMapNormalRepeat();
	List<JsoTextureWrapMode> getMapNormalWrap();
	int getMapNormalAnisotropy();
	List<Double> getMapNormalOffset();
	double getMapNormalFactor();
	
	// Specular
	String getMapSpecular();
	List<Integer> getMapSpecularRepeat();
	List<JsoTextureWrapMode> getMapSpecularWrap();
	int getMapSpecularAnisotropy();
	List<Double> getMapSpecularOffset();
	
	String getMapAlpha();
	List<Integer> getMapAlphaRepeat();
	List<JsoTextureWrapMode> getMapAlphaWrap();
	int getMapAlphaAnisotropy();
	List<Double> getMapAlphaOffset();
}
