package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateCategory(String id, String category, String account) {
        Update update = new Update();
        update = update.set("category", new ObjectId(category));
        update = update.set("account", account);
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(where("_id").is(id)), update, "transactions");
        if (updateResult.getModifiedCount() == 1) {
            log.info("row {} edited with category value {}", id, category);
        }
    }

    public List<AccountBalance> getBalance(ObjectId accountId){
        MatchOperation account = Aggregation.match(where("account").is(accountId));
        GroupOperation group = Aggregation.group("account").sum("value").as("balance");

        Aggregation aggregation = Aggregation.newAggregation(account, group);

        AggregationResults<AccountBalance> transactions = mongoTemplate.aggregate(aggregation, "transactions", AccountBalance.class);
        return transactions.getMappedResults();
    }

    @Override
    public void softlyDelete(String id) {
        Update update = new Update();
        update = update.set("deleted", true);
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(where("_id").is(id)), update, "transactions");
        if (updateResult.getModifiedCount() == 1) {
            log.info("row {} softly deleted", id);
        }
    }
}
