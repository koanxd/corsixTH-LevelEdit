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

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Groups a locale-sensitive object with the keys used to retrieve its strings.
 * This is done to dynamically change the language within the app.
 * 
 * @author Koanxd aka Snowblind
 */
public class StringComponent {
	private Object object;
	private String key = "";
	private String toolTipKey = "";

	private void build(Object object, String key, String toolTipKey) {
		this.object = object;
		this.key = key;
		this.toolTipKey = toolTipKey;
		Strings.addStringWrapper(this);
		update();
	}

	/**
	 * @param label
	 *            the label to be updated on every language change.
	 * @param key
	 *            to retrieve the label text from language files
	 * @param toolTipKey
	 *            to retrieve the tooltip text from language files
	 * @return the same label
	 */
	public JLabel label(JLabel label, String key, String toolTipKey) {
		build(label, key, toolTipKey);
		return label;
	}

	/**
	 * @param key
	 *            to retrieve the label text from language files
	 * @param toolTipKey
	 *            to retrieve the tooltip text from language files
	 * @return a new label which is automatically updated on every language
	 *         change
	 */
	public JLabel newLabel(String key, String toolTipKey) {
		return label(new JLabel(key), key, toolTipKey);
	}

	/**
	 * @return a description label for the given param and field, which is
	 *         automatically updated on every language change
	 */
	public JLabel newParamLabel(String paramName, String fieldName) {
		String labelKey = "lb" + paramName;
		String tooltipKey = "tt" + paramName;
		if (fieldName != null) {
			labelKey += "." + fieldName;
			tooltipKey += "." + fieldName;
		}
		return newLabel(labelKey, tooltipKey);
	}

	/**
	 * @param comboBox
	 *            the comboBox to be updated on every language change
	 * @return the same comboBox
	 */
	public JComboBox<?> comboBox(JComboBox<?> comboBox) {
		build(comboBox, null, null);
		return comboBox;
	}

	/**
	 * @param key
	 *            to retrieve the button text from language files.
	 * @return a new button which is automatically updated on every language
	 *         change.
	 */
	public JButton newButton(String key) {
		JButton btn = new JButton(key);
		build(btn, key, null);
		return btn;
	}

	/**
	 * @param panel
	 *            the panel to be updated on every language change
	 * @param titleKey
	 *            to retrieve the title text from language files
	 * @return the same panel
	 */
	public GridPanel panel(GridPanel panel, String titleKey) {
		if (!(panel.getBorder() instanceof TitledBorder))
			panel.setBorder(BorderFactory.createTitledBorder(titleKey));
		build(panel, titleKey, null);
		return panel;
	}

	/**
	 * @param menu
	 *            the menu to be updated on every language change
	 * @param titleKey
	 *            to retrieve the title text from language files
	 * @return the same menu
	 */
	public JMenu menu(JMenu menu, String titleKey) {
		build(menu, titleKey, null);
		return menu;
	}

	/**
	 * @param menuItem
	 *            the menuItem to be updated on every language change
	 * @param titleKey
	 *            to retrieve the title text from language files
	 * @return the same menuItem
	 */
	public JMenuItem menuItem(JMenuItem menuItem, String titleKey) {
		build(menuItem, titleKey, null);
		return menuItem;
	}

	/**
	 * @param frame
	 *            the frame to be updated on every language change
	 * @param titleKey
	 *            to retrieve the title text from language files
	 * @return the same frame.
	 */
	public JFrame frame(JFrame frame, String titleKey) {
		build(frame, titleKey, null);
		return frame;
	}

	/**
	 * @param tabBar
	 *            the tabBar to be updated on every language change
	 * @return the same tabBar
	 */
	public TabBar tabBar(TabBar tabBar) {
		build(tabBar, null, null);
		return tabBar;
	}

	/**
	 * Update the wrapped object to match the currently used language.
	 * Additionally, if the wrapped object is a comboBox, then the displayed
	 * items are also sorted alphabetically.
	 */
	public void update() {
		String text = Strings.get(key);
		if (object instanceof JLabel) {
			((JLabel) object).setText(text);
		} else if (object instanceof AbstractButton) {
			((AbstractButton) object).setText(text);
		} else if (object instanceof GridPanel) {
			updateGridPanel(text);
		} else if (object instanceof TabBar) {
			updateTabBar();
		} else if (object instanceof JComboBox) {
			updateComboBox();
		} else if (object instanceof JFrame) {
			((JFrame) object).setTitle(text);
		}
		// general case
		if (object instanceof JComponent) {
			updateJComponent();
		}
	}

	private void updateGridPanel(String text) {
		Border border = ((GridPanel) object).getBorder();
		if (border instanceof TitledBorder) {
			((TitledBorder) border).setTitle(text);
		}
	}

	private void updateTabBar() {
		TabBar tabBar = ((TabBar) object);
		Map<String, Tab> tabs = tabBar.getTabsByKey();
		for (String tabKey : tabs.keySet()) {
			tabBar.setTitleAt(tabBar.indexOfComponent(tabs.get(tabKey)), Strings.get(tabKey));
		}
	}

	private void updateJComponent() {
		((JComponent) object).repaint();
		if (toolTipKey != null) {
			((JComponent) object).setToolTipText(Strings.get(toolTipKey));
		}
	}

	private void updateComboBox() {
		// comboBoxes are a special case. All items are being removed, re-added
		// in alphabetical order, and the previously selected item is
		// re-selected.

		@SuppressWarnings("unchecked")
		JComboBox<ComboItem> box = (JComboBox<ComboItem>) object;
		ComboItem previouslySelected = (ComboItem) box.getSelectedItem();
		box.removeAllItems();
		for (ItemListener listener : box.getItemListeners()) {
			if (listener instanceof ComboBoxController) {

				ArrayList<ComboItem> items = new ArrayList<ComboItem>(((ComboBoxController) listener).getItems());

				// sort alphabetically
				items.sort(new ComboItem.ItemComparator());

				// add items back
				for (ComboItem item : items) {
					box.addItem(item);
				}
			}
		}
		if (previouslySelected != null) {
			box.setSelectedItem(previouslySelected);
		}
	}
}
