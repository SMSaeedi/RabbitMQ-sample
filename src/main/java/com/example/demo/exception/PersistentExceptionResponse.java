package com.example.demo.exception;

import java.time.Instant;
import java.util.List;

public record PersistentExceptionResponse(Instant timestamp, List<String> details) {
}
