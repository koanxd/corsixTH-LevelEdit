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

public class TextFieldController_Integer extends TextFieldController{
	protected IntGetter minInt = new IntGetter(Integer.MIN_VALUE);
	protected IntGetter maxInt = new IntGetter(Integer.MAX_VALUE);
	
	public TextFieldController_Integer(JTextComponent component, String paramName, String fieldName, int minInput, int maxInput) {
		super(component, paramName, fieldName);
		this.minInt = new IntGetter(minInput);
		this.maxInt = new IntGetter(maxInput);
		reload();
	}
	
	public TextFieldController_Integer(JTextComponent component, String paramName, String fieldName, IntGetter minInput, IntGetter maxInput) {
		super(component, paramName, fieldName);
		this.minInt = minInput;
		this.maxInt = maxInput;
		reload();
	}

	@Override
	public void processInput() {
		try { // automatically cap integers at the min/max value
			Integer input = Integer.parseInt(component.getText());
			int min = minInt.get();
			int max = maxInt.get();
			if (input < min)
				input = min;
			else if (input > max)
				input = max;

			params.setValue(paramName, fieldName, input.toString());
			String newText = input.toString();
			component.setText(newText);
		} catch (NumberFormatException nfe) {
			component.setText(lastContent);
		}
	}

}
