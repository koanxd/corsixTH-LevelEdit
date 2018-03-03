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

import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.corsixth.leveledit.readwrite.ReaderWriter;

/**
 * A JFileChooser with app-specific implementations of the open and save dialogs
 * 
 * @author Koanxd aka Snowblind
 * 
 */
@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {
	
	private ReaderWriter readerWriter;

    // Used to get the CorsixTH icon in the top bar of the dialog.
    private JFrame mainFrame;

    /**
     * Upon creation, the file chooser is set to filter files by .level
     * extension.
     * 
     * @param frame
     *            the frame for the file chooser dialog to appear in
     */
    public FileChooser(ReaderWriter rw, JFrame frame) {
    	readerWriter = rw;
    	
        setCurrentDirectory(readerWriter.getLastFilePath());
        mainFrame = frame;
    }

    /**
     * Opens the fileChooser dialog for opening a file.
     * 
     * @return the file selected by the user, or null if the dialog was
     *         cancelled.
     */
    public File open() throws IOException {
        setFilters(false);
        int returnVal = showOpenDialog(mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            readerWriter.saveLastFilePath(getSelectedFile().getPath());
            return getSelectedFile();
        } else {
            return null;
        }
    }

    /**
     * Opens a fileChooser save dialog. Files are saved with .level extension.
     * Choosing an existing file triggers a confirmation dialog.
     * 
     * @return the file selected by the user, or null if the dialog was
     *         cancelled.
     */
    public File saveAs() throws IOException {
        setFilters(true);
        int returnVal = showSaveDialog(mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = getSelectedFile();
            readerWriter.saveLastFilePath(file.getPath());

            if (file.exists()) {
                // confirm to overwrite file
                JOptionPane overwrite = new JOptionPane(Strings.get("dOverwriteFile"));
                Object[] options = new String[] { Strings.get("dYes"), Strings.get("dNo") };
                overwrite.setOptions(options);

                JDialog dialog = overwrite.createDialog(null, Strings.get("dConfirm"));
                dialog.setVisible(true);

                if ((overwrite.getValue()) == options[0])
                    return (getSelectedFile());
                else
                    return null;

            } else {
                // if no file extension is given, append one.
                if (!file.getPath().endsWith(".level")) {
                    file = new File(file.getPath() + ".level");
                }
                return file;
            }
        } else {
            return null;
        }
    }

    /**
     * Set file filters for opening or saving, so that they are displayed in the
     * current language.
     * 
     * @param saving
     *            whether the filters should be set for saving files (true) or
     *            opening (false)
     */
    private void setFilters(boolean saving) {
        resetChoosableFileFilters();
        addChoosableFileFilter(new FileNameExtensionFilter(
        		Strings.get("dFileFilterLevel"), "level"));
        if (!saving) {
            addChoosableFileFilter(new FileNameExtensionFilter(
            		Strings.get("dFileFilterSAM"), "SAM"));
        }
        setAcceptAllFileFilterUsed(!saving);
    }
}
