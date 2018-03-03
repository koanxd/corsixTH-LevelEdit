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
public class Table_LoseCriteria extends Table {

	public Table_LoseCriteria(GridPanel panel, String baseName, int startIndex, int componentsPerRow, String[] fields) {
		super(panel, baseName, startIndex, componentsPerRow, fields);
	}

	@Override
	protected List<JComponent> rowEntries(String param) {
		// user cannot add more than 3 lose conditions, but the program can
		if (rows.size() >= 3 && params.getValue(param, "Criteria") == null) {
			return null;
		}

		List<JComponent> entries = new ArrayList<>();

		if (params.contains(param) && params.getValue(param, "Criteria") != null) {
			switch (params.getValue(param, "Criteria")) {
			case "1":
			case "2":
			case "5":
				break;
			default: // this means the criteria is unknown. do not create UI
						// components for it.
				return null;
			}
		}

		JComboBox<ComboItem> criteriaBox = new JComboBox<>();
		List<ComboItem> items = new ArrayList<>();
		items.add(new ComboItem("cbLoseReputation", 1));
		items.add(new ComboItem("cbLoseBalance", 2));
		items.add(new ComboItem("cbLoseDeaths", 5));

		ComboItem selectedItem = (rows.size() < 3) ? items.get(rows.size()) : null;
		if (params.getValue(param, "Criteria") == null) {
			// default values
			String criteria = "1";
			String value = "300";
			String maxMin = "0";
			String bound = "0";
			switch (rows.size()) {
			case 0:
				bound = "450";
				break;
			case 1:
				criteria = "2";
				value = "-20000";
				break;
			case 2:
				criteria = "5";
				value = "30";
				maxMin = "1";
				bound = "10";
				break;
			}
			params.setValue(param, "Criteria", criteria);
			params.setValue(param, "Value", value);
			params.setValue(param, "Group", Integer.toString(rows.size()));
			params.setValue(param, "Bound", bound);
			params.setValue(param, "MaxMin", maxMin);

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

		entries.add(new StringComponent().newLabel("lbLoseBound", null));
		JTextField bound = new JTextField(GuiImpl.TFSIZE_L);
		entries.add(bound);

		ItemListener criteriaListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (criteriaBox.getItemCount() == 0) {
					return;
				}
				IntGetter min = new IntGetter(-100000);
				IntGetter max = new IntGetter(100000);
				TextFieldController boundController = null;
				switch (((ComboItem) criteriaBox.getSelectedItem()).getID()) {
				case 1: // reputation
					min = new IntGetter(param, "Value");
					max = new IntGetter(1000);
					boundController = new TextFieldController_Integer(bound, param, "Bound", min, max);
					break;
				case 2: // bank balance
					min = new IntGetter(param, "Value");
					boundController = new TextFieldController_Integer(bound, param, "Bound", min, max);
					break;
				case 5: // percent killed
					min = new IntGetter(100);
					max = new IntGetter(param, "Value");
					boundController = new TextFieldController_Integer(bound, param, "Bound", min, max);
					break;
				}
				new TextFieldController_Integer(value, param, "Value", min, max).processInput();
				boundController.processInput();
			}
		};
		criteriaBox.addItemListener(criteriaListener);
		criteriaListener.itemStateChanged(null);

		return entries;
	}
}