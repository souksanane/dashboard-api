package com.dashboard.dashboard_api.transaction.service;

import com.dashboard.dashboard_api.transaction.exception.TransactionNotFoundException;
import com.dashboard.dashboard_api.transaction.model.Transaction;
import com.dashboard.dashboard_api.transaction.model.TransactionRepository;
import com.dashboard.dashboard_api.transaction.model.TransactionType;
import com.dashboard.dashboard_api.transaction.view.TransactionRequest;
import com.dashboard.dashboard_api.transaction.view.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public Page<TransactionResponse> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(TransactionResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public TransactionResponse findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction non trouvée avec l'id: " + id));
        return TransactionResponse.fromEntity(transaction);
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .category(request.getCategory() != null ? request.getCategory().trim() : null)
                .build();
        transaction = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(transaction);
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction non trouvée avec l'id: " + id));
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(request.getCategory() != null ? request.getCategory().trim() : null);
        transaction = transactionRepository.save(transaction);
        return TransactionResponse.fromEntity(transaction);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException("Transaction non trouvée avec l'id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> findByType(TransactionType type, Pageable pageable) {
        return transactionRepository.findByType(type, pageable).map(TransactionResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> findByCategory(String category, Pageable pageable) {
        return transactionRepository.findByCategoryIgnoreCase(category, pageable)
                .map(TransactionResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> findByDateRange(Instant from, Instant to, Pageable pageable) {
        return transactionRepository.findByDateRange(from, to, pageable).map(TransactionResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalanceBetween(Instant from, Instant to) {
        BigDecimal credits = transactionRepository.sumAmountByTypeAndDateRange(TransactionType.CREDIT, from, to);
        BigDecimal debits = transactionRepository.sumAmountByTypeAndDateRange(TransactionType.DEBIT, from, to);
        if (credits == null) credits = BigDecimal.ZERO;
        if (debits == null) debits = BigDecimal.ZERO;
        return credits.subtract(debits);
    }
}
