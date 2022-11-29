package pl.adriankozlowski.budgetbackend.application.cqrs.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetSingleTransaction {

    private String id;
}
