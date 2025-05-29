package com.echoverse.gateway.repository;


import com.echoverse.gateway.dto.request.IntrospectRequestDto;
import com.echoverse.gateway.dto.response.ApiResponseDto;
import com.echoverse.gateway.dto.response.IntrospectResponseDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(value = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponseDto<IntrospectResponseDto>> introspect(@RequestBody IntrospectRequestDto request);
}
