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

package org.parallax3d.parallax.system.jsonbind.vm.json;

public class JSON {
    JSON() {
    }

    static double checkDouble(double var0) throws JSONException {
        if(!Double.isInfinite(var0) && !Double.isNaN(var0)) {
            return var0;
        } else {
            throw new JSONException("Forbidden numeric value: " + var0);
        }
    }

    static Boolean toBoolean(Object var0) {
        if(var0 instanceof Boolean) {
            return (Boolean)var0;
        } else {
            if(var0 instanceof String) {
                String var1 = (String)var0;
                if("true".equalsIgnoreCase(var1)) {
                    return Boolean.valueOf(true);
                }

                if("false".equalsIgnoreCase(var1)) {
                    return Boolean.valueOf(false);
                }
            }

            return null;
        }
    }

    static Double toDouble(Object var0) {
        if(var0 instanceof Double) {
            return (Double)var0;
        } else if(var0 instanceof Number) {
            return Double.valueOf(((Number)var0).doubleValue());
        } else {
            if(var0 instanceof String) {
                try {
                    return Double.valueOf((String)var0);
                } catch (NumberFormatException var2) {
                    ;
                }
            }

            return null;
        }
    }

    static Integer toInteger(Object var0) {
        if(var0 instanceof Integer) {
            return (Integer)var0;
        } else if(var0 instanceof Number) {
            return Integer.valueOf(((Number)var0).intValue());
        } else {
            if(var0 instanceof String) {
                try {
                    return Integer.valueOf((int)Double.parseDouble((String)var0));
                } catch (NumberFormatException var2) {
                    ;
                }
            }

            return null;
        }
    }

    static Long toLong(Object var0) {
        if(var0 instanceof Long) {
            return (Long)var0;
        } else if(var0 instanceof Number) {
            return Long.valueOf(((Number)var0).longValue());
        } else {
            if(var0 instanceof String) {
                try {
                    return Long.valueOf((long)Double.parseDouble((String)var0));
                } catch (NumberFormatException var2) {
                    ;
                }
            }

            return null;
        }
    }

    static String toString(Object var0) {
        return var0 instanceof String?(String)var0:(var0 != null?String.valueOf(var0):null);
    }

    public static JSONException typeMismatch(Object var0, Object var1, String var2) throws JSONException {
        if(var1 == null) {
            throw new JSONException("Value at " + var0 + " is null.");
        } else {
            throw new JSONException("Value " + var1 + " at " + var0 + " of type " + var1.getClass().getName() + " cannot be converted to " + var2);
        }
    }

    public static JSONException typeMismatch(Object var0, String var1) throws JSONException {
        if(var0 == null) {
            throw new JSONException("Value is null.");
        } else {
            throw new JSONException("Value " + var0 + " of type " + var0.getClass().getName() + " cannot be converted to " + var1);
        }
    }
}
