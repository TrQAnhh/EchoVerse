package identityservice.exception;

import identityservice.dto.response.ApiResponseDto;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponseDto> handleRuntimeException(RuntimeException e) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setCode(400);
        apiResponseDto.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(apiResponseDto);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        Map<String, Object> attributes = null;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintsViolation = e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attributes = constraintsViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException ex) {
            // log here
        }

        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setCode(errorCode.getCode());
        apiResponseDto.setMessage(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponseDto);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponseDto> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setCode(errorCode.getCode());
        apiResponseDto.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponseDto);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponseDto> handleException(Exception e) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setCode(ErrorCode.UNCATEGORIZED.getCode());
        apiResponseDto.setMessage(ErrorCode.UNCATEGORIZED.getMessage());

        return ResponseEntity.badRequest().body(apiResponseDto);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponseDto> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
                ApiResponseDto.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }
}
