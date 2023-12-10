package com.ultimatestorytelling.backend.novel.command.application.dto.create;

import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NovelCreateRequestDTO {

    private String novelName;
    private String mainCategory;
    private String subCategory;
    private String minCategory;
    private String novelDetail;

    @Builder
    public NovelCreateRequestDTO(String novelName, String mainCategory, String subCategory, String minCategory, String novelDetail) {
        this.novelName = novelName;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.minCategory = minCategory;
        this.novelDetail = novelDetail;
    }
    public Novel toEntity(Member member) {
        return Novel.builder()
                .novelName(novelName)
                .novelDetail(novelDetail)
                .mainCategory(mainCategory)
                .subCategory(subCategory)
                .minCategory(minCategory)
                .novelIsDeleted(false)
                .member(member)
                .build();
    }
}
