package com.denisborovkov.javacrm.common.exception;

public record ErrorResponse(String message,
                            String errorCode,
                            int status,
                            long timestamp,
                            String path) {
}



