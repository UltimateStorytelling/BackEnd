package com.ultimatestorytelling.backend.novel.command.application.controller;

import com.ultimatestorytelling.backend.common.page.Pagenation;
import com.ultimatestorytelling.backend.common.page.PagingButtonInfo;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelDTO;

import com.ultimatestorytelling.backend.novel.command.application.dto.NovelRequestDto;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelResponseDto;
import com.ultimatestorytelling.backend.novel.command.application.service.NovelService;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.awt.print.Pageable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;


    @PostMapping("/novelai")
    public String novelAi(@RequestParam String detail) {
        return novelService.novelAi(detail);
    }

    @PostMapping("/novelPost")
    public Long novelSave(@RequestBody final NovelRequestDto params) {
            return novelService.save(params);
    }

    @GetMapping("/novelList")
    public List<NovelResponseDto> findAll() {
            return novelService.findAll();
    }

    @PutMapping("/novels/{id}")
    public Long novelSave(@RequestHeader String accessToken, @PathVariable final Long id, @RequestBody final NovelRequestDto params) {
            return novelService.update(id, params);
    }

    @DeleteMapping("/novels/{id}")
    public void deleteNovel(@RequestHeader String accessToken, @PathVariable Long id) {
        novelService.delete(id);
    }

    @GetMapping("/novel")
    public ResponseEntity<NovelResponseDto.ResponseDto> findNovelList(ModelAndView mv, @PageableDefault Pageable pageable) {

        Page<NovelDTO> novelList = novelService.findAllNovel(pageable);

        PagingButtonInfo paging = Pagenation.getPaginButtonInfo(novelList);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("novelList", novelList);
        responseMap.put("paging", paging);

        return ResponseEntity.ok().body(new NovelResponseDto.ResponseDto(HttpStatus.OK, "전체 조회 성공", responseMap));
    }

}
