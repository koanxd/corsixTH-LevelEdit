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

import java.util.List;

import javax.swing.text.JTextComponent;

import com.corsixth.leveledit.params.Params;

public class TextFieldController_Population extends TextFieldController{

	public TextFieldController_Population(JTextComponent component, String paramName, String fieldName) {
		super(component, paramName, fieldName);
		reload();
	}
	
	@Override
	public void processInput(){
		// calculate and display patient count for a given month
		int numPatients = 0;
		int lastMonth = 0;
		int lastChange = 0;
		int thisMonth = Integer.parseInt(params.getValue(paramName, "Month"));
		if (thisMonth != 0) {
		    List<String> table = params.getTable(Params.getBaseName(paramName));
			for (int i = 0; i < table.size(); i++) {
				int currentMonth = Integer.parseInt(params.getValue(table.get(i), "Month"));
				if (currentMonth > 0) {
					numPatients += lastChange * (currentMonth - lastMonth);
				}
				lastChange = Integer.parseInt(params.getValue(table.get(i), "Change"));
				lastMonth = currentMonth;
				if (currentMonth == thisMonth) {
					break;
				}
			}
		}
		component.setText(Integer.toString(numPatients));
	}

}
