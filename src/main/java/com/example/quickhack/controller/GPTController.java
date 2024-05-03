package com.example.quickhack.controller;

import com.example.quickhack.dto.*;
import com.example.quickhack.model.IDCard;
import com.example.quickhack.service.IDCardService;
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

import java.util.Optional;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GPTController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String baseUrl;

    private final RestTemplate template;

    private final IDCardService idCardService;

    @PostMapping(path = "/chat", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ChatResponse> chat(@RequestParam(required = false) String type, @RequestPart MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("purpose", "assistants");
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        FileResponse fileResponse = template.postForEntity(baseUrl + "/files", requestEntity, FileResponse.class).getBody();
        System.out.println(fileResponse);

        if (fileResponse == null || fileResponse.getId() == null) {
            return ResponseEntity.ok(new ChatResponse("NOT_DOC"));
        }
        String url = "http://localhost:8000/file-vs?file_id=" + fileResponse.getId();

        return ResponseEntity.ok(template.postForObject(url, "", ChatResponse.class));
    }

    @PostMapping(path = "/chat2", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ChatResponse> chat2(@RequestParam(required = false) String type, @RequestPart MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("purpose", "assistants");
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        FileResponse fileResponse = template.postForEntity(baseUrl + "/files", requestEntity, FileResponse.class).getBody();
        System.out.println(fileResponse);

        if (fileResponse == null || fileResponse.getId() == null) {
            return ResponseEntity.ok(new ChatResponse("NOT_DOC"));
        }
        String url = "http://localhost:8000/file?file_id=" + fileResponse.getId();
//        Map<String, String> params = new HashMap<String, String>();
        ChatResponse response = template.postForObject(url, "", ChatResponse.class);
        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }
        if (response.getStatus().equals("error")) {
            return ResponseEntity.ok(new ChatResponse("NOT_DOC"));
        }
        IDCardGPTResponse data = response.getData();
        Optional<IDCard> optionalIDCard = idCardService.getById(data.getIin());
        if (optionalIDCard.isEmpty()) {
            return ResponseEntity.ok(new ChatResponse("FAKE"));
        }
        IDCard idCard = optionalIDCard.get();
        System.out.println(idCard);
        System.out.println(data);
        if (!idCard.getBirthday().equals(data.getBirthday())
                || !idCard.getCardId().equals(data.getCard_id())
                || !idCard.getGivenDate().equals(data.getGiven_date())
                || !idCard.getExpirationDate().equals(data.getExpiration_date())) {
            return ResponseEntity.ok(new ChatResponse("FAKE"));
        }
        return ResponseEntity.ok(new ChatResponse("OK"));
    }

    @GetMapping("idcard")
    public Optional<IDCard> getIDCard(@RequestParam String id) {
        return idCardService.getById(id);
    }
}
