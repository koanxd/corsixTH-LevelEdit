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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * @author Koanxd aka Snowblind
 */
public class Table_WinCriteria extends Table {

	public Table_WinCriteria(GridPanel panel, String baseName, int startIndex, int componentsPerRow, String[] fields) {
		super(panel, baseName, startIndex, componentsPerRow, fields);
	}

	@Override
	protected List<JComponent> rowEntries(String param) {
		// user cannot add more than 5 win conditions, but the program can
		if (rows.size() >= 5 && params.getValue(param, "Criteria") == null) {
			return null;
		}

		List<JComponent> entries = new ArrayList<>();

		if (params.contains(param) && params.getValue(param, "Criteria") != null) {
			switch (params.getValue(param, "Criteria")) {
			case "1":
			case "2":
			case "3":
			case "4":
			case "6":
				break;
			default: // this means the criteria is unknown. do not create UI
						// components for it.
				return null;
			}
		}

		JComboBox<ComboItem> criteriaBox = new JComboBox<>();
		List<ComboItem> items = new ArrayList<>();
		items.add(new ComboItem("cbWinReputation", 1));
		items.add(new ComboItem("cbWinBalance", 2));
		items.add(new ComboItem("cbWinTreated", 3));
		items.add(new ComboItem("cbWinCures", 4));
		items.add(new ComboItem("cbWinValue", 6));

		ComboItem selectedItem = (rows.size() < 5) ? items.get(rows.size()) : null;
		if (params.getValue(param, "Criteria") == null) {
			// initialize new param
			String criteria = "1";
			String value = "500";
			switch (rows.size()) {
			case 1:
				criteria = "2";
				value = "10000";
				break;
			case 2:
				criteria = "3";
				value = "50";
				break;
			case 3:
				criteria = "4";
				value = "100";
				break;
			case 4:
				criteria = "6";
				value = "85000";
				break;
			}
			params.setValue(param, "Criteria", criteria);
			params.setValue(param, "Value", value);
			params.setValue(param, "MaxMin", "1");
			params.setValue(param, "Group", "1");
			params.setValue(param, "Bound", "0");
		} else {
			for (ComboItem item : items) {
				if (params.getValue(param, "Criteria").equals(Integer.toString(item.getID()))) {
					selectedItem = item;
				}
			}
		}

		entries.add(criteriaBox);
		new ComboBoxController_FieldValues(criteriaBox, param, "Criteria", items, selectedItem);

		JTextField value = new JTextField(GuiImpl.TFSIZE_L);
		entries.add(value);

		ItemListener criteriaListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (criteriaBox.getItemCount() == 0) {
					return;
				}
				int max = 1000000000; // default for bank balance & hospital
										// value
				switch (((ComboItem) criteriaBox.getSelectedItem()).getID()) {
				case 1: // reputation
					max = 1000;
					break;
				case 3: // percentage treated
					max = 100;
					break;
				case 4: // cure count
					max = 1000000;
					break;
				}
				new TextFieldController_Integer(value, param, "Value", 1, max).processInput();
			}
		};
		criteriaBox.addItemListener(criteriaListener);
		criteriaListener.itemStateChanged(null);

		return entries;
	}
}