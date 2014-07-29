import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class ColorPanel extends JPanel {

    private JLabel label;
    private JPanel colorPanel;

    private String name;
    private Color color;

    public ColorPanel(String name, Color color) {
        this.name = name;
        this.color = color;

        createComponents();
    }

    private void createComponents() {
        createNameLabel();

        createColorLabel();
    }

    private void createNameLabel() {
        label = new JLabel(this.name, SwingConstants.RIGHT);
        this.add(label);
    }

    private void createColorLabel() {
        colorPanel = new JPanel();
        colorPanel.setBackground(this.color);
        colorPanel.addMouseListener(new ColorMouseListener());
        this.add(colorPanel);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    class ColorMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            color = JColorChooser.showDialog(null, "Choose the range color", color);
            JPanel panel = (JPanel)e.getSource();
            panel.setBackground(color);
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
