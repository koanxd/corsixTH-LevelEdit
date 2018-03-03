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

public class TextFieldController_Map extends TextFieldController_String{

	public TextFieldController_Map(JTextComponent component, String paramName, String fieldName) {
		super(component, paramName, fieldName);
		reload();
	}
	
	@Override
	public void processInput(){
		String input = component.getText();
		// if the input is not a .map file or one of the original
		// levels (level.l1 - level.l44), append ".map" automatically.
		if (!((input.toLowerCase().matches(".*\\.map"))
				|| (input.toLowerCase().matches("(level\\.l[1-9])|(level\\.l[1-3]\\d)|(level\\.l4[0-4])")))) {
			input += ".map";
		}
		params.setValue(paramName, fieldName, input);
		component.setText(input);
	}

}
