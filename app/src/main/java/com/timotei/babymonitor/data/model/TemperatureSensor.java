package com.timotei.babymonitor.data.model;

import java.util.Objects;

public class TemperatureSensor {

    private String degrees;
    private String status;

    public TemperatureSensor() {
    }

    public TemperatureSensor(String degrees, String status) {
        this.degrees = degrees;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureSensor that = (TemperatureSensor) o;
        return  Objects.equals(degrees, that.degrees) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degrees,status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }
}
