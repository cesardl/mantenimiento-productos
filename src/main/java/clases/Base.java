package clases;

import clases.etc.NumberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;

/**
 * @author cesardiaz
 */
public class Base {

    private static final Logger LOG = LoggerFactory.getLogger(Base.class);

    /**
     * @return
     */
    public static String leerDato() {
        String dato = "inicio";
        try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            dato = teclado.readLine();
        } catch (IOException e) {
            LOG.error("Error de Lectura de datos.", e);
        }
        return dato;
    }

    /**
     * @param numero numero a formatear
     * @return el numero formateado
     */
    public static String formatearNumeroYDigitos(double numero) {
        String pattern = "#0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(numero);
    }

    /**
     * @param cadena
     * @return
     */
    public static int convertirCadenaEntero(String cadena) {
        int dato = -99999;
        try {
            dato = Integer.parseInt(cadena);
        } catch (NumberFormatException nfe) {
            LOG.error("Error al convertir - " + cadena + " - a Entero.", nfe);
        }
        return dato;
    }

    /**
     * @param cadena
     * @return
     */
    public static double convertirCadenaReal(String cadena) {
        double dato = -99999;
        try {
            dato = Double.parseDouble(cadena);
        } catch (NumberFormatException nfe) {
            LOG.error("Error al convertir - " + cadena + " - a Real.", nfe);
        }
        return dato;
    }

    /**
     * @param cadena
     * @return
     */
    public static long convertirCadenaEnteroLargo(String cadena) {
        long dato = -99999;
        try {
            dato = Long.parseLong(cadena);
        } catch (NumberFormatException nfe) {
            LOG.error("Error al convertir - " + cadena + " - a Entero largo.", nfe);
        }
        return dato;
    }

    /**
     * @param cadena
     * @param cantidad
     * @param simbolo
     * @return
     */
    public static String completarIzquierda(String cadena, int cantidad, String simbolo) {
        String aux = "";
        double veces = cantidad;

        veces = veces / simbolo.length();
        veces = Math.ceil(veces);

        for (int i = 0; i < veces; i++) {
            aux = aux + simbolo;
        }

        if (cantidad - cadena.length() >= 0) {
            aux = aux.substring(0, cantidad - cadena.length());
            aux = aux + cadena;
        } else {
            aux = cadena.substring(0, cantidad);
        }

        return aux;
    }

    /**
     * @param cadena
     * @param cantidad
     * @param simbolo
     * @return
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
     * @param cadena
     * @param cantidad
     * @param simbolo
     * @return
     */
    public static String centrarCadena(String cadena, int cantidad, String simbolo) {
        String aux = "";
        String cadenaD, cadenaI;
        int tamanio, tamanioI;
        int cantidadD, cantidadI;

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
     * @return
     */
    public static String getFecha() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.DAY_OF_MONTH), 2, "0");
        dato = dato + "/" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MONTH)) + 1), 2, "0");
        dato = dato + "/" + completarIzquierda("" + calendario.get(GregorianCalendar.YEAR), 4, "0");
        return dato;
    }

    /**
     * @return
     */
    public static String getDia() {
        GregorianCalendar calendario = new GregorianCalendar();
        return completarIzquierda("" + calendario.get(GregorianCalendar.DAY_OF_MONTH), 2, "0");
    }

    /**
     * @return
     */
    public static String getMes() {
        GregorianCalendar calendario = new GregorianCalendar();
        return completarIzquierda("" + ((calendario.get(GregorianCalendar.MONTH)) + 1), 2, "0");
    }

    /**
     * @return
     */
    public static String getAnio() {
        GregorianCalendar calendario = new GregorianCalendar();
        return completarIzquierda("" + calendario.get(GregorianCalendar.YEAR), 4, "0");
    }

    /**
     * @return
     */
    public static String getHoraMinSeg() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        dato = dato + ":" + completarIzquierda("" + calendario.get(GregorianCalendar.SECOND), 2, "0");
        return dato;
    }

    /**
     * @return
     */
    public static String getHoraMin() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        return dato;
    }

    /**
     * @return
     */
    public static String getHoraMinSegAmPm() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        dato = dato + ":" + completarIzquierda("" + calendario.get(GregorianCalendar.SECOND), 2, "0");
        if (calendario.get(GregorianCalendar.AM_PM) == 0) {
            dato = dato + " AM";
        } else {
            dato = dato + " PM";
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getHoraMinAmPm() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        if (calendario.get(GregorianCalendar.AM_PM) == 0) {
            dato = dato + " AM";
        } else {
            dato = dato + " PM";
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getHoraMinSegAmPm2() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        dato = dato + ":" + completarIzquierda("" + calendario.get(GregorianCalendar.SECOND), 2, "0");
        if (calendario.get(GregorianCalendar.AM_PM) == 0) {
            dato = dato + " A.M.";
        } else {
            dato = dato + " P.M.";
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getHoraMinAmPm2() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        if (calendario.get(GregorianCalendar.AM_PM) == 0) {
            dato = dato + " A.M.";
        } else {
            dato = dato + " P.M.";
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getHora() {
        GregorianCalendar calendario = new GregorianCalendar();
        return completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
    }

    /**
     * @return
     */
    public static String getMinuto() {
        GregorianCalendar calendario = new GregorianCalendar();
        return completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
    }

    /**
     * @return
     */
    public static String getSegundo() {
        GregorianCalendar calendario = new GregorianCalendar();
        return completarIzquierda("" + calendario.get(GregorianCalendar.SECOND), 2, "0");
    }

    /**
     * @return
     */
    public static String getDireccionIp() {
        String dato;
        int posicion;
        try {
            dato = java.net.InetAddress.getLocalHost().toString();
            posicion = dato.indexOf("/");
            if (posicion >= 0) {
                dato = dato.substring(posicion + 1);
            }
        } catch (UnknownHostException uhe) {
            LOG.error(uhe.getMessage(), uhe);
            dato = "No se encontro LocalHost" + uhe.toString() + " " + uhe.getMessage();
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getNombreMaquina() {
        String dato;
        int posicion;
        try {
            dato = java.net.InetAddress.getLocalHost().toString();
            posicion = dato.indexOf("/");
            if (posicion >= 0) {
                dato = dato.substring(0, posicion);
            }
        } catch (UnknownHostException uhe) {
            LOG.error(uhe.getMessage(), uhe);
            dato = "No se encontro LocalHost" + uhe.toString() + " " + uhe.getMessage();
        }
        return dato;
    }

    /**
     * @param mes
     * @return
     */
    public static String getMesEnLetra(int mes) {
        String dato = "";
        switch (mes) {
            case 1:
                dato = "Enero";
                break;
            case 2:
                dato = "Febrero";
                break;
            case 3:
                dato = "Marzo";
                break;
            case 4:
                dato = "Abril";
                break;
            case 5:
                dato = "Mayo";
                break;
            case 6:
                dato = "Junio";
                break;
            case 7:
                dato = "Julio";
                break;
            case 8:
                dato = "Agosto";
                break;
            case 9:
                dato = "Setiembre";
                break;
            case 10:
                dato = "Octubre";
                break;
            case 11:
                dato = "Noviembre";
                break;
            case 12:
                dato = "Diciembre";
                break;
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getDiaEnLetra() {
        GregorianCalendar calendario = new GregorianCalendar();
        int dia = calendario.get(GregorianCalendar.DAY_OF_WEEK);
        String dato = "";

        switch (dia) {
            case 1:
                dato = "Domingo";
                break;
            case 2:
                dato = "Lunes";
                break;
            case 3:
                dato = "Martes";
                break;
            case 4:
                dato = "Miercoles";
                break;
            case 5:
                dato = "Jueves";
                break;
            case 6:
                dato = "Viernes";
                break;
            case 7:
                dato = "Sabado";
                break;
        }
        return dato;
    }

    /**
     * @return
     */
    public static String getFechaEnLetra() {
        String dato;
        dato = getDia() + " de " + getMesEnLetra(convertirCadenaEntero(getMes())) + " del " + getAnio();
        return dato;
    }

    /**
     * @param strMensaje
     * @param strTitulo
     * @param iTipoIcono
     */
    public static void mensaje(String strMensaje, String strTitulo, int iTipoIcono) {
        JOptionPane.showMessageDialog(null, strMensaje, strTitulo, iTipoIcono);
    }

    /**
     * @param jfrmVentana
     */
    public static void centrarVentana(JFrame jfrmVentana) {
        try {
            Dimension tamPantalla = Toolkit.getDefaultToolkit().getScreenSize();
            int x, y, width, height;
            x = ((int) tamPantalla.getWidth());
            y = ((int) tamPantalla.getHeight());
            width = ((int) jfrmVentana.getSize().getWidth());
            height = ((int) jfrmVentana.getSize().getHeight());
            x = (x / 2) - (width / 2);
            y = (y / 2) - (height / 2);
            jfrmVentana.setBounds(x, y, width, height);
        } catch (HeadlessException he) {
            LOG.error(he.getMessage(), he);
        }
    }

    /**
     * @param tipo
     * @param cantEnt
     * @param cantFra
     * @param caracter
     * @return
     */
    public static DefaultFormatterFactory creaFormatoControl(
            NumberType tipo, int cantEnt, int cantFra, char caracter) {
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
        MaskFormatter numcase;
        switch (tipo) {
            case ENTERO:
                try {
                    numcase = new MaskFormatter(formato.toString());
                    numcase.setPlaceholderCharacter(caracter);
                    numcase.setOverwriteMode(true);
                    numcase.setValidCharacters("0123456789");
                    factory = new DefaultFormatterFactory(numcase);
                } catch (ParseException pe) {
                    LOG.error("Error al dar Formato de Numero Entero", pe);
                }
                break;
            case REAL:
                try {
                    numcase = new MaskFormatter(formato.toString());
                    numcase.setPlaceholderCharacter(caracter);
                    numcase.setOverwriteMode(true);
                    numcase.setValidCharacters("0123456789");
                    factory = new DefaultFormatterFactory(numcase);
                } catch (ParseException pe) {
                    LOG.error("Error al dar Formato de Numero Real", pe);
                }
                break;
        }
        return factory;
    }
}
