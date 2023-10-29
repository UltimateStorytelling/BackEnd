package com.ultimatestorytelling.backend.novel.command.application.controller;

import com.ultimatestorytelling.backend.common.message.ResponseMessage;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelDTO;

import com.ultimatestorytelling.backend.novel.command.application.dto.NovelRequestDto;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelResponseDto;
import com.ultimatestorytelling.backend.novel.command.application.service.NovelService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
@RequestMapping("/api")
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
    public Long novelSave(@PathVariable final Long id, @RequestBody final NovelRequestDto params) {
            return novelService.update(id, params);
    }

    @DeleteMapping("/novels/{id}")
    public void deleteNovel(@PathVariable Long id) {
        novelService.delete(id);
    }


}
