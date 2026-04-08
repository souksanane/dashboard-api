package com.dashboard.dashboard_api.transaction.view;

import com.dashboard.dashboard_api.transaction.model.Transaction;
import com.dashboard.dashboard_api.transaction.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Response to a transaction request or a list of transactions
 * with pagination, sorting and filtering
 * @param id the id of the transaction
 * @param amount the amount of the transaction
 * @param type the type of the transaction (CREDIT or DEBIT)
 * @param description the description of the transaction
 * @param category the category of the transaction
 * @param createdAt the creation date of the transaction
 * @param updatedAt the last update date of the transaction
 * @return the transaction response
 */
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;

    public static TransactionResponse fromEntity(Transaction entity) {
        return TransactionResponse.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .type(entity.getType())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
