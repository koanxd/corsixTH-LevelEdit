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
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.corsixth.leveledit.readwrite.ReaderWriter;

/**
 * Defines the top menu and related button press actions
 * 
 * @author Koanxd aka Snowblind
 */
@SuppressWarnings("serial")
public class Menu extends JMenuBar {
	
	private ReaderWriter readerWriter;
	private GuiImpl gui;

    private JMenuItem fileNew = new StringComponent().menuItem(
            new JMenuItem(), "mNew");

    private JMenuItem fileOpen = new StringComponent().menuItem(
            new JMenuItem(), "mOpen");

    private JMenuItem fileSaveAs = new StringComponent().menuItem(
            new JMenuItem(), "mSaveAs");

    private JMenu menuLanguage = new StringComponent().menu(new JMenu(),
            "mLanguage");

    private JFrame frame;

    public Menu(ReaderWriter rw, GuiImpl gui, final JFrame frame) {
    	readerWriter = rw;
    	this.gui = gui;
        this.frame = frame;
        createFileMenu();
        add(menuLanguage);
        createLanguageMenu();
    }

    private void createFileMenu() {
        JMenu menuFile = new StringComponent().menu(new JMenu(), "mFile");
        menuFile.add(fileNew);
        menuFile.add(fileOpen);
        menuFile.add(fileSaveAs);
        add(menuFile);

        fileNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonNew();
            }
        });

        fileOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                buttonOpen();
            }
        });

        fileSaveAs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                buttonSaveAs();
            }
        });

    }

    private void createLanguageMenu() {
        // scan for available language files and create buttons for each
        for (ResourceBundle bundle : Strings.getLocaleBundles()) {
            final Locale locale = bundle.getLocale();
            String display = locale.getDisplayLanguage();

            // "" is our default language, so we name it "English"
            if (display.equals("")) {
                display = Locale.ENGLISH.getDisplayLanguage();
            }
            JMenuItem languageButton = new JMenuItem(display);
            menuLanguage.add(languageButton);

            languageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	Strings.setLanguage(locale);
                    // the language menu is a unique case, so it isn't handled
                    // by setLanguage. We are simply creating the menu buttons
                    // anew, with a different JVM locale.
                    menuLanguage.removeAll();
                    createLanguageMenu();
                }
            });
        }
    }

    private void buttonNew() {
        try {
            readerWriter.reset();
            readerWriter.newFile();
        } catch (IOException e) {
            GuiImpl.showErrorDialog(e.getMessage());
            // cached file may be corrupted -> exit
            System.exit(1);
        }
        gui.reloadEntries();
    }

    private void buttonOpen() {
        try {
            //gui.getTabBar().setEnabled(false);
            File file = (new FileChooser(readerWriter, frame).open());
            if (file != null) {
                if (!file.getPath().toLowerCase().endsWith(".level")) {
                    if (! fileTypeNotSupportedDialog()) {
                    	//gui.getTabBar().setEnabled(true);
                        return;
                    }
                }
                readerWriter.open(file);
            }
        } catch (IOException e) {
            GuiImpl.showErrorDialog(e.getMessage());
            // cached file may be corrupted -> exit
            System.exit(1);
        }
        //gui.getTabBar().setEnabled(true);
        gui.reloadEntries();
    }

    /**
     * @return true if user pressed "Ok", false for "Cancel"
     */
    private boolean fileTypeNotSupportedDialog() {
        JOptionPane optionPane = new JOptionPane(
        		Strings.get("dFileTypeNotSupported"));
        Object[] options = new String[] { Strings.get("dOk"),
        		Strings.get("dCancel") };
        optionPane.setOptions(options);
        JDialog dialog = optionPane.createDialog(null,
        		Strings.get("dWarning"));
        dialog.setVisible(true);

        if (optionPane.getValue() == options[1]) {
            return false;
        } else
            return true;
    }

    private void buttonSaveAs() {
        try {
            File file = (new FileChooser(readerWriter, frame).saveAs());
            if (file != null) {
                readerWriter.save(file);
            }
        } catch (IOException e) {
            GuiImpl.showErrorDialog(Strings.get("dSaveLevelFailed"));
        }
    }
}
