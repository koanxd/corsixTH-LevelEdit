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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.corsixth.leveledit.readwrite.ReaderWriter;

/**
 * The frame used to display the GUI. Currently also manages the refresh process
 * for UI components, and some global UI configuration.
 * 
 * @author Koanxd aka Snowblind
 */
/*
 * This class could be split up to separate each individual task.
 */
@SuppressWarnings("serial")
public class GuiImpl extends JFrame implements Gui {

	private ReaderWriter readerWriter;

	// GUI-constants
	public static final int TFSIZE_S = 4;
	public static final int TFSIZE_M = 6;
	public static final int TFSIZE_L = 10;
	public static final int TFSIZE_XL = 20;
	public static final int TFSIZE_XXL = 50;
	private int toolTipDelay = 200;
	private int toolTipStayTime = 10000;
	private int windowSizeWidth = 800;
	private int windowSizeHeight = 600;

	// internal references
	private static List<Reloadable> reloadables = new ArrayList<>();

	/**
	 * Create GUI with a default configuration
	 */
	public GuiImpl(ReaderWriter rw) {

		readerWriter = rw;

		setIconImages(getIcons());
		lookAndFeelSetup();
		windowSetup();
		GUISetup();
		reloadEntries();
	}

	private void windowSetup() {
		new StringComponent().frame(this, "lbLevelEdit");
		setSize(windowSizeWidth, windowSizeHeight);
		setLocationRelativeTo(null); // center of screen

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				onExit();
			}
		});
	}

	private void GUISetup() {
		ToolTipManager.sharedInstance().setInitialDelay(toolTipDelay);
		ToolTipManager.sharedInstance().setDismissDelay(toolTipStayTime);
	}

	private void lookAndFeelSetup() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// do nothing; fall back to java look and feel
		}
	}

	/**
	 * @return list of icon images from source files
	 */
	private List<Image> getIcons() {
		List<Image> icons = new ArrayList<Image>();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon256.png")));
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon128.png")));
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon64.png")));
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon48.png")));
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon32.png")));
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon24.png")));
		icons.add(toolkit.getImage(ClassLoader.getSystemResource("icon16.png")));
		return icons;
	}

	/**
	 * Gives the user a chance to save any changes before exiting.
	 */
	protected void onExit() {
		JOptionPane exit = new JOptionPane(Strings.get("dSaveLevelChanges"));
		Object[] options = new String[] { Strings.get("dSave"), Strings.get("dDontSave"), Strings.get("dCancel") };
		exit.setOptions(options);

		JDialog exitDialog = exit.createDialog(this, Strings.get("dExit"));
		exitDialog.setVisible(true);

		if (exit.getValue() == options[0]) {
			try {
				File file = (new FileChooser(readerWriter, this).saveAs());
				if (file != null) {
					readerWriter.save(file);
					System.exit(0);
				}
			} catch (IOException e) {
				showErrorDialog("dSaveLevelFailed");
			}
		} else if (exit.getValue() == options[1]) {
			System.exit(0);
		}

	}

	/**
	 * Displays an error dialog with a message.
	 */
	public static void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, Strings.get("dError"), JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void registerReloadable(Reloadable reloadable) {
		reloadables.add(reloadable);
	}
	
	@Override
	public void removeReloadable(Reloadable reloadable){
		reloadables.remove(reloadable);
	}

	@Override
	public void reloadEntries() {
		List<Reloadable> reloadablesCopy = new ArrayList<Reloadable>(reloadables);
		for (Reloadable reloadable : reloadablesCopy) {
			reloadable.reload();
		}
	}

}
