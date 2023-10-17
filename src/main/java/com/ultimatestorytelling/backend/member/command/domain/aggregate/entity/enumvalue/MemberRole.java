package com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue;

import lombok.Getter;

@Getter
public  enum MemberRole {
    GUEST("ROLE_GUEST","손님"),
    MEMBER("ROLE_MEMBER","사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;

    MemberRole(String key, String title){
        this.key = key;
        this.title = title;
    }

}
