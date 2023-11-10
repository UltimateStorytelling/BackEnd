package com.ultimatestorytelling.backend.novel.command.application.dto.update;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class NovelUpdateRequestDTO {

    private String novelName;
    private String mainCategory;
    private String subCategory;
    private String minCategory;
    private String novelDetail;

    @Builder
    public NovelUpdateRequestDTO(String novelName, String mainCategory, String subCategory, String minCategory, String novelDetail) {
        this.novelName = novelName;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.minCategory = minCategory;
        this.novelDetail = novelDetail;
    }
}
