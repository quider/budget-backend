package pl.adriankozlowski.budgetbackend.application.cqrs.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteTransactionCommand {
    private String id;
}
