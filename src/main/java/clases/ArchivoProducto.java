package clases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author cesardiaz
 */
public final class ArchivoProducto {

    private static final Logger LOG = LoggerFactory.getLogger(ArchivoProducto.class);

    private ArchivoProducto() {
    }

    /**
     * @return cadena numerica aleatoria
     */
    public static String obtenerNumeroSecuencia() {
        return Base.completarIzquierda(String.valueOf(new Random().nextInt(10000)), 5, "0");
    }

    /**
     * @param producto producto a agregar
     * @param ruta     ubicacion del fichero de datos
     */
    public static void crearArchivo(Producto producto, String ruta) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(producto);
        } catch (IOException e) {
            LOG.error("Error de E/S de Archivo: {}", ruta, e);
        }
    }

    /**
     * @param productos objeto destino
     * @param ruta      ubicacion del fichero de datos
     */
    public static void cargarRegistrosArray(List<Producto> productos, String ruta) {
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
                boolean created = archivo.createNewFile();
                if (created) {
                    LOG.debug("Archivo creado en {}", archivo.getAbsolutePath());
                } else {
                    LOG.warn("Archivo no pudo ser creado");
                }
            }
        } catch (EOFException e) {
            LOG.error("Error de fin de Archivo: {}", ruta);
        } catch (ClassNotFoundException e) {
            LOG.error("Clase no encontrada", e);
        } catch (IOException e) {
            LOG.error("Error de E/S de Archivo: {}", ruta, e);
        }
    }

    /**
     * @param ruta ubicacion del fichero de datos
     * @return total de registros
     */
    public static int cantidadRegistros(String ruta) {
        int cantidad = 0;

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
            while (entrada.readObject() != null) {
                cantidad++;
            }
        } catch (EOFException e) {
            LOG.error("Error de fin de Archivo: {}", ruta);
        } catch (ClassNotFoundException e) {
            LOG.error("Clase no encontrada", e);
        } catch (IOException e) {
            LOG.error("Error de E/S de Archivo: {}", ruta, e);
        }

        return cantidad;
    }

    /**
     * @param producto producto a agregar
     * @param ruta     ubicacion del fichero de datos
     */
    public static void adicionarRegistro(Producto producto, String ruta) {
        try (AppendingObjectOutputStream salida = new AppendingObjectOutputStream(new FileOutputStream(ruta, true))) {
            salida.writeUnshared(producto);
            salida.flush();
            LOG.info("Producto agregado: {}", producto);
        } catch (IOException e) {
            LOG.error("Error de E/S de Archivo: {}", ruta, e);
        }
    }

    /**
     * @param producto producto a modificar
     * @param ruta     ubicacion del fichero de datos
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
                LOG.info("Producto modificado: {}", producto);
            } catch (IOException ioe) {
                LOG.error("Error de E/S de Archivo: {}", ruta, ioe);
            }
        }
    }

    /**
     * @param codigo codigo de producto a anular
     * @param ruta   ubicacion del fichero de datos
     * @return registro anulado
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
                LOG.info("Producto anulado: {}", codigo);
            } catch (IOException ioe) {
                LOG.error("Error de E/S de Archivo: {}", ruta, ioe);
            }
        }
        return band;
    }

    /**
     * @param codigo codigo de producto a buscar
     * @param ruta   ubicacion del fichero de datos
     * @return registro consultado
     */
    public static String consultarRegistro(final String codigo, String ruta) {
        ArrayList<Producto> v = new ArrayList<>();

        cargarRegistrosArray(v, ruta);

        LOG.info("Buscando registro con codigo '{}'", codigo);

        return v.stream()
                .filter(p -> p.getCodigo().compareTo(codigo) == 0)
                .findFirst()
                .map(Producto::toString)
                .orElse("");
    }

}
