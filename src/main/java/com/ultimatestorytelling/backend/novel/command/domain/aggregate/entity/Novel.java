package com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.common.AuditingFields;
import com.ultimatestorytelling.backend.likes.command.domain.aggregate.entity.Likes;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.application.dto.update.NovelUpdateRequestDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Novel extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long novelNo;

    @Column
    private String novelName;

    @Column
    private String mainCategory;

    @Column
    private String subCategory;

    @Column
    private String minCategory;

    @Column
    private String novelDetail;

    @Column
    private Long novelView;

    @Column
    private Boolean novelIsDeleted;

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    //댓글
    @OneToMany(mappedBy = "novel", fetch = FetchType.LAZY)
    private List<Comments> commentList;

    //좋아요
    @OneToMany(mappedBy = "novel", fetch = FetchType.LAZY)
    private List<Likes> likesList;

    @Builder
    public Novel(Long novelNo, String novelName, String mainCategory, String subCategory,
                 String minCategory,String novelDetail, Long novelView, Boolean novelIsDeleted,
                 Member member, List<Comments> commentList, List<Likes> likesList) {
        this.novelNo = novelNo;
        this.novelName = novelName;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.minCategory = minCategory;
        this.novelDetail = novelDetail;
        this.novelView = novelView;
        this.novelIsDeleted = novelIsDeleted;
        this.member = member;
        this.commentList = commentList;
        this.likesList = likesList;
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

    // 조회수 증가 메서드
    public void increaseViewCount() {
        if (this.novelView == null) {
            this.novelView = 1L;
        } else {
            this.novelView += 1;
        }
    }
}
