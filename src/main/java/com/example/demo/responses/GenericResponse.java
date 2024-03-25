package com.example.demo.responses;

import java.util.List;

public class GenericResponse<T> {
    private List<T> data;
    private long total;

    public GenericResponse(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    // Getters and Setters
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
