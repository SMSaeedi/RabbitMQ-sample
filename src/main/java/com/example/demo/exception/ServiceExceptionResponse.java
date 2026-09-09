package com.example.demo.exception;

import java.time.Instant;

public record ServiceExceptionResponse(Instant timestamp, String details) {
}
