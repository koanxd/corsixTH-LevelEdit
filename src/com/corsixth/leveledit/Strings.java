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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;

public class Strings {

	/**
	 * Ensures that resources have been initialized before they are referenced,
	 * but keeps method calls from outside short.
	 */
	private static class Singleton {
		private static Singleton s;

		private List<ResourceBundle> localeBundles = new ArrayList<>();
		private ResourceBundle defaultStrings = ResourceBundle.getBundle("strings", Locale.ROOT);
		private ResourceBundle strings = defaultStrings;
		private List<StringComponent> stringWrappers = new ArrayList<>();

		private Singleton() {
			loadLanguages();
			setLanguage(Locale.getDefault());
		}

		private static Singleton get() {
			if (s == null) {
				s = new Singleton();
			}
			return s;
		}

		/**
		 * load available properties files into a list of resource bundles
		 */
		private void loadLanguages() {
			localeBundles.add(strings); // the base locale

			for (Locale locale : Locale.getAvailableLocales()) {
				ResourceBundle bundle = ResourceBundle.getBundle("strings", locale);
				if (!localeBundles.contains(bundle)) {
					localeBundles.add(bundle);
				}
			}
		}

		private void setLanguage(Locale locale) {
			strings = ResourceBundle.getBundle("strings", locale);
			Locale.setDefault(locale);
			JComponent.setDefaultLocale(locale);
			for (StringComponent guiString : stringWrappers) {
				guiString.update();
			}
		}
	}

	/**
	 * @return the string from the .properties files for the given key and
	 *         current language. <br>
	 *         If the key doesn't exist in the current language, use the default
	 *         language.<br>
	 *         If the key doesn't exist in the default language either, return
	 *         the key itself <br>
	 *         If key == null return "null"
	 */
	public static String get(String key) {
		if (key == null) {
			return "null";
		}
		if (Singleton.get().strings.containsKey(key)) {
			return Singleton.get().strings.getString(key);
		} else if (Singleton.get().defaultStrings.containsKey(key)) {
			return Singleton.get().defaultStrings.getString(key);
		} else
			return key;
	}

	/**
	 * Updates strings for all language-sensitive GUI-components, and changes
	 * the default JVM locale.
	 * 
	 * @throws MissingResourceException
	 *             if no resource bundle for the specified locale can be found.
	 */
	public static void setLanguage(Locale locale) throws MissingResourceException {
		Singleton.get().setLanguage(locale);
	}

	/**
	 * Registers the specified stringWrapper, so that it is updated whenever the
	 * language changes
	 */
	public static void addStringWrapper(StringComponent guiStringWrapper) {
		Singleton.get().stringWrappers.add(guiStringWrapper);
	}

	/**
	 * @return an unmodifiable view of the available language resources
	 */
	public static Collection<ResourceBundle> getLocaleBundles() {
		return Collections.unmodifiableCollection(Singleton.get().localeBundles);
	}

}
