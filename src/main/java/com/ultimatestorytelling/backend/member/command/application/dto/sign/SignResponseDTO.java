package com.ultimatestorytelling.backend.member.command.application.dto.sign;


import com.ultimatestorytelling.backend.member.command.domain.aggregate.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignResponseDTO {

    private Long memberNo;
    private String memberEmail;
    private String memberNickname;
    private String memberImage;

    @Builder
    public SignResponseDTO(Member member){
        this.memberNo = member.getMemberNo();
        this.memberEmail = member.getMemberEmail();
        this.memberNickname = member.getMemberNickname();
        this.memberImage = member.getMemberImage();
    }
}
