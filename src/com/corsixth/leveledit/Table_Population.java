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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.corsixth.leveledit.params.Params;

public class Table_Population extends Table {
	
	private List<TextFieldController> monthControllers = new ArrayList<>();
	private List<TextFieldController> changeControllers = new ArrayList<>();

    public Table_Population(GridPanel panel, String baseName, int startIndex,
            int componentsPerRow, String[] fields) {
        super(panel, baseName, startIndex, componentsPerRow, fields);
    }

    @Override
    protected ArrayList<JComponent> rowEntries(String param) {
        ArrayList<JComponent> entries = new ArrayList<>();

        int index = getCurrentIndex();
        String previousParam = null;

        // configure GUI-components
        JTextField tfMonth = new JTextField(GuiImpl.TFSIZE_S);
        JTextField tfChange = new JTextField(GuiImpl.TFSIZE_S);
        IntGetter minMonth = new IntGetter(0);
        IntGetter maxMonth = new IntGetter((index == startIndex) ? 0 : 10000);
        JTextField tfPatients = new JTextField(GuiImpl.TFSIZE_S);
        if (index > startIndex) {
            previousParam = params.getTable(Params.getBaseName(param)).get(index - 1);
            minMonth = new IntGetter(previousParam, "Month", +1);
            monthControllers.get(index - 1).setLink(tfMonth);
            changeControllers.get(index - 1).setLink(tfChange);
        }

        if (params.getValue(param, "Month") == null) {
            // initialize fields for newly created param
            if (rows.size() == 1) {
                params.setValue(param, "Month", "1");
            } else {
                params.setValue(param, "Month",
                        Integer.toString((minMonth.get() + 6) / 6 * 6));
            }

            if (rows.size() == 0) {
                params.setValue(param, "Change", "4");
            } else {
                params.setValue(param, "Change", "0");
            }
        }

        entries.add(new StringComponent().newLabel("lb#popn.Month", "tt#popn.Month"));
        entries.add(tfMonth);
        TextFieldController monthController = new TextFieldController_Integer(tfMonth, param, "Month", minMonth, maxMonth);
        monthController.setLink(tfPatients);
        monthControllers.add(monthController);

        entries.add(new StringComponent().newLabel("lb#popn.Change", "tt#popn.Change"));
        entries.add(tfChange);
        TextFieldController changeController = new TextFieldController_Integer(tfChange, param, "Change", -1000, 1000);
        changeController.setLink(tfPatients);
        changeControllers.add(changeController);
        
        if (index > startIndex) {
        	entries.add(new StringComponent().newLabel("lbPop_numPatients", "ttPop_numPatients"));
            entries.add(tfPatients);
            new TextFieldController_Population(tfPatients, param, null);
        } else {
            panel.next(2);
        }
        
        tfPatients.setEditable(false);

        return entries;
    }
    
    @Override
    protected void removeLastRow(){
    	super.removeLastRow();
		monthControllers.remove(monthControllers.size() - 1);
		changeControllers.remove(changeControllers.size() - 1);
    }
    
    @Override
    public void reload(){
    	super.reload();

        if (rows.size() > 0 && params.getTable("#popn").size() > 0) {
            monthControllers.get(0).processInput();
            changeControllers.get(0).processInput();
        }
    }

}
