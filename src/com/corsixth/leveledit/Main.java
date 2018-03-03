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

import com.corsixth.leveledit.params.Params;
import com.corsixth.leveledit.params.ParamsImpl;
import com.corsixth.leveledit.readwrite.ReaderWriter;
import com.corsixth.leveledit.readwrite.ReaderWriterImpl;

/**
 * The main entry point of the Level Editor which starts the Gui and handles
 * dependency injection.
 * 
 * @author Koanxd aka Snowblind
 * 
 */
public class Main {

	public static void main(String[] args) {

		/*
		 * A dependency injection framework is not used at this time.
		 * Static injection methods were added to classes which require
		 * dependencies, where adding the dependency as a constructor
		 * argument was impractical. All dependencies must be assigned to a
		 * class before it is instantiated. An exception should be
		 * thrown in the constructor of a class if its dependencies were not
		 * yet assigned.
		 */

		try {
			Params params = new ParamsImpl();
			
			CheckBoxController.injectParamsInstance(params);
			ComboBoxController.injectParamsInstance(params);
			IntGetter.injectParamsInstance(params);
			Tab.injectParamsInstance(params);
			Table.injectParamsInstance(params);
			TextFieldController.injectParamsInstance(params);

			ReaderWriter rw = new ReaderWriterImpl(params);
			
			GuiImpl gui = new GuiImpl(rw);
			
			Tab.injectGuiInstance(gui);
			Table.injectGuiInstance(gui);
			CheckBoxController.injectGuiInstance(gui);
			TextFieldController.injectGuiInstance(gui);
			ComboBoxController.injectGuiInstance(gui);
			
	        Menu menu = new Menu(rw, gui, gui);
	        TabBar tabBar = new StringComponent().tabBar(new TabBar());
	        gui.setJMenuBar(menu);
	        gui.setContentPane(tabBar);
			
			gui.setVisible(true);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			GuiImpl.showErrorDialog(e.getMessage());
			System.exit(1);
		}
	}
}