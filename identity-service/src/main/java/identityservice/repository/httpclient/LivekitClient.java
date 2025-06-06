package identityservice.repository.httpclient;

import identityservice.dto.request.ImterestRequestDto;
import identityservice.dto.request.StreamerRequestDto;
import identityservice.dto.response.StreamerResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class LivekitClient {
    private final String EXPRESS_BASE_URL = "http://localhost:3002";

    @Autowired
    private RestTemplate restTemplate;


    public StreamerResponseDto StreamerRegister(StreamerRequestDto streamerRequestDto) {
        String url = EXPRESS_BASE_URL + "/streamer";

        HttpEntity<StreamerRequestDto> request = new HttpEntity<>(streamerRequestDto);

        try {
            ResponseEntity<StreamerResponseDto> response = restTemplate.postForEntity(url, request, StreamerResponseDto.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to create user on Streamer. Status: " + response.getStatusCode());
            }
        } catch (HttpStatusCodeException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("Error response from Streamer API: {}", errorBody, e);
            throw new RuntimeException("Error from Streamer API: ", e);
        }
    }
}
