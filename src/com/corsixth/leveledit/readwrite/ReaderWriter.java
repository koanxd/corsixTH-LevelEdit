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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.corsixth.leveledit.params.Params;

/**
 * Reads and writes .level files.
 * 
 * @author Koanxd aka Snowblind
 * 
 */
public interface ReaderWriter {

    /**
     * Opens new.level
     * 
     * @see ReaderWriter#open(File file)
     * 
     * @throws IOException
     */
    public void newFile() throws IOException;

    /**
     * Calls {@link Params#clearNonDefault()} and {@link Params#clearTables()}
     * and then loads the base level.
     * 
     * @throws IOException
     */
    public void reset() throws IOException;

    /**
     * Calls {@link #reset()}, then opens the given file. All readable fields
     * and values are saved in {@link Params}. Comments and structure are saved
     * in this {@link ReaderWriter}.
     * <p>
     * Currently allowed syntax: <br>
     * - Readable lines must start with '%' or '#'<br>
     * - %-params must have a quoted string value <br>
     * - #-params can have a text or integer value <br>
     * - Only #-params can specify additional fields. Field names start with a
     * dot '.', and must appear immediately after the param name or after
     * another field name, without whitespace. <br>
     * - Field values must be separated from the param and field names and from
     * each other by any amount of whitespace (not newlines).
     * 
     * @throws IOException
     */
    public void open(final InputStream file) throws IOException;

    /**
     * @see {@link #open(InputStream)}
     */
	public void open(File file) throws IOException;

    /**
     * Creates a lastFilePath.txt containing a single line with the given
     * file path
     */
    public void saveLastFilePath(String filepath) throws IOException;

    /**
     * Retrieve the last used file path from lastFilePath.txt
     */
    public File getLastFilePath();

    /**
     * Save the file, so that it contains all the changes made by the user and
     * the program.
     * 
     * @param file
     *            the file to be saved
     */
    public void save(File file) throws IOException;
    
    
}
