package pl.adriankozlowski.budgetbackend.application.cqrs.query;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class AllAccountsQuery {
    private final Pageable pageable;
}
