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

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Tab_Emergencies extends Tab {

	private GridPanel randomPanel = new GridPanel(1);
	private GridPanel semiRandomPanel = new GridPanel(1);
	private GridPanel intervalVariancePanel = new GridPanel(2);
	private GridPanel controlledPanel = new GridPanel(1);
	private GridPanel controlTablePanel = new GridPanel(1);
	private GridPanel emptyPanel = new GridPanel(1);
	private GridPanel selected = emptyPanel;
	private Table_Emergencies emergencyTable;
	private String firstParam;
	private String lastValueForInterval;
	private String lastValueForVariance;

	private ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton randomRB = new JRadioButton();
	private JRadioButton semiRandomRB = new JRadioButton();
	private JRadioButton controlledRB = new JRadioButton();

	public Tab_Emergencies() {
		super(1);
		
		firstParam = "#emergency_control[0]";

		createRandomPanel();
		createSemiRandomPanel();
		createControlledPanel();
		createTopPanel();
	}

	private void createRandomPanel() {
		randomPanel.add(new StringComponent().newLabel("lbEmergencyModeRandomInfo", null));
	}

	private void createSemiRandomPanel() {
		GridPanel infoPanel = new GridPanel(1);
		infoPanel.add(new StringComponent().newLabel("lbEmergencyModeSemiRandomInfo", null));
		semiRandomPanel.add(infoPanel);
		semiRandomPanel.next();
	}

	private void createControlledPanel() {
		controlledPanel.add(new StringComponent().newLabel("lbEmergencyModeControlledInfo", null));
		emergencyTable = new Table_Emergencies(controlTablePanel);
		controlledPanel.add(controlTablePanel);
	}

	/**
	 * Create the panel which allows switching between emergency modi
	 */
	private void createTopPanel() {
		JPanel topPanel = new JPanel();
		tabPanel.add(topPanel);

		JLabel randomLabel = new StringComponent().newLabel("lbEmergencyModeRandom", null);
		JLabel semiRandomLabel = new StringComponent().newLabel("lbEmergencyModeSemiRandom", null);
		JLabel controlledLabel = new StringComponent().newLabel("lbEmergencyModeControlled", null);
		buttonGroup.add(randomRB);
		buttonGroup.add(semiRandomRB);
		buttonGroup.add(controlledRB);
		randomRB.setActionCommand("random");
		semiRandomRB.setActionCommand("semiRandom");
		controlledRB.setActionCommand("controlled");

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onRadioButtonPressed(e.getActionCommand());
			}
		};

		topPanel.add(randomRB);
		randomRB.addActionListener(listener);
		topPanel.add(randomLabel);

		topPanel.add(semiRandomRB);
		semiRandomRB.addActionListener(listener);
		topPanel.add(semiRandomLabel);

		topPanel.add(controlledRB);
		controlledRB.addActionListener(listener);
		topPanel.add(controlledLabel);

		tabPanel.add(emptyPanel);
	}

	private void onRadioButtonPressed(String newMode) {
		// step 1: cleanup params and GUI from previous mode
		if (selected == controlledPanel) {
			emergencyTable.setEnabled(false);
		} else if (selected == semiRandomPanel) {
			lastValueForInterval = params.getValue(firstParam, "Interval");
			lastValueForVariance = params.getValue(firstParam, "Variance");
			intervalVariancePanel.removeAll();
			semiRandomPanel.remove(intervalVariancePanel);
		}
		if (params.contains(firstParam)){
		    params.remove(firstParam);
		}
		// step 2: update model (params)
		switch (newMode) {
		case "random":
		    params.add("#emergency_control[0]");
		    params.addField(firstParam, "Random", "1");
			break;
		case "semiRandom":
			params.add("#emergency_control[0]");
			params.addField(firstParam, "Interval", lastValueForInterval);
			params.setValue(firstParam, "Interval", "120");
			params.addField(firstParam, "Variance", lastValueForVariance);
			params.setValue(firstParam, "Variance", "30");
			break;
		case "controlled":
			emergencyTable.setEnabled(true);
			break;
		default:
		}
		// step 3: update view (panels)
		reload();
	}

    @Override
	public void reload() {
		GridPanel newSelection = null;
		switch (emergencyTable.getMode()) {
		case Random:
			newSelection = randomPanel;
			buttonGroup.setSelected(randomRB.getModel(), true);
			break;
		case SemiRandom:
			newSelection = semiRandomPanel;
			buttonGroup.setSelected(semiRandomRB.getModel(), true);
			createIntervalVariancePanel();
			break;
		case Controlled:
			newSelection = controlledPanel;
			buttonGroup.setSelected(controlledRB.getModel(), true);
			break;
		case Unknown:
		default:
			newSelection = emptyPanel;
			buttonGroup.setSelected(buttonGroup.getSelection(), false);
		}
		swapPanel(newSelection);
	}

	private void createIntervalVariancePanel() {
		semiRandomPanel.add(intervalVariancePanel);
		
		intervalVariancePanel.add(new StringComponent().newLabel("lb#emergency_control.Interval", null));
		JTextField interval = new JTextField(GuiImpl.TFSIZE_M);
		intervalVariancePanel.add(interval);
		new TextFieldController_Integer(interval, "#emergency_control[0]", "Interval", 1, 9999);
		
		intervalVariancePanel.add(new StringComponent().newLabel("lb#emergency_control.Variance", null));
		JTextField variance = new JTextField(GuiImpl.TFSIZE_M);
		intervalVariancePanel.add(variance);
		new TextFieldController_Integer(variance, "#emergency_control[0]", "Variance", 0, 999);
	}

	private void swapPanel(GridPanel newSelection) {
		tabPanel.swap(selected, newSelection);
		selected = newSelection;
		tabPanel.updateUI();
	}
}
