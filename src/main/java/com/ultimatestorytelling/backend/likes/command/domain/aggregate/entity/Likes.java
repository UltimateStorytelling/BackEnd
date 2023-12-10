package com.ultimatestorytelling.backend.likes.command.domain.aggregate.entity;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.common.AuditingFields;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long likesNo;

    //좋아요 상태
    @Column
    private Boolean likesStatus;

    //좋아요 누른 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    //좋아요 소설
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_no")
    private Novel novel;

    //좋아요 누른 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_no")
    private Comments comment;

    @Builder
    public Likes(Long likesNo, Boolean likesStatus, Novel novel, Comments comment,
                 Member member) {
        this.likesNo = likesNo;
        this.likesStatus = likesStatus;
        this.member = member;
        this.novel = novel;
        this.comment = comment;
    }

    //좋아요 상태변경
    public void setLikesStatus(Boolean likesStatus) {
        this.likesStatus = likesStatus;
    }

}
