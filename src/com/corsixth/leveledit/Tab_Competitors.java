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

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class Tab_Competitors extends Tab {
    private GridPanel infoPanel = new GridPanel(1);
    private GridPanel entryPanel = new GridPanel(2);

    public Tab_Competitors() {
        super(1);
        tabPanel.add(infoPanel);
        tabPanel.add(entryPanel);
        infoPanel.add(new StringComponent().newLabel("lbComp_info1", null));
        infoPanel.add(new StringComponent().newLabel("lbComp_info2", null));
        addEntries();
    }

    private void addEntries() {
        for(int i=0; i<15; i++){
        	String param = "#computer["+i+"]";
        	JCheckBox box = new JCheckBox();
        	entryPanel.add(box);
        	new CheckBoxController(box, param, "Playing");
        	entryPanel.add(new StringComponent().newParamLabel(param, null));
        }
    }

    @Override
    public void reload() {

    }
}
