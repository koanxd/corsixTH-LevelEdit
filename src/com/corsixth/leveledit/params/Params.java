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

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Holds the state of all relevant level parameters in memory. Allows reading,
 * changing, adding and deleting params and param values.
 * 
 * @author Koanxd aka Snowblind
 *
 */
public interface Params {

	/**
	 * Create an empty param, if it has not been created yet.
	 */
	public void add(String paramName);

	/**
	 * @return true if specified param exists.
	 */
	public boolean contains(String paramName);

	/**
	 * Remove param, if it exists.
	 */
	public void remove(String paramName);
	
	/**
	 * {@link #remove(String)} all params in the collection
	 */
	public void removeAll(Collection<String> paramNames);

	/**
	 * Add the given field to the given param, initialized with the given value.
	 * Param must start with '#'.
	 * 
	 * @throws IllegalArgumentException
	 *             if the param does not exist or param does not start with '#'
	 */
	public void addField(String paramName, String fieldName, String value);

	/**
	 * Remove the given field from this param
	 * 
	 * @throws IllegalArgumentException
	 *             if the param does not exist.
	 */
	public void removeField(String paramName, String fieldName);

	/**
	 * Add a default field to a param. Param must start with '#'.
	 * 
	 * @param value
	 *            the initial value for the field
	 * @throws IllegalArgumentException
	 *             if the param does not exist, or is not default, or param does
	 *             not start with '#'
	 */
	public void addDefaultField(String paramName, String fieldName, String value);

	/**
	 * Sets the value of the specified param and field to the given value.
	 * Passing null as fieldName is equivalent to calling
	 * {@link #setValue(String, String)}
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified param or field does not exist
	 */
	public void setValue(String paramName, String fieldName, String value);

	/**
	 * Sets the value of the specified param to the given value.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified param does not exist
	 */
	public void setValue(String paramName, String value);

	/**
	 * @return the value of the given field. Passing null as fieldname is
	 *         equivalent to calling {@link #getValue(String)}
	 * @throws IllegalArgumentException
	 *             if the param does not exist or does not have a field of the
	 *             given name
	 */
	public String getValue(String paramName, String fieldName);

	/**
	 * @return the value of this param, if the given param requires no fields.
	 * @throws IllegalArgumentException
	 *             if the param does not exist
	 */
	public String getValue(String paramName);

	/**
	 * @return true if the given param has a field 'fieldName'
	 * @throws IllegalArgumentException
	 *             if the param does not exist
	 */
	public boolean hasField(String paramName, String fieldName);

	/**
	 * Remove all non-default params, and remove all non-default fields from
	 * each param.
	 * 
	 * @see #isDefault(String)
	 * @see #isDefault(String, String)
	 */
	public void clearNonDefault();

	/**
	 * @return true if the param exists and was marked as default by a call to
	 *         {@link #setAsDefault(String)}
	 */
	public boolean isDefault(String paramName);

	/**
	 * @return true if the param exists and contains a default field with the
	 *         given fieldname.
	 */
	public boolean isDefault(String paramName, String fieldName);

	/**
	 * Mark the param as default.
	 * <p>
	 * This method should be called if the param is known by the application as
	 * soon as startup is complete.
	 * 
	 * @throws IllegalArgumentException
	 *             if the param does not exist
	 */
	public void setAsDefault(String paramName);

	/**
	 * Remove all params belonging to a table.
	 * 
	 * @see #getTable(String)
	 */
	public void clearTables();

	/**
	 * @return an unmodifiable map of all table basenames (keys) and associated
	 *         tables (values). (The map contains, for example, the table of
	 *         earthquakes as one of its values)
	 */
	public Map<String, List<String>> getAllTableLists();

	/**
	 * @return an unmodifiable list of all table params for the given basename,
	 *         or null if there is no table with the given basename.
	 * @see #getBaseName(String)
	 */
	public List<String> getTable(String baseName);

	/**
	 * @return the index of the given param, if it belongs to a table; <br>
	 *         -1 else
	 */
	public int getIndex(String paramName);

	/**
	 * @return an unmodifiable collection of the fieldnames in this param.
	 * 
	 * @throws IllegalArgumentException
	 *             if the param does not exist
	 */
	public Collection<String> getFieldNames(String paramName);

	/**
	 * @return the name of the param without the table index and at the end.
	 */
	public static String getBaseName(String paramName) {
		return paramName.split("\\[")[0];
	}

}
