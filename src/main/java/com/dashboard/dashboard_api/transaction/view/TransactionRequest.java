package com.dashboard.dashboard_api.transaction.view;

import com.dashboard.dashboard_api.transaction.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Request to create a new transaction or update an existing transaction
 */
public class TransactionRequest {

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "type is required (CREDIT or DEBIT)")
    private TransactionType type;

    private String description;

    private String category;
}
