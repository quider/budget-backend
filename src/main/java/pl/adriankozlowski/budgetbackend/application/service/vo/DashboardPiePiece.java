package pl.adriankozlowski.budgetbackend.application.service.vo;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DashboardPiePiece {
    String name;
    BigDecimal value;
}
