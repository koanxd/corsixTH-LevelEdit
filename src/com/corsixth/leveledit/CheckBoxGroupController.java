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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

/**
 * A checkbox with the functionality to simultaneously (de-)select a group of
 * assigned checkboxes.
 * 
 * @author Koanxd aka Snowblind
 */
@SuppressWarnings("serial")
public class CheckBoxGroupController extends JCheckBox {
	
	private boolean clickInProgress;

	private List<JCheckBox> children = new ArrayList<>();

	public CheckBoxGroupController() {
		super();

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onClick();
			}
		});
	}

	/**
	 * Add a checkbox to the group, so that everytime the user (de)selects the
	 * controller, the selected-state of all boxes in the group are matched to
	 * it.
	 * <p>
	 * In addition, the controller shows selected if all of its children are
	 * selected. If at least one child is deselected, the controller shows
	 * deselected.
	 */
	public void addBox(JCheckBox box) {
		children.add(box);
		update();
		
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (!clickInProgress)
					update();
			}
		});
	}

	/**
	 * Select the controller if all of its children are selected. Otherwise,
	 * deselect the controller.
	 */
	private void update() {
		for (JCheckBox box : children) {
			if (!box.isSelected()) {
				this.setSelected(false);
				return;
			}
		}
		this.setSelected(true);
	}

	private void onClick() {
		clickInProgress = true;
		for (JCheckBox box : children) {
			if (this.isSelected()) {
				box.setSelected(true);
			} else {
				box.setSelected(false);
			}
		}
		clickInProgress = false;
	}
}
