package com.example.financedashboard.repository;

import com.example.financedashboard.entity.FinancialRecord;
import com.example.financedashboard.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    Page<FinancialRecord> findAllByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<FinancialRecord> findAllByUserIdAndDateBetweenAndDeletedFalse(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = :type AND f.deleted = false")
    BigDecimal sumByUserIdAndType(@Param("userId") Long userId, @Param("type") RecordType type);

    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f " +
            "WHERE f.user.id = :userId AND f.type = :type AND f.deleted = false " +
            "GROUP BY f.category")
    List<Object[]> getCategoryTotals(@Param("userId") Long userId, @Param("type") RecordType type);

    @Query("SELECT EXTRACT(YEAR FROM f.date) as year, EXTRACT(MONTH FROM f.date) as month, " +
            "SUM(CASE WHEN f.type = 'INCOME' THEN f.amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN f.type = 'EXPENSE' THEN f.amount ELSE 0 END) as expense " +
            "FROM FinancialRecord f WHERE f.user.id = :userId AND f.deleted = false " +
            "GROUP BY EXTRACT(YEAR FROM f.date), EXTRACT(MONTH FROM f.date) " +
            "ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlySummary(@Param("userId") Long userId);
}