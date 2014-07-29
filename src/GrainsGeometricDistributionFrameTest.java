import ij.gui.Roi;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JFormattedTextField;

import junit.framework.TestCase;


public class GrainsGeometricDistributionFrameTest extends TestCase {
    static double delta = 1.0e-6;

    GrainsJParameters parameters = new GrainsJParameters(null, null);
    GrainsOutline grainsOutline = new GrainsOutline(parameters);
    GrainsGeometricDistributionFrame frame = new GrainsGeometricDistributionFrame(parameters, grainsOutline);

    protected void setUp() throws Exception {
        super.setUp();
        parameters = new GrainsJParameters(null, null);
        parameters.setDefaultValues();
        grainsOutline = new GrainsOutline(parameters);
        frame = new GrainsGeometricDistributionFrame(parameters, grainsOutline);
    }

    public void testGrainsGeometricDistributionFrame() {
        int numberComponents = 2;
        assertEquals(numberComponents, frame.getComponentCount());
    }

    public void testClose() {
        frame.setVisible(true);
        assertTrue(frame.isVisible());
        frame.closeButton.doClick();
        assertFalse(frame.isVisible());
    }

    public void testNumberColorsSpinner() {
        int numberColors = 6;
        frame.numberColorsSpinner.setValue(numberColors);
        assertEquals(numberColors, frame.getNumberColors());
        assertEquals(numberColors, frame.colorRangesBox.getComponentCount());

        numberColors = 2;
        frame.numberColorsSpinner.setValue(numberColors);
        assertEquals(numberColors, frame.getNumberColors());
        assertEquals(numberColors, frame.colorRangesBox.getComponentCount());
    }


    public void testRangeTypeComboBox() {
        assertEquals(3, frame.rangeTypeComboBox.getItemCount());

        for (TypeOfDiameterRange typeReference : TypeOfDiameterRange.values()){
            frame.rangeTypeComboBox.setSelectedIndex(typeReference.ordinal());
            TypeOfDiameterRange type = frame.getTypeOfDiameterRange();
            assertEquals(typeReference, type);
        }
    }

    public void testRangeChange() {
        RangePanel panel = (RangePanel)frame.colorRangesBox.getComponent(0);
        DiameterRange diameterRange = panel.getDiameterRange();

        assertEquals(0.0, diameterRange.getMinimum(), delta);
        assertEquals(0.0, diameterRange.getMaximum(), delta);

        double value = 123.56;
        double step = 456.636;
        for(Component component : panel.getComponents()) {
            if (component instanceof JFormattedTextField) {
                ((JFormattedTextField) component).setValue(value);
                ((JFormattedTextField) component).postActionEvent();
                value += step;
            }
        }

        diameterRange = null;
        diameterRange = frame.getDiameterRanges().get(0);

        value = 123.56;
        assertEquals(value, diameterRange.getMinimum(), delta);
        assertEquals(value+step, diameterRange.getMaximum(), delta);
    }

    public void testGetValues() {
        RangePanel panel = (RangePanel)frame.colorRangesBox.getComponent(0);
        DiameterRange diameterRange = panel.getDiameterRange();

        assertEquals(0.0, diameterRange.getMinimum(), delta);
        assertEquals(0.0, diameterRange.getMaximum(), delta);

        double value = 123.56;
        double step = 456.636;
        for(Component component : panel.getComponents()) {
            if (component instanceof JFormattedTextField) {
                ((JFormattedTextField) component).setValue(value);
                value += step;
            }
        }

        frame.getValues();
        diameterRange = null;
        diameterRange = frame.getDiameterRanges().get(0);

        value = 123.56;
        assertEquals(value, diameterRange.getMinimum(), delta);
        assertEquals(value+step, diameterRange.getMaximum(), delta);
    }

}
