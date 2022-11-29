package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.adriankozlowski.budgetbackend.domain.Rule;

public interface RuleRepository extends MongoRepository<Rule, String> {
}
