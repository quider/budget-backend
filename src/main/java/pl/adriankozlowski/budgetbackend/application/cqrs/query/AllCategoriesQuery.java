package pl.adriankozlowski.budgetbackend.application.cqrs.query;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Builder
@Data
public class AllCategoriesQuery {

    private Pageable pageable;
}
