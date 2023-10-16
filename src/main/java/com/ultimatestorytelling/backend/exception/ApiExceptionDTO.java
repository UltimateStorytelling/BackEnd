package com.ultimatestorytelling.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiExceptionDTO {

    private int state;
    private String message;


}

