package com.ultimatestorytelling.backend.novel.command.application.controller;

import com.ultimatestorytelling.backend.common.message.ResponseMessage;
import com.ultimatestorytelling.backend.novel.command.application.dto.NovelDTO;
import com.ultimatestorytelling.backend.novel.command.application.service.NovelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class NovelController {

    private final NovelService novelService;

    public NovelController(NovelService novelService) {
        this.novelService = novelService;
    }

    @PostMapping("/novelai")
    public String novelDetail(@RequestParam String detail) {
        return novelService.novelDetail(detail);
    }

    @PostMapping("/novel")
    public ResponseEntity<ResponseMessage> registNovel(@RequestBody NovelDTO novelDTO) {

        return ResponseEntity.ok().body(new ResponseMessage(HttpStatus.CREATED, "소설 등록 성공", novelService.registNovel(novelDTO)));
    }

    @PutMapping("/novel/{novelId}")
    public ResponseEntity<ResponseMessage> updateNovel(@PathVariable Long novelId, @RequestBody NovelDTO novelDTO) {

        return ResponseEntity.ok().body(new ResponseMessage(HttpStatus.CREATED, "소설 수정 성공", novelService.updateNovel(novelId, novelDTO)));
    }

}
