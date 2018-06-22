package org.sanmarcux;

import org.sanmarcux.mantenimiento.JFrameRegistro;

import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author cesar.diaz
 */
public class Main {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            LOG.error("Error with class", e);
        } catch (InstantiationException e) {
            LOG.error("Error while instantiation", e);
        } catch (IllegalAccessException e) {
            LOG.error("Error illegal access", e);
        } catch (javax.swing.UnsupportedLookAndFeelException e) {
            LOG.error("Error loading look and feel", e);
        }
        //</editor-fold>

        try {
            String appHome = System.getProperty("app.home");

            Properties prop = new Properties();
            java.io.InputStream is;
            if (appHome == null) {
                LOG.debug("Reading properties from classpath");
                is = Main.class.getResourceAsStream("/app.properties");
                appHome = System.getProperty("user.dir");
            } else {
                LOG.debug("Reading properties from external resource");
                is = new java.io.FileInputStream(Paths.get(appHome, "app.properties").toFile());
            }
            prop.load(is);

            final String path = Paths.get(appHome, prop.getProperty("file.data")).toString();

            /* Create and display the form */
            javax.swing.SwingUtilities.invokeLater(() -> new JFrameRegistro(path).setVisible(true));
        } catch (java.io.IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
