package com.ultimatestorytelling.backend.novel.command.application.dto;

import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class NovelRequestDto {

    private Long novelId;
    private String novelName;
    private String novelWriter;
    private String mainCategory;
    private String subCategory;
    private String minCategory;
    private String novelDetail;
    private Long novelView;

    public Novel toEntity() {
        return Novel.builder()
                .novelId(novelId)
                .novelName(novelName)
                .novelDetail(novelDetail)
                .novelWriter(novelWriter)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .minCategory(minCategory)
                .build();
    }

}
