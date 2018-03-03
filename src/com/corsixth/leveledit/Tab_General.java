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

import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Tab_General extends Tab {

	private GridPanel infoPanel = new GridPanel(1);
	private GridPanel entryPanel = new GridPanel(2);

	public Tab_General() {
		super(1);
		tabPanel.add(infoPanel);
		infoPanel.add(new StringComponent().newLabel("lbGeneralInfo1", null));
		tabPanel.add(entryPanel);
		addEntries();
	}

	private void addEntries() {
		
		addFileParams();
		
		addTownParams();
		
		addIntField("#gbv", "LandCostPerTile", 0, 1000);
		addIntField("#gbv", "HoldVisualMonths", 0, 1000);
		addIntField("#gbv", "HoldVisualPeepCount", 0, 1000);
		addIntField("#gbv", "LeaveMax", 10, 1000);
		addIntField("#gbv", "VomitLimit", 1, 100);
		addIntField("#gbv", "LitterDrop", 1, 100);
		addIntField("#gbv", "MayorLaunch", 1, 10000);
		addIntField("#gbv", "RemoveRatHoleChance", 1, 10000);
	}
	
	private void addFileParams(){
		{
			entryPanel.add(new StringComponent().newParamLabel("%Name", null));
			JTextField tf = new JTextField(GuiImpl.TFSIZE_XXL);
			entryPanel.add(tf);
			new TextFieldController_String(tf, "%Name", null);
		}
		{
			entryPanel.add(new StringComponent().newParamLabel("%LevelFile", null));
			JTextField tf = new JTextField(GuiImpl.TFSIZE_XXL);
			entryPanel.add(tf);
			new TextFieldController_Map(tf, "%LevelFile", null);
		}
		{
			entryPanel.add(new StringComponent().newParamLabel("%LevelBriefing", null));
			JTextArea tf = new JTextArea(3, GuiImpl.TFSIZE_XXL);
			tf.setLineWrap(true);
			tf.setWrapStyleWord(true);
			entryPanel.add(tf);
			new TextFieldController_String(tf, "%LevelBriefing", null);
		}
	}
	
	private void addTownParams(){
		addIntField("#town", "StartCash", -99999999, 99999999);
		
		{
			entryPanel.add(new StringComponent().newParamLabel("#town", "InterestRate"));
			JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
			entryPanel.add(tf);
			new TextFieldController_Interest(tf, "#town", "InterestRate");
		}		
	}

	/**
	 * shortcut method for some of the fields. params cannot be null.
	 */
	 private void addIntField(String paramName, String fieldName, int minInput, int maxInput){
		 entryPanel.add(new StringComponent().newParamLabel(paramName, fieldName));
		 JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		 entryPanel.add(tf);
		 new TextFieldController_Integer(tf, paramName, fieldName, minInput, maxInput);
	 }

	@Override
	public void reload() {

	}
}
