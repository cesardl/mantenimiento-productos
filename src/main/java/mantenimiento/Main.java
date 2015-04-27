package mantenimiento;

import java.io.IOException;
import java.util.Properties;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;

/**
 *
 * @author cesardiaz
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            log.error("Error with class", e);
        } catch (InstantiationException e) {
            log.error("Error while instantiation", e);
        } catch (IllegalAccessException e) {
            log.error("Error illegal access", e);
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Error loading look and feel", e);
        }

        try {
            Properties prop = new Properties();
            prop.load(Main.class
                    .getResourceAsStream("/app.properties"));

            final String strRuta = System.getProperty("user.home") + prop.getProperty("file.data");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new JFrameRegistro(strRuta).setVisible(true);
                }
            });
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
