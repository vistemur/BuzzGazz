package BuzzGazz.gui;

import javax.swing.*;
import java.awt.*;

public class BuzzGazzWindow extends JFrame {

    public BuzzGazzWindow() {
        super("BuzzGazz");
        setSize(750, 500);
        setLocation(300, 200);
        setMinimumSize(new Dimension(470, 410));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public BuzzGazzWindow(JFrame root) {
        super("BuzzGazz");
        setSize(root.getSize());
        setLocation(root.getLocation());
        setMinimumSize(root.getMinimumSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setDimension(JFrame root) {
        setSize(root.getSize());
        setLocation(root.getLocation());
        setMinimumSize(root.getMinimumSize());
    }
}

