package BuzzGazz.gui;

import javax.swing.*;

public class ButtonImage extends JButton implements ImageObject {

    public ButtonImage(int width, int height, String path) {
        super();
        setContentAreaFilled(false);
        setBorderPainted(false);
        construct(width, height, path);
    }

    public ButtonImage() {
        super();
    }
}