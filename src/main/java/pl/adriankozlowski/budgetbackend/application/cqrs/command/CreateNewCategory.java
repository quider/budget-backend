package pl.adriankozlowski.budgetbackend.application.cqrs.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateNewCategory {
    private final String name;
}
