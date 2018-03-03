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
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.corsixth.leveledit.params.Params;

public class Table_StaffLevels extends Table {

	protected List<TextFieldController> monthControllers = new ArrayList<>();

	public Table_StaffLevels(GridPanel panel) {
		super(panel, "#staff_levels", 0, 10, new String[] { "Month", "Doctors", "Nurses", "Handymen", "Receptionists",
				"ShrkRate", "SurgRate", "RschRate", "ConsRate", "JrRate" });
	}

	@Override
	public ArrayList<JComponent> rowEntries(String param) {
		ArrayList<JComponent> entries = new ArrayList<JComponent>();

		int index = getCurrentIndex();
		String previousParam = null;

		IntGetter minMonth = new IntGetter(0);
		if (index > startIndex) {
			previousParam = params.getTable(Params.getBaseName(param)).get(index - 1);
			minMonth = new IntGetter(previousParam, "Month", 1);
		}

		if (params.getValue(param, "Month") == null) {
			// set initial values for newly created param.
			int monthInit = (index == startIndex) ? 0 : minMonth.get() + 5;
			params.setValue(param, "Month", Integer.toString(monthInit));
			params.setValue(param, "Nurses", Integer.toString(3 + new Random().nextInt(4)));
			params.setValue(param, "Doctors", Integer.toString(6 + new Random().nextInt(5)));
			params.setValue(param, "Handymen", Integer.toString(3 + new Random().nextInt(4)));
			params.setValue(param, "Receptionists", Integer.toString(2 + new Random().nextInt(4)));
			params.setValue(param, "ShrkRate", "10");
			params.setValue(param, "SurgRate", "10");
			params.setValue(param, "RschRate", "10");
			params.setValue(param, "ConsRate", "10");
			params.setValue(param, "JrRate", "10");
		}

		entries.add(new StringComponent().newLabel("lb#staff_levels.Month", null));
		JTextField tfMonth = new JTextField(GuiImpl.TFSIZE_S);
		entries.add(tfMonth);
		TextFieldController monthController = new TextFieldController_Integer(tfMonth, param, "Month", minMonth,
				new IntGetter(9999));
		monthControllers.add(monthController);

		if (index > startIndex) {
			monthControllers.get(entries.size() - 1).setLink(tfMonth);
		}
		for (int i = 1; i < fieldsInOrder.length; i++) {
			if (!params.hasField(param, fieldsInOrder[i])){
				params.addField(param, fieldsInOrder[i], "10");
			}
			entries.add(new StringComponent().newLabel("lb#staff_levels." + fieldsInOrder[i], null));
			JTextField entry = new JTextField(GuiImpl.TFSIZE_S);
			entries.add(entry);
			
			if (fieldsInOrder[i].endsWith("Rate")) {
				new TextFieldController_FractionOfOne(entry, param, fieldsInOrder[i]);
			} else {
				new TextFieldController_Integer(entry, param, fieldsInOrder[i], 0, 99);
			}
		}

		return entries;
	}

	@Override
	protected void removeLastRow() {
		super.removeLastRow();
		monthControllers.remove(monthControllers.size() - 1);
	}
}
