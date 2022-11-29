package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;
import pl.adriankozlowski.budgetbackend.domain.model.Direction;
import pl.adriankozlowski.budgetbackend.domain.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String>, CustomTransactionRepository {

    @Query("{transactionId: ?0, direction: ?1, description: ?2}")
    Transaction findByTransactionId(String transactionId, Direction direction, String description);
//    @Query("update Transaction t set t.category = :category where t.id = :id")

    @Query("{deleted: false}")
    Page<Transaction> findAllNotDeleted(Pageable pageable);

    //    @Query("select c.name as name, sum(t.value) as value from Transaction t join Category c on t.category.id = c.id" +
//            " join Account a on a.id = t.account.id where a.main=true and t.direction='OUT'" +
//            "group by t.direction, c.name, a.name")
//    List<TransactionPresentation> getPieData();
}
