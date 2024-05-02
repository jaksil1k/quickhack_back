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
            return ResponseEntity.ok(new ChatResponse("NOT_DOC"));
        }
        GPTRequest request = new GPTRequest(model, fileResponse.getId());
        ChatResponse response = null;
        if (chatRequest.getType().equals("IDCard")) {
            template.getInterceptors().add(((request2, body2, execution) -> {
                request2.getHeaders().add("OpenAI-Beta", "assistants=v2");
                return execution.execute(request2, body2);
            }));
             response = template.postForObject(baseUrl + "/assistance", request, ChatResponse.class);
        }
        else {
            response = new ChatResponse("TYPE_NOT_SUPPORTED");
        }
        return ResponseEntity.ok(new ChatResponse("OK", response.getData()));
    }

    @PostMapping(path="/chat2", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ChatResponse> chat2(@RequestParam String type, @RequestPart MultipartFile file) {
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
//    public static ChatResponse parseJson(String json) {
////        StringBuilder newJson = new StringBuilder();
////        for (int i = 0;i < json.length();++i) {
////            if (json.charAt(i) == '\\') {
////                if (json.charAt(i + 1) == 'n') {
////                    i += 2;
////                }
////            }
////            newJson.append(json.charAt(i));
////        }
////        json = newJson.toString();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            // Parse JSON string to JsonNode
//            System.out.println(json);
//            JsonNode rootNode = objectMapper.readTree(json);
//
//            // Get values from JsonNode
//            String status = rootNode.get("status").asText();
//            System.out.println(status);
//            if (status.equals("error")) {
//                return new ChatResponse("error");
//            }
//            JsonNode dataNode = rootNode.get("data");
//            String iin = dataNode.get("iin").asText();
//            String fullname = dataNode.get("fullname").asText();
//            String birthday = dataNode.get("birthday").asText();
//            String cardId = dataNode.get("card_id").asText();
//            String givenDate = dataNode.get("given_date").asText();
//            String expirationDate = dataNode.get("expiration_date").asText();
//
//            return new ChatResponse(status, new IDCardGPTResponse(iin, fullname, birthday, cardId, givenDate, expirationDate));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ChatResponse("error");
//    }
}
