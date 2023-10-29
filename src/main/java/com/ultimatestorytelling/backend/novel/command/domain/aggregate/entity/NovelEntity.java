package com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name = "novel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name = "NOVEL_SEQ_GENERATOR",
        sequenceName = "SEQ_NOVEL_NUM",
        initialValue = 1,
        allocationSize = 1
)
public class NovelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOVEL_ID")
    private Long novelNum;

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

}
