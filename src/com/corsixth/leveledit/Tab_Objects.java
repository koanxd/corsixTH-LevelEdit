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
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Tab_Objects extends Tab {

    private CheckBoxGroupController availableChecker = new CheckBoxGroupController();
    private CheckBoxGroupController startAvailChecker = new CheckBoxGroupController();

    public Tab_Objects() {
        super(5);
        tabPanel.setInsets(2, 7, 2, 7);

        addHeadings();
        addEntries();
    }
    
    private void addHeadings(){
        tabPanel.add(new StringComponent().newLabel("lbCheckAll", null));
        tabPanel.add(availableChecker);
        tabPanel.add(startAvailChecker);
        tabPanel.nextRow();

        tabPanel.next();
        tabPanel.add(new StringComponent().newLabel("lbObjectAvailable",
                "ttObjectAvailable"));
        tabPanel.add(new StringComponent().newLabel("lbObjectStartAvail",
                "ttObjectStartAvail"));
        tabPanel.add(new StringComponent().newLabel("lbObjectResearch",
                "ttObjectResearch"));
        tabPanel.add(new StringComponent().newLabel("lbObjectStrength",
                "ttObjectStrength"));
        tabPanel.nextRow();
    }

    private void addEntries() {
        addRow(55, false); // autopsy
        addRow(40, false); // computer
        addRow(41, false); // atom analyser
        addRow(37, false); // projector
        addRow(57, false); // video game
        addRow(8, false); //ward
        addRow(20, false); //general diagnosis
        addRow(18, false); //psychiatry
        addRow(39, false); // pharmacy
        addRow(9, true); //inflator
        addRow(13, true); //cardio
        addRow(26, true); //slack tongue
        addRow(27, true); //x-ray
        addRow(30, true); //OP
        addRow(24, true); //cast remover
        addRow(14, true); //scanner
        addRow(25, true); //hair restorer
        addRow(42, true); // blood machine
        addRow(46, true); //electrolysis
        addRow(54, true); //decon shower
        addRow(22, true); //ultrascan
        addRow(47, true); //jelly vat
        addRow(23, true); //DNA restorer
        // addEntry(19, false); //sofa
        // addEntry(10, false); //snooker
        // addEntry(51, false); //toilet
        // addEntry(7, false); //drinks
        // addEntry(4, false); //bench
        // addEntry(45, false); //plant
        // addEntry(44, false); //radiator
        // addEntry(43, false); //extinguisher
        // addEntry(50, false); //bin
    }

    private void addRow(int id, boolean isMachine) {
        String param = "#objects[" + id + "]";

        tabPanel.add(new StringComponent().newLabel("lb"+param, null));
        
        JCheckBox available = new JCheckBox();
        tabPanel.add(available);
        new CheckBoxController(available, param, "AvailableForLevel");
        availableChecker.addBox(available);

        JCheckBox startAvailable = new JCheckBox();
        tabPanel.add(startAvailable);
        new CheckBoxController(startAvailable, param, "StartAvail");
        startAvailChecker.addBox(startAvailable);

        if (id != 55) {
        	JTextField res = new JTextField(GuiImpl.TFSIZE_L);
        	tabPanel.add(res);
        	new TextFieldController_Integer(res, param, "RschReqd", 0, 999999);
        } else { // cannot research autopsy machine
            tabPanel.next();
        }

        if (isMachine) {
        	JTextField str = new JTextField(GuiImpl.TFSIZE_L);
        	tabPanel.add(str);
        	new TextFieldController_Integer(str, param, "StartStrength", 1, 99);
        }

        tabPanel.nextRow();
    }

    @Override
    public void reload() {
        
    }
}