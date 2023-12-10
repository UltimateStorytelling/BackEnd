package com.ultimatestorytelling.backend.likes.command.application.controller;

import com.ultimatestorytelling.backend.common.message.ResponseMessage;
import com.ultimatestorytelling.backend.likes.command.application.service.CommentsLikesService;
import com.ultimatestorytelling.backend.likes.command.application.service.NovelLikesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "Likes API")
@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikesController {

    private final NovelLikesService novelLikesService;
    private final CommentsLikesService commentsLikesService;

    @ApiOperation(value = "소설에 대한 좋아요 상태 확인")
    @GetMapping("/novel/{novelNo}")
    public ResponseEntity<ResponseMessage> checkLikeStatusForNovel(@PathVariable Long novelNo, @RequestHeader("Authorization") String accessToken) {
        try {
            boolean status = novelLikesService.checkMyLikeStatusForNovel(novelNo, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("novelLikeStatus", status);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "좋아요 상태 조회 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @ApiOperation(value = "소설에 대한 좋아요 토글")
    @PostMapping("/novel/{novelNo}")
    public ResponseEntity<ResponseMessage> toggleLikeForNovel(@PathVariable Long novelNo, @RequestHeader("Authorization") String accessToken) {
        try {
            novelLikesService.toggleLikeForNovel(novelNo, accessToken);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "좋아요 토글 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @ApiOperation(value = "소설에 대한 좋아요 갯수 조회")
    @GetMapping("/novel/{novelNo}/count")
    public ResponseEntity<ResponseMessage> getTotalLikesForNovel(@PathVariable Long novelNo) {
        try {
            int count = novelLikesService.getTotalLikesForNovel(novelNo);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("novelLikeCount", count);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "좋아요 갯수 조회 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @ApiOperation(value = "댓글에 대한 좋아요 상태 확인")
    @GetMapping("/comment/{commentNo}")
    public ResponseEntity<ResponseMessage> checkLikeStatusForComment(@PathVariable Long commentNo, @RequestHeader("Authorization") String accessToken) {
        try {
            boolean status = commentsLikesService.checkMyLikeStatusForComment(commentNo, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("commentsLikeStatus", status);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "좋아요 상태 조회 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @ApiOperation(value = "댓글에 대한 좋아요 토글")
    @PostMapping("/comment/{commentNo}")
    public ResponseEntity<ResponseMessage> toggleLikeForComment(@PathVariable Long commentNo, @RequestHeader("Authorization") String accessToken) {
        try {
            commentsLikesService.toggleLikeForComment(commentNo, accessToken);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "좋아요 토글 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @ApiOperation(value = "댓글에 대한 좋아요 갯수 조회")
    @GetMapping("/comment/{commentNo}/count")
    public ResponseEntity<ResponseMessage> getTotalLikesForComment(@PathVariable Long commentNo) {
        try {
            int count = commentsLikesService.getTotalLikesForComment(commentNo);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("commentsLikeCount", count);
            return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "좋아요 갯수 조회 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }
}
