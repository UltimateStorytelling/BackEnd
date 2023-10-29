package com.ultimatestorytelling.backend.common.message;

import com.ultimatestorytelling.backend.novel.command.application.dto.NovelDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ResponseMessage {

    private int httpStatus;
    private String message;
    private Map<String, Object> results;

    @Builder
    public ResponseMessage(int httpStatus, String message, Map<String, Object> results){
        this.httpStatus = httpStatus;
        this.message = message;
        this.results = results;
    }


}
