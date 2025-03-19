package com.accenture.controller.advice;

import java.time.LocalDateTime;

public record MessageError( LocalDateTime date,
                            String type,
                            String message)

 {
}
