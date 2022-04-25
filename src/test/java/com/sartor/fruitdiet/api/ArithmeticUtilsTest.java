package com.sartor.fruitdiet.api;

import com.sartor.fruitdiet.api.utils.ArithmeticUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArithmeticUtilsTest {

    @Test
    public void sum_shouldSum() {
        assertEquals("6.66", ArithmeticUtils.sum(6.11, 0.554).toString());
        assertEquals("6.66999", ArithmeticUtils.sum(6.110000001, 0.55999, 5).toString());
    }

    @Test
    public void divide_shouldDivide() {
        assertEquals("0.33", ArithmeticUtils.divide(1.0, 3).toString());
        assertEquals("0.3333", ArithmeticUtils.divide(1.0, 3, 4).toString());
    }

    @Test
    public void subtract_shouldSubtract() {
        assertEquals("0.42", ArithmeticUtils.subtract(1.0, 0.5811).toString());
        assertEquals("0.0042", ArithmeticUtils.subtract(1.0, 0.9958, 4).toString());
    }

}
