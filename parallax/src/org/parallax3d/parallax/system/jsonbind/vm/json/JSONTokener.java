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

public class JSONTokener {
    private final String in;
    private int pos;

    public JSONTokener(String var1) {
        if(var1 != null && var1.startsWith("\ufeff")) {
            var1 = var1.substring(1);
        }

        this.in = var1;
    }

    public Object nextValue() throws JSONException {
        int var1 = this.nextCleanInternal();
        switch(var1) {
            case -1:
                throw this.syntaxError("End of input");
            case 34:
            case 39:
                return this.nextString((char)var1);
            case 91:
                return this.readArray();
            case 123:
                return this.readObject();
            default:
                --this.pos;
                return this.readLiteral();
        }
    }

    private int nextCleanInternal() throws JSONException {
        while(this.pos < this.in.length()) {
            char var1 = this.in.charAt(this.pos++);
            switch(var1) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    break;
                case '#':
                    this.skipToEndOfLine();
                    break;
                case '/':
                    if(this.pos == this.in.length()) {
                        return var1;
                    }

                    char var2 = this.in.charAt(this.pos);
                    switch(var2) {
                        case '*':
                            ++this.pos;
                            int var3 = this.in.indexOf("*/", this.pos);
                            if(var3 == -1) {
                                throw this.syntaxError("Unterminated comment");
                            }

                            this.pos = var3 + 2;
                            continue;
                        case '/':
                            ++this.pos;
                            this.skipToEndOfLine();
                            continue;
                        default:
                            return var1;
                    }
                default:
                    return var1;
            }
        }

