package org.sanmarcux;

import org.sanmarcux.mantenimiento.JFrameRegistro;

import java.io.IOException;
import java.util.Properties;

/**
 * @author cesardiaz
 */
public class Main {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
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

            javax.swing.SwingUtilities.invokeLater(() -> new JFrameRegistro(path).setVisible(true));
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
