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
public class Tab_Epidemics extends Tab {

	public Tab_Epidemics() {
		super(2);
		addEntries();
	}

	private void addEntries() {
		addIntField("#gbv", "EpidemicConcurrentLimit", 0, 10);
		addIntField("#gbv", "ReduceContMonths", 0, 1000);
		addIntField("#gbv", "ReduceContPeepCount", 0, 1000);
		addIntField("#gbv", "ReduceContRate", 0, 100);
		addIntField("#gbv", "EpidemicRepLossMinimum", 0, 1000);
		addIntField("#gbv", "EpidemicEvacMinimum", 1, 1000);
		addIntField("#gbv", "EpidemicFine", 0, 100000);

		JTextField minCompensation = new JTextField(GuiImpl.TFSIZE_L);
		
		JTextField maxCompensation = new JTextField(GuiImpl.TFSIZE_L);
		tabPanel.add(new StringComponent().newParamLabel("#gbv", "EpidemicCompLo"));
		tabPanel.add(minCompensation);
		TextFieldController_Integer min = new TextFieldController_Integer(minCompensation, "#gbv", "EpidemicCompLo",
				new IntGetter(0), new IntGetter("#gbv", "EpidemicCompHi"));
		min.setLink(maxCompensation);
		
		tabPanel.add(new StringComponent().newParamLabel("#gbv", "EpidemicCompHi"));
		tabPanel.add(maxCompensation);
		TextFieldController_Integer max = new TextFieldController_Integer(maxCompensation, "#gbv", "EpidemicCompHi",
				new IntGetter("#gbv", "EpidemicCompLo"), new IntGetter(100000));
		max.setLink(minCompensation);

		addIntField("#gbv", "ContagiousSpreadFactor", 0, 100);
		addIntField("#gbv", "VacCost", 0, 3000);
	}

	/**
	 * shortcut method for some of the fields. params cannot be null.
	 */
	private void addIntField(String paramName, String fieldName, int minInput, int maxInput) {
		tabPanel.add(new StringComponent().newParamLabel(paramName, fieldName));
		JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		tabPanel.add(tf);
		tf.addFocusListener(new TextFieldController_Integer(tf, paramName, fieldName, minInput, maxInput));
	}

	@Override
	public void reload() {
	}

}
