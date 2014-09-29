package clases;

import org.apache.log4j.Logger;

public class ArchivoProducto {

    private static Logger log = Logger.getLogger(ArchivoProducto.class);

    public static int cantidadRegistros(String ruta) {
        int cantidad = 0;
        java.io.ObjectInputStream entrada = null;
        try {
            entrada = new java.io.ObjectInputStream(new java.io.FileInputStream(ruta));
            while (((Producto) entrada.readObject()) != null) {
                cantidad++;
            }
            entrada.close();
        } catch (java.io.EOFException e) {
            try {
                if (entrada != null) {
                    entrada.close();
                }
                log.info("Fin de Archivo: " + ruta);
            } catch (java.io.IOException ioe) {
                log.error("Error de E/S de Archivo: " + ruta, e);
            }
        } catch (ClassNotFoundException e) {
            log.error("Clase no encontrada", e);
        } catch (java.io.IOException e) {
            log.error("Error de E/S de Archivo: " + ruta, e);
        }

        return cantidad;
    }

    public static String obtenerNumeroSecuencia() {
        return Base.completarIzquierda(String.valueOf((int) Math.round(Math.random() * 10000)), 5, "0");
    }

    public static void crearArchivo(Producto producto, String ruta) {
        try {
            java.io.ObjectOutputStream salida = new java.io.ObjectOutputStream(
                    new java.io.FileOutputStream(ruta));
            salida.writeObject(producto);
            salida.close();
        } catch (java.io.IOException e) {
            log.error("Error de E/S de Archivo: " + ruta, e);
        }
    }

    public static void adicionarRegistro(Producto producto, String ruta) {
        try {
            MiObjectOutputStream salida = new MiObjectOutputStream(
                    new java.io.FileOutputStream(ruta, true));
            salida.writeUnshared(producto);
            salida.close();
        } catch (java.io.IOException e) {
            log.error("Error de E/S de Archivo: " + ruta, e);
        }
    }

    public static void modificarRegistro(Producto producto, String ruta) {
        boolean band = false;
        java.util.ArrayList<Producto> v = new java.util.ArrayList<Producto>();
        cargarRegistrosArray(v, ruta);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().equals(producto.getCodigo())) {
                v.set(i, producto);
                band = true;
                break;
            }
        }
        if (band) {
            try {
                java.io.ObjectOutputStream salida = new java.io.ObjectOutputStream(
                        new java.io.FileOutputStream(ruta));
                salida.reset();
                for (int i = 0; i < v.size(); i++) {
                    salida.writeObject(v.get(i));
                }
                salida.close();
            } catch (java.io.IOException ioe) {
                log.error(ioe, ioe);
            }
        }
    }

    public static boolean anularRegistro(String strCodigo, String ruta) {
        boolean band = false;
        java.util.ArrayList<Producto> v = new java.util.ArrayList<Producto>();
        cargarRegistrosArray(v, ruta);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().equals(strCodigo)) {
                v.remove(i);
                band = true;
                break;
            }
        }
        if (band) {
            try {
                java.io.ObjectOutputStream salida = new java.io.ObjectOutputStream(
                        new java.io.FileOutputStream(ruta));
                salida.reset();
                for (int i = 0; i < v.size(); i++) {
                    salida.writeObject(v.get(i));
                }
                salida.close();
            } catch (java.io.IOException ioe) {
                log.error(ioe, ioe);
            }
        }
        return band;
    }

    public static String consultarRegistro(String codigo, String ruta) {
        java.util.ArrayList<Producto> v = new java.util.ArrayList<Producto>();
        cargarRegistrosArray(v, ruta);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().equals(codigo)) {
                return p.toString();
            }
        }
        return "";
    }

    public static java.util.ArrayList<Producto> cargarRegistrosArray(
            java.util.ArrayList<Producto> vProducto, String ruta) {
        java.io.ObjectInputStream entrada = null;

        try {
            java.io.File archivo = new java.io.File(ruta);
            if (archivo.exists()) {
                entrada = new java.io.ObjectInputStream(new java.io.FileInputStream(archivo));
                Producto auxProducto;
                while ((auxProducto = (Producto) entrada.readObject()) != null) {
                    vProducto.add(auxProducto);
                }
                entrada.close();
            } else {
                archivo.createNewFile();
                log.info("Archivo creado en " + archivo.getAbsolutePath());
            }
        } catch (java.io.EOFException eofe) {
            try {
                if (entrada != null) {
                    entrada.close();
                }
                log.info("Fin de Archivo: " + ruta);
            } catch (java.io.IOException ioe) {
                log.error("Error de E/S de Archivo: " + ruta, ioe);
            }
        } catch (ClassNotFoundException cnfe) {
            log.error("Clase no encontrada", cnfe);
        } catch (java.io.IOException ioe) {
            log.error("Error de E/S de Archivo: " + ruta, ioe);
        }

        return vProducto;
    }
}
