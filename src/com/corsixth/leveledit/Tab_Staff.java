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

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Tab_Staff extends Tab {

	public Tab_Staff() {
		super(1);

		createPerformancePanel();
		createSalaryPanel();
		createStaffLevels();
		createStartStaff();
	}

	private void createStartStaff() {
		GridPanel startPanel = new StringComponent().panel(new GridPanel(1), "lbStartStaffPanel");
		new Table_StartStaff(startPanel);
		tabPanel.add(startPanel);
	}

	private void createStaffLevels() {
		GridPanel levelsPanel = new StringComponent().panel(new GridPanel(1), "lbStaffLevelsPanel");
		new Table_StaffLevels(levelsPanel);
		tabPanel.add(levelsPanel);
	}

	private void createPerformancePanel() {
		GridPanel performance = new GridPanel(4);
		new StringComponent().panel(performance, "lbStaffPerformancePanel");
		tabPanel.add(performance);

		addIntField("#gbv", "TrainingRate", 1, 255, performance);
		performance.next(2);

		addIntField("#gbv", "RecoveryFactor", 1, 3000, performance);

		addIntField("#gbv", "CrackUpTired", 1, 1000, performance);
	}

	private void createSalaryPanel() {
		GridPanel salary = new GridPanel(1);
		salary.setInsets(0);
		new StringComponent().panel(salary, "lbSalaryPanel");
		tabPanel.add(salary);

		// min salary
		GridPanel minSalary = new GridPanel(8);
		salary.add(new StringComponent().newLabel("lbMinSalary", "ttMinSalary"));
		for (int i = 0; i < 4; i++) {
			addIntField("#staff[" + i + "]", "MinSalary", 1, 10000, minSalary);
		}
		salary.add(minSalary);

		// added salary
		GridPanel addedSalary = new GridPanel(12);
		salary.add(new StringComponent().newLabel("lbAddedSalary", "ttAddedSalary"));
		for (int i = 3; i < 9; i++) {
			addIntField("#gbv", "SalaryAdd[" + i + "]", -100, 10000, addedSalary);
		}
		salary.add(addedSalary);

		// params
		GridPanel panelParams = new GridPanel(6);
		salary.add(new StringComponent().newLabel("lbSalaryParams", "ttSalaryParams"));

		addIntField("#gbv", "SalaryAbilityDivisor", 1, 100, panelParams);
		salary.add(panelParams);
	}

	/**
	 * shortcut method for some of the fields. params cannot be null.
	 */
	private void addIntField(String paramName, String fieldName, int minInput, int maxInput, GridPanel panel) {
		String labelKey = "lb" + paramName + "." + fieldName;
		String tooltipKey = "tt" + paramName + "." + fieldName;
		if (labelKey.contains("[")){
			tooltipKey = null;
		}
		panel.add(new StringComponent().newLabel(labelKey, tooltipKey));
		JTextField tf = new JTextField(GuiImpl.TFSIZE_S);
		panel.add(tf);
		new TextFieldController_Integer(tf, paramName, fieldName, minInput, maxInput);
	}

	@Override
	public void reload() {

	}
}