package com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity;

import com.ultimatestorytelling.backend.common.AuditingFields;
import com.ultimatestorytelling.backend.likes.command.domain.aggregate.entity.Likes;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comments extends AuditingFields {
    @Id
    @GeneratedValue
    @Column
    private Long commentNo;

    @Column
    private String commentsContent;

    @Column
    private Boolean isDeleted;

    //소설
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_no")
    private Novel novel;

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    //좋아요
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<Likes> likesList;

    @Builder
    public Comments(Long commentNo, String commentsContent, Boolean isDeleted,
                    Novel novel, Member member, List<Likes> likesList) {
        this.commentNo = commentNo;
        this.commentsContent = commentsContent;
        this.isDeleted = isDeleted;
        this.novel = novel;
        this.member = member;
        this.likesList = likesList;
    }

    //댓글 삭제
    public void delete() {
        this.isDeleted = true;
    }

    //댓글 수정
    public void update(String commentsContent) {
        this.commentsContent = commentsContent;
    }



}
