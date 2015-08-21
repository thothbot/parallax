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

package thothbot.parallax.core.shared.helpers;

import thothbot.parallax.core.shared.core.Geometry;
import thothbot.parallax.core.shared.materials.LineBasicMaterial;
import thothbot.parallax.core.shared.materials.Material;
import thothbot.parallax.core.shared.math.Color;
import thothbot.parallax.core.shared.math.Vector3;
import thothbot.parallax.core.shared.objects.Line;

// port from three.js r70 GridHelper.js
/**
 * 
 * @author bartolomiew 
 *
 */
public class GridHelper extends Line {

    private Color color1;
    private Color color2;

    public GridHelper(double size, double step) {
        super(new Geometry(), new LineBasicMaterial(), Line.MODE.PIECES);
        Geometry geometry = (Geometry) getGeometry();
        
        LineBasicMaterial material = (LineBasicMaterial) getMaterial();
        material.setVertexColors(Material.COLORS.VERTEX);

        color1 = new Color(0x444444);
        color2 = new Color(0x888888);

        for (double i = -size; i <= size; i += step) {

            geometry.getVertices().add(new Vector3(-size, 0, i));
            geometry.getVertices().add(new Vector3(size, 0, i));
            geometry.getVertices().add(new Vector3(i, 0, -size));
            geometry.getVertices().add(new Vector3(i, 0, size));

            Color color = i == 0 ? this.color1 : this.color2;

            geometry.getColors().add(color);
            geometry.getColors().add(color);
            geometry.getColors().add(color);
            geometry.getColors().add(color);

        }
    }

    public void setColors(int colorCenterLine, int colorGrid) 
    {
    	setColors(new Color(colorCenterLine), new Color(colorGrid));
    }
    
    public void setColors(Color colorCenterLine, Color colorGrid) 
    {

        color1.copy( colorCenterLine );
        color2.copy( colorGrid );

        geometry.setColorsNeedUpdate(true);

    }

}