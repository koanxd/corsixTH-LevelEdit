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


@SuppressWarnings("serial")
public class Tab_Population extends Tab {

    private GridPanel tablePanel = new GridPanel(1);
    private GridPanel infoPanel = new GridPanel(1);

    public Tab_Population() {
        super(1);
        tabPanel.add(infoPanel);
        infoPanel.add(new StringComponent().newLabel("lbPop_info", null));
        infoPanel.add(new StringComponent().newLabel("lbPop_info2", null));
        infoPanel.add(new StringComponent().newLabel("lbPop_info3", null));
        infoPanel.add(new StringComponent().newLabel("lbPop_info4", null));
        tabPanel.add(tablePanel);
        
        new Table_Population(tablePanel, "#popn", 0, 6,
                new String[] { "Month", "Change" });
    }

    @Override
    public void reload() {
    }
}
