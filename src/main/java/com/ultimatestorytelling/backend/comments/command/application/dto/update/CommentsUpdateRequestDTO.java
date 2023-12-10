package com.ultimatestorytelling.backend.comments.command.application.dto.update;

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
