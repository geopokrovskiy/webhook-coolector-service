package com.geopokrovskiy.webhook_collector_service.errorhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geopokrovskiy.dto.webhook_service.response.ErrorCallbackResponseDto;
import com.geopokrovskiy.webhook_collector_service.exception.XSignatureNotValidException;
import com.geopokrovskiy.webhook_collector_service.security.WebhookSecurity;
import com.geopokrovskiy.webhook_collector_service.service.WebhookService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@AllArgsConstructor
public class RequestCachingFilter extends OncePerRequestFilter {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;
    private final WebhookSecurity webhookSecurity;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        long start = System.currentTimeMillis();
        // this is for request logging and for bad request treatment
        ContentCachingRequestWrapper wrappedRequest =
                (request instanceof ContentCachingRequestWrapper)
                        ? (ContentCachingRequestWrapper) request
                        : new ContentCachingRequestWrapper(request);


        // this if for response logging
        ContentCachingResponseWrapper wrappedResponse =
                (response instanceof ContentCachingResponseWrapper)
                        ? (ContentCachingResponseWrapper) response
                        : new ContentCachingResponseWrapper(response);

        String requestBody = "";
        String responseBody = "";

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);

            // this is for signature validation
            String xSignatureHeader = wrappedRequest.getHeader("X-Signature");
            String providerName = wrappedRequest.getHeader("Cookie");

            requestBody = getContentAsString(wrappedRequest.getContentAsByteArray());
            responseBody = getContentAsString(wrappedResponse.getContentAsByteArray());
            boolean webhookIsValid = webhookSecurity.validateXSignature(xSignatureHeader, requestBody, providerName);
            if (!webhookIsValid) {
                throw new XSignatureNotValidException();
            }
        } catch (XSignatureNotValidException e) {
            log.warn("Incorrect X-Signature or Cookie header");
            webhookService.createUnknownCallback(requestBody);
            processIncorrectRequest(wrappedResponse, "Incorrect X-Signature header", 401);
        } catch (Exception e) {
            log.warn("Unprocessable request body: {} ", requestBody);
            webhookService.createUnknownCallback(requestBody);
            processIncorrectRequest(wrappedResponse, "Unprocessable request body", 422);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("HTTP Request: method={}, uri={}, status={}, durationMs={}, ip={}, body={}, response={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    wrappedResponse.getStatus(),
                    duration,
                    request.getRemoteAddr(),
                    requestBody,
                    responseBody
            );
            // copying the cached response body back to the response
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void processIncorrectRequest(ContentCachingResponseWrapper wrappedResponse, String message, int status) throws IOException {
        ErrorCallbackResponseDto errorCallbackResponseDto = new ErrorCallbackResponseDto(message, HttpStatusCode.valueOf(422));
        wrappedResponse.setStatus(status);
        wrappedResponse.setContentType("application/json");
        wrappedResponse.getWriter().write(objectMapper.writeValueAsString(errorCallbackResponseDto));
    }

    private String getContentAsString(byte[] buf) {
        try {
            return new String(buf, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[unreadable]";
        }
    }
}
