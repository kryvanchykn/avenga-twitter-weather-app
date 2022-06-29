package com.example.avengatwitterweatherapp.model;

import lombok.Getter;

@Getter
public enum SortField{
    REGION("region"),
    STRIKE_DATE("strikeDate");

    private final String name;
    SortField(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
