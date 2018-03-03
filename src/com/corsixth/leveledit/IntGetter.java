/*
Copyright (c) 2018 Koanxd aka Snowblind

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.corsixth.leveledit;

import com.corsixth.leveledit.params.Params;

/**
 * Wrapper for an int value which can be a constant or linked to a value from a
 * particular field.
 * <p>
 * This is used, for example, to force the end month of an earthquake to be >=
 * to the start month.
 * 
 * @author Koanxd aka Snowblind
 */
public class IntGetter {
	protected static Params params;
	
	private int value;
	private String paramName;
	private String fieldName;
	private int constant = 0;

	private IntGetter() {
		if (params == null) {
			throw new IllegalStateException(
					"Class IntGetter has missing dependencies");
		}
	}

	/**
	 * Create a wrapper for the specified value, which is constant.
	 * 
	 * @throws IllegalStateException if an instance of {@link Params} has not been assigned to this class
	 */
	public IntGetter(int value) {
		this();
		this.value = value;
	}

	/**
	 * Create a wrapper which always points to the current value of the param
	 * and field.
	 * 
	 * @throws IllegalStateException if an instance of {@link Params} has not been assigned to this class
	 */
	public IntGetter(String paramName, String fieldName) {
		this(paramName, fieldName, 0);
	}

	/**
	 * Create a wrapper which always points to the current value of the param
	 * and field, but adds the specified constant to the result of
	 * {@link #get()}.
	 * 
	 * @throws IllegalStateException if an instance of {@link Params} has not been assigned to this class
	 */
	public IntGetter(String paramName, String fieldName, int plusConstant) {
		this();
		this.paramName = paramName;
		this.fieldName = fieldName;
		this.constant = plusConstant;
	}

	/**
	 * @return the value as specified by the constructor.
	 */
	public int get() {
		if (paramName != null)
			return (Integer.parseInt(params.getValue(paramName, fieldName))) + constant;
		return value;
	}

	/**
	 * Assigns a params instance that this class should operate on. Can be
	 * changed for unit testing.
	 */
	public static void injectParamsInstance(Params params) {
		IntGetter.params = params;
	}
}