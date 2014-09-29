/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

/**
 *
 * @author lns
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JFrameRegistro().setVisible(true);
            }
        });
    }
}
