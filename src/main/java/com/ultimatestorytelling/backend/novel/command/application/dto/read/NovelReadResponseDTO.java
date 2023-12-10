package com.ultimatestorytelling.backend.novel.command.application.dto.read;

import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NovelReadResponseDTO {

    private Long novelNo;
    private String novelName;
    private String memberNickname;
    private String mainCategory;
    private String subCategory;
    private String minCategory;
    private String novelDetail;
    private Long novelView;



    public NovelReadResponseDTO(Novel novel){
        this.novelNo = novel.getNovelNo();
        this.novelName = novel.getNovelName();
        this.memberNickname = novel.getMember().getMemberNickname();
        this.mainCategory = novel.getMainCategory();
        this.subCategory = novel.getSubCategory();
        this.minCategory = novel.getMinCategory();
        this.novelDetail = novel.getNovelDetail();
        this.novelView = novel.getNovelView();
    }

}
