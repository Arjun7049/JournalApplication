package com.journal.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorInfo {
    private String errorMessage;
    private String errorCode;
    private LocalDateTime timestamp;
}
