package com.palenica.david;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void testLoss() {
        assertEquals(0.0, Utils.loss(1.0, 1.0), 0.0);
        assertEquals(0.0, Utils.loss(0.0, 0.0), 0.0);

        assertEquals(Double.POSITIVE_INFINITY, Utils.loss(1.0, 0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, Utils.loss(0.0, 1.0), 0.0);

        assertEquals(Double.POSITIVE_INFINITY, Utils.loss(0.5, 0.0), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, Utils.loss(0.5, 1.0), 0.0);

        assertEquals(Math.log(2.0), Utils.loss(1.0, 0.5), 0.0);
        assertEquals(Math.log(2.0), Utils.loss(0.0, 0.5), 0.0);

        assertEquals(Math.log(4.0), Utils.loss(1.0, 0.25), 0.0);
        assertEquals(Math.log(4.0 / 3.0), Utils.loss(0.0, 0.25), 1e-16);
    }

}