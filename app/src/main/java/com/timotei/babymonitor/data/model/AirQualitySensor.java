package com.timotei.babymonitor.data.model;

import java.util.Objects;

public class AirQualitySensor {

    private String value;

    public AirQualitySensor() {
    }

    public AirQualitySensor(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirQualitySensor that = (AirQualitySensor) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
