/**
 * Returns a GridBagConstraints object with values set to user-determined values 
 * or the default values if not explicitly set by the user.
 */

package alarmclock;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GridBagConstraintsBuilder {

    GridBagConstraints c;
    
    public GridBagConstraintsBuilder() {
        c = new GridBagConstraints();
    }
    
    public GridBagConstraintsBuilder gridx(int x) {
        c.gridx = x;
        return this;
    }
    
    public GridBagConstraintsBuilder gridy(int y) {
        c.gridy = y;
        return this;
    }
    
    public GridBagConstraintsBuilder gridwidth(int x) {
        c.gridwidth = x;
        return this;
    }
    
    public GridBagConstraintsBuilder gridheight(int y) {
        c.gridheight = y;
        return this;
    }
    
    public GridBagConstraintsBuilder weightx(double x) {
        c.weightx = x;
        return this;
    }
    
    public GridBagConstraintsBuilder weighty(double y) {
        c.weighty = y;
        return this;
    }
    
    public GridBagConstraintsBuilder anchor(int x) {
        c.anchor = x;
        return this;
    }
    
    public GridBagConstraintsBuilder fill(int x) {
        c.fill = x;
        return this;
    }
    
    public GridBagConstraintsBuilder insets(Insets x) {
        c.insets = x;
        return this;
    }
    
    public GridBagConstraintsBuilder ipadx(int x) {
        c.ipadx = x;
        return this;
    }
    
    public GridBagConstraintsBuilder ipady(int y) {
        c.ipady = y;
        return this;
    }
    
    public GridBagConstraints build() {
        return c;
    }
}
