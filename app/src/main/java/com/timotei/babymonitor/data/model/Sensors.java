package com.timotei.babymonitor.data.model;

import java.util.Objects;

public class Sensors {
    private String air_quality;
    private String current_weight;
    private String heart_rate;
    private String humidity;
    private String last_weight;
    private String temperature;

    public Sensors() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensors sensors = (Sensors) o;
        return Objects.equals(air_quality, sensors.air_quality) && Objects.equals(current_weight, sensors.current_weight) && Objects.equals(heart_rate, sensors.heart_rate) && Objects.equals(humidity, sensors.humidity) && Objects.equals(last_weight, sensors.last_weight) && Objects.equals(temperature, sensors.temperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(air_quality, current_weight, heart_rate, humidity, last_weight, temperature);
    }

    public String getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(String air_quality) {
        this.air_quality = air_quality;
    }

    public String getCurrent_weight() {
        return current_weight;
    }

    public void setCurrent_weight(String current_weight) {
        this.current_weight = current_weight;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLast_weight() {
        return last_weight;
    }

    public void setLast_weight(String last_weight) {
        this.last_weight = last_weight;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
