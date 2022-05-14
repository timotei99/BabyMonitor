package com.timotei.babymonitor.data.model;

import java.util.Objects;

public class PulseSensor {

    private String rate;
    private String status;

    public PulseSensor() {
    }

    public PulseSensor(String rate, String status) {
        this.rate = rate;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PulseSensor that = (PulseSensor) o;
        return Objects.equals(rate, that.rate) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate, status);
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
