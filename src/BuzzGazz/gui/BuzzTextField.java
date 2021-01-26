package BuzzGazz.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class BuzzTextField extends JTextField {

    public BuzzTextField() {
        super();
        Color Buzzain = new Color(0, 209, 178);

        setForeground(Buzzain);
        setCaretColor(Buzzain);
        addListeners();
    }

    private void addListeners() {
        addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                setFont(getTextFontForDimension(getSize(), getText()));
            }

            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
        });

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                selectAll();
            }

            public void focusLost(FocusEvent e) {}
        });

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setFont(getTextFontForDimension(getSize(), getText()));
            }
        });
    }

    private Font getTextFontForDimension(Dimension dimension, String text) {
        int max = 250;
        int current = 0;
        Font font;
        int textWidth;
        int textHeight;

        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        do {
            font = new Font("SansSerif", Font.BOLD, current);
            textWidth = (int)(font.getStringBounds(text, frc).getWidth());
            textHeight = (int)(font.getStringBounds(text, frc).getHeight());
            current++;
        } while (current < max && textWidth < dimension.width - 20 && textHeight < dimension.height);
        return new Font("SansSerif", Font.BOLD, current - 1);
    }
}
