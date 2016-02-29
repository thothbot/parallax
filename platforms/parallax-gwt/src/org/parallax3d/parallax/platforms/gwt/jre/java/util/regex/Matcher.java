/*
 * Copyright 2012 Alex Usachev, thothbot@gmail.com
 * This file is based on libgdx code
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

package java.util.regex;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.MatchResult;

/** Emulation of the {@link Matcher} class, uses {@link RegExp} as internal implementation.
 * @author hneuer */
public class Matcher {
	private final RegExp regExp;
	private final String input;

	MatchResult result;

	Matcher (Pattern pattern, CharSequence input) {
		this.regExp = pattern.regExp;
		this.input = String.valueOf(input);
	}

	public boolean matches () {
		return regExp.test(input);
	}

	public boolean find() {
		result = regExp.exec(input);
		return result != null;
	}

	public String group(int i) {
		if(result != null && i <= result.getGroupCount())
			return result.getGroup(i);

		return null;
	}
}
