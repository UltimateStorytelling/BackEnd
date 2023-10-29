package com.ultimatestorytelling.backend.novel.command.application.service;


import com.ultimatestorytelling.backend.exception.CustomException;
import com.ultimatestorytelling.backend.exception.ErrorCode;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelRequestDto;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelResponseDto;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import com.ultimatestorytelling.backend.novel.command.domain.repository.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;

    private final ModelMapper modelMapper;


    // 게시글 생성
    @Transactional
    public Long save(final NovelRequestDto params) {
        Novel entity = novelRepository.save(params.toEntity());

        return entity.getNovelId();
    }

    // 게시글 리스트 조회
    public List<NovelResponseDto> findAll() {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Novel> list = novelRepository.findAll(sort);
        return list.stream().map(NovelResponseDto::new).collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public Long update(final Long id, final NovelRequestDto params) {

        Novel entity = novelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        entity.update(params.getNovelName(), params.getMainCategory(), params.getSubCategory(), params.getMinCategory(), params.getNovelDetail());

        return id;
    }

    // 게시글 삭제
    @Transactional
    public void delete(final Long id) {

        Novel entity = novelRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        novelRepository.delete(entity);
    }

    public String novelAi(String detail) {


        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("detail", detail);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<String> responseEntity = rt.exchange(
            "http://localhost:8080",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String response =responseEntity.getBody();
        System.out.println(response);

        return response;
    }

}
