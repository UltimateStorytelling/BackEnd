package com.ultimatestorytelling.backend.login.command.application.dto.login;

import lombok.Builder;
import lombok.Getter;


@Getter
public class LoginRequestDTO {

    //로그인 입력
    private String memberEmail;
    private String memberPwd;

    @Builder
    public LoginRequestDTO(String memberEmail, String memberPwd){
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
    }
}
