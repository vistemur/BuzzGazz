package BuzzGazz.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeLabelFont extends JLabel {
    public static final int MIN_FONT_SIZE=1;
    public static final int MAX_FONT_SIZE=250;
    Graphics g;

    public ResizeLabelFont() {
        super("", SwingConstants.CENTER);
        init();
    }

    public ResizeLabelFont(String text) {
        super(text, SwingConstants.CENTER);
        init();
    }

    public void resize() {
        adaptLabelFont(ResizeLabelFont.this);
    }

    protected void init() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adaptLabelFont(ResizeLabelFont.this);
            }
        });
    }

    protected void adaptLabelFont(JLabel l) {
        if (g == null) {
            return;
        }
        Rectangle r = l.getBounds();
        int fontSize = MIN_FONT_SIZE;
        Font f = l.getFont();

        Rectangle r1 = new Rectangle();
        Rectangle r2 = new Rectangle();
        while (fontSize < MAX_FONT_SIZE) {
            r1.setSize(getTextSize(l, f.deriveFont(f.getStyle(), fontSize)));
            r2.setSize(getTextSize(l, f.deriveFont(f.getStyle(),fontSize + 1)));
            if ((r.width > r1.width && r.height > r1.height) && !(r.width > r2.width && r.height > r2.height)) {
                break;
            }
            fontSize++;
        }

        setFont(f.deriveFont(f.getStyle(), fontSize));
        repaint();
    }

    private Dimension getTextSize(JLabel l, Font f) {
        Dimension size = new Dimension();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        size.width = fm.stringWidth(l.getText());
        size.height = fm.getHeight();

        return size;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;
        resize();
    }
}