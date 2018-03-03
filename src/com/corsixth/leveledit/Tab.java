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

import javax.swing.JScrollPane;

import com.corsixth.leveledit.params.Params;

@SuppressWarnings("serial")
abstract public class Tab extends JScrollPane implements Reloadable{
    
    protected static Params params;
    protected static Gui gui;
    
    protected GridPanel tabPanel;

    /**
	 * @throws IllegalStateException if an instance of {@link Params} or {@link Gui} has not been assigned to this class
     */
    public Tab(int noColumns) {
		if (params == null || gui == null) {
			throw new IllegalStateException(
					"Class Tab has missing dependencies");
		}
        this.tabPanel = new GridPanel(noColumns);
        getVerticalScrollBar().setUnitIncrement(20);
        getHorizontalScrollBar().setUnitIncrement(20);
        setViewportView(tabPanel);
        gui.registerReloadable(this);
    }

	/**
	 * Assigns a params instance that this class should operate on. Can be
	 * changed for unit testing.
	 */
	public static void injectParamsInstance(Params paramsInstance) {
		params = paramsInstance;
	}

	/**
	 * Assigns a {@link Gui} instance that this class should operate on. Can be
	 * changed for unit testing.
	 */
	public static void injectGuiInstance(Gui g) {
		gui = g;
	}
}
