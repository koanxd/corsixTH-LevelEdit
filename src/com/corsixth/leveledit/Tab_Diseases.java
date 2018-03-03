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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Tab_Diseases extends Tab {

    private SubPanel drugPanel = new SubPanel("lbDrugPanel", 8, true);
    private SubPanel psychPanel = new SubPanel("lbPsychPanel", 7);
    private SubPanel opPanel = new SubPanel("lbOpPanel", 7);
    private SubPanel clinicPanel = new SubPanel("lbClinicPanel", 7);
    private GridPanel selected = drugPanel;

    /**
     * maps #expertise ID to the panel of the belonging category
     */
    private SubPanel[] categories = new SubPanel[44];

    /**
     * List of expertise indices which represent contagious diseases
     */
    private List<Integer> contagious = new ArrayList<>();

    /**
     * panel which represents a disease category. Defines column headings and
     * components which affect all rows in a table.
     */
    private class SubPanel extends GridPanel {
        public CheckBoxGroupController availableChecker = new CheckBoxGroupController();
        public CheckBoxGroupController knownChecker = new CheckBoxGroupController();

        public SubPanel(String titleKey, int noColumns) {
            this(titleKey, noColumns, false);
        }

        public SubPanel(String titleKey, int noColumns, boolean isDrug) {
            super(noColumns);

            new StringComponent().panel(this, titleKey);
            add(new StringComponent().newLabel("lbCheckAll", null));
            next();
            add(availableChecker);
            add(knownChecker);
            nextRow();
            next();
            add(new StringComponent().newLabel("lbDiseasePrice",
                    "ttDiseasePrice"));
            add(new StringComponent().newLabel("lbDiseaseAvailable",
                    "ttDiseaseAvailable"));
            add(new StringComponent().newLabel("lbExpertiseKnown",
                    "ttExpertiseKnown"));
            add(new StringComponent().newLabel("lbDiseaseContagious",
                    "ttDiseaseContagious"));
            add(new StringComponent().newLabel("lbDiseaseDifficulty",
                    "ttDiseaseDifficulty"));
            if (isDrug) {
                add(new StringComponent().newLabel("lbExpertiseResearch",
                        "ttExpertiseResearch"));
            }
            add(new StringComponent().newLabel("lbDiseaseMonth",
                    "ttDiseaseMonth"));
            nextRow();
        }

    }

    public Tab_Diseases() {
        super(1);

        putCategories();

        putContagious();

        addDiseaseEntries();

        createTopPanel();
    }

    private void addDiseaseEntries() {
        for (String param : params.getTable("#visuals")) {
            if (params.getIndex(param) > 13) {
                // unknown or unsupported disease
                continue;
            }
            addDiseaseEntry(param, true);
        }

        for (String param : params.getTable("#non_visuals")) {
            if (params.getIndex(param) > 19) {
                // unknown or unsupported disease
                continue;
            }
            addDiseaseEntry(param, false);
        }

    }

    /**
     * Adds all UI components for a disease, which are displayed in one row.
     */
    private void addDiseaseEntry(String param, boolean isVisual) {
        // Get the correct table indices for #expertise. The original game
        // files use the following pattern: visual id +2 and non_visual id +16.
        int id = params.getIndex(param);
        int expIndex = (isVisual) ? id + 2 : id + 16;
        String expertise = "#expertise[" + expIndex + "]";
        SubPanel panel = categories[expIndex];

        // name
        panel.add(new StringComponent().newLabel("lb"+expertise, null));

        // price
        JTextField price = new JTextField(GuiImpl.TFSIZE_M);
        price.setEditable(false);
        new TextFieldController_Integer(price, expertise, "StartPrice", 0, Integer.MAX_VALUE);
        panel.add(price);

        // available
        JCheckBox available = new JCheckBox();
        panel.add(available);
        new CheckBoxController(available, param, null);
        panel.availableChecker.addBox(available);
        
        // known
        JCheckBox known = new JCheckBox();
        panel.add(known);
        new CheckBoxController(known, expertise, "Known");
        panel.knownChecker.addBox(known);
        
        // how contagious
        if (contagious.contains(expIndex)) {
        	JTextField contagious = new JTextField(GuiImpl.TFSIZE_S);
        	panel.add(contagious);
        	new TextFieldController_FractionOfOne(contagious, expertise, "ContRate");
        } else {
        	panel.next();
        }
        
        // difficulty
        JTextField diff = new JTextField(GuiImpl.TFSIZE_M);
        panel.add(diff);
        new TextFieldController_Integer(diff, expertise, "MaxDiagDiff", 100, 1000);
        
        // research
        if (panel == drugPanel) {
            JTextField res = new JTextField(GuiImpl.TFSIZE_M);
            panel.add(res);
            new TextFieldController_Integer(res, expertise, "RschReqd", 0, 999999);
        }
        // month available
        if (isVisual) {
            JTextField month = new JTextField(GuiImpl.TFSIZE_S);
            panel.add(month);
            new TextFieldController_Integer(month, "#visuals_available[" + id + "]", null, 0, 999);
        }
        
        panel.nextRow();
    }

    /**
     * Assign each of the original diseases to their respective treatment
     * category. This information can then be retrieved from 'categories'
     */
    private void putCategories() {
        categories[2] = clinicPanel; // bloaty head
        categories[3] = clinicPanel; // hairyitus
        categories[4] = psychPanel; // elvis
        categories[5] = drugPanel; // invisibility
        categories[6] = clinicPanel; // radiation
        categories[7] = clinicPanel; // slack tongue
        categories[8] = clinicPanel; // alien
        categories[9] = clinicPanel; // broken bones
        categories[10] = clinicPanel; // baldness
        categories[11] = drugPanel; // discrete itching
        categories[12] = clinicPanel; // jellyitus
        categories[13] = drugPanel; // sleeping illness
        categories[14] = opPanel; // pregnant
        categories[15] = drugPanel; // transparency
        categories[16] = drugPanel; // uncommon cold
        categories[17] = drugPanel; // broken wind
        categories[18] = opPanel; // spare ribs
        categories[19] = opPanel; // kidney beans
        categories[20] = opPanel; // broken heart
        categories[21] = opPanel; // ruptured nodules
        categories[22] = psychPanel; // TV personality
        categories[23] = psychPanel; // infectious laughter
        categories[24] = drugPanel; // corrugated ankles
        categories[25] = drugPanel; // chronis nosehair
        categories[26] = psychPanel; // 3rd degree sideburns
        categories[27] = psychPanel; // fake blood
        categories[28] = drugPanel; // gastric ejections
        categories[29] = drugPanel; // the squits
        categories[30] = opPanel; // iron lungs
        categories[31] = psychPanel;// sweaty palms
        categories[32] = drugPanel; // heaped piles
        categories[33] = drugPanel; // gut rot
        categories[34] = opPanel; // golf stones
        categories[35] = opPanel; // unexpected swelling
    }

    private void putContagious() {
        for (int i = 2; i < 36; i++) {
            contagious.add(i);
        }
        // exceptions (non-contagious):
        contagious
                .removeAll(Arrays.asList(2, 3, 4, 5, 7, 8, 9, 10, 12, 14, 15));
    }

    /**
     * Create the panel which allows switching between room categories.
     */
    private void createTopPanel() {
        JPanel topPanel = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        JLabel drugLabel = new StringComponent().newLabel("lbDrugPanel", null);
        JLabel psychLabel = new StringComponent().newLabel("lbPsychPanel",
                null);
        JLabel opLabel = new StringComponent().newLabel("lbOpPanel", null);
        JLabel clinicLabel = new StringComponent().newLabel("lbClinicPanel",
                null);
        JRadioButton drugRB = new JRadioButton();
        JRadioButton psychRB = new JRadioButton();
        JRadioButton clinicRB = new JRadioButton();
        JRadioButton opRB = new JRadioButton();

        tabPanel.add(topPanel);
        buttonGroup.add(drugRB);
        buttonGroup.add(psychRB);
        buttonGroup.add(opRB);
        buttonGroup.add(clinicRB);
        drugRB.setActionCommand("drug");
        psychRB.setActionCommand("psych");
        opRB.setActionCommand("op");
        clinicRB.setActionCommand("clinic");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GridPanel newSelection;
                switch (e.getActionCommand()) {
                case "op":
                    newSelection = opPanel;
                    break;
                case "clinic":
                    newSelection = clinicPanel;
                    break;
                case "psych":
                    newSelection = psychPanel;
                    break;
                default:
                    newSelection = drugPanel;
                }

                tabPanel.swap(selected, newSelection);
                selected = newSelection;
                tabPanel.updateUI();
            }
        };

        topPanel.add(drugRB);
        drugRB.addActionListener(listener);
        topPanel.add(drugLabel);

        topPanel.add(psychRB);
        psychRB.addActionListener(listener);
        topPanel.add(psychLabel);

        topPanel.add(opRB);
        opRB.addActionListener(listener);
        topPanel.add(opLabel);

        topPanel.add(clinicRB);
        clinicRB.addActionListener(listener);
        topPanel.add(clinicLabel);

        tabPanel.add(drugPanel);
        buttonGroup.setSelected(drugRB.getModel(), true);
    }

    @Override
    public void reload() {

    }
}