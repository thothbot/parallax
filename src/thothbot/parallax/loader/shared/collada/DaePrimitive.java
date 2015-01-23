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

package thothbot.parallax.loader.shared.collada;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;

public class DaePrimitive extends DaeElement 
{
	private DaeMesh mesh;
	private String material;
	private int count;

//	protected Map<String, Map<Integer, String>> sourceMaps;
//	protected Map<String, Map<Integer, int[]>> indices;
//	protected Map<String, Map<Integer, Integer>> indexCounts;

	public DaePrimitive(Node node, DaeMesh mesh)
	{
		super(node);
		this.mesh = mesh;
//		this.sourceMaps  = new HashMap<String, Map<Integer,String>>();
//		this.indices     = new HashMap<String, Map<Integer, int[]>>();
//		this.indexCounts = new HashMap<String, Map<Integer,Integer>>();
	
		Log.debug("DaePrimitive() " + toString()); 
	}
		
	@Override
	public void destroy() 
	{
		super.destroy();
//		indices.clear();
//		sourceMaps.clear();
//		indices = null;
//		sourceMaps = null;
	}

	public int getCount() {
		return count;
	}

	public String getMaterial() {
		return material;
	}
	
	public DaeMesh getMesh() {
		return mesh;
	}
	
	@Override
	public void read() 
	{
		this.material = readAttribute("material");
		this.count = readIntAttribute("count", 0);
	}

//	public void addIndex(DaeInput input, int index, int maxItems) 
//	{
//		String semantic = input.getSemantic();
//		int set = input.getSet();
//
//		if (!indices.containsKey(semantic)) 
//		{
//			indices.put(semantic, new HashMap<Integer, int[]>());
//		}
//
//		if (!indexCounts.containsKey(semantic)) 
//		{
//			indexCounts.put(semantic, new HashMap<Integer, Integer>());
//		}
//
//		HashMap<Integer, int[]> in = (HashMap<Integer, int[]>) indices.get(semantic);
//		if (!in.containsKey(set)) 
//		{
//			in.put(set, new int[this.count*3]);
//		}
//
//		if (!indexCounts.get(semantic).containsKey(set)) 
//		{
//			indexCounts.get(semantic).put(set, 0);
//		}
//
//		int current = indexCounts.get(semantic).get(set);
//		if (current < in.get(set).length) 
//		{
//			in.get(set)[current] = index;
//			indexCounts.get(semantic).put(set, current+1);
//		} 
//		else 
//		{
//			Log.error("out of bounds " + current + " " + in.get(set).length);
//		}
//
//
//		if (!sourceMaps.containsKey(semantic)) 
//		{
//			Log.debug("DaePrimitive() putting sourcemaps " + semantic);
//			sourceMaps.put(semantic, new HashMap<Integer, String>());
//		}
//
//		HashMap<Integer, String> sourceMap = (HashMap<Integer, String>) sourceMaps.get(semantic);
//
//		if (!sourceMap.containsKey(set)) 
//		{
//			Log.debug("DaePrimitive() putting sourcemap set " + set + " input source: " + input.getSource());
//			sourceMap.put(set, input.getSource());
//		}
//	}

//	public float[] getNormals() 
//	{
//		int[] vIndices = getIndices("VERTEX", 0);
//		int[] nIndices = getIndices("NORMAL", 0);
//		DaeSource normalSource = DaeDocument.getSourceByID(getNode(), getSource("NORMAL", 0));
//
//		float[] ret = null;
//
//		if (normalSource != null && normalSource.getFloats() != null &&
//			vIndices != null && nIndices != null && vIndices.length == nIndices.length) 
//		{
//
//			float[] data = normalSource.getFloats();
//
//			ret = new float[data.length];
//
//			for (int i = 0; i < vIndices.length; i++) 
//			{
//				int v = vIndices[i] * 3;
//				int n = nIndices[i] * 3;
//
//				ret[v] = data[n];
//				ret[v+1] = data[n+1];
//				ret[v+2] = data[n+2];
//			}
//		}
//
//		return ret;
//	}

//	public int[] getIndices(String semantic, Integer set) 
//	{
//		if (indices.containsKey(semantic)) 
//		{
//			set = indices.get(semantic).containsKey(set) ? set : getLowestSet(semantic);
//			if (indices.get(semantic).containsKey(set)) 
//			{
//				return indices.get(semantic).get(set);
//			}
//		}
//		return null;
//	}
//
//	public String getSource(String semantic, Integer set) 
//	{
//		if (sourceMaps.containsKey(semantic)) 
//		{
//			HashMap<Integer, String> sourceMap = (HashMap<Integer, String>) sourceMaps.get(semantic);
//			set = sourceMap.containsKey(set) ? set : getLowestSet(semantic);
//			return sourceMap.get(set);
//		}
//		return null;
//	}
//
//	private Integer getLowestSet(String semantic) 
//	{
//		if (indices.containsKey(semantic)) 
//		{
//			HashMap<Integer, int[]> indexMap = 
//				(HashMap<Integer, int[]>) indices.get(semantic);
//			int min = 10000;
//			for (int set: indexMap.keySet()) 
//			{
//				min = Math.min(min, set);
//			}
//			if (indexMap.containsKey(min)) 
//			{
//				return min;
//			}
//		}
//		return null;
//	}
	
	public String toString()
	{
		return "{material=" + this.material + ", count=" + this.count + "}";
	}
}