package com.ultimatestorytelling.backend.login.command.application.dto.auth;


import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberSocialLogin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseDTO {

    //로그인응답 (토큰응답)
    private String accessToken;
    private MemberSocialLogin memberSocialLogin;

    public AuthResponseDTO(String accessToken, MemberSocialLogin memberSocialLogin){
        this.accessToken = accessToken;
        this.memberSocialLogin = memberSocialLogin;
    }


}
