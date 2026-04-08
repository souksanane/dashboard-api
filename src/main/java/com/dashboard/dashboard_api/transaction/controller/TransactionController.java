package com.dashboard.dashboard_api.transaction.controller;

import com.dashboard.dashboard_api.transaction.model.TransactionType;
import com.dashboard.dashboard_api.transaction.service.TransactionService;
import com.dashboard.dashboard_api.transaction.view.TransactionRequest;
import com.dashboard.dashboard_api.transaction.view.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "API de gestion des transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Liste paginée des transactions")
    public Page<TransactionResponse> findAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return transactionService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une transaction")
    public TransactionResponse findById(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer une transaction")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse created = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une transaction")
    public TransactionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        return transactionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une transaction")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        transactionService.deleteById(id);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Filtrer par type (CREDIT ou DEBIT)")
    public Page<TransactionResponse> findByType(
            @PathVariable TransactionType type,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return transactionService.findByType(type, pageable);
    }

    @GetMapping("/category")
    @Operation(summary = "Filtrer par catégorie")
    public Page<TransactionResponse> findByCategory(
            @RequestParam String category,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return transactionService.findByCategory(category, pageable);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Filtrer par plage de dates")
    public Page<TransactionResponse> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return transactionService.findByDateRange(from, to, pageable);
    }

    @GetMapping("/balance")
    @Operation(summary = "Solde sur une période (crédits - débits)")
    public Map<String, Object> getBalance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return Map.of(
                "from", from,
                "to", to,
                "balance", transactionService.getBalanceBetween(from, to)
        );
    }
}
