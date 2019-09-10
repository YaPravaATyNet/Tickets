package com.example.tickets;

import java.util.Objects;

public class Segment {
    private double value;
    private double firstValue;
    private double secondValue;
    private int separator;
    private char sign;

    public Segment(double value, double firstValue, double secondValue, int separator, char sign) {
        this.value = value;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.separator = separator;
        this.sign = sign;
    }

    public Segment(double value, int separator, char sign) {
        this.value = value;
        this.firstValue = value;
        this.secondValue = value;
        this.separator = separator;
        this.sign = sign;
    }

    public double getValue() {
        return value;
    }

    public double getFirstValue() {
        return firstValue;
    }

    public double getSecondValue() {
        return secondValue;
    }

    public int getSeparator() {
        return separator;
    }

    public char getSign() {
        return sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Double.compare(segment.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
