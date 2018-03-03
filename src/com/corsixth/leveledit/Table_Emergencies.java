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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.corsixth.leveledit.params.Params;

public class Table_Emergencies extends Table {

	public enum Mode {
		Random, SemiRandom, Controlled, Unknown
	}

	private boolean enabled = true;

	// maps paramnames to a map of fieldnames and field values
	private HashMap<String, HashMap<String, String>> paramsBackup = new HashMap<String, HashMap<String, String>>();

	private List<TextFieldController> endMonths = new ArrayList<>();

	public Table_Emergencies(GridPanel panel) {
		super(panel, "#emergency_control", 0, 14,
				new String[] { "Illness", "StartMonth", "EndMonth", "Min", "Max", "Bonus", "PercWin" });
	}

	@Override
	protected ArrayList<JComponent> rowEntries(String param) {
		if (!enabled)
			return null;

		ArrayList<JComponent> entries = new ArrayList<>();

		int index = getCurrentIndex();
		String previousParam = null;
		int illness = 2;
		if (params.getValue(param, "Illness") != null) {
			illness = Integer.parseInt(params.getValue(param, "Illness"));
		}

		// configure GUI-components
		JTextField tfStart = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfEnd = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfMin = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfMax = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfPercentWin = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfBonus = new JTextField(GuiImpl.TFSIZE_M);
		JComboBox<ComboItem> comboBox = new JComboBox<>();
		new StringComponent().comboBox(comboBox);
		ArrayList<ComboItem> items = new ArrayList<>();
		for (int i = 0; i < 34; i++) {
			items.add(new ComboItem("lb#expertise[" + (i + 2) + "]", i + 2));
		}
		items.sort(new ComboItem.ItemComparator());
		ComboItem selectedIllness = items.get(illness - 2);
		IntGetter minStartMonth = new IntGetter(0);
		IntGetter maxStartMonth = new IntGetter(param, "EndMonth");
		IntGetter minEndMonth = new IntGetter(param, "StartMonth");
		IntGetter maxValueForMinPatients = new IntGetter(param, "Max");
		IntGetter minValueForMaxPatients = new IntGetter(param, "Min");
		if (index > startIndex) {
			previousParam = params.getTable(Params.getBaseName(param)).get(index - 1);
			minStartMonth = new IntGetter(previousParam, "EndMonth");
			endMonths.get(index - 1).setLink(tfStart);
		}

		// initialize fields
		if (params.getValue(param, "StartMonth") == null) {
			illness = new Random().nextInt(34) + 2;
			params.setValue(param, "Illness", Integer.toString(illness));
			params.setValue(param, "StartMonth", Integer.toString(minStartMonth.get() + 3));
			params.setValue(param, "EndMonth", Integer.toString(minEndMonth.get() + 3));
			params.setValue(param, "Min", "3");
			params.setValue(param, "Max", "8");
			params.setValue(param, "Bonus", Integer.toString(700 + new Random().nextInt(10) * 100));
			params.setValue(param, "PercWin", "75");
			selectedIllness = items.get(illness - 2);
		}

		String lb = "lb#emergency_control.";
		String tt = "tt#emergency_control.";

		entries.add(comboBox);
		new ComboBoxController_FieldValues(comboBox, param, "Illness", items, selectedIllness);
		//reload()

		entries.add(new StringComponent().newLabel(lb + "StartMonth", tt + "StartMonth"));
		entries.add(tfStart);
		new TextFieldController_Integer(tfStart, param, "StartMonth", minStartMonth, maxStartMonth);

		entries.add(new StringComponent().newLabel(lb + "EndMonth", tt + "EndMonth"));
		entries.add(tfEnd);
		TextFieldController endMonth = new TextFieldController_Integer(tfEnd, param, "EndMonth", minEndMonth,
				new IntGetter(10000));
		endMonths.add(endMonth);

		entries.add(new StringComponent().newLabel(lb + "Min", tt + "Min"));
		entries.add(tfMin);
		new TextFieldController_Integer(tfMin, param, "Min", new IntGetter(1), maxValueForMinPatients);

		entries.add(new StringComponent().newLabel(lb + "Max", tt + "Max"));
		entries.add(tfMax);
		new TextFieldController_Integer(tfMax, param, "Max", minValueForMaxPatients, new IntGetter(10000));

		entries.add(new StringComponent().newLabel(lb + "Bonus", tt + "Bonus"));
		entries.add(tfBonus);
		new TextFieldController_Integer(tfBonus, param, "Bonus", 0, 1000000);

		entries.add(new StringComponent().newLabel(lb + "PercWin", tt + "PercWin"));
		entries.add(tfPercentWin);
		new TextFieldController_Integer(tfPercentWin, param, "PercWin", 0, 100);

		return entries;
	}

	/**
	 * Sets enabled status. Changing from enabled to disabled removes all
	 * params of this table from the internal list of params. Changing from
	 * disabled to enabled adds them back again.
	 */
	public void setEnabled(boolean enable) {
		if (enable == enabled) {
			return;
		}

		enabled = enable;

		if (enable) {
			for (String param : paramsBackup.keySet()) {
				params.add(param);
				for (String field : paramsBackup.get(param).keySet()) {
					params.addField(param, field, paramsBackup.get(param).get(field));
				}
			}
			super.reload();
			paramsBackup.clear();
		} else {
			while (!paramList.isEmpty()) {
				HashMap<String, String> backup = new HashMap<>();
				for (String field : params.getFieldNames(paramList.get(0))) {
					backup.put(field, params.getValue(paramList.get(0), field));
				}
				paramsBackup.put(paramList.get(0), backup);
				params.remove(paramList.get(0));
			}
		}
	}

	/**
	 * Inspect the table and return the emergency control mode.
	 */
	public Mode getMode() {
		String first = "#emergency_control[0]";
		if (paramList.isEmpty() && paramsBackup.isEmpty())
			return Mode.Controlled;

		if (params.hasField(first, "Random"))
			return Mode.Random;
		else if (params.hasField(first, "Interval") && params.hasField(first, "Variance"))
			return Mode.SemiRandom;
		else if (params.hasField(first, "StartMonth") && params.hasField(first, "EndMonth"))
			return Mode.Controlled;
		else
			return Mode.Unknown;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	protected void removeLastRow() {
		super.removeLastRow();
		endMonths.remove(endMonths.size() - 1);
	}

	@Override
	public void reload() {
		paramsBackup.clear();
		enabled = (getMode() == Mode.Controlled) ? true : false;
		super.reload();
	}

}
