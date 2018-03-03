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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages the state of all relevant level parameters
 * 
 * @author Koanxd aka Snowblind
 *
 */
public class ParamsImpl implements Params {

	// list of params by name.
	private HashMap<String, Param> params = new HashMap<>();

	// maps basename of a table param to the corresponding table list.
	private HashMap<String, List<String>> tableLists = null;

	// lists containing all the paramnames in each table.
	private List<String> staffLevels = new ArrayList<>();
	private List<String> startStaff = new ArrayList<>();
	private List<String> emergencies = new ArrayList<>();
	private List<String> earthquakes = new ArrayList<>();
	private List<String> populations = new ArrayList<>();
	private List<String> visuals = new ArrayList<>();
	private List<String> non_visuals = new ArrayList<>();
	private List<String> win_criteria = new ArrayList<>();
	private List<String> lose_criteria = new ArrayList<>();
	private List<String> computers = new ArrayList<>();

	public ParamsImpl() {
		tableLists = new HashMap<String, List<String>>();
		tableLists.put("#staff_levels", staffLevels);
		tableLists.put("#quake_control", earthquakes);
		tableLists.put("#emergency_control", emergencies);
		tableLists.put("#start_staff", startStaff);
		tableLists.put("#popn", populations);
		tableLists.put("#visuals", visuals);
		tableLists.put("#non_visuals", non_visuals);
		tableLists.put("#win_criteria", win_criteria);
		tableLists.put("#lose_criteria", lose_criteria);
		tableLists.put("#computer", computers);
	}

	@Override
	public void add(String paramName) {

		if (!contains(paramName)) {
			if (paramName.startsWith("#")) {
				params.put(paramName, new HashParam(paramName, new String[] {}));
			} else {
				params.put(paramName, new CustomParam(paramName));
			}
			List<String> table = tableLists.get(Params.getBaseName(paramName));
			if (table != null)
				table.add(paramName);
		}
	}

	@Override
	public boolean contains(String paramName) {
		return params.containsKey(paramName);
	}

	@Override
	public void remove(String paramName) {
		params.remove(paramName);
		List<String> table = tableLists.get(Params.getBaseName(paramName));
		if (table != null)
			table.remove(paramName);
	}
	
	@Override
	public void removeAll(Collection<String> paramNames){
		for (String param : paramNames){
			remove(param);
		}
	}

	@Override
	public void addField(String paramName, String fieldName, String value) {
		if (!contains(paramName)) {
			throw new IllegalArgumentException("param " + paramName + " does not exist");
		}
		Param param = params.get(paramName);
		if (!(param instanceof HashParam)) {
			throw new IllegalArgumentException("param " + paramName + " does not allow fields");
		}
		((HashParam) params.get(paramName)).addField(fieldName, value);
	}

	@Override
	public void removeField(String paramName, String fieldName) {
		if (!contains(paramName)) {
			throw new IllegalArgumentException("param " + paramName + " does not exist");
		}
		Param param = params.get(paramName);
		if (param instanceof HashParam) {
			((HashParam) params.get(paramName)).removeField(fieldName);
		}
	}

	@Override
	public void addDefaultField(String paramName, String fieldName, String value) {
		if (!isDefault(paramName)) {
			throw new IllegalArgumentException("Param " + paramName + "is not a default param");
		}
		Param param = params.get(paramName);
		if (!(param instanceof HashParam)) {
			throw new IllegalArgumentException("param " + paramName + " does not allow fields");
		}
		((HashParam) params.get(paramName)).addDefaultField(fieldName, value);
	}

	@Override
	public void setValue(String paramName, String fieldName, String value) {
		if (fieldName == null) {
			setValue(paramName, value);
			return;
		}
		if (!contains(paramName)) {
			throw new IllegalArgumentException("Tried to set value on non-existing param: " + paramName);
		}
		Param param = params.get(paramName);
		if (!param.hasField(fieldName)) {
			throw new IllegalArgumentException(
					"Tried to set value on non-existing field: " + fieldName + " in param " + paramName);
		}
		((HashParam) param).setValue(fieldName, value);
	}

