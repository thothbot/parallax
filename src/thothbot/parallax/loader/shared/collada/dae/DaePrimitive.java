/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
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

package thothbot.parallax.loader.shared.collada.dae;

import java.util.HashMap;
import java.util.Map;

import thothbot.parallax.core.shared.Log;

import com.google.gwt.xml.client.Node;

public class DaePrimitive extends DaeElement 
{
	private DaeMesh mesh;
	private String material;
	private int count;

	protected Map<String, Map<Integer, String>> sourceMaps;
	protected Map<String, Map<Integer, int[]>> indices;
	protected Map<String, Map<Integer, Integer>> indexCounts;

	public DaePrimitive(DaeDocument document, DaeMesh mesh) 
	{
		super(document);
		this.mesh = mesh;
		this.sourceMaps = new HashMap<String, Map<Integer,String>>();
		this.indices = new HashMap<String, Map<Integer, int[]>>();
		this.indexCounts = new HashMap<String, Map<Integer,Integer>>();
	}

	public DaePrimitive(DaeDocument document, Node node, DaeMesh mesh) 
	{
		this(document, mesh);
		this.mesh = mesh;
		if (node != null) {
			read(node);
		}
	}

	public void addIndex(DaeInput input, int index, int maxItems) 
	{
		String semantic = input.getSemantic();
		int set = input.getSet();

		if (!indices.containsKey(semantic)) 
		{
			indices.put(semantic, new HashMap<Integer, int[]>());
		}

		if (!indexCounts.containsKey(semantic)) 
		{
			indexCounts.put(semantic, new HashMap<Integer, Integer>());
		}

		HashMap<Integer, int[]> in = (HashMap<Integer, int[]>) indices.get(semantic);
		if (!in.containsKey(set)) 
		{
			in.put(set, new int[this.count*3]);
		}

		if (!indexCounts.get(semantic).containsKey(set)) 
		{
			indexCounts.get(semantic).put(set, 0);
		}

		int current = indexCounts.get(semantic).get(set);
		if (current < in.get(set).length) 
		{
			in.get(set)[current] = index;
			indexCounts.get(semantic).put(set, current+1);
		} 
		else 
		{
			Log.error("out of bounds " + current + " " + in.get(set).length);
		}


		if (!sourceMaps.containsKey(semantic)) 
		{
			Log.debug("putting sourcemaps " + semantic);
			sourceMaps.put(semantic, new HashMap<Integer, String>());
		}

		HashMap<Integer, String> sourceMap = (HashMap<Integer, String>) sourceMaps.get(semantic);

		if (!sourceMap.containsKey(set)) 
		{
			Log.debug("putting sourcemap set " + set + " input source: " + input.getSource());
			sourceMap.put(set, input.getSource());
		}
	}

	public float[] getNormals() 
	{
		int[] vIndices = getIndices("VERTEX", 0);
		int[] nIndices = getIndices("NORMAL", 0);
		DaeSource normalSource = getDocument().getSourceByID(getSource("NORMAL", 0));

		float[] ret = null;

		if (normalSource != null && normalSource.getFloats() != null &&
			vIndices != null && nIndices != null && vIndices.length == nIndices.length) 
		{

			float[] data = normalSource.getFloats();

			ret = new float[data.length];

			for (int i = 0; i < vIndices.length; i++) 
			{
				int v = vIndices[i] * 3;
				int n = nIndices[i] * 3;

				ret[v] = data[n];
				ret[v+1] = data[n+1];
				ret[v+2] = data[n+2];
			}
		}

		return ret;
	}

	@Override
	public void destroy() 
	{
		super.destroy();
		indices.clear();
		sourceMaps.clear();
		indices = null;
		sourceMaps = null;
	}

	public int getCount() {
		return count;
	}

	public String getMaterial() {
		return material;
	}

	public int[] getIndices(String semantic, Integer set) 
	{
		if (indices.containsKey(semantic)) 
		{
			set = indices.get(semantic).containsKey(set) ? set : getLowestSet(semantic);
			if (indices.get(semantic).containsKey(set)) 
			{
				return indices.get(semantic).get(set);
			}
		}
		return null;
	}

	public String getSource(String semantic, Integer set) 
	{
		if (sourceMaps.containsKey(semantic)) 
		{
			HashMap<Integer, String> sourceMap = (HashMap<Integer, String>) sourceMaps.get(semantic);
			set = sourceMap.containsKey(set) ? set : getLowestSet(semantic);
			return sourceMap.get(set);
		}
		return null;
	}

	public DaeMesh getMesh() {
		return mesh;
	}

	@Override
	public void read(Node node) 
	{
		super.read(node);

		this.material = readAttribute(node, "material");
		this.count = readIntAttribute(node, "count", 0);
	}

	private Integer getLowestSet(String semantic) 
	{
		if (indices.containsKey(semantic)) 
		{
			HashMap<Integer, int[]> indexMap = 
				(HashMap<Integer, int[]>) indices.get(semantic);
			int min = 10000;
			for (int set: indexMap.keySet()) 
			{
				min = Math.min(min, set);
			}
			if (indexMap.containsKey(min)) 
			{
				return min;
			}
		}
		return null;
	}
}