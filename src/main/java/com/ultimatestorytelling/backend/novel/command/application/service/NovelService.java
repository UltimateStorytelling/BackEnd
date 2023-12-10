package com.ultimatestorytelling.backend.novel.command.application.service;


import com.ultimatestorytelling.backend.common.page.Pagenation;
import com.ultimatestorytelling.backend.common.page.PagingButtonInfo;
import com.ultimatestorytelling.backend.common.service.AuthenticationService;
import com.ultimatestorytelling.backend.common.service.EntityFinderService;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.novel.command.application.dto.create.NovelCreateRequestDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.read.NovelReadResponseDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.update.NovelUpdateRequestDTO;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import com.ultimatestorytelling.backend.novel.command.domain.repository.NovelRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;
    private final AuthenticationService authenticationService;
    private final EntityFinderService entityFinderService;


    // 소설 전체 리스트 조회
    @Transactional(readOnly = true)
    public Map<String, Object> findAll(Pageable pageable) {

        // 페이징된 소설 조회
        Page<Novel> novelPage = novelRepository.findNovelByNovelIsDeletedFalse(pageable);

        // 조회 결과를 DTO로 변환
        Page<NovelReadResponseDTO> novels = novelPage.map(NovelReadResponseDTO::new);

        //페이징 적용
        PagingButtonInfo pagingButtonInfo = Pagenation.getPaginButtonInfo(novelPage);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("novels", novels);
        responseMap.put("pagingButtonInfo", pagingButtonInfo);

        return responseMap;
    }

    // 소설 번호로 소설 조회
    @Transactional(readOnly = true)
    public NovelReadResponseDTO findNovel(Long novelNo) {

        // 소설번호로 소설 조회
        Novel novel = entityFinderService.getNovel(novelNo);

        novel.increaseViewCount(); // 조회수 증가
        novelRepository.save(novel); // 변경 사항 저장

        return new NovelReadResponseDTO(novel);
    }

    // 소설 작성
    @Transactional
    public Long saveNovel(NovelCreateRequestDTO novelDTO, String accessToken){

        //엑세스 토큰을 기반으로 Member 객체를 조회합니다.
        Member member = authenticationService.getAuthenticatedMember(accessToken);

        //DTO를 Entity로 변환
        Novel novel = novelDTO.toEntity(member);

        //소설 저장
        novelRepository.save(novel);

        return novel.getNovelNo();
    }

    // 게시글 수정
    @Transactional
    public Long updateNovel(Long novelNo, NovelUpdateRequestDTO novelDTO, String accessToken) {

        //엑세스 토큰을 기반으로 Member 객체를 조회합니다.
        Member member = authenticationService.getAuthenticatedMember(accessToken);

        // 소설번호로 소설 조회
        Novel novel = entityFinderService.getNovel(novelNo);

        // 관리자 또는 작성자 본인인 경우에만 수정 허용
        if (!authenticationService.isAdmin(accessToken) && !novel.getMember().equals(member)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        novel.update(novelDTO);
        return novel.getNovelNo();
    }

    // 게시글 삭제
    @Transactional
    public Long deleteNovel(final Long novelNo, String accessToken) {

        //엑세스 토큰을 기반으로 Member 객체를 조회합니다.
        Member member = authenticationService.getAuthenticatedMember(accessToken);

        // 소설번호로 소설 조회
        Novel novel = entityFinderService.getNovel(novelNo);

        // 관리자 또는 작성자 본인인 경우에만 삭제 허용
        if (!authenticationService.isAdmin(accessToken) && !novel.getMember().equals(member)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        novel.delete();
        return novel.getNovelNo();
    }
}
