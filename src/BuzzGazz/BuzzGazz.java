package BuzzGazz;

import BuzzGazz.exceptions.NoContentException;
import BuzzGazz.gui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.BorderFactory.createEmptyBorder;

public class BuzzGazz extends BuzzWindow {

    private JPanel topRowPanel;
    private JPanel rightColumnPanel;
    private JPanel lowLeftBlockPanel;
    private JPanel searchPanel;
    private JPanel logoPanel;
    private JPanel plusMinusPanel;
    private JPanel editPanel;
    private JPanel spacerPanel;

    private IconLabel logo;

    private ButtonImage searchButton;
    private ButtonImage addSessionButton;
    private ButtonImage deleteSessionButton;
    private ButtonImage editFilmsButton;
    private ButtonImage editSessionButton;

    private BuzzTextField searchTextField;
    private BuzzComboBox searchParameterComboBox;

    private DefaultTableModel tableModel;
    private BuzzTable sessionsTable;
    private JScrollPane sessionsScrollTable;

    public BuzzGazz() {
        super();
        logger.info("Program opened");
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
        searchTextField.setPreferredSize(new Dimension(190, 50));

        searchParameterComboBox = new BuzzComboBox();
        searchParameterComboBox.setPreferredSize(new Dimension(100, 50));

        searchButton = new ButtonImage(60, 60, "./img/search.png");

        logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 400, 60));

        logo = new IconLabel(60, 60, "./img/BuzzGazz.png");
        logo.setToolTipText("created by Roman Pozdnyakov");

        rightColumnPanel = new JPanel();
        rightColumnPanel.setPreferredSize(new Dimension(80, buzzGazzWindow.getHeight()));

        plusMinusPanel = new JPanel();
        plusMinusPanel.setLayout(new BoxLayout(plusMinusPanel, BoxLayout.Y_AXIS));
        plusMinusPanel.setPreferredSize(new Dimension(60, 145));

        addSessionButton = new ButtonImage(60, 60, "./img/add.png");

        deleteSessionButton = new ButtonImage(60, 60, "./img/delete.png");

        spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(60, buzzGazzWindow.getHeight() - 420));

        editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
        editPanel.setPreferredSize(new Dimension(60, 145));

        editFilmsButton = new ButtonImage(60, 60, "./img/films.png");

        editSessionButton = new ButtonImage(60, 60, "./img/pencil.png");

        lowLeftBlockPanel = new JPanel();
        int lowLeftBlockPreferredWidth = buzzGazzWindow.getWidth() - 80;
        int lowLeftBlockPreferredHeight = buzzGazzWindow.getHeight() - 80;
        lowLeftBlockPanel.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth, lowLeftBlockPreferredHeight));

        tableModel = new DefaultTableModel(sessions.getData(), columns);
        sessionsTable = new BuzzTable(tableModel);
        sessionsScrollTable = new JScrollPane(sessionsTable);
        sessionsScrollTable.setBorder(createEmptyBorder());
        sessionsScrollTable.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth - 100,
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
        topRowPanel.add(searchPanel);
        searchPanel.add(searchTextField);
        searchPanel.add(searchParameterComboBox);
        topRowPanel.add(logoPanel);
        topRowPanel.add(logo);
        logoPanel.add(searchButton, BorderLayout.LINE_START);
        buzzGazzWindow.add(rightColumnPanel, BorderLayout.LINE_END);
        rightColumnPanel.add(plusMinusPanel, BorderLayout.PAGE_START);
        plusMinusPanel.add(addSessionButton);
        plusMinusPanel.add(deleteSessionButton);
        rightColumnPanel.add(spacerPanel, BorderLayout.LINE_START);
        rightColumnPanel.add(editPanel, BorderLayout.PAGE_END);
        editPanel.add(editFilmsButton);
        editPanel.add(editSessionButton);
        buzzGazzWindow.add(lowLeftBlockPanel, BorderLayout.LINE_START);
        lowLeftBlockPanel.add(sessionsScrollTable);
    }

    @Override
    protected void setActions() {
        searchButton.addActionListener(e -> {
            FindSessionsWindow findSessionsWindow = new FindSessionsWindow(buzzWindow);
            buzzGazzWindow.setVisible(false);
            findSessionsWindow.show();
        });

        addSessionButton.addActionListener(e -> navigateTo(new AddSessionWindow(buzzWindow)));

        deleteSessionButton.addActionListener(e -> {
            try {
                sessions.remove(sessionsTable.getSelectedRows(),
                                searchParameterComboBox.getSelectedIndex(),
                                searchTextField.getText());
                find();
            } catch (NoContentException error) {
                JOptionPane.showMessageDialog(sessionsTable, error.getMessage());
            }
        });

        editFilmsButton.addActionListener(e -> navigateTo(new FilmsWindow(buzzWindow)));

        editSessionButton.addActionListener(e -> {
            try {
                EditSessionWindow editSessionWindow =
                        new EditSessionWindow(buzzWindow, sessions.getSession(sessionsTable.getSelectedRows(),
                                                        searchParameterComboBox.getSelectedIndex(),
                                                        searchTextField.getText()));
                editSessionWindow.show();
                buzzGazzWindow.setVisible(false);
            }  catch (NoContentException error) {
                JOptionPane.showMessageDialog(sessionsTable, error.getMessage());
            }
        });

        searchTextField.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                find();
            }

            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
        });

        searchParameterComboBox.addActionListener(e -> find());
    }

    public void find() {
        switch (searchParameterComboBox.getSelectedIndex()) {
            case 0:
                tableModel.setDataVector(sessions.getDataByFilmName(searchTextField.getText()), columns);
                break;
            case 1:
                tableModel.setDataVector(sessions.getDataByFilmAuthor(searchTextField.getText()), columns);
                break;
            case 2:
                tableModel.setDataVector(sessions.getDataByTime(searchTextField.getText()), columns);
                break;
            case 3:
                tableModel.setDataVector(sessions.getDataByDate(searchTextField.getText()), columns);
                break;
        }
    }

    @Override
    protected void resizeLayout() {
        int lowLeftBlockPreferredWidth = buzzGazzWindow.getWidth() - 80;
        int lowLeftBlockPreferredHeight = buzzGazzWindow.getHeight() - 80;
        lowLeftBlockPanel.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth, lowLeftBlockPreferredHeight));
        sessionsScrollTable.setPreferredSize(new Dimension(lowLeftBlockPreferredWidth - 35,
                                                           lowLeftBlockPreferredHeight - 45));
        spacerPanel.setPreferredSize(new Dimension(60, buzzGazzWindow.getHeight() - 420));
        logoPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 400, 60));
    }

    @Override
    protected void refresh() {
        find();
    }
}