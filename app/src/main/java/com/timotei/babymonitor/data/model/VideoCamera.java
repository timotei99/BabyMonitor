package com.timotei.babymonitor.data.model;

import android.util.Log;

import java.util.Objects;

public class VideoCamera {
    private String status;

    public VideoCamera(){
    }

    public VideoCamera(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        Log.d("VIDEO_CAMERA","Setting status to "+status);
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCamera that = (VideoCamera) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
