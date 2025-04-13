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
    ApiResponse<UserResponseDto> register(@RequestBody @Valid UserCreationRequestDto userDto) {
        return ApiResponse.<UserResponseDto>builder()
                .result(userService.createUser(userDto))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponseDto> login(@RequestBody @Valid AuthenticationRequestDto authenticationRequestDto) {
        var result = authenticationService.login(authenticationRequestDto);
        return ApiResponse.<AuthenticationResponseDto>builder()
                .result(result).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequestDto);
        return ApiResponse.<Void>builder().code(200).message("user logout successfully").build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponseDto> introspect(@RequestBody @Valid IntrospectRequestDto introspectRequestDto) throws ParseException, JOSEException {
        var result = authenticationService.introspect(introspectRequestDto);
        ApiResponse<IntrospectResponseDto> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setResult(result);
        return apiResponse;
    }

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(refreshTokenRequestDto);
        return ApiResponse.<RefreshTokenResponseDto>builder()
                .result(result)
                .code(200)
                .build();
    }
}
