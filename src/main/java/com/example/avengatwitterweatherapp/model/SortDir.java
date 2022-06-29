package com.example.avengatwitterweatherapp.model;

import lombok.Getter;

@Getter
public enum SortDir{
    ASC("asc"),
    DESC("desc");

    private final String name;
    SortDir(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
