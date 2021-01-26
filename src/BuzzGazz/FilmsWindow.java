package BuzzGazz;

import BuzzGazz.exceptions.NoContentException;
import BuzzGazz.gui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.BorderFactory.createEmptyBorder;

public class FilmsWindow extends BuzzWindow {

    private JPanel topRowPanel;
    private JPanel rightColumnPanel;
    private JPanel lowLeftBlockPanel;
    private JPanel searchPanel;
    private JPanel logoPanel;
    private JPanel plusMinusPanel;
    private JPanel editPanel;
    private JPanel spacerPanel;

    private ButtonImage backButton;
    private ButtonImage searchButton;
    private ButtonImage addButton;
    private ButtonImage deleteButton;
    private ButtonImage editButton;

    private BuzzTextField searchTextField;
    private BuzzComboBox searchParameterComboBox;

    private DefaultTableModel tableModel;
    private BuzzTable filmsTable;
    private JScrollPane filmsScrollTable;

    public FilmsWindow(BuzzWindow backWindow) {
        super(backWindow);
    }

    @Override
    protected void initialize() {
        topRowPanel = new JPanel();
        topRowPanel.setLayout(new FlowLayout());
        topRowPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth(), 80));

        searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setPreferredSize(new Dimension(300, 60));

        searchTextField = new BuzzTextField();

        searchParameterComboBox = new BuzzComboBox();
        searchParameterComboBox.setPreferredSize(new Dimension(100, 50));

        searchButton = new ButtonImage(60, 60, "./img/search.png");
        backButton = new ButtonImage(60, 60, "./img/back.png");

        logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 400, 60));

        rightColumnPanel = new JPanel();
        rightColumnPanel.setPreferredSize(new Dimension(80, buzzGazzWindow.getHeight()));

        plusMinusPanel = new JPanel();
        plusMinusPanel.setLayout(new BoxLayout(plusMinusPanel, BoxLayout.Y_AXIS));
        plusMinusPanel.setPreferredSize(new Dimension(60, 145));

        addButton = new ButtonImage(60, 60, "./img/add.png");

        deleteButton = new ButtonImage(60, 60, "./img/delete.png");

        spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(60, buzzGazzWindow.getHeight() - 420));

        editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setPreferredSize(new Dimension(60, 145));

        editButton = new ButtonImage(60, 60, "./img/pencil.png");

        lowLeftBlockPanel = new JPanel();
        int lowLeftBlockPreferredWidth = buzzGazzWindow.getWidth() - 80;
        int lowLeftBlockPreferredHeight = buzzGazzWindow.getHeight() - 80;
        lowLeftBlockPanel.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth, lowLeftBlockPreferredHeight));

        tableModel = new DefaultTableModel(new String[0][0], columns);
        filmsTable = new BuzzTable(tableModel);
        filmsScrollTable = new JScrollPane(filmsTable);
        filmsScrollTable.setBorder(createEmptyBorder());
        filmsScrollTable.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth - 100,
                lowLeftBlockPreferredHeight - 45));
    }

    @Override
    protected void showColoredLayout() {
        topRowPanel.setBackground(Color.BLUE);
        searchPanel.setBackground(Color.MAGENTA);
        logoPanel.setBackground(Color.PINK);
        rightColumnPanel.setBackground(Color.RED);
        plusMinusPanel.setBackground(Color.ORANGE);
        spacerPanel.setBackground(Color.CYAN);
        editPanel.setBackground(Color.ORANGE);
        lowLeftBlockPanel.setBackground(Color.GRAY);
    }

    @Override
    protected void layout() {
        buzzGazzWindow.add(topRowPanel, BorderLayout.PAGE_START);
        topRowPanel.add(backButton);
        topRowPanel.add(searchPanel);
        searchPanel.add(searchTextField);
        searchPanel.add(searchParameterComboBox);
        topRowPanel.add(logoPanel);
        logoPanel.add(searchButton, BorderLayout.LINE_START);
        buzzGazzWindow.add(rightColumnPanel, BorderLayout.LINE_END);
        rightColumnPanel.add(plusMinusPanel, BorderLayout.PAGE_START);
        plusMinusPanel.add(addButton);
        plusMinusPanel.add(deleteButton);
        rightColumnPanel.add(spacerPanel, BorderLayout.LINE_START);
        rightColumnPanel.add(editPanel, BorderLayout.PAGE_END);
        editPanel.add(editButton);
        buzzGazzWindow.add(lowLeftBlockPanel, BorderLayout.LINE_START);
        lowLeftBlockPanel.add(filmsScrollTable);
    }

    @Override
    protected void resizeLayout() {
        int lowLeftBlockPreferredWidth = buzzGazzWindow.getWidth() - 80;
        int lowLeftBlockPreferredHeight = buzzGazzWindow.getHeight() - 100;
        lowLeftBlockPanel.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth, lowLeftBlockPreferredHeight));
        filmsScrollTable.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth - 35,
                lowLeftBlockPreferredHeight - 25));
        spacerPanel.setPreferredSize(new Dimension(60, buzzGazzWindow.getHeight() - 340));
        logoPanel.setPreferredSize(new Dimension(70, 60));
        searchPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 170, 50));
        searchTextField.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 300, 50));
    }

    @Override
    protected void setActions() {
        backButton.addActionListener(e -> back());

        searchTextField.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                find();
            }

            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
        });

        deleteButton.addActionListener(e -> {
            try {
                films.remove(   filmsTable.getSelectedRows(),
                                searchParameterComboBox.getSelectedIndex(),
                                searchTextField.getText(),
                                sessions);
                find();
            } catch (NoContentException error) {
                JOptionPane.showMessageDialog(filmsTable, error.getMessage());
            }
        });

        editButton.addActionListener(e -> {
            try {
                navigateTo(new EditFilmWindow(  buzzWindow, films.getFilm(  filmsTable.getSelectedRows(),
                                                                            searchParameterComboBox.getSelectedIndex(),
                                                                            searchTextField.getText())));
            }  catch (NoContentException error) {
                JOptionPane.showMessageDialog(filmsTable, error.getMessage());
            }
        });

        searchParameterComboBox.addActionListener(e -> find());
        searchButton.addActionListener(e -> navigateTo(new FindFilmsWindow(buzzWindow)));
        addButton.addActionListener(e -> navigateTo(new AddFilmWindow(buzzWindow)));
    }

    @Override
    protected void refresh() {
        find();
    }

    private void find() {
        tableModel.setDataVector(films.getDataByParam(searchParameterComboBox.getSelectedIndex(), searchTextField.getText()), columns);
    }
}
