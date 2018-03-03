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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;

import com.corsixth.leveledit.params.Params;

/**
 * Modifies params according to combobox item selection.
 * 
 * @author Koanxd aka Snowblind
 */
public abstract class ComboBoxController implements ItemListener, Reloadable {
    protected static Params params;
	protected static Gui gui;
    
    private JComboBox<ComboItem> box;
    protected Map<Object, ComboItem> items = new HashMap<>();

    /**
	 * @throws IllegalStateException if dependencies are missing
     */
    protected ComboBoxController(JComboBox<ComboItem> box) {
		
		if (params == null || gui == null) {
			throw new IllegalStateException(
					"Missing dependencies in class ComboBoxListener");
		}
        this.box = box;
        box.addItemListener(this);
        gui.registerReloadable(this);
    }

    /**
     * @return an unmodifiable view of the items added to this comboBox
     */
    public Collection<ComboItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    /**
     * @return the comboBoxItem identified by the key
     */
    public ComboItem getItem(Object key) {
        return items.getOrDefault(key, null);
    }

    /**
     * @return the comboBox associated with this listener
     */
    public JComboBox<ComboItem> getBox() {
        return box;
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
