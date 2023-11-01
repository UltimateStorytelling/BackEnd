package com.ultimatestorytelling.backend.novel.command.application.controller;

import com.ultimatestorytelling.backend.novel.command.application.dto.NovelRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/write")
public class NovelWriteController {
    @GetMapping
    public String showWritePage(Model model) {
        NovelRequestDto novelRequestDto = new NovelRequestDto();

        model.addAttribute("novelRequestDto", novelRequestDto);
        return "write";
    }
}


