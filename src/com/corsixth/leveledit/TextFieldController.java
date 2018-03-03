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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import com.corsixth.leveledit.params.Params;

/**
 * Defines textfield behaviour, namely: <br>
 * <p>
 * - select content when clicked <br>
 * - enforce valid input <br>
 * - propagate changes to {@link Params} when textfield focus is lost <br>
 * - update textfield when a new file is loaded
 * </p>
 * 
 * @author Koanxd aka Snowblind
 */
abstract public class TextFieldController implements FocusListener, Reloadable {
	protected static Params params;
	protected static Gui gui;

	protected String paramName;
	protected String fieldName;
	protected JTextComponent component;
	protected List<JTextComponent> linkedComponents = new ArrayList<>();
	protected String lastContent;

	/**
	 * @throws IllegalStateException
	 *             if dependencies are missing
	 */
	public TextFieldController(JTextComponent component, String paramName, String fieldName) {
		if (params == null || gui == null) {
			throw new IllegalStateException(
					"Class TextFieldController has missing dependencies");
		}
		this.component = component;
		this.paramName = paramName;
		this.fieldName = fieldName;
		component.addFocusListener(this);
		gui.registerReloadable(this);
	}

	/**
	 * Enforces valid input on this textfield. If input is valid, propagate
	 * changes to {@link Params}
	 */
	public abstract void processInput();

	/**
	 * Select textfield and save the current content, so we can revert back to
	 * it if needed.
	 */
	@Override
	public void focusGained(FocusEvent e) {
		if (component instanceof JTextField)
			component.selectAll();
		lastContent = component.getText();
	}

	@Override
	public void focusLost(FocusEvent e) {
		processInput();
		triggerLinkedComponents();
	}

	/**
	 * Link this textfield to another component. On every
	 * {@link #focusLost(FocusEvent)}, the linked component is refreshed.
	 * <p>
	 * This can be used to force table values to increase monotonically (e.g.
	 * the months in the earthquake table)
	 */
	public void setLink(final JTextComponent comp) {
		this.linkedComponents.add(comp);
	}

	private void triggerLinkedComponents() {
		// re-validate input for linked components
		for (JTextComponent comp : linkedComponents) {
			for (FocusListener listener : comp.getFocusListeners())
				if (listener instanceof TextFieldController)
					((TextFieldController) listener).processInput();
		}
	}

	@Override
	public void reload() {
		if (!params.contains(paramName)){
			gui.removeReloadable(this);
			return;
		}
		String text = "";
		if (fieldName != null || params.getFieldNames(paramName).size() == 0) {
			text = params.getValue(paramName, fieldName);
		}
		component.setText(text);
		processInput();
	}

	public JTextComponent getComponent() {
		return component;
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
