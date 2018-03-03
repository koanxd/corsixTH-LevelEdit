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

import javax.swing.text.JTextComponent;

public class TextFieldController_Interest extends TextFieldController{

	public TextFieldController_Interest(JTextComponent component, String paramName, String fieldName) {
		super(component, paramName, fieldName);
		reload();
	}

	@Override
	public void processInput() {
		try { // parse double and cap at min/max value.
			String inputString = component.getText().replaceAll(",", ".");
			Double inputDouble = Double.parseDouble(inputString);
			if (inputDouble < 0) {
				inputDouble = 0d;
			} else if (inputDouble >= 100)
				inputDouble = 99.9;
			inputString = inputDouble.toString();
			int interestInt = (int) (inputDouble * 100);
			params.setValue(paramName, fieldName, Integer.toString(interestInt));
			component.setText(inputString);
		} catch (NumberFormatException nfe) {
			component.setText(lastContent);
		}
	}
	
	@Override
	public void reload(){
		String text = "";
		if (fieldName != null || params.getFieldNames(paramName).size() == 0) {
			text = params.getValue(paramName, fieldName);
		}

		// interest rate: convert from level file representation to percentage
		if (fieldName != null && fieldName.equals("InterestRate")) {
			text = Double.toString((Double.parseDouble(text)) / 100);
		}
		
		component.setText(text);
		processInput();
	}

}
