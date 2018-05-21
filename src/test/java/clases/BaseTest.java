package clases;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created on 16/12/2017.
 *
 * @author Cesardl
 */
public class BaseTest {

    @Test
    public void formatearNumeroYDigitos() {
        String result = Base.formatearNumeroYDigitos(12312.2211);
        assertEquals("12312.22", result);

        result = Base.formatearNumeroYDigitos(332312.2291);
        assertEquals("332312.23", result);
    }

    @Test
    public void convertirCadenaEntero() {
        int result = Base.convertirCadenaEntero("11");
        assertEquals(11, result);

        result = Base.convertirCadenaEntero("abc");
        assertEquals(-99999, result);
    }
}