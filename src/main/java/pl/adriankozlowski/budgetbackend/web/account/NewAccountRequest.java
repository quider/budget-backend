package pl.adriankozlowski.budgetbackend.web.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewAccountRequest {
    private String name;
    private BigDecimal startBalance;
}
