package com.example.quickhack.controller;

import com.example.quickhack.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GPTController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String baseUrl;

    private final RestTemplate template;

    @PostMapping(path="/chat", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ChatResponse> chat(@RequestParam String type, @RequestPart MultipartFile file) {
        ChatRequest chatRequest = new ChatRequest(type, file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("purpose", "assistants");
        body.add("file", chatRequest.getFile().getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        FileResponse fileResponse = template.postForEntity(baseUrl + "/files", requestEntity, FileResponse.class).getBody();
        System.out.println(fileResponse);

        if (fileResponse == null || fileResponse.getId() == null) {
            return ResponseEntity.ok(new ChatResponse("NOT_DOC", new BaseGPTResponse("can not upload file to api")));
        }
        GPTRequest request = new GPTRequest(model, fileResponse.getId());
        BaseGPTResponse response = null;
        if (chatRequest.getType().equals("IDCard")) {
            template.getInterceptors().add(((request2, body2, execution) -> {
                request2.getHeaders().add("OpenAI-Beta", "assistants=v2");
                return execution.execute(request2, body2);
            }));
             response = template.postForObject(baseUrl + "/assistance", request, IDCardGPTResponse.class);
        }
        return ResponseEntity.ok(new ChatResponse("ok", response));
    }

    @PostMapping(path="/chat2", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> chat2(@RequestParam String type, @RequestPart MultipartFile file) {
        ChatRequest chatRequest = new ChatRequest(type, file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("purpose", "assistants");
        body.add("file", chatRequest.getFile().getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        FileResponse fileResponse = template.postForEntity(baseUrl + "/files", requestEntity, FileResponse.class).getBody();
        System.out.println(fileResponse);

        if (fileResponse == null || fileResponse.getId() == null) {
            return ResponseEntity.ok(new ChatResponse("NOT_DOC", new BaseGPTResponse("can not upload file to api")));
        }


        String url = "http://localhost:8000/file?file_id=" + fileResponse.getId();
//        Map<String, String> params = new HashMap<String, String>();
        if (!template.postForObject(url, "", String.class).isEmpty()) {
            return ResponseEntity.ok(new ChatResponse("success"));
        }
        return ResponseEntity.ok(new ChatResponse("error"));

    }
}
