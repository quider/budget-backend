package pl.adriankozlowski.budgetbackend.web.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.adriankozlowski.budgetbackend.domain.model.Account;
import pl.adriankozlowski.budgetbackend.domain.model.Category;
import pl.adriankozlowski.budgetbackend.domain.model.Direction;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {
    private String description;
    private DropdownObject account;
    private DropdownObject targetAccount;
    private BigDecimal value;
    private Direction direction;
    private String transactionDate;
    private DropdownObject category;
    private Boolean isTransfer;
    private String id;
}
