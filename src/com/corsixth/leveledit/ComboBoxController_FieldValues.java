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
import java.util.List;

import javax.swing.JComboBox;

/**
 * Use this controller if all items have the same field name in the level file,
 * but different values (such as the disease ID for an emergency);
 * 
 * @author Koanxd aka Snowblind
 */
public class ComboBoxController_FieldValues extends ComboBoxController {
	private String param;
	private String fieldName;

	public ComboBoxController_FieldValues(JComboBox<ComboItem> box, String param, String fieldName,
			List<ComboItem> items, ComboItem selectedItem) {

		super(box);
		this.param = param;
		this.fieldName = fieldName;
		for (int i = 0; i < items.size(); i++) {
			this.items.put(items.get(i).getID(), items.get(i));
			box.addItem(items.get(i));
		}
		box.setSelectedItem(selectedItem);
        reload();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			Object item = e.getItem();
			if (item instanceof ComboItem) {
				ComboItem boxItem = (ComboItem) item;

				params.setValue(param, fieldName, Integer.toString(boxItem.getID()));
			}
		}
	}

	@Override
	public void reload() {
		if (!params.contains(param)){
			gui.removeReloadable(this);
			return;
		}
		// the internal field value is the ID for item to select.
		ComboItem toSelect = getItem(params.getValue(param, fieldName).toString());
		if (toSelect != null) {
			getBox().setSelectedItem(toSelect);
		}		
	}
}
