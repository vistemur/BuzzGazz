package BuzzGazz;

import BuzzGazz.gui.ButtonImage;
import BuzzGazz.gui.BuzzLabel;
import BuzzGazz.gui.BuzzTextField;
import BuzzGazz.gui.BuzzWindow;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;

import javax.swing.*;
import java.awt.*;

public class AddFilmWindow extends BuzzWindow {

    private JPanel topPanel;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;
    private JPanel centralPanel;
    private JPanel nameAuthorPanel;
    private JPanel nameAuthorSpacerPanel;
    private JPanel smallNameAuthorPanel;
    private JPanel namePanel;
    private JPanel authorPanel;
    private JPanel timePanel;
    private JPanel datePanel;
    private JPanel lowPanel;

    private ButtonImage backButton;
    private ButtonImage addButton;

    private BuzzTextField nameTextField;
    private BuzzTextField authorTextField;
    private BuzzTextField timeTextField;
    private BuzzTextField dateTextField;

    private BuzzLabel timeLabel;
    private BuzzLabel dateLabel;
    private BuzzLabel nameLabel;
    private BuzzLabel authorLabel;

    public AddFilmWindow(BuzzWindow backWindow) {
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
        nameAuthorPanel = new JPanel();
        nameAuthorPanel.setSize(new Dimension(buzzGazzWindow.getWidth() - 280, 100));
        nameAuthorSpacerPanel = new JPanel();
        smallNameAuthorPanel = new JPanel();
        namePanel = new JPanel();
        authorPanel = new JPanel();
        timePanel = new JPanel();
        timePanel.setPreferredSize(new Dimension(120, 80));
        datePanel = new JPanel();
        datePanel.setPreferredSize(new Dimension(120, 80));
        timeTextField = new BuzzTextField();
        timeTextField.setPreferredSize(new Dimension(100, 30));
        timeLabel = new BuzzLabel("Time");
        timeLabel.setSmallFont();
        dateTextField = new BuzzTextField();
        dateTextField.setPreferredSize(new Dimension(100, 30));
        dateLabel = new BuzzLabel("Creation date");
        dateLabel.setSmallFont();
        nameLabel = new BuzzLabel("Name");
        nameLabel.setSmallFont();
        nameTextField = new BuzzTextField();
        authorLabel = new BuzzLabel("Author");
        authorLabel.setSmallFont();
        authorTextField = new BuzzTextField();
        lowPanel = new JPanel();
        lowPanel.setPreferredSize(new Dimension(100, 80));
    }

    @Override
    protected void resizeLayout() {
        topRightPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 120, 80));
        nameAuthorPanel.setPreferredSize(new Dimension( buzzGazzWindow.getWidth() - 260,
                                                        buzzGazzWindow.getHeight() - 200));
        nameAuthorSpacerPanel.setPreferredSize(new Dimension(   buzzGazzWindow.getWidth() - 280,
                                                                buzzGazzWindow.getHeight() / 2 - 155));
        smallNameAuthorPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 280, 100));
        namePanel.setPreferredSize(new Dimension(nameAuthorPanel.getWidth() / 2 - 20, 80));
        authorPanel.setPreferredSize(new Dimension(nameAuthorPanel.getWidth() / 2 - 20, 80));
        nameTextField.setPreferredSize(new Dimension(nameAuthorPanel.getWidth() / 2 - 40, 30));
        authorTextField.setPreferredSize(new Dimension(nameAuthorPanel.getWidth() / 2 - 40, 30));
    }

    @Override
    protected void showColoredLayout() {
        topPanel.setBackground(Color.RED);
        topRightPanel.setBackground(Color.GRAY);
        topLeftPanel.setBackground(Color.black);
        centralPanel.setBackground(Color.BLUE);
        lowPanel.setBackground(Color.MAGENTA);
        nameAuthorPanel.setBackground(Color.CYAN);
        timePanel.setBackground(Color.PINK);
        datePanel.setBackground(Color.ORANGE);
        nameAuthorSpacerPanel.setBackground(Color.MAGENTA);
        namePanel.setBackground(Color.green);
        authorPanel.setBackground(Color.blue);
    }

    @Override
    protected void layout() {
        topLeftPanel.add(backButton);
        topPanel.add(topLeftPanel);
        topPanel.add(topRightPanel);
        timePanel.add(timeLabel);
        timePanel.add(timeTextField);
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        authorPanel.add(authorLabel);
        authorPanel.add(authorTextField);
        smallNameAuthorPanel.add(namePanel);
        smallNameAuthorPanel.add(authorPanel);
        nameAuthorPanel.add(nameAuthorSpacerPanel);
        nameAuthorPanel.add(smallNameAuthorPanel);
        centralPanel.add(nameAuthorPanel);
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

        addButton.addActionListener(e -> {
            StringBuilder errorText;
            boolean error;
            Author author;
            Time time;
            Date date;

            error = false;
            errorText = new StringBuilder("ERROR\n");
            author = new Author(authorTextField.getText());
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
            if (nameTextField.getText().length() == 0) {
                error = true;
                errorText.append("no name\n");
            }
            if (authorTextField.getText().length() == 0) {
                error = true;
                errorText.append("no author\n");
            }
            if (films.has(nameTextField.getText(), author, date, time)) {
                error = true;
                errorText.append("this film is already in database\n");
            }
            if (error) {
                JOptionPane.showMessageDialog(authorPanel, errorText.toString());
            } else {
                films.addFilm(nameTextField.getText(), author, date, time);
                logger.info("film added(name: " + nameTextField.getText() + "author: " + authorTextField.getText() + " time: " + time + " date: " + date + ")");
                back();
            }
        });
    }
}
