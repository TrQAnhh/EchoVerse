package identityservice.repository.httpclient;

import identityservice.dto.request.ImterestRequestDto;
import identityservice.dto.request.UserCreationRequestDto;
import identityservice.dto.response.ImterestResponseDto;
import identityservice.dto.response.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ImterestClient {

    private final String EXPRESS_BASE_URL = "https://imterest-be.fly.dev";

    @Autowired
    private RestTemplate restTemplate;


    public ImterestResponseDto createUserImterest(ImterestRequestDto userRequest) {
        String url = EXPRESS_BASE_URL + "/auth/register";

        HttpEntity<ImterestRequestDto> request = new HttpEntity<>(userRequest);

        try {
            ResponseEntity<ImterestResponseDto> response = restTemplate.postForEntity(url, request, ImterestResponseDto.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to create user on Imterest. Status: " + response.getStatusCode());
            }
        } catch (HttpStatusCodeException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("Error response from Interest API: {}", errorBody, e);
            throw new RuntimeException("Error from Imterest API: ", e);
        }
    }
}
