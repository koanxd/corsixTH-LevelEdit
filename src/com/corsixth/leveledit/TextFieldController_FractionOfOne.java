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

public class TextFieldController_FractionOfOne extends TextFieldController {

	public TextFieldController_FractionOfOne(JTextComponent component, String paramName, String fieldName) {
		super(component, paramName, fieldName);
		reload();
	}

	@Override
	public void processInput() {
		try { // retrieve integer value from input in form of fraction or
				// integer, cap between 0 and 100.
			String text = component.getText();
			Integer input;
			if (text.startsWith("1/")) {
				input = Integer.parseInt(text.substring(2));
			} else {
				input = Integer.parseInt(text);
			}
			if (input < 0)
				input = 0;
			else if (input > 100)
				input = 100;

			String newText = input.toString();
			params.setValue(paramName, fieldName, newText);
			if (!newText.equals("0")) {
				newText = "1/" + newText;
			}
			component.setText(newText);
		} catch (NumberFormatException nfe) {
			component.setText(lastContent);
		}
	}

}
