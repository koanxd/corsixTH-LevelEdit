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
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Table_StartStaff extends Table {

	private static String[] fields = { "Doctor", "Shrink", "Surgeon", "Researcher", "Nurse", "Handyman", "Receptionist",
			"Skill" };

	private List<List<JComponent>> staffQualifications = new ArrayList<>();

	public Table_StartStaff(GridPanel panel) {
		super(panel, "#start_staff", 1, 9, fields);
	}

	@Override
	protected List<JComponent> rowEntries(String param) {
		List<JComponent> entries = new ArrayList<>();
		initializeFields(param);
		ArrayList<JComponent> staffQualificationsEntry = new ArrayList<>();
		staffQualifications.add(staffQualificationsEntry);

		// staff skill
		// if param was added by the user (not read from file): randomize skill
		if (params.getValue(param, "Skill").equals("0")) {
			params.setValue(param, "Skill", Integer.toString(new Random().nextInt(51) + 50));
		}

		// staff select
		// initialize comboBox and items
		JComboBox<ComboItem> comboBox = new JComboBox<>();
		String[] boxFields = new String[] { "Doctor", "Nurse", "Handyman", "Receptionist" };
		ComboItem[] items = new ComboItem[boxFields.length];
		for (int i = 0; i < boxFields.length; i++) {
			items[i] = new ComboItem("cb#start_staff." + boxFields[i], boxFields[i]);
		}
		entries.add(comboBox);
		new ComboBoxController_FieldNames(comboBox, param, boxFields, items);

		entries.add(new StringComponent().newLabel("lb#start_staff.Skill", null));
		JTextField skill = new JTextField(GuiImpl.TFSIZE_S);
		entries.add(skill);
		new TextFieldController_Integer(skill, param, "Skill", 1, 100);

		// doctor qualifications
		JCheckBox shrink = new JCheckBox();
		entries.add(shrink);
		new CheckBoxController(shrink, param, "Shrink");
		JLabel shrinkLabel = new StringComponent().newLabel("lb#start_staff.Shrink", null);
		entries.add(shrinkLabel);
		staffQualificationsEntry.add(shrink);
		staffQualificationsEntry.add(shrinkLabel);

		JCheckBox surgeon = new JCheckBox();
		entries.add(surgeon);
		new CheckBoxController(surgeon, param, "Surgeon");
		JLabel surgeonLabel = new StringComponent().newLabel("lb#start_staff.Surgeon", null);
		entries.add(surgeonLabel);
		staffQualificationsEntry.add(surgeon);
		staffQualificationsEntry.add(surgeonLabel);

		JCheckBox researcher = new JCheckBox();
		entries.add(researcher);
		new CheckBoxController(researcher, param, "Researcher");
		JLabel resLabel = new StringComponent().newLabel("lb#start_staff.Researcher", null);
		entries.add(resLabel);
		staffQualificationsEntry.add(researcher);
		staffQualificationsEntry.add(resLabel);

		// add special UI controller
		addStaffSelectListener(comboBox, items, staffQualificationsEntry);

		return entries;
	}

	private void initializeFields(String param) {
		for (String field : params.getFieldNames(param)) {
			// if field was just created (not read from file): give it a value
			if (params.getValue(param, field) == null) {
				params.setValue(param, field, "0");
			}
		}

		for (String field : fields) {
			// make sure no fields are missing. This can be the case when
			// reading a file.
			if (!params.hasField(param, field)) {
				params.addField(param, field, "0");
			}
		}
	}

	private void addStaffSelectListener(JComboBox<ComboItem> box, ComboItem[] items, List<JComponent> qualifications) {

		box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// if not doctor, prevent selection of special qualifications
				if (box.getSelectedItem() != items[0]) {
					for (JComponent c : qualifications) {
						if (c instanceof AbstractButton)
							((AbstractButton) c).setSelected(false);
					}
					setQualificationsVisible(false);
				} else {
					setQualificationsVisible(true);
				}
			}

			private void setQualificationsVisible(boolean visible) {
				for (JComponent c : qualifications) {
					c.setVisible(visible);
				}
			}
		});

	}
}
