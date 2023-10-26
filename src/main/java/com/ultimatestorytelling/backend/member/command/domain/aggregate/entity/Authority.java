package com.ultimatestorytelling.backend.member.command.domain.aggregate.entity;

import com.ultimatestorytelling.backend.common.AuditingFields;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberSocialLogin;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authNo;

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberSocialLogin memberSocialLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberNo")
    private Member member;

    @Builder
    public Authority(Long authNo, String tokenType, String accessToken, String refreshToken, MemberSocialLogin memberSocialLogin, Member member){
        this.authNo = authNo;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberSocialLogin = memberSocialLogin;
        this.member = member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    //액세스토큰 재발행
    public void reissueAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    //업데이트 토큰
    public void updateToken(String tokenType, String accessToken, String refreshToken, MemberSocialLogin memberSocialLogin) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberSocialLogin = memberSocialLogin;
    }
}
