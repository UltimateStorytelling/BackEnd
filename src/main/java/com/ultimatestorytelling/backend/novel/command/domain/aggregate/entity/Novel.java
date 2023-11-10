package com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity;

import com.ultimatestorytelling.backend.common.AuditingFields;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.application.dto.update.NovelUpdateRequestDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Novel extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOVEL_ID")
    private Long novelNo;

    @Column(name = "NOVEL_NAME")
    private String novelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOVEL_WRITER")
    private Member member;

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

    @Column(name = "NOVEL_IS_DELETED")
    private Boolean novelIsDeleted;

    @Builder
    public Novel(Long novelNo, String novelName, Member member, String mainCategory, String subCategory,
                 String minCategory, String novelDetail, Long novelView, Boolean novelIsDeleted) {
        this.novelNo = novelNo;
        this.novelName = novelName;
        this.member = member;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.minCategory = minCategory;
        this.novelDetail = novelDetail;
        this.novelView = novelView;
        this.novelIsDeleted = novelIsDeleted;
    }

    // 글 작성
    public void setMember(Member member) {
        this.member = member;
    }

    // 글 수정
    public void update(NovelUpdateRequestDTO novelUpdateRequestDTO) {
        this.novelName = novelUpdateRequestDTO.getNovelName();
        this.mainCategory = novelUpdateRequestDTO.getMainCategory();
        this.subCategory = novelUpdateRequestDTO.getSubCategory();
        this.minCategory = novelUpdateRequestDTO.getMinCategory();
        this.novelDetail = novelUpdateRequestDTO.getNovelDetail();
    }

    // 글 삭제
    public void delete() {
        this.novelIsDeleted = true;
    }
}
