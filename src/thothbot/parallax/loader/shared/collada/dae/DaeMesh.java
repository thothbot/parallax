/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.loader.shared.collada.dae;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thothbot.parallax.core.shared.Log;
import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.core.Vector3f;
import thothbot.parallax.core.shared.objects.Mesh;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class DaeMesh extends DaeElement 
{
	private Map<String, DaeSource> sources;
	private DaeVertices vertices;
	private List<DaePrimitive> primitives;
	private Geometry geometry;
	//	private String verticesID;

	public DaeMesh(Node node) 
	{
		super(node);
		
		Log.debug("DaeMesh() " + toString());
		getGeometry();
	}

	@Override
	public void destroy() 
	{
		super.destroy();

		if (sources != null) 
		{
			sources.clear();
			sources = null;
		}

		if (primitives != null) 
		{
			for (DaePrimitive primitive: primitives) 
				primitive.destroy();

			primitives.clear();
			primitives = null;
		}

		vertices = null;
//		verticesID = null;
	}

	@Override
	public void read() 
	{
		sources = new HashMap<String, DaeSource>();
		primitives = new ArrayList<DaePrimitive>();
		vertices = null;
//		verticesID = null;

		NodeList list = getNode().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) 
		{
			Node child = list.item(i);
			String nodeName = child.getNodeName();

			if (nodeName.compareTo("source") == 0) 
			{
				DaeSource source = new DaeSource(child);
				sources.put(source.getID(), source);
			} 
			else if (nodeName.compareTo("vertices") == 0) 
			{
				vertices = new DaeVertices(child);
			}
			else if (nodeName.compareTo("triangles") == 0) 
			{
				primitives.add(new DaeTriangles(child, this));
			}
		}
	}
	
	public Geometry getGeometry()
	{
		if(this.geometry == null)
		{
			this.geometry = new Geometry();
			DaeInput input = this.vertices.getInput();
			if(input.getSemantic().compareTo("POSITION") == 0)
			{
				float[] vertexData = sources.get(input.getSource()).getData().getData();
				for ( int i = 0; i < vertexData.length; i += 3 ) 
				{
					this.geometry.getVertices().add( new Vector3f(
							vertexData[ i ], vertexData[ 1 + i ], vertexData[ 2 + i ]) );
				}
			}
		}
		
		for ( int i = 0; i < this.primitives.size(); i ++ ) 
		{
			DaePrimitive primitive = this.primitives.get( i );
//			primitive.setVertices( this.vertices );
			handlePrimitive( primitive);

		}

		this.geometry.computeCentroids();
		this.geometry.computeFaceNormals(false);
		
//		if ( this.geometry.calcNormals )
//			this.geometry.computeVertexNormals();		
		
		this.geometry.computeBoundingBox();
		
		return this.geometry;
	}
	
	private void handlePrimitive(DaePrimitive primitive)
	{
		
	}

//	private DaeSource readSource(String id) 
//	{
//		if (id != null) 
//		{
//			DaeSource source = DaeDocument.getSourceByID(getNode(), id);
//			if (source != null && source.getID() != null) 
//			{
//				if (sources.get(id) == null) 
//				{
//					sources.put(source.getID(), source);
//				}
//				return source;
//			}
//		} 
//		return null;
//	}

//	private void readVertices(Node node) 
//	{
//		NodeList list = node.getChildNodes();
//		for (int i = 0; i < list.getLength(); i++) 
//		{
//			Node child = list.item(i);
//			String nodeName = child.getNodeName();
//
//			if (nodeName.compareTo("input") == 0) 
//			{
//				DaeInput input = new DaeInput(child);
//				if (input.getSemantic().compareTo("POSITION") == 0) 
//				{
//					vertices = readSource(input.getSource());
//					verticesID = readAttribute(node, "id");
//				}
//			}
//		}
//	}

	public List<DaePrimitive> getPrimitives() {
		return primitives;
	}

	public Map<String, DaeSource> getSources() {
		return sources;
	}

	public DaeVertices getVertices() {
		return vertices;
	}

//	public String getVerticesID() {
//		return verticesID;
//	}
	
	@Override
	public String toString()
	{
		return "Mesh";
	}
}
