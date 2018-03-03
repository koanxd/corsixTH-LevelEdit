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

package com.corsixth.leveledit.readwrite;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.corsixth.leveledit.params.Params;

/**
 * Reads and writes .level files.
 * 
 * @author Koanxd aka Snowblind
 * 
 */
public class ReaderWriterImpl implements ReaderWriter {
    private Params params;

	Toolkit toolkit = Toolkit.getDefaultToolkit();
    private InputStream currentFile;

    /**
     * Contains the whole file which was last read
     */
    private List<Line> lines = new ArrayList<>(600);
    private Set<String> tableEntries = new HashSet<>();

    // the last appearance of each table is saved so that added table
    // entries can be inserted at the correct position.
    private Map<List<String>, Line> lastAppearanceOfTable = new HashMap<>();

    private List<Line> baseParamsHeader = new ArrayList<>();
    
    private InputStream getNewLevel(){
    	return this.getClass().getResourceAsStream("/new.level");
    }
    
    private InputStream getBaseLevel(){
    	return this.getClass().getResourceAsStream("/base.level");
    }

    private class Line {
        public String param;
        public Collection<String> fields = new HashSet<String>();
        public String comment = new String("");

        public Line() {
        }

        public Line(String lineComment) {
            comment = lineComment;
        }
    }

    /**
     * Initialize a ReaderWriter by loading params and fields from base.level
     * into {@link Params}, and then loading new.level
     * 
     * @throws IOException
     */
    public ReaderWriterImpl(Params params) throws IOException {
        this.params = params;
        readFile(getBaseLevel());
        newFile();

        baseParamsHeader.add(new Line(""));
        baseParamsHeader.add(new Line(
                "----------------------- Misc. -------------------"));
        baseParamsHeader.add(new Line(""));
    }

    @Override
    public void newFile() throws IOException {
        open(getNewLevel());
    }

    @Override
    public void reset() throws IOException {
        params.clearNonDefault();
        params.clearTables();
        readFile(getBaseLevel());
        lines.clear();
        tableEntries.clear();
    }

    @Override
    public void open(final InputStream file) throws IOException {
        reset();
        readFile(file);
    }


    @Override
    public void open(final File file) throws IOException {
        open (new FileInputStream(file));
    }

    /**
     * Calls {@link ReaderWriterImpl#processLine(String)} for every line in the
     * file.
     */
    private void readFile(final InputStream file) throws IOException {
        currentFile = file;
        try (BufferedReader read = new BufferedReader(new InputStreamReader(file))) {
            String lineText;
            while ((lineText = read.readLine()) != null) {
                processLine(lineText);
            }
        }
    }

    /**
     * Responsible for saving a line in {@link lines} and saving the fields and
     * values at this line in {@link Params}.
     */
    private void processLine(String lineText) {

        Line lineObject = new Line();
        lines.add(lineObject);

        if (processComment(lineText, lineObject))
            return;

        List<String> words = getSplit(lineText, "[\\.\\s+]");
        List<String> spaceSplit = getSplit(lineText, "\\s+");

        boolean isPercent = processPercentParam(lineText, lineObject, words);

        int fieldCount = processHashParam(lineText, lineObject, words,
                spaceSplit);

        // save the end of each line as comment,
        // starting with the first word after the last value.
        if (isPercent) {
            List<String> quoteSplit = getSplit(lineText, "\"");
            if (quoteSplit.size() > 2) {
                for (int i = 2; i < quoteSplit.size(); i++) {
                    lineObject.comment += " " + getSplit(lineText, "\"").get(i);
                }
            }
        } else {
            int valueCount = (fieldCount > 0) ? fieldCount : 1;
            for (int i = 1 + valueCount; i < spaceSplit.size(); i++) {
                lineObject.comment += " " + spaceSplit.get(i);
            }
        }
    }

    /**
     * Check lineText for a full line comment. If found, save it in lineObject.
     * 
     * @return true if line starts with a comment
     */
    private boolean processComment(String lineText, Line lineObject) {
        if (!hasHashParam(lineText) && !hasPercentParam(lineText)) {
            lineObject.comment = lineText;
            return true;
        } else
            return false;
    }

    /**
     * Check line for % param. If one was found, save the value (currently
     * assuming that values for % params are always quoted strings)
     * 
     * @return true if line starts with %
     */
    private boolean processPercentParam(String lineText, Line lineObject,
            List<String> words) {

        if (!hasPercentParam(lineText))
            return false;

        List<String> quoteSplit = getSplit(lineText, "\"");
        if (quoteSplit.size() >= 2) {
            readValue(lineObject, words.get(0), null, quoteSplit.get(1));
        }
        return true;
    }

