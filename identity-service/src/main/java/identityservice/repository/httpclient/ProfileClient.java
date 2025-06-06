package identityservice.repository.httpclient;

import identityservice.configuration.AuthenticationRequestInterceptor;
import identityservice.dto.request.ProfileCreationRequestDto;
import identityservice.dto.response.ApiResponseDto;
import identityservice.dto.response.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponseDto createProfile(@RequestBody ProfileCreationRequestDto request);

    @GetMapping("/internal/user/{userId}")
    ApiResponseDto<UserProfileResponseDto> getProfileByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/internal")
    ApiResponseDto<List<UserProfileResponseDto>> getAllProfiles();
}
