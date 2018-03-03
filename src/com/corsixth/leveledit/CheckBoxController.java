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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import com.corsixth.leveledit.params.Params;

/**
 * Defines behaviour for checkboxes which belong to a param:
 * <p>
 * - When selected, change param value to "1", else change it to "0". <br>
 * - update component when a new file is loaded.
 * 
 * @author Koanxd aka Snowblind
 */
public class CheckBoxController implements ItemListener, Reloadable{
	protected static Params params;
	protected static Gui gui;

	private String paramName;
	private String fieldName;
	private JCheckBox box;

	/**
	 * @param paramName
	 *            name of the param to change when selecting/deselecting
	 * @param fieldName
	 *            field to change within the param. can be null.
	 * @throws NullPointerException
	 *             if param paramName is null.
	 * @throws IllegalStateException
	 *             if dependencies are missing
	 */
	public CheckBoxController(JCheckBox box, String paramName, String fieldName) {
		if (params == null || gui == null) {
			throw new IllegalStateException("Class CheckBoxController has missing dependencies.");
		}
		if (paramName == null)
			throw new NullPointerException();
		this.box = box;
		this.paramName = paramName;
		this.fieldName = fieldName;
		box.addItemListener(this);
		gui.registerReloadable(this);
		reload();
	}

	/**
	 * Propagate state change to {@link Params}
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			params.setValue(paramName, fieldName, "1");
		} else if (e.getStateChange() == ItemEvent.DESELECTED) {
			params.setValue(paramName, fieldName, "0");
		}
	}

	@Override
	public void reload() {
		if (!params.contains(paramName)){
			gui.removeReloadable(this);
			return;
		}
		String value = params.getValue(paramName, fieldName);
		if (value != null && !value.equals("0")) {
			box.setSelected(true);
		} else {
			box.setSelected(false);
		}
	}

	/**
	 * Assigns a params instance that this class should operate on. Can be
	 * changed for unit testing.
	 */
	public static void injectParamsInstance(Params instance) {
		params = instance;
	}

	/**
	 * Assigns a {@link Gui} instance that this class should operate on. Can be
	 * changed for unit testing.
	 */
	public static void injectGuiInstance(Gui g) {
		gui = g;
	}

}
