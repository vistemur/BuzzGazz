package BuzzGazz;

import BuzzGazz.gui.ButtonImage;
import BuzzGazz.gui.BuzzLabel;
import BuzzGazz.gui.BuzzTextField;
import BuzzGazz.gui.BuzzWindow;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;

import javax.swing.*;
import java.awt.*;

public class EditFilmWindow extends BuzzWindow {

    private JPanel topPanel;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;
    private JPanel centralPanel;
    private JPanel nameAuthorPanel;
    private JPanel nameAuthorSpacerPanel;
    private JPanel smallNameAuthorPanel;
    private JPanel namePanel;
    private JPanel authorPanel;
    private JPanel lengthPanel;
    private JPanel creationDatePanel;

    private ButtonImage backButton;

    private BuzzTextField nameTextField;
    private BuzzTextField authorTextField;
    private BuzzTextField lengthTextField;
    private BuzzTextField creationDateTextField;

    private BuzzLabel lengthLabel;
    private BuzzLabel creationDateLabel;
    private BuzzLabel nameLabel;
    private BuzzLabel authorLabel;

    private Film film;

    public EditFilmWindow(BuzzWindow backWindow, Film film) {
        super(backWindow);
        this.film = film;
    }

    @Override
    protected void initialize() {
        backButton = new ButtonImage(60, 60, "./img/back.png");

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
        lengthPanel = new JPanel();
        lengthPanel.setPreferredSize(new Dimension(120, 80));
        creationDatePanel = new JPanel();
        creationDatePanel.setPreferredSize(new Dimension(120, 80));
        lengthTextField = new BuzzTextField();
        lengthTextField.setPreferredSize(new Dimension(100, 30));
        lengthLabel = new BuzzLabel("Length");
        lengthLabel.setSmallFont();
        creationDateTextField = new BuzzTextField();
        creationDateTextField.setPreferredSize(new Dimension(100, 30));
        creationDateLabel = new BuzzLabel("Creation date");
        creationDateLabel.setSmallFont();
        nameLabel = new BuzzLabel("Name");
        nameLabel.setSmallFont();
        nameTextField = new BuzzTextField();
        authorLabel = new BuzzLabel("Author");
        authorLabel.setSmallFont();
        authorTextField = new BuzzTextField();
    }

    @Override
    public void show() {
        setData(film);
        super.show();
    }

    private void setData(Film film) {
        nameTextField.setText(film.getName());
        authorTextField.setText(film.getAuthor());
        lengthTextField.setText(film.getLength().toString());
        creationDateTextField.setText(film.getCreationDate().toString());
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
        nameAuthorPanel.setBackground(Color.CYAN);
        lengthPanel.setBackground(Color.PINK);
        creationDatePanel.setBackground(Color.ORANGE);
        nameAuthorSpacerPanel.setBackground(Color.MAGENTA);
        namePanel.setBackground(Color.green);
        authorPanel.setBackground(Color.blue);
    }

    @Override
    protected void layout() {
        topLeftPanel.add(backButton);
        topPanel.add(topLeftPanel);
        topPanel.add(topRightPanel);
        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthTextField);
        creationDatePanel.add(creationDateLabel);
        creationDatePanel.add(creationDateTextField);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        authorPanel.add(authorLabel);
        authorPanel.add(authorTextField);
        smallNameAuthorPanel.add(namePanel);
        smallNameAuthorPanel.add(authorPanel);
        nameAuthorPanel.add(nameAuthorSpacerPanel);
        nameAuthorPanel.add(smallNameAuthorPanel);
        centralPanel.add(nameAuthorPanel);
        centralPanel.add(lengthPanel);
        centralPanel.add(creationDatePanel);
        buzzGazzWindow.add(topPanel, BorderLayout.PAGE_START);
        buzzGazzWindow.add(centralPanel, BorderLayout.CENTER);
    }

    @Override
    protected void setActions() {
        backButton.addActionListener(e -> {
            String name;
            boolean error;
            Author author;
            Date creationDate;
            Time length;
            StringBuilder errorText;

            error = false;
            name = nameTextField.getText();
            author = new Author(getAuthorFirstName(), getAuthorLastName());
            creationDate = new Date(creationDateTextField.getText());
            length = new Time(lengthTextField.getText());
            errorText = new StringBuilder();
            if (nameTextField.getText().equals("")) {
                error = true;
                errorText.append("no film name\n");
            }
            if (authorTextField.getText().equals("")) {
                error = true;
                errorText.append("no author\n");
            }
            if (creationDateTextField.getText().equals("")) {
                error = true;
                errorText.append("no creation date\n");
            }
            if (lengthTextField.getText().equals("")) {
                error = true;
                errorText.append("no film length\n");
            }
            if (length.compareTo(new Time(0)) < 0) {
                error = true;
                errorText.append("length is too small\n");
            }
            if (creationDate.compareTo(new Date("0:0:0")) <= 0) {
                error = true;
                errorText.append("length is too small\n");
            }
            if (error) {
                JOptionPane.showMessageDialog(authorPanel, errorText.toString());
            } else {
                film.setData(name, author, creationDate, length);
                back();
            }
        });
    }

    private String getAuthorFirstName() {
        int spacerPos = authorTextField.getText().indexOf(" ");
        if (spacerPos != -1)
            return authorTextField.getText().substring(0, spacerPos);
        return "";
    }

    private String getAuthorLastName() {
        int length = authorTextField.getText().length();
        int spacerPos = authorTextField.getText().indexOf(" ");
        if (spacerPos != -1)
            return authorTextField.getText().substring(spacerPos, length);
        return "";
    }
}
