package com.ultimatestorytelling.backend.novel.command.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NovelAiService {

    private final WebClient.Builder webClientBuilder;

    //ai 소설 생성
    public String novelAi(String message) {
        String apiUrl = "https://6b97-221-163-19-218.ngrok-free.app/generate_story_page3/"; // 호출하려는 API의 URL

        //변수랑 매
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("story", message);

        String story = webClientBuilder.build()
                .post() // POST 요청
                .uri(apiUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .bodyValue(requestBody) // Request Body 설정
                .retrieve()
                .bodyToMono(String.class) // Response Body의 타입 설정
                .block();

        return story;

    }
}
