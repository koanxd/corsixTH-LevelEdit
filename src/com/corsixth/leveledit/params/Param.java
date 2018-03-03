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

package com.corsixth.leveledit.params;

/**
 * Representation of a level parameter containing one or more values. Examples:
 * "#gbv", "%Name", "#objects[6]"
 * 
 * @author Koanxd aka Snowblind
 */
abstract class Param {
    protected String name;
	private String value;

    protected boolean isDefault = false;

    /**
     * @return the name of the param as it will appear in the level file,
     *         including the first character '%' or '#' and an optional index
     *         '[d]'
     */
    String getName() {
        return name;
    }

	String getValue() {
		return value;
	}

	void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return true if the param contains the given field.
	 */
	abstract boolean hasField(String fieldName);

    /**
     * Mark this param as default, implying that the param is known after startup.
     */
    void setAsDefault() {
        isDefault = true;
    }

    /**
     * @return true if this param was marked as default by a call to
     *         {@link #setAsDefault()}
     */
    boolean isDefault() {
        return isDefault;
    }

}
