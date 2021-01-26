package BuzzGazz;

import BuzzGazz.exceptions.NoContentException;
import BuzzGazz.gui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.BorderFactory.createEmptyBorder;

public class FindFilmsWindow extends BuzzWindow {

    private JPanel topPanel;
    private JPanel[] topPanels;
    private JPanel centerPanel;
    private JPanel[] centerPanels;
    private JPanel timeFromToPanel;
    private JPanel timeFromPanel;
    private JPanel timeToPanel;
    private JPanel dateFromToPanel;
    private JPanel dateFromPanel;
    private JPanel dateToPanel;
    private JPanel lowPanel;

    private ButtonImage backButton;
    private ButtonImage deleteButton;
    private ButtonImage addButton;
    private ButtonImage editButton;

    private BuzzLabel nameLabel;
    private BuzzLabel authorLabel;
    private BuzzLabel timeLabel;
    private BuzzLabel timeFromLabel;
    private BuzzLabel timeToLabel;
    private BuzzLabel dateLabel;
    private BuzzLabel dateFromLabel;
    private BuzzLabel dateToLabel;

    private BuzzTextField nameTextField;
    private BuzzTextField authorTextField;
    private BuzzTextField timeFromTextField;
    private BuzzTextField timeToTextField;
    private BuzzTextField dateFromTextField;
    private BuzzTextField dateToTextField;

    private DefaultTableModel tableModel;
    private BuzzTable filmsTable;
    private JScrollPane filmsScrollTable;

    public FindFilmsWindow(BuzzWindow backWindow) {
        super(backWindow);
    }

