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
public class Tab_Research extends Tab{

    public Tab_Research() {
        super(2);
        addEntries();
    }
    
    private void addEntries(){

    	addIntField("#gbv", "ResearchPointsDivisor", 1, 100);
    	addIntField("#gbv", "StartRating", 0, 100);
    	addIntField("#gbv", "DrugImproveRate", 0, 100);
    	
    	addDrugCost();

    	addIntField("#gbv", "AutopsyRschPercent", 0, 100);
    	addIntField("#gbv", "AutopsyRepHitPercent", 0, 99);
    	addIntField("#gbv", "RschImproveCostPercent", 0, 1000);
    	addIntField("#gbv", "RschImproveIncrementPercent", 0, 1000);
    	addIntField("#gbv", "ResearchIncrement", 0, 99);
    	addIntField("#gbv", "MaxObjectStrength", 0, 99);
        
    }
    
    private void addDrugCost() {
    	//ensuring that min drug cost is <= start drug cost

		JTextField tfStartCost = new JTextField(GuiImpl.TFSIZE_L);
    	JTextField tfMinCost = new JTextField(GuiImpl.TFSIZE_L);
    	
		tabPanel.add(new StringComponent().newParamLabel("#gbv", "StartCost"));
		tabPanel.add(tfStartCost);
		new TextFieldController_Integer(tfStartCost, "#gbv", "StartCost", 0, 9999).setLink(tfMinCost);

		tabPanel.add(new StringComponent().newParamLabel("#gbv", "MinDrugCost"));
		tabPanel.add(tfMinCost);
		new TextFieldController_Integer(tfMinCost, "#gbv", "MinDrugCost", new IntGetter(0), new IntGetter("#gbv", "StartCost"));
    	
    }

	/**
	 * shortcut method for some of the fields. params cannot be null.
	 */
	 private void addIntField(String paramName, String fieldName, int minInput, int maxInput){
		 tabPanel.add(new StringComponent().newParamLabel(paramName, fieldName));
		 JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		 tabPanel.add(tf);
		 tf.addFocusListener(new TextFieldController_Integer(tf, paramName, fieldName, minInput, maxInput));
	 }

    @Override
    public void reload() {
    }

}
