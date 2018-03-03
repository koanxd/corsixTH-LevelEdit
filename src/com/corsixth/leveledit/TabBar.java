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

import java.awt.Component;
import java.awt.Container;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JTabbedPane;

/**
 * A JTabbedPane and all actual tabs
 * 
 * @author Koanxd
 *
 */
@SuppressWarnings("serial")
public class TabBar extends JTabbedPane {

    private Map<String, Tab> tabs = new LinkedHashMap<String, Tab>();

    public TabBar() {

        tabs.put("lbTabGeneral", new Tab_General());
        tabs.put("lbTabResearch", new Tab_Research());
        tabs.put("lbTabEpidemics", new Tab_Epidemics());
        tabs.put("lbTabDiseases", new Tab_Diseases());
        tabs.put("lbTabObjects", new Tab_Objects());
        tabs.put("lbTabStaff", new Tab_Staff());
        tabs.put("lbTabEmergencies", new Tab_Emergencies());
        tabs.put("lbTabEarthquakes", new Tab_Earthquakes());
        tabs.put("lbTabPopulation", new Tab_Population());
        tabs.put("lbTabAwards", new Tab_Awards());
        tabs.put("lbTabGoals", new Tab_Goals());
        tabs.put("lbTabCompetitors", new Tab_Competitors());

        for (String key : tabs.keySet()) {
            addTab(key, tabs.get(key));
        }
    }
    
    public Map<String, Tab> getTabsByKey(){
        return Collections.unmodifiableMap(tabs);
    }
    
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        for (Tab tab : tabs.values()){
            setEnabledRecursive(tab, enabled);
        }
    }
    
    private void setEnabledRecursive(Container container, boolean enabled){
        for (Component comp : container.getComponents()){
            comp.setEnabled(enabled);
            if (comp instanceof Container){
                setEnabledRecursive((Container) comp, enabled);
            }
        }        
    }
}
