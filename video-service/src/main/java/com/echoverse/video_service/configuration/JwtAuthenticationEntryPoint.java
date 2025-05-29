package com.echoverse.video_service.configuration;

import com.echoverse.video_service.exception.ErrorCode;
import com.echoverse.video_service.dto.response.ApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    private void handleErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponseDto apiResponseDto = ApiResponseDto.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponseDto));
        response.flushBuffer();
    }

    //401 Unauthorized
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        handleErrorResponse(response, ErrorCode.UNAUTHENTICATED);
    }

    //  403 Forbidden
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        handleErrorResponse(response, ErrorCode.UNAUTHORIZED);
    }
}
