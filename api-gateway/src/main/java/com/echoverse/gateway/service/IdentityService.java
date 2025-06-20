package com.echoverse.gateway.service;

import com.echoverse.gateway.dto.request.IntrospectRequestDto;
import com.echoverse.gateway.dto.response.ApiResponseDto;
import com.echoverse.gateway.dto.response.IntrospectResponseDto;
import com.echoverse.gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponseDto<IntrospectResponseDto>> introspect(String token) {
        return identityClient.introspect(IntrospectRequestDto
                .builder().token(token)
                .build());
    }
}
