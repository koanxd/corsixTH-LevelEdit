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
public class Tab_Awards extends Tab {

	private GridPanel cashPanel = new GridPanel(4);
	private GridPanel repPanel = new GridPanel(4);
	private GridPanel selectedPanel = cashPanel;
	private String award = "#awards_trophies";
	private String cashBonus = "lbAwardCashBonus";
	private String repBonus = "lbAwardRepBonus";
	private String cashPenalty = "lbAwardCashPenalty";
	private String repPenalty = "lbAwardRepPenalty";

	public Tab_Awards() {
		super(1);
		cashPanel = new StringComponent().panel(cashPanel, "lbAwardCashPanel");
		repPanel = new StringComponent().panel(repPanel, "lbAwardRepPanel");
		addCashEntries();
		addRepEntries();
		createTopPanel();
	}

	/**
	 * Create the panel which allows switching between award categories.
	 */
	private void createTopPanel() {
		JPanel topPanel = new JPanel();
		ButtonGroup buttonGroup = new ButtonGroup();
		JLabel cashLabel = new StringComponent().newLabel("lbAwardCashLabel", null);
		JLabel repLabel = new StringComponent().newLabel("lbAwardRepLabel", null);
		JRadioButton cashRB = new JRadioButton();
		JRadioButton repRB = new JRadioButton();

		tabPanel.add(topPanel);
		buttonGroup.add(cashRB);
		buttonGroup.add(repRB);
		cashRB.setActionCommand("cash");
		repRB.setActionCommand("rep");
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GridPanel newSelection;
				switch (e.getActionCommand()) {
				case "cash":
					newSelection = cashPanel;
					break;
				case "rep":
					newSelection = repPanel;
					break;
				default:
					newSelection = cashPanel;
				}

				tabPanel.swap(selectedPanel, newSelection);
				selectedPanel = newSelection;
				tabPanel.updateUI();
			}
		};

		topPanel.add(cashRB);
		cashRB.addActionListener(listener);
		topPanel.add(cashLabel);

		topPanel.add(repRB);
		repRB.addActionListener(listener);
		topPanel.add(repLabel);

		tabPanel.add(cashPanel);
		buttonGroup.setSelected(cashRB.getModel(), true);
	}

	private void addCashEntries() {

		// no deaths
		cashPanel.add(new StringComponent().newParamLabel(award, "TrophyDeathBonus"));
		cashPanel.next();
		addCashBonusEntry("TrophyDeathBonus", 0, 1000000, true);

		// 100% cures
		cashPanel.add(new StringComponent().newParamLabel(award, "TrophyCuresBonus"));
		cashPanel.next();
		addCashBonusEntry("TrophyCuresBonus", 0, 1000000, true);

		// reputation
		addCashEntry("Reputation", 1, 1000);
		addCashBonusEntry("TrophyReputationBonus", 0, 1000000, true);

		// drinks sold
		addCashEntry("CansofCoke", 1, 100000);
		addCashBonusEntry("CansofCokeBonus", 0, 1000000, true);

		// cures bonus
		addCashEntry("CuresAward", 1, 255);
		addCashBonusEntry("CuresBonus", 0, 1000000, true);

		// cures penalty
		addCashEntry("CuresPoor", 0, 255);
		addCashBonusEntry("CuresPenalty", -100000, 0, false);

		// deaths bonus
		addCashEntry("DeathsAward", 0, 255);
		addCashBonusEntry("DeathsBonus", 0, 1000000, true);

		// deaths penalty
		addCashEntry("DeathsPoor", 1, 255);
		addCashBonusEntry("DeathsPenalty", -100000, 0, false);

		// cures:deaths bonus
		addCashEntry("CuresVDeathsAward", 1, 100);
		addCashBonusEntry("CuresVDeathsBonus", 0, 1000000, true);

		// cures:deaths penalty
		addCashEntry("CuresVDeathsPoor", 0, 100);
		addCashBonusEntry("CuresVDeathsBonus", -100000, 0, false);

		// reputation bonus
		addCashEntry("ReputationAward", 1, 1000);
		addCashBonusEntry("AwardReputationBonus", 0, 1000000, true);

		// reputation penalty
		addCashEntry("ReputationPoor", 1, 1000);
		addCashBonusEntry("AwardReputationPenalty", -100000, 0, false);

		// rat kills percentage
		addCashEntry("RatKillsPercentage", 1, 100);
		addCashBonusEntry("RatKillsPercentageBonus", 0, 1000000, true);

		// new tech bonus
		cashPanel.add(new StringComponent().newParamLabel(award, "NewTechAward"));
		cashPanel.next();
		addCashBonusEntry("NewTechAward", 0, 1000000, true);
	}

	private void addRepEntries() {

		// VIP happiness
		repPanel.add(new StringComponent().newParamLabel(award, "TrophyMayorBonus"));
		repPanel.next();
		addRepBonusEntry("TrophyMayorBonus", 0, 255, true);

		// research bonus
		repPanel.add(new StringComponent().newParamLabel(award, "ResearchBonus"));
		repPanel.next();
		addRepBonusEntry("ResearchBonus", 0, 125, true);

		// research penalty
		repPanel.add(new StringComponent().newParamLabel(award, "ResearchPenalty"));
		repPanel.next();
		addRepBonusEntry("ResearchPenalty", -125, 0, false);

		// staff happiness
		addRepEntry("TrophyStaffHappiness", 1, 100);
		addRepBonusEntry("TrophyStaffHappinessBonus", 0, 255, true);

		// litter bonus
		addRepEntry("CleanlinessAward", 1, 100);
		addRepBonusEntry("CleanlinessBonus", 0, 125, true);

		// litter penalty
		addRepEntry("CleanlinessPoor", 0, 100);
		addRepBonusEntry("CleanlinessPenalty", -125, 0, false);

		// emergency patients saved bonus
		addRepEntry("EmergencyAward", 1, 100);
		addRepBonusEntry("EmergencyBonus", 0, 125, true);

		// emergency patients saved penalty
		addRepEntry("EmergencyPoor", 0, 100);
		addRepBonusEntry("EmergencyPenalty", -125, 0, false);

		// patient happiness bonus
		addRepEntry("PeepHappinessAward", 1, 100);
		addRepBonusEntry("PeepHappinessBonus", 0, 125, true);

		// patient happiness penalty
		addRepEntry("PeepHappinessPoor", 0, 100);
		addRepBonusEntry("PeepHappinessPenalty", -125, 0, false);

		// population percentage bonus
		addRepEntry("PopulationPercentageAward", 1, 100);
		addRepBonusEntry("PopulationPercentageBonus", 0, 255, true);

		// population percentage penalty
		addRepEntry("PopulationPercentagePoor", 0, 100);
		addRepBonusEntry("PopulationPercentagePenalty", -125, 0, false);

		// plants watered
		addRepEntry("Plant", 1, 100);
		addRepBonusEntry("PlantBonus", 0, 255, true);

		// rat kills absolute
		addRepEntry("RatKillsAbsolute", 1, 10000);
		addRepBonusEntry("RatKillsAbsoluteBonus", 0, 255, true);

		// hospital value bonus
		addRepEntry("HospValueAward", 1, 10000000);
		addRepBonusEntry("HospValueBonus", 0, 125, true);

		// hospital value penalty
		addRepEntry("HospValuePoor", 0, 10000000);
		addRepBonusEntry("HospValuePenalty", -125, 0, false);

		// staff happiness bonus
		addRepEntry("StaffHappinessAward", 1, 100);
		addRepBonusEntry("AwardStaffHappinessBonus", 0, 125, true);

		// staff happiness penalty
		addRepEntry("StaffHappinessPoor", 0, 100);
		addRepBonusEntry("AwardStaffHappinessPenalty", -125, 0, false);

		// waiting times bonus
		addRepEntry("WaitingTimesAward", 1, 100);
		addRepBonusEntry("WaitingTimesBonus", 0, 125, true);
		
		// waiting times penalty
		addRepEntry("WaitingTimesPoor", 0, 100);
		addRepBonusEntry("WaitingTimesPenalty", -125, 0, false);

		// machine maintenance bonus
		addRepEntry("WellKeptTechAward", 1, 100);
		addRepBonusEntry("WellKeptTechBonus", 0, 125, true);

		// machine maintenance penalty
		addRepEntry("WellKeptTechPoor", 0, 100);
		addRepBonusEntry("WellKeptTechPenalty", -125, 0, false);

	}
	
	private void addCashEntry(String fieldName, int minInput, int maxInput) {
		cashPanel.add(new StringComponent().newParamLabel(award, fieldName));
		JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		cashPanel.add(tf);
		tf.addFocusListener(new TextFieldController_Integer(tf, award, fieldName, minInput, maxInput));
	}

	private void addCashBonusEntry(String fieldName, int minInput, int maxInput, boolean bonus) {
		String label = (bonus) ? cashBonus : cashPenalty;
		cashPanel.add(new StringComponent().newLabel(label, null));
		JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		cashPanel.add(tf);
		tf.addFocusListener(new TextFieldController_Integer(tf, award, fieldName, minInput, maxInput));
	}

	private void addRepEntry(String fieldName, int minInput, int maxInput) {
		repPanel.add(new StringComponent().newParamLabel(award, fieldName));
		JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		repPanel.add(tf);
		tf.addFocusListener(new TextFieldController_Integer(tf, award, fieldName, minInput, maxInput));
	}

	private void addRepBonusEntry(String fieldName, int minInput, int maxInput, boolean bonus) {
		String label = (bonus) ? repBonus : repPenalty;
		repPanel.add(new StringComponent().newLabel(label, null));
		JTextField tf = new JTextField(GuiImpl.TFSIZE_L);
		repPanel.add(tf);
		tf.addFocusListener(new TextFieldController_Integer(tf, award, fieldName, minInput, maxInput));
	}

	@Override
	public void reload() {

	}
}
