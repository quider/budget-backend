package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;

import java.util.UUID;

public interface AccountBalanceRepository extends MongoRepository<AccountBalance, UUID>, CustomAccountBalanceRepository {
}
