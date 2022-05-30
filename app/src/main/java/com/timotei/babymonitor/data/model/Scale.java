package com.timotei.babymonitor.data.model;

import java.util.Objects;

public class Scale {
    private String status;
    private String value;
    private String last_weight;

    public Scale() {
    }

    public Scale(String status, String value, String last_weight) {
        this.status = status;
        this.value = value;
        this.value = last_weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scale scale = (Scale) o;
        return Objects.equals(status, scale.status) && Objects.equals(value, scale.value) && Objects.equals(last_weight, scale.last_weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, value,last_weight);
    }

    public String getLast_weight() {
        return last_weight;
    }

    public void setLast_weight(String last_weight) {
        this.last_weight = last_weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
