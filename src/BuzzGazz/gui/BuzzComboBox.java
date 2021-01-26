package BuzzGazz.gui;

        import javax.swing.*;
        import java.awt.*;

public class BuzzComboBox extends JComboBox {

    private static final Color Buzzain = new Color(0, 209, 178);
    private static final String[] columns = {"name", "author", "time", "date"};

    public BuzzComboBox() {
        super(columns);
        setForeground(Buzzain);
    }
}
