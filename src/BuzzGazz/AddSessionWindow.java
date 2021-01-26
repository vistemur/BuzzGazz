package BuzzGazz;

import BuzzGazz.gui.*;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.BorderFactory.createEmptyBorder;

public class AddSessionWindow extends BuzzWindow {

    private JPanel topPanel;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;
    private JPanel centralPanel;
    private JPanel filmPanel;
    private JPanel filmTopPanel;
    private JPanel timePanel;
    private JPanel datePanel;
    private JPanel lowPanel;

    private ButtonImage backButton;
    private ButtonImage addButton;

    private BuzzTextField filmSearchTextField;
    private BuzzTextField timeTextField;
    private BuzzTextField dateTextField;

    private BuzzComboBox filmSearchParameterComboBox;

    private BuzzLabel timeLabel;
    private BuzzLabel dateLabel;

    private DefaultTableModel filmsTableModel;
    private BuzzTable filmsTable;
    private JScrollPane filmsScrollTable;

    public AddSessionWindow(BuzzWindow backWindow) {
        super(backWindow);
    }

    @Override
    protected void initialize() {
        backButton = new ButtonImage(60, 60, "./img/back.png");
        addButton = new ButtonImage(60, 60, "./img/add.png");

        topPanel = new JPanel();
        topLeftPanel= new JPanel();
        topLeftPanel.setPreferredSize(new Dimension(100, 80));
        topRightPanel = new JPanel();
        centralPanel = new JPanel();
        centralPanel.setLayout(new FlowLayout());
        filmPanel = new JPanel();
        timePanel = new JPanel();
        filmTopPanel = new JPanel();
        filmSearchTextField = new BuzzTextField();
        filmSearchParameterComboBox = new BuzzComboBox();
        filmSearchParameterComboBox.setPreferredSize(new Dimension(100, 60));
        timePanel.setPreferredSize(new Dimension(120, 80));
        datePanel = new JPanel();
        datePanel.setPreferredSize(new Dimension(120, 80));
        timeTextField = new BuzzTextField();
        timeTextField.setPreferredSize(new Dimension(100, 30));
        timeLabel = new BuzzLabel("Time");
        dateTextField = new BuzzTextField();
        dateTextField.setPreferredSize(new Dimension(100, 30));
        dateLabel = new BuzzLabel("Date");
        lowPanel = new JPanel();
        lowPanel.setPreferredSize(new Dimension(100, 80));
        filmsTableModel = new DefaultTableModel(films.getData(), columns);
        filmsTable = new BuzzTable(filmsTableModel);
        filmsScrollTable = new JScrollPane(filmsTable);
        filmsScrollTable.setBorder(createEmptyBorder());
    }

    @Override
    protected void resizeLayout() {
        topRightPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 120, 80));
        filmPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 260, buzzGazzWindow.getHeight() - 200));
        filmTopPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 280, 60));
        filmSearchTextField.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 400, 50));
        filmsScrollTable.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 280, buzzGazzWindow.getHeight() - 220));
    }

    @Override
    protected void showColoredLayout() {
        topPanel.setBackground(Color.RED);
        topRightPanel.setBackground(Color.GRAY);
        topLeftPanel.setBackground(Color.black);
        centralPanel.setBackground(Color.BLUE);
        lowPanel.setBackground(Color.MAGENTA);
        filmPanel.setBackground(Color.CYAN);
        filmTopPanel.setBackground(Color.GREEN);
        timePanel.setBackground(Color.PINK);
        datePanel.setBackground(Color.ORANGE);
    }

    @Override
    protected void layout() {
        topLeftPanel.add(backButton);
        topPanel.add(topLeftPanel);
        topPanel.add(topRightPanel);
        filmTopPanel.add(filmSearchTextField);
        filmTopPanel.add(filmSearchParameterComboBox);
        filmPanel.add(filmTopPanel);
        filmPanel.add(filmsScrollTable, BorderLayout.CENTER);
        timePanel.add(timeLabel);
        timePanel.add(timeTextField);
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);
        centralPanel.add(filmPanel);
        centralPanel.add(timePanel);
        centralPanel.add(datePanel);
        lowPanel.add(addButton);
        buzzGazzWindow.add(topPanel, BorderLayout.PAGE_START);
        buzzGazzWindow.add(centralPanel, BorderLayout.CENTER);
        buzzGazzWindow.add(lowPanel, BorderLayout.PAGE_END);
    }

    @Override
    protected void setActions() {
        backButton.addActionListener(e -> back());

        filmSearchTextField.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                find();
            }

            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
        });

        filmSearchParameterComboBox.addActionListener(e -> find());

        addButton.addActionListener(e -> {
            StringBuilder errorText;
            boolean error;
            Film film;
            Time time;
            Date date;

            film = null;
            error = false;
            errorText = new StringBuilder("ERROR\n");
            time = new Time(timeTextField.getText());
            date = new Date(dateTextField.getText());
            if (time.compareTo(new Time(0)) < 0) {
                error = true;
                errorText.append("time is too small\n");
            } else if (time.compareTo(new Time(24, 0, 0)) > 0) {
                error = true;
                errorText.append("time is too big\n");
            }
            if (dateTextField.getText().length() == 0) {
                error = true;
                errorText.append("no date\n");
            }
            if (filmsTable.getSelectedRows().length == 1) {
                film = films.getFilmsByParam(filmSearchParameterComboBox.getSelectedIndex(),
                                                                     filmSearchTextField.getText())[filmsTable.getSelectedRow()];
            } else {
                error = true;
                if (filmsTable.getSelectedRows().length > 1)
                    errorText.append("multiple films chosen\n");
                else
                    errorText.append("no film chosen\n");
            }
            if (error) {
                JOptionPane.showMessageDialog(filmSearchParameterComboBox, errorText.toString());
            } else {
                sessions.add(film, time, date);
                logger.info("session added(film: " + film.getName() + " time: " + time + " date: " + date + ")");
                back();
            }
        });
    }

    @Override
    protected void refresh() {
        find();
    }

    private void find() {
        switch (filmSearchParameterComboBox.getSelectedIndex()) {
            case 0:
                filmsTableModel.setDataVector(films.getDataByName(filmSearchTextField.getText()), columns);
                break;
            case 1:
                filmsTableModel.setDataVector(films.getDataByAuthor(filmSearchTextField.getText()), columns);
                break;
            case 2:
                filmsTableModel.setDataVector(films.getDataByLength(filmSearchTextField.getText()), columns);
                break;
            case 3:
                filmsTableModel.setDataVector(films.getDataByDate(filmSearchTextField.getText()), columns);
                break;
        }
    }
}
