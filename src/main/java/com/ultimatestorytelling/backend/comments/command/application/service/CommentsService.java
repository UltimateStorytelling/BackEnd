package com.ultimatestorytelling.backend.comments.command.application.service;

import com.ultimatestorytelling.backend.comments.command.application.dto.create.CommentsCreateRequestDTO;
import com.ultimatestorytelling.backend.comments.command.application.dto.read.CommentsReadResponseDTO;
import com.ultimatestorytelling.backend.comments.command.application.dto.update.CommentsUpdateRequestDTO;
import com.ultimatestorytelling.backend.comments.command.domain.aggregate.entity.Comments;
import com.ultimatestorytelling.backend.comments.command.domain.repository.CommentsRepository;
import com.ultimatestorytelling.backend.common.service.AuthenticationService;
import com.ultimatestorytelling.backend.common.service.EntityFinderService;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final AuthenticationService authenticationService;
    private final EntityFinderService entityFinderService;



    //소설 번호로 댓글 조회
    @Transactional(readOnly = true)
    public Map<String, Object> findCommentsByNovelNo(Long novelNo) {

        //소설번호를 기반으로 novel 객체 조회
        Novel novel = entityFinderService.getNovel(novelNo);

        // 소설 번호에 맞는 댓글 조회
        List<Comments> comments = commentsRepository.findByNovelAndIsDeletedFalseOrderByCommentNoAsc(novel);

        //조회 결과를 DTO로 변환
        List<CommentsReadResponseDTO> commentsDTOs = comments.stream()
                .map(CommentsReadResponseDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();

        if (commentsDTOs.isEmpty()) {
            response.put("message", "등록된 댓글이 없습니다.");
        } else {
            response.put("comments", commentsDTOs);
        }

        return response;
    }

    //댓글 작성
    @Transactional
    public Long createComment(Long novelNo, CommentsCreateRequestDTO requestDTO, String accessToken) {

        //엑세스 토큰을 기반으로 member 객체 조회
        Member member = authenticationService.getAuthenticatedMember(accessToken);

        //소설번호를 기반으로 novel 객체 조회
        Novel novel = entityFinderService.getNovel(novelNo);

        //DTO를 Entity로 변환
        Comments comment = requestDTO.toEntity(member, novel);

        //댓글 저장
        Comments savedComment = commentsRepository.save(comment);

        return savedComment.getCommentNo();
    }

    //댓글 수정
    @Transactional
    public Long updateComment(Long commentNo, CommentsUpdateRequestDTO requestDTO, String accessToken) {

        //엑세스 토큰을 기반으로 member 객체 조회
        Member member = authenticationService.getAuthenticatedMember(accessToken);

        //댓글번호로 댓글 조회
        Comments comment = entityFinderService.getComment(commentNo);

        // 관리자 또는 작성자 본인인 경우에만 수정 허용
        if (!authenticationService.isAdmin(accessToken) && !comment.getMember().equals(member)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }


        comment.update(requestDTO.getCommentsContent());
        return comment.getCommentNo();
    }

    //댓글 삭제
    @Transactional
    public Long deleteComment(Long commentNo, String accessToken) {

        //엑세스 토큰을 기반으로 member 객체 조회
        Member member = authenticationService.getAuthenticatedMember(accessToken);

        //댓글번호로 댓글 조회
        Comments comment = entityFinderService.getComment(commentNo);

        // 관리자 또는 작성자 본인인 경우에만 수정 허용
        if (!authenticationService.isAdmin(accessToken) && !comment.getMember().equals(member)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        //댓글 삭제
        comment.delete();
        return comment.getCommentNo();
    }

}
