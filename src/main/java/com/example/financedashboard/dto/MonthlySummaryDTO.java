package com.example.financedashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MonthlySummaryDTO {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal netBalance;
}