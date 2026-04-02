package com.example.financedashboard.service;

import com.example.financedashboard.dto.DashboardSummaryDTO;
import com.example.financedashboard.dto.FinancialRecordDTO;
import com.example.financedashboard.dto.MonthlySummaryDTO;
import com.example.financedashboard.entity.FinancialRecord;
import com.example.financedashboard.entity.User;
import com.example.financedashboard.enums.RecordType;
import com.example.financedashboard.exception.ResourceNotFoundException;
import com.example.financedashboard.repository.FinancialRecordRepository;
import com.example.financedashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public Page<FinancialRecordDTO> getUserRecords(Long userId, Pageable pageable) {
        return recordRepository.findAllByUserIdAndDeletedFalse(userId, pageable)
                .map(this::mapToDTO);
    }

    public FinancialRecordDTO getRecordById(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return mapToDTO(record);
    }

    @Transactional
    public FinancialRecordDTO createRecord(Long userId, FinancialRecordDTO recordDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        FinancialRecord record = new FinancialRecord();
        record.setAmount(recordDTO.getAmount());
        record.setType(recordDTO.getType());
        record.setCategory(recordDTO.getCategory());
        record.setDate(recordDTO.getDate());
        record.setDescription(recordDTO.getDescription());
        record.setUser(user);

        FinancialRecord savedRecord = recordRepository.save(record);
        return mapToDTO(savedRecord);
    }

    @Transactional
    public FinancialRecordDTO updateRecord(Long id, FinancialRecordDTO recordDTO) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        record.setAmount(recordDTO.getAmount());
        record.setType(recordDTO.getType());
        record.setCategory(recordDTO.getCategory());
        record.setDate(recordDTO.getDate());
        record.setDescription(recordDTO.getDescription());

        FinancialRecord updatedRecord = recordRepository.save(record);
        return mapToDTO(updatedRecord);
    }

    @Transactional
    public void deleteRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        record.setDeleted(true);
        recordRepository.save(record);
    }

    public DashboardSummaryDTO getDashboardSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = Optional.ofNullable(
                recordRepository.sumByUserIdAndType(userId, RecordType.INCOME)
        ).orElse(BigDecimal.ZERO);

        BigDecimal totalExpenses = Optional.ofNullable(
                recordRepository.sumByUserIdAndType(userId, RecordType.EXPENSE)
        ).orElse(BigDecimal.ZERO);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        List<Object[]> categoryResults = recordRepository.getCategoryTotals(userId, RecordType.EXPENSE);
        for (Object[] result : categoryResults) {
            categoryTotals.put((String) result[0], (BigDecimal) result[1]);
        }

        List<MonthlySummaryDTO> monthlySummary = new ArrayList<>();
        List<Object[]> monthlyResults = recordRepository.getMonthlySummary(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Object[] result : monthlyResults) {
            int year = ((Number) result[0]).intValue();
            int month = ((Number) result[1]).intValue();
            BigDecimal income = (BigDecimal) result[2];
            BigDecimal expense = (BigDecimal) result[3];

            monthlySummary.add(MonthlySummaryDTO.builder()
                    .month(String.format("%d-%02d", year, month))
                    .income(income)
                    .expenses(expense)
                    .netBalance(income.subtract(expense))
                    .build());
        }

        return DashboardSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .categoryTotals(categoryTotals)
                .monthlySummary(monthlySummary)
                .build();
    }

    private FinancialRecordDTO mapToDTO(FinancialRecord record) {
        FinancialRecordDTO dto = new FinancialRecordDTO();
        dto.setId(record.getId());
        dto.setAmount(record.getAmount());
        dto.setType(record.getType());
        dto.setCategory(record.getCategory());
        dto.setDate(record.getDate());
        dto.setDescription(record.getDescription());
        dto.setUserId(record.getUser().getId());
        return dto;
    }
}