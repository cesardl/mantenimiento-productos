package org.sanmarcux;

import org.sanmarcux.mantenimiento.JFrameRegistro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author cesar.diaz
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
            String appHome = System.getProperty("app.home");

            Properties prop = new Properties();
            InputStream is;
            if (appHome == null) {
                is = Main.class.getResourceAsStream("/app.properties");
                appHome = System.getProperty("user.dir").concat(File.separator);
            } else {
                is = new FileInputStream(appHome.concat("app.properties"));
            }
            prop.load(is);

            final String path = appHome.concat(prop.getProperty("file.data"));

            javax.swing.SwingUtilities.invokeLater(() -> new JFrameRegistro(path).setVisible(true));
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
