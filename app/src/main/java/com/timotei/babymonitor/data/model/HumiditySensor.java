package com.timotei.babymonitor.data.model;

import java.util.Objects;

public class HumiditySensor {


    private String status;
    private String value;

    public HumiditySensor() {
    }

    public HumiditySensor(String status, String value) {
        this.status = status;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumiditySensor that = (HumiditySensor) o;
        return Objects.equals(status, that.status) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, value);
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
