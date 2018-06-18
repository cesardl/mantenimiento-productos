package org.sanmarcux.clases;

import org.junit.Before;
import org.junit.Test;
import org.sanmarcux.ProductMock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created on 17/06/2018.
 *
 * @author Cesardl
 */
public class ArchivoProductoTest {

    private static final String PATH = "target/product.dat";

    private ProductMock mock = ProductMock.getInstance();

    @Before
    public void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(PATH));

        for (int i = 3; i < mock.getProducts().size(); i++) {
            Producto p = mock.getProducts().get(i);
            if (i == 3) {
                ArchivoProducto.crearArchivo(p, PATH);
            } else {
                ArchivoProducto.adicionarRegistro(p, PATH);
            }
        }

        int total = ArchivoProducto.cantidadRegistros(PATH);
        assertEquals(10, total);
    }

    @Test
    public void registerAndUnregisterProductsTest() {
        Producto product = mock.getProducts().get(1);

        ArchivoProducto.adicionarRegistro(product, PATH);
        int total = ArchivoProducto.cantidadRegistros(PATH);

        assertEquals(11, total);

        boolean result = ArchivoProducto.anularRegistro(product.getCodigo(), PATH);
        assertTrue(result);

        total = ArchivoProducto.cantidadRegistros(PATH);
        assertEquals(10, total);
    }

    @Test
    public void updatingProductTest() {
        Producto product = mock.getProducts().get(12);

        ArchivoProducto.modificarRegistro(product, PATH);

        String result = ArchivoProducto.consultarRegistro(product.getCodigo(), PATH);
        assertFalse(result.isEmpty());
    }

    @Test
    public void tryingToUpdateUnregisterProductTest() {
        Producto p = mock.getProducts().get(0);
        ArchivoProducto.modificarRegistro(p, PATH);

        String result = ArchivoProducto.consultarRegistro(p.getCodigo(), PATH);
        assertTrue(result.isEmpty());
    }
}