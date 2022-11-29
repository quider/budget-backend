package pl.adriankozlowski.budgetbackend.application.cqrs.query;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AccountBalanceQuery {
    private UUID id;
}
