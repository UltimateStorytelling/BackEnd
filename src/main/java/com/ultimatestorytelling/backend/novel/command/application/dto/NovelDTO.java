package com.ultimatestorytelling.backend.novel.command.application.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NovelDTO {

    private Long novelId;
    private String novelName;
    private Long novelLike;
    private String novelWriter;
    private java.util.Date novelDate;
    private String mainCategory;
    private String subCategory;
    private String minCategory;
    private String novelDetail;
    private Long novelView;
}
