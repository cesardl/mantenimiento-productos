package org.sanmarcux.clases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.GregorianCalendar;

/**
 * @author cesar.diaz
 */
public final class Base {

    private static final Logger LOG = LoggerFactory.getLogger(Base.class);

    private Base() {
    }

    /**
     * @param numero numero a formatear
     * @return el numero formateado
     */
    public static String formatearNumeroYDigitos(double numero) {
        String pattern = "#0.00";
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(java.util.Locale.ENGLISH);

        return new DecimalFormat(pattern, symbols).format(numero);
    }

    /**
     * @param cadena a convertir
     * @return el valor convertido a entero
     */
    public static int convertirCadenaEntero(String cadena) {
        int dato = -99999;
        try {
            dato = Integer.parseInt(cadena);
        } catch (NumberFormatException nfe) {
            LOG.error("Error al convertir - {} - a Entero.", cadena);
        }
        return dato;
    }

    /**
     * @param cadena a convertir
     * @return el valor convertido a real
     */
    public static double convertirCadenaReal(String cadena) {
        double dato = -99999;
        try {
            dato = Double.parseDouble(cadena);
        } catch (NumberFormatException nfe) {
            LOG.error("Error al convertir - {} - a Real.", cadena);
        }
        return dato;
    }

    /**
     * @param cadena a convertir
     * @return el valor convertido a entero largo
     */
    public static long convertirCadenaEnteroLargo(String cadena) {
        long dato = -99999;
        try {
            dato = Long.parseLong(cadena);
        } catch (NumberFormatException nfe) {
            LOG.error("Error al convertir - {} - a Entero largo.", cadena);
        }
        return dato;
    }

    /**
     * @param cadena   base string
     * @param cantidad desired string size
     * @param simbolo  character used to complete
     * @return altered string
     */
    public static String completarIzquierda(String cadena, int cantidad, String simbolo) {
        StringBuilder aux = new StringBuilder();
        double veces = cantidad;

        veces = veces / simbolo.length();
        veces = Math.ceil(veces);

        for (int i = 0; i < veces; i++) {
            aux.append(simbolo);
        }

        if (cantidad - cadena.length() >= 0) {
            aux = new StringBuilder(aux.substring(0, cantidad - cadena.length()));
            aux.append(cadena);
        } else {
            aux = new StringBuilder(cadena.substring(0, cantidad));
        }

        return aux.toString();
    }

    /**
     * @param cadena   base string
     * @param cantidad desired string size
     * @param simbolo  character used to complete
     * @return altered string
     */
    public static String completarDerecha(String cadena, int cantidad, String simbolo) {
        StringBuilder aux = new StringBuilder();
        double veces = cantidad;

        veces = veces / simbolo.length();
        veces = Math.ceil(veces);

        for (int i = 0; i < veces; i++) {
            aux.append(simbolo);
        }

        if (cantidad - cadena.length() >= 0) {
            aux.insert(0, cadena);
            aux = new StringBuilder(aux.substring(0, cantidad));
        } else {
            aux = new StringBuilder(cadena.substring(0, cantidad));
        }

        return aux.toString();
    }

    /**
     * @param cadena   base string
     * @param cantidad desired string size
     * @param simbolo  character used to complete
     * @return altered string
     */
    public static String centrarCadena(String cadena, int cantidad, String simbolo) {
        String aux = "";
        String cadenaD;
        String cadenaI;
        int tamanio;
        int tamanioI;
        int cantidadD;
        int cantidadI;

        if (cantidad - cadena.length() > 0) {
            tamanio = cadena.length();
            tamanioI = (tamanio + 1) / 2;
            cantidadI = (cantidad + 1) / 2;
            cantidadD = cantidad - cantidadI;
            cadenaI = completarIzquierda(cadena.substring(0, tamanioI), cantidadI, simbolo);
            cadenaD = completarDerecha(cadena.substring(tamanioI, tamanio), cantidadD, simbolo);
            aux = cadenaI + cadenaD;
        }

        return aux;
    }

