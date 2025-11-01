package com.geopokrovskiy.webhook_collector_service.errorhandling;

import com.geopokrovskiy.dto.webhook_service.response.ErrorCallbackResponseDto;
import com.geopokrovskiy.webhook_collector_service.service.WebhookService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Data
@Slf4j
public class ControllerExceptionHandler {

    private final WebhookService webhookService;

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorCallbackResponseDto> handleIOException(IOException exception) {
        ErrorCallbackResponseDto errorCallbackResponseDto = new ErrorCallbackResponseDto(exception.getMessage(), HttpStatusCode.valueOf(400));
        return new ResponseEntity<>(errorCallbackResponseDto, HttpStatusCode.valueOf(400));
    }
}
