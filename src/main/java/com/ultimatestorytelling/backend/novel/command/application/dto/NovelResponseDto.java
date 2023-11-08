package com.ultimatestorytelling.backend.novel.command.application.dto;

import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class NovelResponseDto {

    private Long novelId;
    private String novelName;
    private Long novelLike;
    private String novelWriter;
    private String mainCategory;
    private String subCategory;
    private String minCategory;
    private String novelDetail;
    private Long novelView;

    public NovelResponseDto(Novel entity) {
        this.novelId = entity.getNovelId();
        this.novelName = entity.getNovelName();
        this.novelLike = entity.getNovelLike();
        this.novelWriter = entity.getNovelWriter();
        this.mainCategory = entity.getMainCategory();
        this.subCategory = entity.getSubCategory();
        this.minCategory = entity.getMinCategory();
        this.novelDetail = entity.getNovelDetail();
        this.novelView = entity.getNovelView();
    }
    public static class ResponseDto {

        private int status;
        private String message;
        private Object result;

        public ResponseDto(HttpStatus status, String message, Object result) {
            this.status = status.value();
            this.message = message;
            this.result = result;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "ResponseDto{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    ", result=" + result +
                    '}';
        }
    }
}
