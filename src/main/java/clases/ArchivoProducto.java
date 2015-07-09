package clases;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cesardiaz
 */
public class ArchivoProducto {

    private static final Logger log = LoggerFactory.getLogger(ArchivoProducto.class);

    /**
     *
     * @return
     */
    public static String obtenerNumeroSecuencia() {
        return Base.completarIzquierda(String.valueOf((int) Math.round(Math.random() * 10000)), 5, "0");
    }

    /**
     *
     * @param producto
     * @param ruta
     */
    public static void crearArchivo(Producto producto, String ruta) {
        try (ObjectOutputStream salida = new ObjectOutputStream(
                new FileOutputStream(ruta))) {
            salida.writeObject(producto);
        } catch (IOException e) {
            log.error("Error de E/S de Archivo: {}", ruta, e);
        }
    }

    /**
     *
     * @param productos
     * @param ruta
     * @return
     */
    public static ArrayList<Producto> cargarRegistrosArray(ArrayList<Producto> productos, String ruta) {
        try {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
                    Producto auxProducto;
                    while ((auxProducto = (Producto) entrada.readObject()) != null) {
                        productos.add(auxProducto);
                    }
                }
            } else {
                archivo.createNewFile();
                log.info("Archivo creado en {}", archivo.getAbsolutePath());
            }
        } catch (EOFException e) {
            log.error("Error de fin de Archivo: {}", ruta);
        } catch (ClassNotFoundException e) {
            log.error("Clase no encontrada", e);
        } catch (IOException e) {
            log.error("Error de E/S de Archivo: {}", ruta, e);
        }

        return productos;
    }

    /**
     *
     * @param ruta
     * @return
     */
    public static int cantidadRegistros(String ruta) {
        int cantidad = 0;

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
            while (entrada.readObject() != null) {
                cantidad++;
            }
        } catch (EOFException e) {
            log.error("Error de fin de Archivo: {}", ruta);
        } catch (ClassNotFoundException e) {
            log.error("Clase no encontrada", e);
        } catch (IOException e) {
            log.error("Error de E/S de Archivo: {}", ruta, e);
        }

        return cantidad;
    }

    /**
     *
     * @param producto
     * @param ruta
     */
    public static void adicionarRegistro(Producto producto, String ruta) {
        try (AppendingObjectOutputStream salida = new AppendingObjectOutputStream(new FileOutputStream(ruta, true))) {
            salida.writeUnshared(producto);
            salida.flush();
            log.info("Producto agregado: {}", producto);
        } catch (IOException e) {
            log.error("Error de E/S de Archivo: {}", ruta, e);
        }
    }

    /**
     *
     * @param producto
     * @param ruta
     */
    public static void modificarRegistro(Producto producto, String ruta) {
        boolean band = false;
        ArrayList<Producto> v = new ArrayList<>();
        cargarRegistrosArray(v, ruta);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().compareTo(producto.getCodigo()) == 0) {
                v.set(i, producto);
                band = true;
                break;
            }
        }
        if (band) {
            try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
                salida.reset();
                for (Producto p : v) {
                    salida.writeObject(p);
                }
                log.info("Producto modificado: {}", producto);
            } catch (IOException ioe) {
                log.error("Error de E/S de Archivo: {}", ruta, ioe);
            }
        }
    }

    /**
     *
     * @param codigo
     * @param ruta
     * @return
     */
    public static boolean anularRegistro(String codigo, String ruta) {
        boolean band = false;
        ArrayList<Producto> v = new ArrayList<>();
        cargarRegistrosArray(v, ruta);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().compareTo(codigo) == 0) {
                v.remove(i);
                band = true;
                break;
            }
        }

        if (band) {
            try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
                salida.reset();
                for (Producto p : v) {
                    salida.writeObject(p);
                }
                log.info("Producto anulado: {}", codigo);
            } catch (IOException ioe) {
                log.error("Error de E/S de Archivo: {}", ruta, ioe);
            }
        }
        return band;
    }

    /**
     *
     * @param codigo
     * @param ruta
     * @return
     */
    public static String consultarRegistro(String codigo, String ruta) {
        ArrayList<Producto> v = new ArrayList<>();

        cargarRegistrosArray(v, ruta);

        for (Producto p : v) {
            if (p.getCodigo().compareTo(codigo) == 0) {
                return p.toString();
            }
        }
        return "";
    }

}
