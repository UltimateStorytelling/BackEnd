package com.ultimatestorytelling.backend.member.command.domain.aggregate.entity;

import com.ultimatestorytelling.backend.common.AuditingFields;
import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.enumvalue.MemberRole;
import com.ultimatestorytelling.backend.novel.command.domain.aggregate.entity.Novel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo; //회원관리번호

    @Column(length = 100, nullable = false, unique = true)
    private String memberEmail; // 로그인 아이디

    @Column(length = 200)
    private String memberPwd; //로그인 비밀번호

    @Column(length = 300)
    private String memberImage; // 프로필 사진
    //권한
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Column
    private String memberNickname;

    @Column
    private Boolean memberIsDeleted;

    @Column
    private int reportCount;

    //소셜 로그인 테이블 조인
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Authority> authority;

    //소설
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Novel> NovelList;

    @Builder
    public Member(Long memberNo, String memberEmail, String memberPwd, String memberImage,
                  String memberNickname, boolean memberIsDeleted, int reportCount, MemberRole memberRole, List<Authority> authority){
        this.memberNo = memberNo;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberImage = memberImage;
        this.memberNickname = memberNickname;
        this.memberIsDeleted = memberIsDeleted;
        this.reportCount = reportCount;
        this.memberRole = memberRole;
        this.authority = authority;
    }

    //비밀번호 변경
    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    //닉네임 변경
    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    //회원탈퇴
    public void deleteMember(String memberEmail, boolean memberIsDeleted, String memberNickname) {
        this.memberEmail = memberEmail;
        this.memberIsDeleted = memberIsDeleted;
        this.memberNickname = memberNickname;
    }

    //프로필사진 변경
    public void setMemberImage(String memberImage) {
        this.memberImage = memberImage;
    }
}
