package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.adriankozlowski.budgetbackend.domain.model.Account;
import pl.adriankozlowski.budgetbackend.domain.presentation.TransactionPresentation;

import java.util.UUID;

public interface AccountRepository extends MongoRepository<Account, String> {

//    @Query(nativeQuery = true, value = "refresh materialized view account_balance")
//    void refreshAccountView();

//    @Query(nativeQuery = true, value = "select a.name as accountName, direction, c.name as categoryName, " +
//            "sum(value) as value from transactions t join categories c on t.category_id = c.id " +
//            "join accounts a on a.id = t.account_id where a.main=true " +
//            "group by direction, c.name, a.name")
//    TransactionPresentation getDashboardCategories();

    @Query(value = "{main: true}",fields = "_id")
    Account getMainAccount();
}
