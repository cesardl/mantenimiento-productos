package clases;

import java.util.ArrayList;

public class ArchivoProducto {

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
                Base.mostrar("Fin de Archivo: " + ruta);
            } catch (java.io.IOException ioe) {
                Base.mostrar("Error de E/S de Archivo: " + ruta);
            }
        } catch (ClassNotFoundException e) {
            Base.mostrar("Clase no encontrada");
        } catch (java.io.IOException e) {
            Base.mostrar("Error de E/S de Archivo: " + ruta);
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
            Base.mostrar("Error de E/S de Archivo: " + ruta);
        }
    }

    public static void adicionarRegistro(Producto producto, String ruta) {
        try {
            MiObjectOutputStream salida = new MiObjectOutputStream(
                    new java.io.FileOutputStream(ruta, true));
            salida.writeUnshared(producto);
            salida.close();
        } catch (java.io.IOException e) {
            Base.mostrar("Error de E/S de Archivo: " + ruta);
        }
    }

    public static void modificarRegistro(Producto producto, String ruta) {
        boolean band = false;
        ArrayList<Producto> v = new ArrayList<Producto>();
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
                Base.mostrar("Error: " + ioe.getMessage());
            }
        }
    }

    public static boolean anularRegistro(String strCodigo, String ruta) {
        boolean band = false;
        ArrayList<Producto> v = new ArrayList<Producto>();
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
                Base.mostrar("Error: " + ioe.getMessage());
            }
        }
        return band;
    }

    public static String consultarRegistro(String codigo, String ruta) {
        ArrayList<Producto> v = new ArrayList<Producto>();
        cargarRegistrosArray(v, ruta);
        for (int i = 0; i < v.size(); i++) {
            Producto p = v.get(i);
            if (p.getCodigo().equals(codigo)) {
                return p.toString();
            }
        }
        return "";
    }

    public static ArrayList<Producto> cargarRegistrosArray(ArrayList<Producto> vProducto, String ruta) {
        java.io.ObjectInputStream entrada = null;

        try {
            entrada = new java.io.ObjectInputStream(new java.io.FileInputStream(ruta));
            Producto auxProducto;
            while ((auxProducto = (Producto) entrada.readObject()) != null) {
                vProducto.add(auxProducto);
            }
            entrada.close();
        } catch (java.io.EOFException eofe) {
            try {
                if (entrada != null) {
                    entrada.close();
                }
                Base.mostrar("Fin de Archivo: " + ruta);
            } catch (java.io.IOException ioe) {
                Base.mostrar("Error de E/S de Archivo: " + ruta);
            }
        } catch (ClassNotFoundException cnfe) {
            Base.mostrar("Clase no encontrada");
        } catch (java.io.IOException ioe) {
            Base.mostrar("Error de E/S de Archivo: " + ruta);
        }

        return vProducto;
    }
}
