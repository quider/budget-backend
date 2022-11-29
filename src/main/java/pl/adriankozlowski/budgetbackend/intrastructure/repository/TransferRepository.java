package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.adriankozlowski.budgetbackend.domain.model.Transfer;

public interface TransferRepository extends MongoRepository<Transfer, Long> {
}
