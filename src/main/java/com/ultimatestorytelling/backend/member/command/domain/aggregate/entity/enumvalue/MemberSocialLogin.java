package com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue;

import lombok.Getter;

@Getter
public enum MemberSocialLogin {

    LOCAL,
    KAKAO,
    GOOGLE,
    NAVER,
    FACEBOOK
}