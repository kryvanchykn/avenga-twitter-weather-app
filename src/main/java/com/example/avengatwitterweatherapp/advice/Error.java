package com.example.avengatwitterweatherapp.advice;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class Error {
    int errorCode;
    String exception;
    List<String> errorMessages;

    @Override
    public String toString() {
        return "errorCode: " + errorCode +
                "\nexception: " + exception +
                "\nerrorMessages:" + Arrays.toString(errorMessages.stream().map(message -> "\n" + message).toArray())
                .replace("[", "")
                .replace("]", "");
    }
}
