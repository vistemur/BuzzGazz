package BuzzGazz.gui;

        import BuzzGazz.AddSessionWindow;
        import BuzzGazz.Films;
        import BuzzGazz.Sessions;
        import org.apache.log4j.Logger;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ComponentAdapter;
        import java.awt.event.ComponentEvent;
        import java.awt.event.WindowEvent;
        import java.awt.event.WindowListener;

public abstract class BuzzWindow {

    protected BuzzGazzWindow buzzGazzWindow  = new BuzzGazzWindow();
    protected BuzzWindow backWindow = null;
    protected static boolean showColoredLayout = false;
    protected static final Color Buzzain = new Color(0, 209, 178);
    protected static final Font Buzzont = new Font("SansSerif", Font.BOLD, 30);
    protected static final Font BuzzontJr = new Font("SansSerif", Font.BOLD, 15);
    protected static final String[] columns = {"name", "author", "time", "date"};
    protected static Films films  = new Films();
    protected static Sessions sessions = new Sessions(films);
    protected static final Logger logger = Logger.getLogger("BuzzWindow.class");
    protected BuzzWindow buzzWindow;

    public BuzzWindow() {
        init();
    }

    public BuzzWindow(BuzzWindow backWindow) {
        this.backWindow = backWindow;
        init();
    }

    public BuzzGazzWindow getFrame() {
        return buzzGazzWindow;
    }

    private void init() {
        buzzWindow = this;
        initialize();
        resizeLayout();
        layout();
        setDefaultActions();
        if (backWindow != null)
            resize(backWindow.getFrame());
        if (showColoredLayout)
            showColoredLayout();
    }

    public void show() {
        refresh();
        buzzGazzWindow.setVisible(true);
    }

    public void resize(JFrame frameDimension) {
        buzzGazzWindow.setDimension(frameDimension);
        resizeLayout();
    }

    protected void back() {
        buzzGazzWindow.setVisible(false);
        backWindow.resize(buzzGazzWindow);
        backWindow.refresh();
        backWindow.show();
    }

    protected void refresh() {};
    protected abstract void initialize();
    protected abstract void showColoredLayout();
    protected abstract void layout();
    protected abstract void resizeLayout();
    protected abstract void setActions();

    protected void close() {
        sessions.save();
        films.save();
        logger.info("Program closed");
    }

    private void setDefaultActions() {
        setActions();

        buzzGazzWindow.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent event) {
                resizeLayout();
            }

            public void componentMoved(ComponentEvent event) {
                resizeLayout();
            }
        });

        buzzGazzWindow.addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent e) {
                close();
            }

            public void windowOpened(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });
    };

    protected void navigateTo(BuzzWindow to) {
        buzzGazzWindow.setVisible(false);
        to.show();
    }
}
