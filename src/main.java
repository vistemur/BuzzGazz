import BuzzGazz.*;
import org.apache.log4j.PropertyConfigurator;

public class main {
    public static void main(String[] args) {
        run();
    }

    private static void run() {
        PropertyConfigurator.configure("./src/log4j.properties");
        BuzzGazz program = new BuzzGazz();

        program.show();
    }
}