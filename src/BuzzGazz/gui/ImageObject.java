package BuzzGazz.gui;

        import javax.imageio.ImageIO;
        import javax.swing.*;
        import java.awt.*;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;

interface ImageObject {

    void setSize(int width, int height);
    void setIcon(Icon icon);

    default void construct(int width, int height, String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Icon icon = new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(width, height);
    }
}