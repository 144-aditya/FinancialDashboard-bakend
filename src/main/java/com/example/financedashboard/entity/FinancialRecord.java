package com.example.financedashboard.entity;

import com.example.financedashboard.enums.RecordType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_records", indexes = {
        @Index(name = "idx_date", columnList = "date"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_category", columnList = "category")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class FinancialRecord extends BaseEntity {

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate date;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}