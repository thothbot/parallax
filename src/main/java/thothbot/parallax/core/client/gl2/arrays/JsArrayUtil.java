/*
 * Copyright 2009-2011 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package thothbot.parallax.core.client.gl2.arrays;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayBoolean;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;

/**
 * Helper class for converting from Java arrays to JavaScript arrays (
 * {@link JsArrayNumber} and {@link JsArrayInteger}).
 * 
 * @author hao1300@gmail.com
 */
public class JsArrayUtil
{

	/**
	 * Converts from a Java array to a JavaScript array.
	 * 
	 * @param array
	 *            the Java array to convert from.
	 * @return the equivalent JavaScript array.
	 */
	public static JsArrayNumber toJsArrayNumber(double... array)
	{
		JsArrayNumber jsArray = JsArrayNumber.createArray().cast();
		for (double v : array) {
			jsArray.push(v);
		}
		return jsArray;
	}

	/**
	 * Converts from a Java array to a JavaScript array.
	 * 
	 * @param array
	 *            the Java array to convert from.
	 * @return the equivalent JavaScript array.
	 */
	public static JsArrayNumber toJsArrayNumberFromDouble(double... array)
	{
		JsArrayNumber jsArray = JsArrayNumber.createArray().cast();
		for (double v : array) {
			jsArray.push(v);
		}
		return jsArray;
	}

	/**
	 * Converts from a Java array to a JavaScript array.
	 * 
	 * @param array
	 *            the Java array to convert from.
	 * @return the equivalent JavaScript array.
	 */
	public static JsArrayInteger toJsArrayInteger(int... array)
	{
		JsArrayInteger jsArray = JsArrayInteger.createArray().cast();
		for (int v : array) {
			jsArray.push(v);
		}
		return jsArray;
	}

	/**
	 * Converts a JsArrayString to a String[].
	 * 
	 * @param jsArrayString
	 *            the array to unwrap.
	 * @return the created String[].
	 */
	public static String[] unwrapArray(JsArrayString jsArrayString)
	{
		if (GWT.isScript()) {
			return jsArrayAsArrayForProdMode(jsArrayString);
		}
		String[] result = new String[jsArrayString.length()];
		for (int i = 0; i < jsArrayString.length(); i++) {
			result[i] = jsArrayString.get(i);
		}
		return result;
	}

	/**
	 * Wraps a Java boolean Array to a JsArrayBoolean.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayBoolean wrapArray(boolean[] srcArray)
	{
		if (GWT.isScript()) {
			return arrayAsJsArrayForProdMode(srcArray);
		}
		JsArrayBoolean result = JavaScriptObject.createArray().cast();
		for (int i = 0; i < srcArray.length; i++) {
			result.set(i, srcArray[i]);
		}
		return result;
	}

	/**
	 * Wraps a Java byte Array to a JsArrayInteger.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayInteger wrapArray(byte[] srcArray)
	{
		if (GWT.isScript()) {
			return arrayAsJsArrayForProdMode(srcArray);
		}
		JsArrayInteger result = JavaScriptObject.createArray().cast();
		for (int i = 0; i < srcArray.length; i++) {
			result.set(i, srcArray[i]);
		}
		return result;
	}

	/**
	 * Wraps a Java byte Array to a JsArrayInteger.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayInteger wrapArray(short[] srcArray)
	{
		if (GWT.isScript()) {
			return arrayAsJsArrayForProdMode(srcArray);
		}
		JsArrayInteger result = JavaScriptObject.createArray().cast();
		for (int i = 0; i < srcArray.length; i++) {
			result.set(i, srcArray[i]);
		}
		return result;
	}

	/**
	 * Wraps a Java double Array to a JsArrayNumber.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayNumber wrapArray(double[] srcArray)
	{
		if (GWT.isScript()) {
			return arrayAsJsArrayForProdMode(srcArray);
		}
		JsArrayNumber result = JavaScriptObject.createArray().cast();
		for (int i = 0; i < srcArray.length; i++) {
			result.set(i, srcArray[i]);
		}
		return result;
	}

	/**
	 * Wraps a Java int Array to a JsArrayInteger.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayInteger wrapArray(int[] srcArray)
	{
		if (GWT.isScript()) {
			return arrayAsJsArrayForProdMode(srcArray);
		}
		JsArrayInteger result = JavaScriptObject.createArray().cast();
		for (int i = 0; i < srcArray.length; i++) {
			result.set(i, srcArray[i]);
		}
		return result;
	}

	/**
	 * Wraps a Java long Array to a JsArrayInteger. Pay attention: Avoid using
	 * long values in GWT if possible (
	 * {@link "http://code.google.com/intl/de-DE/webtoolkit/doc/latest/DevGuideCodingBasicsCompatibility.html#language"}
	 * ). This method has poor performance in production mode compared with the
	 * int[] version ( {@link JsArrayUtil#wrapArray(int[])}). Please note that
	 * in production mode int, short and byte are handled as 64Bit floating
	 * point values, so you can use them for values >2^31-1. Keep in mind that
	 * not every long value can be represented exactly by 64Bit floating values.
	 * Be aware that this won't work correctly in dev mode and no literals above
	 * that limit are supported in Java.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayInteger wrapArray(long[] srcArray)
	{
		String arrayString = Arrays.toString(srcArray);
		return createArrayFromString(arrayString);
	}

	/**
	 * Wraps a Java String Array to a JsArrayString.
	 * 
	 * @param srcArray
	 *            the array to wrap
	 * @return the wrapped array
	 */
	public static JsArrayString wrapArray(String[] srcArray)
	{
		if (GWT.isScript()) {
			return arrayAsJsArrayForProdMode(srcArray);
		}
		JsArrayString result = JavaScriptObject.createArray().cast();
		for (int i = 0; i < srcArray.length; i++) {
			result.set(i, srcArray[i]);
		}
		return result;
	}

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the array as
	 * JsArray. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native JsArrayBoolean arrayAsJsArrayForProdMode(boolean[] array) /*-{
		return array;
	}-*/;

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the array as
	 * JsArray. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native JsArrayInteger arrayAsJsArrayForProdMode(byte[] array) /*-{
		return array;
	}-*/;

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the array as
	 * JsArray. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native JsArrayInteger arrayAsJsArrayForProdMode(short[] array) /*-{
		return array;
	}-*/;

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the array as
	 * JsArray. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native JsArrayNumber arrayAsJsArrayForProdMode(double[] array) /*-{
		return array;
	}-*/;

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the array as
	 * JsArray. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native JsArrayInteger arrayAsJsArrayForProdMode(int[] array) /*-{
		return array;
	}-*/;

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the array as
	 * JsArray. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native JsArrayString arrayAsJsArrayForProdMode(String[] array) /*-{
		return array;
	}-*/;

	/**
	 * Converts a String containing a textual array representation into a
	 * {@link JsArrayInteger}. The String must conform to the pattern explained
	 * in {@link Arrays#toString(int[])}.
	 * 
	 * @param arrayString
	 *            the array string to convert into a {@link JsArrayInteger}
	 * @return the created array.
	 */
	private static native JsArrayInteger createArrayFromString(String arrayString) /*-{
		return eval(arrayString);
	}-*/;

	/**
	 * Does the trick for production mode. In production mode, a JavaScript
	 * array is used for Java arrays. So we can directly use the given JsArray
	 * as Java array. MUST NOT be called in dev mode.
	 * 
	 * @param array
	 *            the array to get the JsArray for
	 * @return an equivalent JsArray
	 */
	private static native String[] jsArrayAsArrayForProdMode(JsArrayString array) /*-{
		return array;
	}-*/;
}
