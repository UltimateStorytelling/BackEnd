package com.ultimatestorytelling.backend.comments.command.application.dto.create;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsCreateRequestDTO {

    private String commentsContent;

    public CommentsCreateRequestDTO(String CommentsContent) {
        this.commentsContent = CommentsContent;
    }

    //댓글 작성시
    public Comments toEntity(Member member, Novel novel) {
        return Comments.builder()
                .commentsContent(commentsContent)
                .isDeleted(false)
                .member(member)
                .novel(novel)
                .build();
    }

}
