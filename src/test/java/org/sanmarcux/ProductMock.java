package org.sanmarcux;

import org.sanmarcux.clases.ArchivoProducto;
import org.sanmarcux.clases.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17/06/2018.
 *
 * @author Cesardl
 */
public class ProductMock {

    private static ProductMock ourInstance = new ProductMock();

    public static ProductMock getInstance() {
        return ourInstance;
    }

    private List<Producto> vProducts;

    private ProductMock() {
        vProducts = new ArrayList<>();

        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "CPU INTEL CORE I5-6500 6M 3.2 GHZ LGA 1151 SEXTA GEN", 100, 238, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "PLACA MSI Z17A GAMING PRO DD4", 100, 230, true, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "MEMORIA 8GB DD4 BUS 2133", 100, 60, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "DISCO D 1TB SATA III SEAGATE 64 MB 72RPM", 100, 52, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "GRABADOR DVD 24X ASUS NEGRO", 100, 17, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "VIDEO MSI NVIDIA GTX970 4GB GDDR5 256BITS", 100, 430, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "VIDEO MSI NVIDIA GTX980 4GB GDDR5 256BITS", 100, 504, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "CASE CORSAIR 500R MT BLACK", 100, 125, true, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "FUENTE 750W CORSAIR CX750W 80+BRONZ", 100, 99, true, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "TECLADO Y MOUSE LOGITECH MK120 USB SP", 100, 10, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "ESTABILIZADOR FORZA 8 AVR 220V FVR-1202", 100, 13, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "MONITOR 27 LED ASUS IPS MX279H FH 5M FULL HD", 100, 320, false, true));
        this.vProducts.add(new Producto(ArchivoProducto.obtenerNumeroSecuencia(), "DISCO SOLIDO SSD 240GB A-DATA SP550 2.5 SATA 6GB", 100, 96, true, false));
    }

    public List<Producto> getProducts() {
        return vProducts;
    }
}
