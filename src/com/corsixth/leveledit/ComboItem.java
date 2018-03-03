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

import java.util.Comparator;

/**
 * Combines an object ID and a string representation for an item in a comboBox.
 * This is used to dynamically change the language of displayed items.
 * 
 * @author Koanxd aka Snowblind
 */
public class ComboItem {

	private String displayNameKey;
	private int id = -1;
	private String fieldName = null;

	/**
	 * Sorts ComboItems alphabetically by the result of their toString()
	 * implementation
	 */
	public static class ItemComparator implements Comparator<ComboItem> {
		@Override
		public int compare(ComboItem o1, ComboItem o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}

	/**
	 * Use this constructor if the item should represent a value for a single
	 * field.
	 * 
	 * @param displayNameKey
	 *            used by toString() to retrieve the display-text
	 * @param id
	 *            refers to a field value
	 */
	public ComboItem(final String displayNameKey, final int id) {
		this.displayNameKey = displayNameKey;
		this.id = id;
	}

	/**
	 * Use this constructor if the item should represent one of several fields,
	 * of which only one can be chosen.
	 * 
	 * @param displayNameKey
	 *            used by toString() to retrieve the display-text
	 * @param fieldName
	 *            the field represented by this ComboBoxItem
	 */
	public ComboItem(final String displayNameKey, final String fieldName) {
		this.displayNameKey = displayNameKey;
		this.fieldName = fieldName;
	}

	@Override
	public String toString() {
		return Strings.get(displayNameKey);
	}

	/**
	 * @return the ID which refers to the field value, or -1 if this
	 *         ComboBoxItem represents one of many choosable fields.
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return the field represented by this ComboBoxItem, or null if this
	 *         ComboBoxItem does not represent one of many choosable fields.
	 */
	public String getFieldName() {
		return fieldName;
	}
}
