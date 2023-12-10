package com.ultimatestorytelling.backend.comments.command.application.dto.update;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsUpdateRequestDTO {

    private String commentsContent;

    @Builder
    public CommentsUpdateRequestDTO(String CommentsContent) {
        this.commentsContent = CommentsContent;
    }


}
