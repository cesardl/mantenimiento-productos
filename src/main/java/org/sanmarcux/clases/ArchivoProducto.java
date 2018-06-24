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
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(producto);
            LOG.info("Creando nuevo archivo, se registro el primer producto");
            band = true;
        } catch (IOException e) {
            LOG.error(IO_ERROR_MESSAGE, path, e);
            band = false;
        }
        return band;
    }

    /**
     * @param products objeto destino
     * @param path     ubicacion del fichero de datos
     */
    public static void cargarRegistrosArray(final List<Producto> products, final String path) {
        try {
            File archivo = new File(path);
            if (archivo.exists()) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(archivo))) {
                    Producto auxProducto;
                    while ((auxProducto = (Producto) inputStream.readObject()) != null) {
                        products.add(auxProducto);
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
            LOG.info("Se cargaron {} registros de products", products.size());

            LOG.error("Error de fin de Archivo: {}", path);
        } catch (ClassNotFoundException e) {
            LOG.error("Clase no encontrada", e);
        } catch (IOException e) {
            LOG.error(IO_ERROR_MESSAGE, path, e);
        }
    }

    /**
     * @param path ubicacion del fichero de datos
     * @return total de registros
     */
    public static int cantidadRegistros(final String path) {
        int cantidad = 0;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
            while (inputStream.readObject() != null) {
                cantidad++;
            }
        } catch (EOFException e) {
            LOG.error("Error de fin de Archivo: {}", path);
        } catch (ClassNotFoundException e) {
            LOG.error("Clase no encontrada", e);
        } catch (IOException e) {
            LOG.error(IO_ERROR_MESSAGE, path, e);
        }

        return cantidad;
    }

    /**
     * @param producto producto a agregar
     * @param ruta     ubicacion del fichero de datos
     */
    public static boolean adicionarRegistro(final Producto producto, final String ruta) {
        boolean band;
        try (AppendingObjectOutputStream outputStream = new AppendingObjectOutputStream(new FileOutputStream(ruta, true))) {
            outputStream.writeUnshared(producto);
            outputStream.flush();
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
     * @param path     ubicacion del fichero de datos
     */
    public static boolean modificarRegistro(final Producto producto, final String path) {
        boolean band = false;
        List<Producto> v = new ArrayList<>();
        cargarRegistrosArray(v, path);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().compareTo(producto.getCodigo()) == 0) {
                v.set(i, producto);
                band = true;
                break;
            }
        }
        if (band) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
                outputStream.reset();
                for (Producto p : v) {
                    outputStream.writeObject(p);
                }
                LOG.info("Producto modificado: {}", producto.getCodigo());
            } catch (IOException ioe) {
                LOG.error(IO_ERROR_MESSAGE, path, ioe);
            }
        } else {
            LOG.warn("No hay producto a modificar");
        }
        return band;
    }

    /**
     * @param code code de producto a anular
     * @param path ubicacion del fichero de datos
     * @return registro anulado
     */
    public static boolean anularRegistro(final String code, final String path) {
        boolean band = false;
        List<Producto> v = new ArrayList<>();
        cargarRegistrosArray(v, path);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().compareTo(code) == 0) {
                v.remove(i);
                band = true;
                break;
            }
        }

        if (band) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
                outputStream.reset();
                for (Producto p : v) {
                    outputStream.writeObject(p);
                }
                LOG.info("Producto anulado: {}", code);
            } catch (IOException ioe) {
                LOG.error(IO_ERROR_MESSAGE, path, ioe);
            }
        }
        return band;
    }

    /**
     * @param code code de producto a buscar
     * @param path ubicacion del fichero de datos
     * @return registro consultado
     */
    public static Producto consultarRegistro(final String code, final String path) {
        List<Producto> v = new ArrayList<>();

        cargarRegistrosArray(v, path);

        LOG.info("Buscando registro con code '{}'", code);

        return v.stream()
                .filter(p -> p.getCodigo().compareTo(code) == 0)
                .findFirst()
                .orElse(null);
    }

}
