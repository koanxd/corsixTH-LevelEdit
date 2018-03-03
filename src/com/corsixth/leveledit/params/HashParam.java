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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Representation of a level parameter starting with '#' and containing one or
 * more fields, separated by '.'
 * 
 * @author Koanxd aka Snowblind
 */
class HashParam extends Param {

	// maps field names to values
	private HashMap<String, String> fields = new HashMap<>();

	// known fields for this param after startup
	private ArrayList<String> defaultFields = new ArrayList<>();

	/**
	 * @param name
	 *            the name of the param as it will appear in the level file,
	 *            including the first character such as '#', and an optional
	 *            index denoted as '[d]'
	 * @param fields
	 *            the fields to be added, without a starting dot. Each field
	 *            should only consist of letters, and no special characters.
	 */
	HashParam(String name, String[] fields) {
		this.name = name;
		for (int i = 0; i < fields.length; i++) {
			this.fields.put(fields[i], null);
		}
	}

	/**
	 * @return the value of the given field
	 * @throws IllegalArgumentException
	 *             if the param does not have a field of the given name
	 */
	String getValue(String fieldName) {
		if (!fields.containsKey(fieldName)) {
			throw new IllegalArgumentException();
		}
		return fields.get(fieldName);
	}

	/**
	 * Sets the value for the given field
	 * 
	 * @throws IllegalArgumentException
	 *             if the param does not contain the given field.
	 */
	void setValue(String fieldName, String value) {
		if (!fields.containsKey(fieldName)) {
			throw new IllegalArgumentException("Param " + name + " does not have a field " + fieldName);
		}
		fields.put(fieldName, value);
	}

	/**
	 * @return true if the param contains the given field.
	 */
	@Override
	boolean hasField(String fieldName) {
		return fields.keySet().contains(fieldName);
	}

	/**
	 * Adds a field to this param, initialized with the given value.
	 */
	void addField(String fieldName, String value) {
		fields.put(fieldName, value);
	}

	/**
	 * Remove the given field from this param
	 */
	void removeField(String fieldName) {
		fields.remove(fieldName);
	}

	/**
	 * Adds a field to this param, initialized with the given value and marked
	 * as a default field.
	 */
	void addDefaultField(String fieldName, String value) {
		addField(fieldName, value);
		defaultFields.add(fieldName);
	}

	/**
	 * @return the fieldnames in this param
	 */
	Collection<String> getFieldNames() {
		return new ArrayList<String>(fields.keySet());
	}

	/**
	 * @return true if the given field was marked as default by a call to
	 *         {@link #addDefaultField(String, String)}
	 */
	boolean fieldIsDefault(String fieldName) {
		return defaultFields.contains(fieldName);
	}

	/**
	 * @return the index of this param, if it is a table entry; -1 else.
	 */
	int getIndex() {
		String[] split = name.split("[\\[\\]]");
		if (split.length < 2)
			return -1;
		try {
			return Integer.parseInt(split[1]);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

}
