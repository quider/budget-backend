package pl.adriankozlowski.budgetbackend.domain.presentation;

import java.math.BigDecimal;

public interface TransactionPresentation {
    String getName();
    BigDecimal getValue();
}
