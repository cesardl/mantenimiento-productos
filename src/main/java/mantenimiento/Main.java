package mantenimiento;

import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cesardiaz
 */
public class Main {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager
                    .getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException e) {
            log.error("Error with class", e);
        } catch (InstantiationException e) {
            log.error("Error while instantiation", e);
        } catch (IllegalAccessException e) {
            log.error("Error illegal access", e);
        } catch (javax.swing.UnsupportedLookAndFeelException e) {
            log.error("Error loading look and feel", e);
        }

        try {
            Properties prop = new Properties();
            prop.load(Main.class.getResourceAsStream("/app.properties"));

            final String path = System.getProperty("user.dir").concat(prop.getProperty("file.data"));

            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new JFrameRegistro(path).setVisible(true);
                }
            });
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
