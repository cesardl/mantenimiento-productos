package clases;

/**
 *
 * @author cesardiaz
 */
public class Producto implements java.io.Serializable {

    private int id;
    private String codigo;
    private String descripcion;
    private int total;
    private double precio;
    private boolean exonerado;
    private boolean visible;

    public Producto() {
        this.id = 0;
        this.codigo = "00000";
        this.descripcion = "NODESCRIPCION";
        this.total = 0;
        this.precio = 0;
        this.exonerado = true;
        this.visible = true;
    }

    public Producto(String codigo, String descripcion, int total, double precio,
            boolean exonerado, boolean visible) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.total = total;
        this.precio = precio;
        this.exonerado = exonerado;
        this.visible = visible;
    }

    public Producto(int id, String codigo, String descripcion, int total, double precio,
            boolean exonerado, boolean visible) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.total = total;
        this.precio = precio;
        this.exonerado = exonerado;
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isExonerado() {
        return exonerado;
    }

    public void setExonerado(boolean exonerado) {
        this.exonerado = exonerado;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Codigo: ");
        builder.append(codigo);
        builder.append("\nDescripcion: ");
        builder.append(descripcion);
        builder.append("\nCantidad: ");
        builder.append(total);
        builder.append("\nPrecio: ");
        builder.append(precio);
        builder.append("\nExonerado: ");
        builder.append(exonerado);
        builder.append("\nVisible: ");
        builder.append(visible);

        return builder.toString();
    }
}
