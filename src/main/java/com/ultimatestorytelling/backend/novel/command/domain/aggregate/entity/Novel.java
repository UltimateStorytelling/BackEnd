package com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOVEL_ID")
    private Long novelId;

    @Column(name = "NOVEL_NAME")
    private String novelName;

    @Column(name = "NOVEL_LIKE")
    private Long novelLike;

    @Column(name = "NOVEL_WRITER")
    private String novelWriter;

    @Column(name = "NOVEL_DATE")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private java.util.Date novelDate;

    @Column(name = "MAIN_CATEGORY")
    private String mainCategory;

    @Column(name = "SUB_CATEGORY")
    private String subCategory;

    @Column(name = "MIN_CATEGORY")
    private String minCategory;

    @Column(name = "NOVEL_DETAIL")
    private String novelDetail;

    @Column(name = "NOVEL_VIEW")
    private Long novelView;

    @Builder
    public Novel(Long novelId, String novelName, Long novelLike, String novelWriter, String mainCategory, String subCategory, String minCategory, String novelDetail, Long novelView) {
        this.novelId = novelId;
        this.novelName = novelName;
        this.novelLike = novelLike;
        this.novelWriter = novelWriter;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.minCategory = minCategory;
        this.novelDetail = novelDetail;
        this.novelView = novelView;
    }

    public void update(String novelName, String mainCategory, String subCategory, String minCategory, String novelDetail) {
        this.novelName = novelName;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.minCategory = minCategory;
        this.novelDetail = novelDetail;
    }
}
