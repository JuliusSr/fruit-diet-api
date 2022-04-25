package com.sartor.fruitdiet.api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ArithmeticUtils {

    public static Double divide(Double dividend, Integer divisor, int decimals) {
        return new BigDecimal(dividend.toString())
                .setScale(decimals, RoundingMode.HALF_UP)
                .divide(new BigDecimal(divisor.toString()),RoundingMode.HALF_UP)
                .setScale(decimals, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Double divide(Double dividend, Integer divisor) {
        return divide(dividend, divisor, 2);
    }

    public static Double subtract(Double minuend, Double subtrahend, int decimals) {
        return new BigDecimal(minuend.toString())
                .setScale(decimals, RoundingMode.HALF_UP)
                .subtract(new BigDecimal(subtrahend.toString()))
                .setScale(decimals, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Double subtract(Double minuend, Double subtrahend) {
        return subtract(minuend, subtrahend, 2);
    }

    public static Double sum(Double addend, Double augend, int decimals) {
        return new BigDecimal(addend.toString())
                .add(new BigDecimal(augend.toString()))
                .setScale(decimals, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Double sum(Double addend, Double augend) {
        return sum(addend, augend, 2);
    }

}
