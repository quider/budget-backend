package pl.adriankozlowski.budgetbackend.application.cqrs.command;

import lombok.Builder;
import lombok.Getter;
import pl.adriankozlowski.budgetbackend.domain.model.AccountType;

import java.math.BigDecimal;

@Getter
@Builder
public class CreateNewAccount {
    private String icon;
    private String name;
    private BigDecimal startBalance;
    private AccountType type;
}
