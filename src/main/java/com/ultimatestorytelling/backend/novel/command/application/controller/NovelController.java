package com.ultimatestorytelling.backend.novel.command.application.controller;

import com.ultimatestorytelling.backend.common.message.ResponseMessage;
import com.ultimatestorytelling.backend.novel.command.application.dto.ai.CreateAiDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.create.NovelCreateRequestDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.read.NovelReadResponseDTO;
import com.ultimatestorytelling.backend.novel.command.application.dto.update.NovelUpdateRequestDTO;
import com.ultimatestorytelling.backend.novel.command.application.service.NovelService;
import com.ultimatestorytelling.backend.novel.command.domain.service.NovelAiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Api(tags= "Novel CRUD API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;
    private final NovelAiService novelAiService;

    //전체소설 조회
    @ApiOperation(value = "전체소설 조회")
    @GetMapping("/novels")
    public ResponseEntity<ResponseMessage> findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                   @RequestParam(value = "sort", defaultValue = "novelNo") String sort,
                                                   @RequestParam(value = "sortInfo", defaultValue = "DESC") String sortInfo) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.Direction.fromString(sortInfo), sort);

        try {
            // 페이지 조회, 페이징 정보 리턴
            Map<String, Object> responseMap = novelService.findAll(pageable);

            return ResponseEntity.ok().body(new ResponseMessage(HttpStatus.OK.value(), "전체 조회 성공", responseMap));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    //소설번호로 조회
    @ApiOperation(value = "소설번호로 조회")
    @GetMapping("/novels/{novelNo}")
    public ResponseEntity<ResponseMessage> findNovelById(@PathVariable Long novelNo){

        try{
            NovelReadResponseDTO novelDTO = novelService.findNovel(novelNo);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("novel", novelDTO);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "소설 조회성공",responseMap));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(),null));
        }
    }

    //소설 작성
    @ApiOperation(value = "소설 작성")
    @PostMapping("/novels")
    public ResponseEntity<ResponseMessage> novelSave(@RequestBody NovelCreateRequestDTO novelCreateRequestDTO,
                                                     @RequestHeader("Authorization") String accessToken) {
        try {
            Long NovelNo = novelService.saveNovel(novelCreateRequestDTO, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("NovelNo", NovelNo);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(HttpStatus.CREATED.value(), "소설 작성 성공", responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    //소설 수정
    @ApiOperation(value = "소설 수정")
    @PutMapping("/novels/{novelNo}")
    public ResponseEntity<ResponseMessage> novelUpdate(@RequestHeader("Authorization") String accessToken,
                                                       @PathVariable Long novelNo,
                                                       @RequestBody NovelUpdateRequestDTO novelDTO) {
        try{
            Long resultNovelNo = novelService.updateNovel(novelNo, novelDTO, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("NovelNo", resultNovelNo);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "소설 수정 성공",responseMap));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    //소설 삭제
    @ApiOperation(value = "소설 삭제")
    @DeleteMapping("/novels/{novelNo}")
    public ResponseEntity<ResponseMessage> deleteNovel(@RequestHeader("Authorization") String accessToken,
                                                       @PathVariable Long novelNo) {
        try{
            Long resultNovelNo = novelService.deleteNovel(novelNo, accessToken);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("NovelNo", resultNovelNo);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "소설 삭제 성공",responseMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @ApiOperation(value = "ai소설 작성")
    @PostMapping("/novels/ai")
    public ResponseEntity<ResponseMessage> novelAi(@RequestBody CreateAiDTO createAiDTO) {
        try {
            String story = novelAiService.novelAi(createAiDTO.getStory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("story", story);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK.value(), "소설 생성 성공",responseMap));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

}
