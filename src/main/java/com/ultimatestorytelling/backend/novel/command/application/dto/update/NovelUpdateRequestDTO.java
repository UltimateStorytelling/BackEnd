package com.ultimatestorytelling.backend.novel.command.application.dto.update;

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


}
