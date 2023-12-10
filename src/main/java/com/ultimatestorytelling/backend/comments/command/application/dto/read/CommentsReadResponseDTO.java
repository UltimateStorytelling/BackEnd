package com.ultimatestorytelling.backend.comments.command.application.dto.read;

import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CommentsReadResponseDTO {
    private Long commentNo;
    private String commentsContent;
    private String memberNickname;
    private Boolean commentsIsDeleted;
    private int likesCount; // 좋아요 수

    // 생성자 또는 빌더를 통해 DTO 초기화
    public CommentsReadResponseDTO(Comments comment) {
        this.commentNo = comment.getCommentNo();
        this.commentsContent = comment.getCommentsContent();
        this.memberNickname = comment.getMember().getMemberNickname();
        this.commentsIsDeleted = comment.getIsDeleted();
        this.likesCount = comment.getLikesList().size(); // 좋아요 수 계산
    }
}
