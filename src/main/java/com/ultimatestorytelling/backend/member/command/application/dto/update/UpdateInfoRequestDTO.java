package com.ultimatestorytelling.backend.member.command.application.dto.update;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UpdateInfoRequestDTO {

    @NotBlank
    @Pattern(regexp ="^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$",
            message = "닉네임은 2자이상 16자이하, 영어 또는 숫자 또는 한글로 구성해야 합니다.")
    private String memberNickname;

}
