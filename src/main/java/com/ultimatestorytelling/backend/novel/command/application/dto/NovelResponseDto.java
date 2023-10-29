package com.ultimatestorytelling.backend.novel.command.application.dto;

import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.Getter;

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
}
