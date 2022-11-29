package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.query.Param;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;

import java.util.List;
import java.util.UUID;

public interface CustomTransactionRepository {
    void updateCategory(@Param("id") String id, @Param("category") String category, String account);

    void softlyDelete(String id);

    public List<AccountBalance> getBalance(ObjectId accountId);
}
