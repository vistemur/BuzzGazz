package BuzzGazz.gui;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class BuzzTable extends JTable {

    private static final Color Buzzain = new Color(0, 209, 178);

    public BuzzTable(TableModel tableModel) {
        super(tableModel);
        setForeground(Buzzain);
        setGridColor(Buzzain);
        getTableHeader().setForeground(Buzzain);
        setSelectionBackground(Color.pink);
    }
}
