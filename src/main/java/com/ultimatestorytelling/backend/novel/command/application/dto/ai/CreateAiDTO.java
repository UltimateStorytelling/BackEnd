package com.ultimatestorytelling.backend.novel.command.application.dto.ai;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAiDTO {

    private String story;

    @Builder
    public CreateAiDTO(String story) {
        this.story = story;
    }
}