    /**
     * Check line for # param. If one was found, save the values for each
     * field.
     * 
     * @return number of fields at this line
     */
    private int processHashParam(String lineText, Line lineObject,
            List<String> words, List<String> spaceSplit) {

        if (!hasHashParam(lineText))
            return 0;

        // get fields by splitting at dots,
        // excluding the param name and any text after whitespace.
        int fieldCount = spaceSplit.get(0).split("\\.").length - 1;

        if (fieldCount == 0) {
            readValue(lineObject, words.get(0), null, words.get(1));
        } else {
            for (int i = 0; i < fieldCount; i++) {
                int fieldIndex = 1 + i;
                int valueIndex = 1 + fieldCount + i;

                if (valueIndex >= words.size()) {
                    // there are missing values on this line;
                    // ignore the remaining fields.
                    break;
                }

                readValue(lineObject, words.get(0), words.get(fieldIndex),
                        words.get(valueIndex));
            }
        }
        return fieldCount;
    }

    /**
     * Load the specified param and field into the file cache and assign the
     * given value to it. params and fields encountered for the first time are
     * newly created.
     */
    private void readValue(Line line, String paramName, String fieldName,
            String value) {
        // create new param if it doesn't exist yet.
        if (!params.contains(paramName)) {
            params.add(paramName);
        }
        if (currentFile == getNewLevel()) {
            params.setAsDefault(paramName);
        }

        // create new field if it doesn't exist yet.
        if (fieldName != null && !params.hasField(paramName, fieldName)) {
            if (currentFile == getNewLevel()) {
                params.addDefaultField(paramName, fieldName, value);
            } else {
                params.addField(paramName, fieldName, value);
            }
        }

        if (fieldName == null) {
            params.setValue(paramName, value);
        } else {
            params.setValue(paramName, fieldName, value);
        }

        // save to file cache
        if (line.param == null) {
            line.param = paramName;
        }
        if (fieldName != null) {
            line.fields.add(fieldName);
        }
        List<String> table = params.getTable(Params.getBaseName(paramName));
        if (table != null) {
            lastAppearanceOfTable.put(table, line);
            tableEntries.add(paramName);
        }
    }

    @Override
    public void saveLastFilePath(String filepath) throws IOException {
        File file = new File("lastFilePath.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(filepath);
        }
    }

    /**
     * Retrieve the last used file path from lastFilePath.txt
     */
    public File getLastFilePath() {
        File returnFile = null;
        try {
            if (new File("lastFilePath.txt").exists()) {
                BufferedReader read = new BufferedReader(new FileReader(
                        "lastFilePath.txt"));
                returnFile = new File(read.readLine());
                read.close();
            }
        } catch (IOException e) {
            return new File(System.getProperty("user.home"));
        }
        return returnFile;
    }

    /**
     * Save the file, so that it contains all the changes made by the user and
     * the program.
     * 
     * @param file
     *            the file to be saved
     */
    public void save(File file) throws IOException {
        if (!lines.containsAll(baseParamsHeader)) {
            lines.addAll(baseParamsHeader);
        }
        updateCache();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Line line : lines) {
                if (line.param == null) {
                    writer.write(line.comment);
                    writer.newLine();
                    continue;
                }
                writer.write(line.param);
                if (line.fields.isEmpty()) {
                    String value = params.getValue(line.param);
                    if (line.param.charAt(0) == '%') {
                        writer.write(" = \"" + value + "\"");
                    } else {
                        writer.write(" " + value);
                    }
                }
                for (String field : line.fields) {
                    writer.write("." + field);
                }
                for (String field : line.fields) {
                    writer.write(" " + params.getValue(line.param, field));
                }
                writer.write(line.comment);
                writer.newLine();
            }
        }
    }

    /**
     * Update the file cache with entries removed and added by the user.
     */
    private void updateCache() {
        for (int i = 0; i < lines.size(); i++) {
            String param = lines.get(i).param;
            if (param == null)
                continue;

            // remove lines containing a removed param.
            if (!params.contains(param)) {
                lines.remove(i);
                List<String> table = params.getTable(Params
                        .getBaseName(param));
                lastAppearanceOfTable.put(table,
                        (i == 0) ? null : lines.get(i - 1));
            }
        }

        // insert new line for each table entry added by the user.
        for (List<String> table : params.getAllTableLists().values()) {
            Line lastAppearance = lastAppearanceOfTable.getOrDefault(table,
                    null);
            for (String param : table) {
                if (tableEntries.contains(param)) {
                    continue;
                }
                Line newLine = new Line();
                newLine.param = param;
                newLine.fields = params.getFieldNames(param);
                int index = lines.indexOf(lastAppearance);
                if (index == -1) {
                    lines.add(newLine);
                } else {
                    lines.add(index, newLine);
                }
            }
        }
    }

    /**
     * @return a list containing the results of {@code lineText.split(regex)},
     *         excluding empty strings.
     */
    private List<String> getSplit(String lineText, String regex) {
        List<String> split = new ArrayList<>();
        for (String word : lineText.split(regex)) {
            if (!word.equals(""))
                split.add(word);
        }
        return split;
    }

    /**
     * @return true if line has a param starting with %
     */
    private boolean hasPercentParam(String lineText) {
        if (lineText.length() == 0)
            return false;
        return lineText.charAt(0) == '%';
    }

    /**
     * @return true if line has a param starting with #
     */
    private boolean hasHashParam(String lineText) {
        if (lineText.length() == 0)
            return false;
        return lineText.charAt(0) == '#';
    }
}