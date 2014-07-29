import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JFormattedTextField;

import junit.framework.TestCase;


public class RangePanelTest extends TestCase {
    static private double delta = 1.0-6;

    public void testRangePanel() {
        DiameterRange diameterRange = new DiameterRange(0, 0, 0, "nm", null);
        RangePanel panel = new RangePanel(diameterRange);

        assertEquals(6, panel.getComponentCount());

        assertEquals(diameterRange, panel.getDiameterRange());
    }

    public void testChangeMinimum() throws Exception {
        DiameterRange diameterRange = new DiameterRange(0, 0, 0, "nm", null);
        RangePanel panel = new RangePanel(diameterRange);

        JFormattedTextField minimumText = (JFormattedTextField)panel.getComponent(1);
        double expectedValue = 123.025;
        minimumText.setValue(expectedValue);
        minimumText.postActionEvent();

        diameterRange = null;
        diameterRange = panel.getDiameterRange();
        assertEquals(expectedValue, diameterRange.getMinimum(), delta);
    }

    public void testChangeMaximum() throws Exception {
        DiameterRange diameterRange = new DiameterRange(0, 0, 0, "nm", null);
        RangePanel panel = new RangePanel(diameterRange);

        JFormattedTextField maximumText = (JFormattedTextField)panel.getComponent(3);
        double expectedValue = 1234.465;
        maximumText.setValue(expectedValue);
        maximumText.postActionEvent();

        diameterRange = null;
        diameterRange = panel.getDiameterRange();
        assertEquals(expectedValue, diameterRange.getMaximum(), delta);
    }
}
