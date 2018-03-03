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

import java.awt.BorderLayout;

public class Tab_Goals extends Tab {
    private static final long serialVersionUID = -6779024025458437391L;

    public Tab_Goals() {
        super(2);

        tabPanel.setLayout(new BorderLayout());
        addWinPanel();
        addLosePanel();
    }

    private void addWinPanel() {
        GridPanel table = new StringComponent().panel(new GridPanel(1),
                "lbWinCriteriaPanel");
        tabPanel.add(table, BorderLayout.LINE_START);

        new Table_WinCriteria(table, "#win_criteria", 0, 2, new String[] {
                "Criteria", "Value", "MaxMin", "Bound", "Group" });
    }

    private void addLosePanel() {
        GridPanel table = new StringComponent().panel(new GridPanel(1),
                "lbLoseCriteriaPanel");
        tabPanel.add(table, BorderLayout.CENTER);

        new Table_LoseCriteria(table, "#lose_criteria", 0, 4, new String[] {
                "Criteria", "Value", "MaxMin", "Bound", "Group" });
    }

    @Override
    public void reload() {

    }
}
