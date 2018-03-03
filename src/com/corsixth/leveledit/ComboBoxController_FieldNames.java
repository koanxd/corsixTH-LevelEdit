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

import javax.swing.JComboBox;

/**
 * Use this controller if the comboBox represents a list of fields, of which
 * only one can be chosen (such as the profession of a starting staffmember,
 * which is represented in the level file by the fields Doctor, Nurse, Handyman
 * and Receptionist)
 * 
 * @author Koanxd aka Snowblind
 */
public class ComboBoxController_FieldNames extends ComboBoxController {
	private String param;
	private String[] fields;

	public ComboBoxController_FieldNames(JComboBox<ComboItem> box, String param, String[] fields, ComboItem[] items) {

		super(box);
		this.param = param;
		this.fields = fields;
		for (int i = 0; i < items.length; i++) {
			this.items.put(items[i].getFieldName(), items[i]);
			box.addItem(items[i]);
		}
        reload();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			Object item = e.getItem();
			if (item instanceof ComboItem) {
				ComboItem boxItem = (ComboItem) item;

				for (String field : fields) {
					params.setValue(param, field, "0");
				}
				
				params.setValue(param, boxItem.getFieldName(), "1");
			}
		}
	}

	@Override
	public void reload() {
		if (!params.contains(param)){
			gui.removeReloadable(this);
			return;
		}
		// find the internally marked field and select it on the combobox
		for (ComboItem item : getItems()) {
			String itemFieldName = item.getFieldName();
			if (itemFieldName != null && params.getValue(param, itemFieldName).equals("1")) {
				getBox().setSelectedItem(item);
			}
		}		
	}

}
