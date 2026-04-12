package com.denisborovkov.javacrm.dto.exception;

public record ErrorResponse(String message,
                            String errorCode,
                            int status,
                            long timestamp,
                            String path) {
}