	@Override
	public void setValue(String paramName, String value) {
		if (!contains(paramName)) {
			throw new IllegalArgumentException("Tried to set value on non-existing param: " + paramName);
		}
		params.get(paramName).setValue(value);
	}

	@Override
	public String getValue(String paramName, String fieldName) {
		if (fieldName == null) {
			return getValue(paramName);
		}
		if (!contains(paramName)) {
			throw new IllegalArgumentException("param " + paramName + " does not exist");
		}
		Param param = params.get(paramName);
		if (!param.hasField(fieldName)) {
			throw new IllegalArgumentException("param " + paramName + " does not have a field " + fieldName);
		}
		return ((HashParam) param).getValue(fieldName);
	}

	@Override
	public String getValue(String paramName) {
		if (!contains(paramName)) {
			throw new IllegalArgumentException("param " + paramName + " does not exist");
		}
		return params.get(paramName).getValue();
	}

	@Override
	public boolean hasField(String paramName, String fieldName) {
		if (!contains(paramName)) {
			throw new IllegalArgumentException("param " + paramName + " does not exist");
		}
		return params.get(paramName).hasField(fieldName);
	}

	@Override
	public void clearNonDefault() {
		Set<String> copy = new HashSet<String>(params.keySet());
		for (String paramName : copy) {
			Param param = params.get(paramName);
			if (!contains(paramName)) {
				continue;
			}
			if (!param.isDefault()) {
				params.remove(paramName);
				if (tableLists.containsKey(Params.getBaseName(paramName))) {
					tableLists.get(Params.getBaseName(paramName)).remove(paramName);
				}
				continue;
			}
			if (param instanceof HashParam) {
				HashParam hashParam = (HashParam) param;
				for (String field : hashParam.getFieldNames()) {
					if (!hashParam.fieldIsDefault(field)) {
						hashParam.removeField(field);
					}
				}
			}
		}
	}

	@Override
	public boolean isDefault(String paramName) {
		if (!contains(paramName))
			return false;
		return params.get(paramName).isDefault();
	}

	@Override
	public boolean isDefault(String paramName, String fieldName) {
		if (!isDefault(paramName))
			return false;
		if (!hasField(paramName, fieldName))
			return false;
		return ((HashParam) params.get(paramName)).fieldIsDefault(fieldName);
	}

	@Override
	public void setAsDefault(String paramName) {
		if (!contains(paramName))
			throw new IllegalArgumentException();
		params.get(paramName).setAsDefault();
	}

	@Override
	public void clearTables() {
		if (tableLists != null) {
			for (List<String> table : tableLists.values()) {
				ArrayList<String> toRemove = new ArrayList<>();
				for (String paramName : table) {
					toRemove.add(paramName);
				}
				removeAll(toRemove);
			}
		}
	}

	@Override
	public Map<String, List<String>> getAllTableLists() {
		return Collections.unmodifiableMap(tableLists);
	}

	@Override
	public List<String> getTable(String baseName) {
		List<String> table = getAllTableLists().get(baseName);
		if (table == null) {
			return null;
		}
		return Collections.unmodifiableList(table);
	}

	@Override
	public int getIndex(String paramName) {
		if (!contains(paramName))
			return -1;
		
		Param param = params.get(paramName);
		if (!(param instanceof HashParam))
			return -1;
		
		return ((HashParam) params.get(paramName)).getIndex();
	}

	@Override
	public Collection<String> getFieldNames(String paramName) {
		if (!contains(paramName)) {
			throw new IllegalArgumentException("param " + paramName + " does not exist");
		}
		Param param = params.get(paramName);
		if (param instanceof HashParam){
			return ((HashParam) params.get(paramName)).getFieldNames();
		} else {
			return new ArrayList<>();
		}
	}

}