    @Override
    protected void initialize() {
        backButton = new ButtonImage(60, 60, "./img/back.png");
        deleteButton = new ButtonImage(60, 60, "./img/delete.png");
        addButton = new ButtonImage(60, 60, "./img/add.png");
        editButton = new ButtonImage(60, 60, "./img/pencil.png");

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 4));
        topPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth(), 80));
        topPanels = new JPanel[4];
        for (int c = 0; c < 4; c++)
            topPanels[c] = new JPanel();
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 4));
        centerPanels = new JPanel[4];
        for (int c = 0; c < 4; c++)
            centerPanels[c] = new JPanel();
        timeFromToPanel = new JPanel();
        timeFromToPanel.setLayout(new BoxLayout(timeFromToPanel, BoxLayout.Y_AXIS));
        timeFromPanel = new JPanel();
        timeToPanel = new JPanel();
        dateFromToPanel = new JPanel();
        dateFromToPanel.setLayout(new BoxLayout(dateFromToPanel, BoxLayout.Y_AXIS));
        dateFromPanel = new JPanel();
        dateToPanel = new JPanel();
        lowPanel = new JPanel();

        nameLabel = new BuzzLabel("Name");
        authorLabel = new BuzzLabel("Author");
        timeLabel = new BuzzLabel("Length");
        timeFromLabel = new BuzzLabel("From:");
        timeFromLabel.setSmallFont();
        timeToLabel = new BuzzLabel("To:");
        timeToLabel.setSmallFont();
        dateLabel = new BuzzLabel("Creation date");
        dateLabel.setSmallFont();
        dateFromLabel = new BuzzLabel("From:");
        dateFromLabel.setSmallFont();
        dateToLabel = new BuzzLabel("To:");
        dateToLabel.setSmallFont();

        nameTextField = new BuzzTextField();
        authorTextField = new BuzzTextField();
        timeFromTextField = new BuzzTextField();
        timeToTextField = new BuzzTextField();
        dateFromTextField = new BuzzTextField();
        dateToTextField = new BuzzTextField();

        tableModel = new DefaultTableModel(new String[0][0], columns);
        filmsTable = new BuzzTable(tableModel);
        filmsScrollTable = new JScrollPane(filmsTable);
        filmsScrollTable.setBorder(createEmptyBorder());
    }

    @Override
    protected void refresh() {
        find();
    }

    @Override
    protected void resizeLayout() {
        int fourthWindow = buzzGazzWindow.getWidth() / 4 - 20;

        lowPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth(), buzzGazzWindow.getHeight() - 300));
        nameTextField.setPreferredSize(new Dimension(fourthWindow, 50));
        authorTextField.setPreferredSize(new Dimension(fourthWindow, 50));
        timeFromToPanel.setPreferredSize(new Dimension(fourthWindow, 140));
        timeFromTextField.setPreferredSize(new Dimension(fourthWindow - 60, 30));
        timeToTextField.setPreferredSize(new Dimension(fourthWindow - 40, 30));
        dateFromToPanel.setPreferredSize(new Dimension(fourthWindow, 140));
        dateFromTextField.setPreferredSize(new Dimension(fourthWindow - 60, 30));
        dateToTextField.setPreferredSize(new Dimension(fourthWindow - 40, 30));
        filmsScrollTable.setPreferredSize(new Dimension( buzzGazzWindow.getWidth() - 20,
                buzzGazzWindow.getHeight() - 320));
    }

    @Override
    protected void showColoredLayout() {
        topPanel.setBackground(Color.YELLOW);
        topPanels[0].setBackground(Color.PINK);
        topPanels[1].setBackground(Color.MAGENTA);
        topPanels[2].setBackground(Color.GREEN);
        topPanels[3].setBackground(Color.BLUE);
        centerPanel.setBackground(Color.CYAN);
        centerPanels[0].setBackground(Color.BLUE);
        centerPanels[1].setBackground(Color.GREEN);
        centerPanels[2].setBackground(Color.MAGENTA);
        centerPanels[3].setBackground(Color.PINK);
        timeFromToPanel.setBackground(Color.black);
        timeFromPanel.setBackground(Color.red);
        timeToPanel.setBackground(Color.blue);
        dateFromToPanel.setBackground(Color.RED);
        dateFromPanel.setBackground(Color.green);
        dateToPanel.setBackground(Color.gray);
        lowPanel.setBackground(Color.ORANGE);
    }

    @Override
    protected void layout() {
        topPanels[0].add(backButton);
        topPanels[1].add(deleteButton);
        topPanels[2].add(addButton);
        topPanels[3].add(editButton);
        for (JPanel topPanelSegment : topPanels)
            topPanel.add(topPanelSegment);
        for (JPanel centerPanelSegment : centerPanels)
            centerPanel.add(centerPanelSegment);
        centerPanels[0].add(nameLabel, BorderLayout.PAGE_START);
        centerPanels[0].add(nameTextField, BorderLayout.CENTER);
        centerPanels[1].add(authorLabel, BorderLayout.PAGE_START);
        centerPanels[1].add(authorTextField, BorderLayout.CENTER);
        centerPanels[2].add(timeLabel, BorderLayout.PAGE_START);
        centerPanels[2].add(timeFromToPanel, BorderLayout.CENTER);
        timeFromPanel.add(timeFromLabel);
        timeFromPanel.add(timeFromTextField);
        timeFromToPanel.add(timeFromPanel);
        timeToPanel.add(timeToLabel);
        timeToPanel.add(timeToTextField);
        timeFromToPanel.add(timeToPanel);
        centerPanels[3].add(dateLabel, BorderLayout.PAGE_START);
        centerPanels[3].add(dateFromToPanel, BorderLayout.CENTER);
        dateFromPanel.add(dateFromLabel);
        dateFromPanel.add(dateFromTextField);
        dateFromToPanel.add(dateFromPanel);
        dateToPanel.add(dateToLabel);
        dateToPanel.add(dateToTextField);
        dateFromToPanel.add(dateToPanel);
        lowPanel.add(filmsScrollTable);
        buzzGazzWindow.add(topPanel, BorderLayout.PAGE_START);
        buzzGazzWindow.add(centerPanel, BorderLayout.CENTER);
        buzzGazzWindow.add(lowPanel, BorderLayout.PAGE_END);
    }

    @Override
    protected void setActions() {
        KeyListener findKeyListener = new KeyListener() {
            public void keyReleased(KeyEvent e) {
                find();
            }

            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
        };

        addButton.addActionListener(e -> navigateTo(new AddFilmWindow(buzzWindow)));

        deleteButton.addActionListener(e -> {
            try {
                films.remove(   filmsTable.getSelectedRows(),
                                sessions,
                                nameTextField.getText(),
                                authorTextField.getText(),
                                timeFromTextField.getText(),
                                timeToTextField.getText(),
                                dateFromTextField.getText(),
                                dateToTextField.getText());
                find();
            } catch (NoContentException error) {
                JOptionPane.showMessageDialog(filmsTable, error.getMessage());
            }
        });

        editButton.addActionListener(e -> {
            try {
                navigateTo(new EditFilmWindow(  buzzWindow, films.getFilm(  filmsTable.getSelectedRows(),
                                                                            nameTextField.getText(),
                                                                            authorTextField.getText(),
                                                                            timeFromTextField.getText(),
                                                                            timeToTextField.getText(),
                                                                            dateFromTextField.getText(),
                                                                            dateToTextField.getText())));
            }  catch (NoContentException error) {
                JOptionPane.showMessageDialog(filmsTable, error.getMessage());
            }
        });

        nameTextField.addKeyListener(findKeyListener);
        authorTextField.addKeyListener(findKeyListener);
        timeFromTextField.addKeyListener(findKeyListener);
        timeToTextField.addKeyListener(findKeyListener);
        dateFromTextField.addKeyListener(findKeyListener);
        dateToTextField.addKeyListener(findKeyListener);

        backButton.addActionListener(e -> back());
    }

    private void find() {
        tableModel.setDataVector(films.getData( nameTextField.getText(),
                                                authorTextField.getText(),
                                                timeFromTextField.getText(),
                                                timeToTextField.getText(),
                                                dateFromTextField.getText(),
                                                dateToTextField.getText()), columns);
    }
}
