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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.corsixth.leveledit.params.Params;

/**
 * Groups components in a table, with buttons to add and remove rows, where each
 * row has the setup of UI elements. Params in a table have an index, e.g.
 * "#emergency_control[8]".
 */
public abstract class Table implements Reloadable {
	protected static Params params;
	protected static Gui gui;

	protected List<List<JComponent>> rows = new ArrayList<>();
	protected List<GridPanel> rowPanels = new ArrayList<>();
	protected List<String> paramList;
	protected int startIndex = 0;
	protected String[] fieldsInOrder;

	protected GridPanel panel;
	private GridPanel buttonPanel = new GridPanel(2);
	private int componentsPerRow = 1;
	private JButton addBtn = new StringComponent().newButton("btnAddRow");
	private JButton removeBtn = new StringComponent().newButton("btnRemoveRow");

	/**
	 * @throws IllegalStateException
	 *             if dependencies are missing
	 */
	public Table(final GridPanel panel, final String baseName, final int startIndex, final int componentsPerRow,
			final String[] fields) {

		if (params == null || gui == null) {
			throw new IllegalStateException("Missing dependencies for class Table");
		}

		gui.registerReloadable(this);
		this.panel = panel;
		this.paramList = params.getTable(baseName);
		this.startIndex = startIndex;
		this.componentsPerRow = componentsPerRow;
		this.fieldsInOrder = fields;
		buttonPanel.add(addBtn);
		buttonPanel.add(removeBtn);
		panel.add(buttonPanel);
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newParam = baseName + "[" + getCurrentIndex() + "]";
				params.add(newParam);
				for (int i = 0; i < fields.length; i++) {
					params.addField(newParam, fields[i], null);
				}
				addRow(rowEntries(newParam));
			}
		});
		removeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeLastRowAndParam();
			}
		});
	}

	/**
	 * The entries to appear in each row are defined here. This method is called
	 * when a new row is added.
	 * 
	 * @return a new list of entries to be added to a row.
	 */
	protected abstract List<JComponent> rowEntries(final String param);

	protected void addRow(final List<JComponent> entries) {
		if (entries == null)
			return;

		rows.add(entries);
		GridPanel rowPanel = new GridPanel(componentsPerRow);
		rowPanels.add(rowPanel);
		for (JComponent entry : entries) {
			rowPanel.add(entry);
			// TextFieldController listener = entry.getListener();
			// if (listener != null) {
			// listener.processInput();
			// }
		}
		panel.add(rowPanel);
		panel.updateUI();
	}

	protected void removeAllRows() {
		while (!rows.isEmpty()) {
			removeLastRow();
		}
	}

	/**
	 * Remove the last row from this table and from the global param list.
	 */
	protected void removeLastRowAndParam() {
		if (paramList.size() <= 0)
			return;

		removeLastRow();
		params.remove(paramList.get(paramList.size() - 1));
	}

	/**
	 * Remove the last row of UI elements from this table.
	 */
	protected void removeLastRow() {
		if (rows.size() <= 0)
			return;

		GridPanel toRemove = rowPanels.get(rowPanels.size() - 1);
		panel.remove(toRemove);
		rows.remove(rows.size() - 1);
		rowPanels.remove(rowPanels.size() - 1);
		panel.updateUI();
	}

	public int getCurrentIndex() {
		return rows.size() + startIndex;
	}

	@Override
	public void reload() {
		removeAllRows();
		for (int i = 0; i < paramList.size(); i++) {
			addRow(rowEntries(paramList.get(i)));
		}
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
