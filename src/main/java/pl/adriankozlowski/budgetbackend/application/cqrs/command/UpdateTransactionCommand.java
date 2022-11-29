package pl.adriankozlowski.budgetbackend.application.cqrs.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateTransactionCommand {
    private String categoryId;
    private String accountId;
    private String id;
    private boolean deleted;
}