        return -1;
    }

    private void skipToEndOfLine() {
        while(true) {
            if(this.pos < this.in.length()) {
                char var1 = this.in.charAt(this.pos);
                if(var1 != 13 && var1 != 10) {
                    ++this.pos;
                    continue;
                }

                ++this.pos;
            }

            return;
        }
    }

    public String nextString(char var1) throws JSONException {
        StringBuilder var2 = null;
        int var3 = this.pos;

        while(this.pos < this.in.length()) {
            char var4 = this.in.charAt(this.pos++);
            if(var4 == var1) {
                if(var2 == null) {
                    return new String(this.in.substring(var3, this.pos - 1));
                }

                var2.append(this.in, var3, this.pos - 1);
                return var2.toString();
            }

            if(var4 == 92) {
                if(this.pos == this.in.length()) {
                    throw this.syntaxError("Unterminated escape sequence");
                }

                if(var2 == null) {
                    var2 = new StringBuilder();
                }

                var2.append(this.in, var3, this.pos - 1);
                var2.append(this.readEscapeCharacter());
                var3 = this.pos;
            }
        }

        throw this.syntaxError("Unterminated string");
    }

    private char readEscapeCharacter() throws JSONException {
        char var1 = this.in.charAt(this.pos++);
        switch(var1) {
            case '\"':
            case '\'':
            case '\\':
            default:
                return var1;
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case 'u':
                if(this.pos + 4 > this.in.length()) {
                    throw this.syntaxError("Unterminated escape sequence");
                } else {
                    String var2 = this.in.substring(this.pos, this.pos + 4);
                    this.pos += 4;
                    return (char)Integer.parseInt(var2, 16);
                }
        }
    }

    private Object readLiteral() throws JSONException {
        String var1 = this.nextToInternal("{}[]/\\:,=;# \t\f");
        if(var1.length() == 0) {
            throw this.syntaxError("Expected literal value");
        } else if("null".equalsIgnoreCase(var1)) {
            return JSONObject.NULL;
        } else if("true".equalsIgnoreCase(var1)) {
            return Boolean.TRUE;
        } else if("false".equalsIgnoreCase(var1)) {
            return Boolean.FALSE;
        } else {
            if(var1.indexOf(46) == -1) {
                byte var2 = 10;
                String var3 = var1;
                if(!var1.startsWith("0x") && !var1.startsWith("0X")) {
                    if(var1.startsWith("0") && var1.length() > 1) {
                        var3 = var1.substring(1);
                        var2 = 8;
                    }
                } else {
                    var3 = var1.substring(2);
                    var2 = 16;
                }

                try {
                    long var4 = Long.parseLong(var3, var2);
                    if(var4 <= 2147483647L && var4 >= -2147483648L) {
                        return Integer.valueOf((int)var4);
                    }

                    return Long.valueOf(var4);
                } catch (NumberFormatException var8) {
                    ;
                }
            }

            try {
                return Double.valueOf(var1);
            } catch (NumberFormatException var7) {
                return new String(var1);
            }
        }
    }

    private String nextToInternal(String var1) {
        int var2;
        for(var2 = this.pos; this.pos < this.in.length(); ++this.pos) {
            char var3 = this.in.charAt(this.pos);
            if(var3 == 13 || var3 == 10 || var1.indexOf(var3) != -1) {
                return this.in.substring(var2, this.pos);
            }
        }

        return this.in.substring(var2);
    }

    private JSONObject readObject() throws JSONException {
        JSONObject var1 = new JSONObject();
        int var2 = this.nextCleanInternal();
        if(var2 == 125) {
            return var1;
        } else {
            if(var2 != -1) {
                --this.pos;
            }

            while(true) {
                Object var3 = this.nextValue();
                if(!(var3 instanceof String)) {
                    if(var3 == null) {
                        throw this.syntaxError("Names cannot be null");
                    }

                    throw this.syntaxError("Names must be strings, but " + var3 + " is of type " + var3.getClass().getName());
                }

                int var4 = this.nextCleanInternal();
                if(var4 != 58 && var4 != 61) {
                    throw this.syntaxError("Expected \':\' after " + var3);
                }

                if(this.pos < this.in.length() && this.in.charAt(this.pos) == 62) {
                    ++this.pos;
                }

                var1.put((String)var3, this.nextValue());
                switch(this.nextCleanInternal()) {
                    case 44:
                    case 59:
                        break;
                    case 125:
                        return var1;
                    default:
                        throw this.syntaxError("Unterminated object");
                }
            }
        }
    }

    private JSONArray readArray() throws JSONException {
        JSONArray var1 = new JSONArray();
        boolean var2 = false;

        while(true) {
            switch(this.nextCleanInternal()) {
                case -1:
                    throw this.syntaxError("Unterminated array");
                case 44:
                case 59:
                    var1.put((Object)null);
                    var2 = true;
                    break;
                case 93:
                    if(var2) {
                        var1.put((Object)null);
                    }

                    return var1;
                default:
                    --this.pos;
                    var1.put(this.nextValue());
                    switch(this.nextCleanInternal()) {
                        case 44:
                        case 59:
                            var2 = true;
                            break;
                        case 93:
                            return var1;
                        default:
                            throw this.syntaxError("Unterminated array");
                    }
            }
        }
    }

    public JSONException syntaxError(String var1) {
        return new JSONException(var1 + this);
    }

    public String toString() {
        return " at character " + this.pos + " of " + this.in;
    }

    public boolean more() {
        return this.pos < this.in.length();
    }

    public char next() {
        return this.pos < this.in.length()?this.in.charAt(this.pos++):'\u0000';
    }

    public char next(char var1) throws JSONException {
        char var2 = this.next();
        if(var2 != var1) {
            throw this.syntaxError("Expected " + var1 + " but was " + var2);
        } else {
            return var2;
        }
    }

    public char nextClean() throws JSONException {
        int var1 = this.nextCleanInternal();
        return var1 == -1?'\u0000':(char)var1;
    }

    public String next(int var1) throws JSONException {
        if(this.pos + var1 > this.in.length()) {
            throw this.syntaxError(var1 + " is out of bounds");
        } else {
            String var2 = this.in.substring(this.pos, this.pos + var1);
            this.pos += var1;
            return var2;
        }
    }

    public String nextTo(String var1) {
        if(var1 == null) {
            throw new NullPointerException("excluded == null");
        } else {
            return this.nextToInternal(var1).trim();
        }
    }

    public String nextTo(char var1) {
        return this.nextToInternal(String.valueOf(var1)).trim();
    }

    public void skipPast(String var1) {
        int var2 = this.in.indexOf(var1, this.pos);
        this.pos = var2 == -1?this.in.length():var2 + var1.length();
    }

    public char skipTo(char var1) {
        int var2 = this.in.indexOf(var1, this.pos);
        if(var2 != -1) {
            this.pos = var2;
            return var1;
        } else {
            return '\u0000';
        }
    }

    public void back() {
        if(--this.pos == -1) {
            this.pos = 0;
        }

    }

    public static int dehexchar(char var0) {
        return var0 >= 48 && var0 <= 57?var0 - 48:(var0 >= 65 && var0 <= 70?var0 - 65 + 10:(var0 >= 97 && var0 <= 102?var0 - 97 + 10:-1));
    }

}
