package identityservice.controller;

import com.nimbusds.jose.JOSEException;
import identityservice.dto.request.*;
import identityservice.dto.response.*;
import identityservice.service.AuthenticationService;
import identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuthenticationController {

    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/register")
    ApiResponseDto<UserResponseDto> register(@RequestBody @Valid UserCreationRequestDto userDto) {
        return ApiResponseDto.<UserResponseDto>builder()
                .result(userService.createUser(userDto))
                .build();
    }

    @PostMapping("/login")
    ApiResponseDto<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationRequestDto authenticationRequestDto) {
        var result = authenticationService.login(authenticationRequestDto);
        return ApiResponseDto.<AuthenticationResponseDto>builder()
                .result(result).build();
    }

    @PostMapping("/logout")
    public ApiResponseDto<Void> logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequestDto);
        return ApiResponseDto.<Void>builder().code(200).message("user logout successfully").build();
    }

    @PostMapping("/introspect")
    public ApiResponseDto<IntrospectResponseDto> introspect(@RequestBody IntrospectRequestDto introspectRequestDto) throws ParseException, JOSEException {
        var result = authenticationService.introspect(introspectRequestDto);
        ApiResponseDto<IntrospectResponseDto> apiResponseDto = new ApiResponseDto<>();
        apiResponseDto.setCode(200);
        apiResponseDto.setResult(result);
        return apiResponseDto;
    }

    @PostMapping("/refresh-token")
    public ApiResponseDto<RefreshTokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(refreshTokenRequestDto);
        return ApiResponseDto.<RefreshTokenResponseDto>builder()
                .result(result)
                .code(200)
                .build();
    }
}
