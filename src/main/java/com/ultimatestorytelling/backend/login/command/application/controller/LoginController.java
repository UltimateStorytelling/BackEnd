package com.ultimatestorytelling.backend.login.command.application.controller;

import com.ultimatestorytelling.backend.common.message.ResponseMessage;
import com.ultimatestorytelling.backend.login.command.application.dto.login.LoginRequestDTO;
import com.ultimatestorytelling.backend.login.command.application.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags= "Login API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @ApiOperation(value = "로그인 요청")
    @PostMapping(value = "/authentication/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {

        try{
            Map<String, Object> loginResult = loginService.login(requestDTO);

            // 리프레시 토큰을 HTTP Only 헤더에 설정
            String refreshToken = (String) loginResult.get("refreshToken");
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);
            // 응답 json맵에서 리프레시 토큰 제거
            loginResult.remove("refreshToken");

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "로그인 성공.",loginResult));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), e.getMessage(),null));
        }
    }

    // LoginController.java

    @ApiOperation(value = "액세스 토큰 재발급 요청")
    @PostMapping("/authentication/renew")
    public ResponseEntity<ResponseMessage> renewAccessToken(HttpServletRequest request) {
        // 쿠키에서 리프레시 토큰을 가져옵니다.
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "만료된 정보입니다.",null));
        }
        try {
            // 액세스 토큰을 재발급 받습니다.
            Map<String, Object> tokenResult = loginService.renewAccessToken(refreshToken);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(),"access토근 발급완료",tokenResult));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),null));
        }
    }


    @ApiOperation(value = "로그아웃 요청") // 쿠키에 refresh토큰 삭제
    @DeleteMapping("/authentication/login")
    public ResponseEntity<ResponseMessage> logout(HttpServletResponse response) {
        try {
            // 리프레시 토큰 쿠키 삭제
            Cookie refreshTokenCookie = new Cookie("refreshToken", null);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(0); // 쿠키 만료
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(),"로그아웃",null));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),null));
        }
    }
}
