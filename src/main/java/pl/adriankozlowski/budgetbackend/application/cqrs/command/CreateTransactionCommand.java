package pl.adriankozlowski.budgetbackend.application.cqrs.command;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import pl.adriankozlowski.budgetbackend.domain.model.Category;
import pl.adriankozlowski.budgetbackend.domain.model.Direction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class CreateTransactionCommand {
    private String accountId;
    private String description;
    private Direction direction;
    private BigDecimal value;
    private Instant transactionDate;
    private String category;
    private String accountNumber;
}
