package org.sanmarcux.mantenimiento;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created on 17/06/2018.
 *
 * @author Cesardl
 */
public class JFrameRegistroTest {

    @Test
    public void checkTest() {
        JFrameRegistro frame = new JFrameRegistro("target/another.dat");
        assertFalse(frame.isVisible());
    }

}