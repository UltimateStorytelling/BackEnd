package com.ultimatestorytelling.backend.comments.command.application.controller;

import com.ultimatestorytelling.backend.comments.command.application.dto.create.CommentsCreateRequestDTO;
import com.ultimatestorytelling.backend.comments.command.application.dto.update.CommentsUpdateRequestDTO;
import com.ultimatestorytelling.backend.comments.command.application.service.CommentsService;
import com.ultimatestorytelling.backend.common.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags= "Comments CRUD API")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    //소설 번호로 댓글 조회
    @ApiOperation(value = "소설 번호로 댓글 조회")
    @GetMapping("/{novelNo}")
    public ResponseEntity<ResponseMessage> findCommentsByNovelNo(@PathVariable Long novelNo) {

        try {
            Map<String, Object> responseMap = commentsService.findCommentsByNovelNo(novelNo);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "댓글 조회 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    // 댓글 작성
    @PostMapping("/{novelNo}")
    @ApiOperation(value = "댓글 작성")
    public ResponseEntity<ResponseMessage> createComment(@PathVariable Long novelNo, @RequestBody CommentsCreateRequestDTO requestDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            Long commentNo = commentsService.createComment(novelNo, requestDTO, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("commentNo", commentNo);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(HttpStatus.CREATED.value(), "댓글 작성 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    // 댓글 수정
    @PutMapping("/{commentNo}")
    @ApiOperation(value = "댓글 수정")
    public ResponseEntity<ResponseMessage> updateComment(@PathVariable Long commentNo, @RequestBody CommentsUpdateRequestDTO requestDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            Long updatedCommentId = commentsService.updateComment(commentNo, requestDTO, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("commentNo", commentNo);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "댓글 수정 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{commentNo}")
    @ApiOperation(value = "댓글 삭제")
    public ResponseEntity<ResponseMessage> deleteComment(@PathVariable Long commentNo, @RequestHeader("Authorization") String accessToken) {
        try {
            Long deletedCommentId = commentsService.deleteComment(commentNo, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("commentNo", commentNo);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "댓글 삭제 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

}
