package BuzzGazz;

import BuzzGazz.gui.ButtonImage;
import BuzzGazz.gui.BuzzWindow;
import BuzzGazz.gui.ResizeLabelFont;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;

import javax.swing.*;
import java.awt.*;

public class EditSessionWindow extends BuzzWindow{

    private ButtonImage backButton;
    private JPanel topPanel;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;
    private JPanel centerPanel;
    private JPanel lowPanel;
    private JPanel timePanel;
    private JPanel datePanel;
    private ResizeLabelFont sessionFilmNameLabel;
    private JPanel lowLeftPanel;
    private JPanel lowRightPanel;
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JTextField timeTextField;
    private JTextField dateTextField;

    private Session session;

    public EditSessionWindow(BuzzWindow backWindow, Session session) {
        super(backWindow);
        this.session = session;
    }

    @Override
    public void show() {
        setData(session);
        super.show();
    }

    private void setData(Session session) {
        sessionFilmNameLabel.setText(session.film.getName());
        timeTextField.setText(session.time.toString());
        dateTextField.setText(session.date.toString());
    }

    @Override
    protected void initialize() {
        topPanel = new JPanel();
        topLeftPanel= new JPanel();
        topLeftPanel.setPreferredSize(new Dimension(100, 80));
        topRightPanel = new JPanel();
        centerPanel = new JPanel();
        lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(1, 2));
        lowLeftPanel = new JPanel();
        lowRightPanel = new JPanel();
        timePanel = new JPanel();
        timePanel.setPreferredSize(new Dimension(120, 80));
        datePanel = new JPanel();
        datePanel.setPreferredSize(new Dimension(120, 80));

        backButton = new ButtonImage(60, 60, "./img/back.png");
        sessionFilmNameLabel = new ResizeLabelFont();
        sessionFilmNameLabel.setForeground(Buzzain);
        timeLabel = new JLabel("Time");
        timeLabel.setForeground(Buzzain);
        dateLabel = new JLabel("Date");
        dateLabel.setForeground(Buzzain);
        timeTextField = new JTextField();
        timeTextField.setPreferredSize(new Dimension(100, 30));
        timeTextField.setForeground(Buzzain);
        timeTextField.setCaretColor(Buzzain);
        dateTextField = new JTextField();
        dateTextField.setPreferredSize(new Dimension(100, 30));
        dateTextField.setForeground(Buzzain);
        dateTextField.setCaretColor(Buzzain);
    }

    @Override
    protected void resizeLayout() {
        topRightPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 120, 80));
        lowPanel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth(), 200));
        sessionFilmNameLabel.setPreferredSize(new Dimension(buzzGazzWindow.getWidth() - 20, 80));
    }

    @Override
    protected void showColoredLayout() {
        topPanel.setBackground(Color.RED);
        topRightPanel.setBackground(Color.GRAY);
        topLeftPanel.setBackground(Color.black);
        centerPanel.setBackground(Color.blue);
        lowPanel.setBackground(Color.MAGENTA);
        timePanel.setBackground(Color.PINK);
        datePanel.setBackground(Color.ORANGE);
        lowLeftPanel.setBackground(Color.CYAN);
        lowRightPanel.setBackground(Color.YELLOW);
        sessionFilmNameLabel.setBackground(Color.GREEN);
        sessionFilmNameLabel.setOpaque(true);
    }

    @Override
    protected void layout() {
        topLeftPanel.add(backButton);
        topPanel.add(topLeftPanel);
        topPanel.add(topRightPanel);
        centerPanel.add(sessionFilmNameLabel, BorderLayout.LINE_START);
        timePanel.add(timeLabel);
        timePanel.add(timeTextField);
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);
        lowPanel.add(lowLeftPanel);
        lowPanel.add(lowRightPanel);
        lowLeftPanel.add(timePanel);
        lowRightPanel.add(datePanel);
        buzzGazzWindow.add(topPanel, BorderLayout.PAGE_START);
        buzzGazzWindow.add(centerPanel, BorderLayout.CENTER);
        buzzGazzWindow.add(lowPanel, BorderLayout.PAGE_END);
    }

    @Override
    protected void setActions() {
        backButton.addActionListener(e -> setSessionData());
    }

    private void setSessionData() {
        StringBuilder errorText;
        boolean error;
        Time time;
        Date date;

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
        if (error) {
            JOptionPane.showMessageDialog(sessionFilmNameLabel, errorText.toString());
        } else {
            session.time = new Time(timeTextField.getText());
            session.date = new Date(dateTextField.getText());
            back();
        }
    }
}
