package com.suzumiya.campustrade.dto;

import lombok.Data;

@Data
public class AuditResponseDTO {
    private boolean passed;
    private String reason;
}
