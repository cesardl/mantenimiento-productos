package clases;

import clases.etc.NumberType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import org.apache.log4j.Logger;

/**
 *
 * @author cesardiaz
 */
public class Base {

    private static final Logger log = Logger.getLogger(Base.class);

    public static String leerDato() {
        String dato = "inicio";
        try {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            dato = teclado.readLine();
        } catch (IOException e) {
            log.error("Error de Lectura de datos.", e);
        }
        return dato;
    }

    public static void mostrar(String cadena) {
        System.out.println(cadena);
    }

    public static void mostrarSeguido(String cadena) {
        System.out.print(cadena);
    }

    public static int convertirCadenaEntero(String cadena) {
        int dato = -99999;
        try {
            dato = Integer.parseInt(cadena);
        } catch (NumberFormatException nfe) {
            log.error("Error al convertir - " + cadena + " - a Entero.", nfe);
        }
        return dato;
    }

    public static double convertirCadenaReal(String cadena) {
        double dato = -99999;
        try {
            dato = Double.parseDouble(cadena);
        } catch (NumberFormatException nfe) {
            log.error("Error al convertir - " + cadena + " - a Real.", nfe);
        }
        return dato;
    }

    public static long convertirCadenaEnteroLargo(String cadena) {
        long dato = -99999;
        try {
            dato = Long.parseLong(cadena);
        } catch (NumberFormatException nfe) {
            log.error("Error al convertir - " + cadena + " - a Entero largo.", nfe);
        }
        return dato;
    }

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

    public static String completarDerecha(String cadena, int cantidad, String simbolo) {
        String aux = "";
        double veces = cantidad;

        veces = veces / simbolo.length();
        veces = Math.ceil(veces);

        for (int i = 0; i < veces; i++) {
            aux = aux + simbolo;
        }

        if (cantidad - cadena.length() >= 0) {
            aux = cadena + aux;
            aux = aux.substring(0, cantidad);
        } else {
            aux = cadena.substring(0, cantidad);
        }

        return aux;
    }

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

    public static String getFecha() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.DAY_OF_MONTH), 2, "0");
        dato = dato + "/" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MONTH)) + 1), 2, "0");
        dato = dato + "/" + completarIzquierda("" + calendario.get(GregorianCalendar.YEAR), 4, "0");
        return dato;
    }

    public static String getDia() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.DAY_OF_MONTH), 2, "0");
        return dato;
    }

    public static String getMes() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + ((calendario.get(GregorianCalendar.MONTH)) + 1), 2, "0");
        return dato;
    }

    public static String getAnio() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.YEAR), 4, "0");
        return dato;
    }

    public static String getHoraMinSeg() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        dato = dato + ":" + completarIzquierda("" + calendario.get(GregorianCalendar.SECOND), 2, "0");
        return dato;
    }

    public static String getHoraMin() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        dato = dato + ":" + completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        return dato;
    }

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

    public static String getHora() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.HOUR), 2, "0");
        return dato;
    }

    public static String getMinuto() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + ((calendario.get(GregorianCalendar.MINUTE))), 2, "0");
        return dato;
    }

    public static String getSegundo() {
        GregorianCalendar calendario = new GregorianCalendar();
        String dato = completarIzquierda("" + calendario.get(GregorianCalendar.SECOND), 2, "0");
        return dato;
    }

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
            log.error(uhe, uhe);
            dato = "No se encontro LocalHost" + uhe.toString() + " " + uhe.getMessage();
        }
        return dato;
    }

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
            log.error(uhe, uhe);
            dato = "No se encontro LocalHost" + uhe.toString() + " " + uhe.getMessage();
        }
        return dato;
    }

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

    public static String getFechaEnLetra() {
        String dato;
        dato = getDia() + " de " + getMesEnLetra(convertirCadenaEntero(getMes())) + " del " + getAnio();
        return dato;
    }

    public static void mensaje(String strMensaje, String strTitulo, int iTipoIcono) {
        JOptionPane.showMessageDialog(null, strMensaje, strTitulo, iTipoIcono);
    }

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
            log.error(he, he);
        }
    }

    /**
     *
     * @param tipo
     * @param cantEnt
     * @param cantFra
     * @param caracter
     * @return
     */
    public static DefaultFormatterFactory creaFormatoControl(
            NumberType tipo, int cantEnt, int cantFra, char caracter) {
        DefaultFormatterFactory factory = null;
        String cadena = "";
        int i;

        for (i = 0; i < cantEnt; i++) {
            cadena = cadena + "#";
        }
        if (cantFra > 0) {
            cadena = cadena + ".";
            for (i = 0; i < cantFra; i++) {
                cadena = cadena + "#";
            }
        }
        MaskFormatter numcase;
        switch (tipo) {
            case ENTERO:
                try {
                    numcase = new MaskFormatter(cadena);
                    numcase.setPlaceholderCharacter(caracter);
                    numcase.setOverwriteMode(true);
                    numcase.setValidCharacters("0123456789");
                    factory = new DefaultFormatterFactory(numcase);
                } catch (ParseException pe) {
                    log.error("Error al dar Formato de Numero Entero", pe);
                }
                break;
            case REAL:
                try {
                    numcase = new MaskFormatter(cadena);
                    numcase.setPlaceholderCharacter(caracter);
                    numcase.setOverwriteMode(true);
                    numcase.setValidCharacters("0123456789");
                    factory = new DefaultFormatterFactory(numcase);
                } catch (ParseException pe) {
                    log.error("Error al dar Formato de Numero Real", pe);
                }
                break;
        }
        return factory;
    }
}
