package com.ultimatestorytelling.backend.common.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMessage {

    private int httpStatus;
    private String message;
    private Map<String, Object> result;

    @Builder
    public ResponseMessage(int httpStatus, String message, Map<String, Object> result){
        this.httpStatus = httpStatus;
        this.message = message;
        this.result = result;
    }

}
