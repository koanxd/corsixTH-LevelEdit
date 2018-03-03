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

public class Table_Earthquakes extends Table {

	protected List<TextFieldController> monthControllers = new ArrayList<>();

	public Table_Earthquakes(GridPanel panel, String baseName, int startIndex, int componentsPerRow, String[] fields) {
		super(panel, baseName, startIndex, componentsPerRow, fields);
	}

	@Override
	protected ArrayList<JComponent> rowEntries(String param) {
		ArrayList<JComponent> entries = new ArrayList<>();

		int index = getCurrentIndex();
		String previousParam = null;

		// configure GUI-components
		JTextField tfStart = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfEnd = new JTextField(GuiImpl.TFSIZE_S);
		JTextField tfSeverity = new JTextField(GuiImpl.TFSIZE_S);
		IntGetter minStartMonth = new IntGetter(0);
		IntGetter maxStartMonth = new IntGetter(param, "EndMonth");
		IntGetter minEndMonth = new IntGetter(param, "StartMonth");
		if (index > startIndex) {
			previousParam = params.getTable(Params.getBaseName(param)).get(index - 1);
			minStartMonth = new IntGetter(previousParam, "EndMonth");
			monthControllers.get(index - 1).setLink(tfStart);
		}

		// initialize fields
		if (params.getValue(param, "StartMonth") == null) {
			params.setValue(param, "StartMonth", Integer.toString(minStartMonth.get() + 6));
			params.setValue(param, "EndMonth", Integer.toString(minEndMonth.get() + 6));
			params.setValue(param, "Severity", Integer.toString(new Random().nextInt(10) + 1));
		}

		String name = "#quake_control[" + index + "]";
		String lb = "lb#quake_control.";
		String tt = "tt#quake_control.";

		entries.add(new StringComponent().newLabel(lb + "StartMonth", tt + "StartMonth"));
		entries.add(tfStart);
		TextFieldController startController = new TextFieldController_Integer(tfStart, name, "StartMonth",
				minStartMonth, maxStartMonth);
		monthControllers.add(startController);

		entries.add(new StringComponent().newLabel(lb + "EndMonth", tt + "EndMonth"));
		entries.add(tfEnd);
		new TextFieldController_Integer(tfEnd, name, "EndMonth", minEndMonth, new IntGetter(10000));

		entries.add(new StringComponent().newLabel(lb + "Severity", tt + "Severity"));
		entries.add(tfSeverity);
		new TextFieldController_Integer(tfSeverity, name, "Severity", 1, 10);

		return entries;
	}

	@Override
	protected void removeLastRow() {
		super.removeLastRow();
		monthControllers.remove(monthControllers.size() - 1);
	}

}
