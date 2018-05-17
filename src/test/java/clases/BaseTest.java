package clases;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 16/12/2017.
 *
 * @author Cesardl
 */
public class BaseTest {

    @Ignore
    @Test
    public void formatearNumeroYDigitos() {
        String result = Base.formatearNumeroYDigitos(12312.2211);
        assertEquals(result, "12312,22");

        result = Base.formatearNumeroYDigitos(332312.2291);
        assertEquals(result, "332312,23");
    }

}