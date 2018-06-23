package org.sanmarcux.clases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author cesar.diaz
 */
public final class ArchivoProducto {

    private static final Logger LOG = LoggerFactory.getLogger(ArchivoProducto.class);

    private static final String IO_ERROR_MESSAGE = "Error de E/S de Archivo: {}";

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
     * @param path     ubicacion del fichero de datos
     */
    public static boolean crearArchivo(final Producto producto, final String path) {
        boolean band;
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(path))) {
            salida.writeObject(producto);
            LOG.info("Creando nuevo archivo, se registro el primer producto");
            band = true;
        } catch (IOException e) {
            LOG.error(IO_ERROR_MESSAGE, path, e);
            band = false;
        }
        return band;
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
                Path directories = Files.createDirectories(Paths.get(archivo.getParent()));
                boolean created = directories.toFile().createNewFile();
                if (created) {
                    LOG.debug("Archivo creado en {}", archivo.getAbsolutePath());
                } else {
                    LOG.warn("Archivo no pudo ser creado en {}", archivo.getAbsolutePath());
                }
            }
        } catch (EOFException e) {
            LOG.info("Se cargaron {} registros de productos", productos.size());

            LOG.error("Error de fin de Archivo: {}", ruta);
        } catch (ClassNotFoundException e) {
            LOG.error("Clase no encontrada", e);
        } catch (IOException e) {
            LOG.error(IO_ERROR_MESSAGE, ruta, e);
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
            LOG.error(IO_ERROR_MESSAGE, ruta, e);
        }

        return cantidad;
    }

    /**
     * @param producto producto a agregar
     * @param ruta     ubicacion del fichero de datos
     */
    public static boolean adicionarRegistro(Producto producto, String ruta) {
        boolean band;
        try (AppendingObjectOutputStream salida = new AppendingObjectOutputStream(new FileOutputStream(ruta, true))) {
            salida.writeUnshared(producto);
            salida.flush();
            LOG.info("Producto agregado: {}", producto.getCodigo());
            band = true;
        } catch (IOException e) {
            LOG.error(IO_ERROR_MESSAGE, ruta, e);
            band = false;
        }
        return band;
    }

    /**
     * @param producto producto a modificar
     * @param ruta     ubicacion del fichero de datos
     */
    public static boolean modificarRegistro(Producto producto, String ruta) {
        boolean band = false;
        List<Producto> v = new ArrayList<>();
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
                LOG.info("Producto modificado: {}", producto.getCodigo());
            } catch (IOException ioe) {
                LOG.error(IO_ERROR_MESSAGE, ruta, ioe);
            }
        } else {
            LOG.warn("No hay producto a modificar");
        }
        return band;
    }

    /**
     * @param codigo codigo de producto a anular
     * @param ruta   ubicacion del fichero de datos
     * @return registro anulado
     */
    public static boolean anularRegistro(String codigo, String ruta) {
        boolean band = false;
        List<Producto> v = new ArrayList<>();
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
                LOG.error(IO_ERROR_MESSAGE, ruta, ioe);
            }
        }
        return band;
    }

    /**
     * @param codigo codigo de producto a buscar
     * @param ruta   ubicacion del fichero de datos
     * @return registro consultado
     */
    public static Producto consultarRegistro(final String codigo, final String ruta) {
        List<Producto> v = new ArrayList<>();

        cargarRegistrosArray(v, ruta);

        LOG.info("Buscando registro con codigo '{}'", codigo);

        return v.stream()
                .filter(p -> p.getCodigo().compareTo(codigo) == 0)
                .findFirst()
                .orElse(null);
    }

}
