package com.ultimatestorytelling.backend.novel.command.application.service;

import com.ultimatestorytelling.backend.novel.command.application.dto.NovelDTO;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.NovelEntity;
import com.ultimatestorytelling.backend.novel.command.domain.repository.NovelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class NovelService {

    private final NovelRepository novelRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public NovelService(NovelRepository novelRepository, ModelMapper modelMapper) {
        this.novelRepository = novelRepository;
        this.modelMapper = modelMapper;
    }

    public String novelDetail(String detail) {

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

    @Transactional
    public NovelDTO registNovel(NovelDTO newNovel) {

        NovelEntity result = novelRepository.save(modelMapper.map(newNovel, NovelEntity.class));

        result.setNovelDate(new java.util.Date());

        return modelMapper.map(result, NovelDTO.class);
    }

    @Transactional
    public NovelDTO updateNovel(Long novelId, NovelDTO updateNovel) {

        NovelEntity novel = novelRepository.findById(novelId).get();

        String novelName = updateNovel.getNovelName();
        java.util.Date date = updateNovel.getNovelDate();
        String novelDetail = updateNovel.getNovelDetail();
        String mainCategory = updateNovel.getMainCategory();
        String subCategory = updateNovel.getSubCategory();
        String minCategory = updateNovel.getMinCategory();

        novel.setNovelName(novelName);
        novel.setNovelDate(date);
        novel.setNovelDetail(novelDetail);
        novel.setMainCategory(mainCategory);
        novel.setSubCategory(subCategory);
        novel.setMinCategory(minCategory);

        NovelEntity resultEntity = novelRepository.save(novel);

        return modelMapper.map(resultEntity, NovelDTO.class);

    }
}
