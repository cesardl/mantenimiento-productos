package org.sanmarcux.clases;

/**
 * @author cesar.diaz
 */
public class Producto implements java.io.Serializable {

    private static final long serialVersionUID = 3762613621712220046L;

    private final String codigo;
    private final String descripcion;
    private final int total;
    private final double precio;
    private final boolean exonerado;
    private final boolean visible;

    public Producto(String codigo, String descripcion, int total, double precio,
                    boolean exonerado, boolean visible) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.total = total;
        this.precio = precio;
        this.exonerado = exonerado;
        this.visible = visible;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTotal() {
        return total;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isExonerado() {
        return exonerado;
    }

    public boolean isVisible() {
        return visible;
    }
}