    /**
     * @return the date
     */
    public static String getFecha() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.DAY_OF_MONTH), 2, "0");
        dato = dato + "/" + completarIzquierda("" + (calendario.get(GregorianCalendar.MONTH) + 1), 2, "0");
        dato = dato + "/" + completarIzquierda("" + calendario.get(GregorianCalendar.YEAR), 4, "0");
        return dato;
    }

    /**
     * @return the hour
     */
    public static String getHoraMinAmPm2() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + calendario.get(GregorianCalendar.MINUTE), 2, "0");
        if (calendario.get(GregorianCalendar.AM_PM) == 0) {
            dato = dato + " A.M.";
        } else {
            dato = dato + " P.M.";
        }
        return dato;
    }

    /**
     * @return obtiene la direccion IP de la PC
     */
    public static String getDireccionIp() {
        String dato;
        int posicion;
        try {
            dato = java.net.InetAddress.getLocalHost().toString();
            posicion = dato.indexOf('/');
            if (posicion >= 0) {
                dato = dato.substring(posicion + 1);
            }
        } catch (java.net.UnknownHostException uhe) {
            LOG.error(uhe.getMessage(), uhe);
            dato = "No se encontro LocalHost: " + uhe.toString() + " " + uhe.getMessage();
        }
        return dato;
    }

    /**
     * @return the machine name
     */
    public static String getNombreMaquina() {
        String dato;
        int posicion;
        try {
            dato = java.net.InetAddress.getLocalHost().toString();
            posicion = dato.indexOf('/');
            if (posicion >= 0) {
                dato = dato.substring(0, posicion);
            }
        } catch (java.net.UnknownHostException uhe) {
            LOG.error(uhe.getMessage(), uhe);
            dato = "No se encontro LocalHost" + uhe.toString() + " " + uhe.getMessage();
        }
        return dato;
    }

    /**
     * @param strMensaje mensaje a mostrar
     * @param strTitulo  titulo de la ventana
     * @param iTipoIcono tipo de mensaje
     */
    public static void mensaje(String strMensaje, String strTitulo, int iTipoIcono) {
        javax.swing.JOptionPane.showMessageDialog(null, strMensaje, strTitulo, iTipoIcono);
    }

    /**
     * @param frame ventana a centrar
     */
    public static void centrarVentana(final javax.swing.JFrame frame) {
        java.awt.Dimension tamPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int x = (int) tamPantalla.getWidth();
        int y = (int) tamPantalla.getHeight();
        LOG.debug("Tamanho de pantalla ({}, {})", x, y);

        int width = (int) frame.getSize().getWidth();
        int height = (int) frame.getSize().getHeight();
        LOG.debug("Dimensión de la aplicacion ({}, {})", width, height);

        x = (x / 2) - (width / 2);
        y = (y / 2) - (height / 2);
        frame.setBounds(x, y, width, height);
    }

    /**
     * @param cantEnt  parte entera
     * @param cantFra  parte decimal
     * @param caracter caracter a completar la base
     * @return formateador
     */
    public static DefaultFormatterFactory creaFormatoControl(int cantEnt, int cantFra, char caracter) {
        DefaultFormatterFactory factory = null;
        StringBuilder formato = new StringBuilder();
        int i;

        for (i = 0; i < cantEnt; i++) {
            formato.append("#");
        }
        if (cantFra > 0) {
            formato.append(".");
            for (i = 0; i < cantFra; i++) {
                formato.append("#");
            }
        }

        try {
            MaskFormatter numcase = new MaskFormatter(formato.toString());
            numcase.setPlaceholderCharacter(caracter);
            numcase.setOverwriteMode(true);
            numcase.setValidCharacters("0123456789");
            factory = new DefaultFormatterFactory(numcase);
        } catch (ParseException pe) {
            LOG.error("Error al dar Formato de Numero Real", pe);
        }
        return factory;
    }
}
