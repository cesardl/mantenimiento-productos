package mantenimiento;

import java.io.IOException;
import java.util.Properties;
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
            Properties prop = new Properties();
            prop.load(Main.class
                    .getResourceAsStream("/app.properties"));

            final String strRuta = System.getProperty("user.home") + prop.getProperty("file.data");

            javax.swing.SwingUtilities.invokeLater(new Runnable() {

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
