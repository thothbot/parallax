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

package org.parallax3d.parallax.loaders.typefacejs;

import org.parallax3d.parallax.system.jsonbind.AutoBean;

public interface JsoOriginalFontInformation {
    
    @AutoBean.PropertyName("postscript_name")
	String getPostscriptName();
    @AutoBean.PropertyName("version_string")
	String getVersionString();
    @AutoBean.PropertyName("vendor_url")
	String getVendorUrl();
    @AutoBean.PropertyName("full_font_name")
	String getFullFontName();
    @AutoBean.PropertyName("font_family_name")
	String getFontFamilyName();

	String getCopyright();
	String getDescription();
	String getTrademark();
	String getDesigner();

    @AutoBean.PropertyName("designer_url")
	String getDesignerUrl();

    @AutoBean.PropertyName("unique_font_identifier")
	String getUniqueFontIdentifier();
    @AutoBean.PropertyName("license_url")
	String getLicenseUrl();
    @AutoBean.PropertyName("license_description")
	String getLicenseDescription();
    @AutoBean.PropertyName("manufacturer_name")
	String getManufacturerName();
    @AutoBean.PropertyName("font_sub_family_name")
	String getFontSubFamilyName();
}
