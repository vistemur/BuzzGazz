package BuzzGazz.gui;

import javax.swing.*;
import java.awt.*;

public class BuzzLabel extends JLabel {

    protected static Color Buzzain = new Color(0, 209, 178);
    protected static Font Buzzont = new Font("SansSerif", Font.BOLD, 30);
    protected static Font BuzzontJr = new Font("SansSerif", Font.BOLD, 15);

    public BuzzLabel() {
        super("");
        init();
    }

    public BuzzLabel(String text) {
        super(text);
        init();
    }

    public void setSmallFont() {
        setFont(BuzzontJr);
    }

    private void init() {
        setFont(Buzzont);
        setForeground(Buzzain);
    }
}
