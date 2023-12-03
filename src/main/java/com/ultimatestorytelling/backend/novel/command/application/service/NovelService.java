package com.ultimatestorytelling.backend.novel.command.application.service;


import com.ultimatestorytelling.backend.common.page.Pagenation;
import com.ultimatestorytelling.backend.common.page.PagingButtonInfo;
import com.ultimatestorytelling.backend.jwt.TokenProvider;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import com.ultimatestorytelling.backend.member.command.domain.repository.MemberRepository;
import com.ultimatestorytelling.backend.novel.command.application.dto.create.NovelCreateRequestDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.read.NovelReadResponseDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.update.NovelUpdateRequestDTO;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import com.ultimatestorytelling.backend.novel.command.domain.repository.NovelRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final WebClient.Builder webClientBuilder;

    //관리자권한 확인
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }

    // 소설 전체 리스트 조회
    @Transactional(readOnly = true)
    public Map<String, Object> findAll(Pageable pageable) {

        // 페이징된 소설 조회
        Page<Novel> novelPage = novelRepository.findNovelByNovelIsDeletedFalse(pageable);

        // 조회 결과를 DTO로 변환
        Page<NovelReadResponseDTO> novels = novelPage.map(NovelReadResponseDTO::new);

        PagingButtonInfo pagingButtonInfo = Pagenation.getPaginButtonInfo(novelPage);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("novels", novels);
        responseMap.put("pagingButtonInfo", pagingButtonInfo);

        return responseMap;
    }

    // 소설 번호로 소설 조회
    @Transactional(readOnly = true)
    public NovelReadResponseDTO findNovel(Long novelNo) {

        // 소설 조회
        Novel novel = novelRepository.findByNovelNo(novelNo);
        // DTO생성
        NovelReadResponseDTO responseDTO = new NovelReadResponseDTO(novel);

        return responseDTO;
    }

    // 소설 작성
    @Transactional
    public Long saveNovel(NovelCreateRequestDTO novelDTO, String accessToken){

        // accessToken을 사용하여 사용자를 인증하고 해당 사용자의 정보를 가져옵니다.
        Authentication authentication = tokenProvider.validateAndGetAuthentication(accessToken);
        String userEmail = authentication.getName();

        // 사용자의 이메일을 기반으로 Member 객체를 조회합니다.
        Member member = memberRepository.findMemberByMemberEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // dto를 이용하여 entity 생
        Novel novel = novelDTO.toEntity();
        novel.setMember(member);

        //소설 저장
        novelRepository.save(novel);

        return novel.getNovelNo();
    }

    // 게시글 수정
    @Transactional
    public Long updateNovel(Long novelNo, NovelUpdateRequestDTO novelDTO, String accessToken) {

        // accessToken을 사용하여 사용자를 인증하고 해당 사용자의 정보를 가져옵니다.
        Authentication authentication = tokenProvider.validateAndGetAuthentication(accessToken);

        //관리자 권한 확인
        boolean isAdmin = isAdmin(authentication);

        //피드 조회
        Novel novel = novelRepository.findByNovelNo(novelNo);

        //수정 권환 확인 ( 어드민이 아닐경우)
        if(!isAdmin) {
            String userEmail = authentication.getName();
            Member member = memberRepository.findMemberByMemberEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            if (!novel.getMember().getMemberEmail().equals(member.getMemberEmail())) {
                throw new IllegalArgumentException("해당 사용자는 삭제할 수 없습니다.");
            }
        }

        //피드 내용 업데이트 (더티체킹)
        novel.update(novelDTO);

        return novel.getNovelNo();
    }

    // 게시글 삭제
    @Transactional
    public Long deleteNovel(final Long novelNo, String accessToken) {

        // accessToken을 사용하여 사용자를 인증하고 해당 사용자의 정보를 가져옵니다.
        Authentication authentication = tokenProvider.validateAndGetAuthentication(accessToken);

        //관리자 권한 확인
        boolean isAdmin = isAdmin(authentication);

        //피드 조회
        Novel novel = novelRepository.findByNovelNo(novelNo);

        //삭제 권환 확인 ( 어드민이 아닐경우)
        if(!isAdmin) {
            String userEmail = authentication.getName();
            Member member = memberRepository.findMemberByMemberEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            if (!novel.getMember().getMemberEmail().equals(member.getMemberEmail())) {
                throw new IllegalArgumentException("해당 사용자는 삭제할 수 없습니다.");
            }
        }

        novel.delete();

        return novel.getNovelNo();
    }

    //ai 소설 생성
    public String novelAi(String message) {
        String apiUrl = "https://6b97-221-163-19-218.ngrok-free.app/generate_story_page3/"; // 호출하려는 API의 URL

        //변수랑 매
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("story", message);

            String story = webClientBuilder.build()
                    .post() // POST 요청
                    .uri(apiUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                    .bodyValue(requestBody) // Request Body 설정
                    .retrieve()
                    .bodyToMono(String.class) // Response Body의 타입 설정
                    .block();

        return story;

    }

}
