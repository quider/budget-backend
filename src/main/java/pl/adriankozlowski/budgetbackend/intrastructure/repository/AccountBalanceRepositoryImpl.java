package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;

import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AccountBalanceRepositoryImpl implements CustomAccountBalanceRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<AccountBalance> findAllAccounts(Pageable pageable) {
        Criteria criteria = new Criteria().orOperator(where("deleted").is(Boolean.FALSE),
                where("deleted").is(null),
                where("deleted").exists(Boolean.FALSE));
        MatchOperation deleted = Aggregation.match(criteria);
        LookupOperation lookup = Aggregation.lookup("accounts", "account", "_id", "accountDetails");
        UnwindOperation unwind = Aggregation.unwind("accountDetails", true);
        GroupOperation balance = Aggregation.group("accountDetails._id","accountDetails.name").sum("value").as("balance");
        Aggregation aggregation = newAggregation(List.of(deleted,lookup, unwind, balance));
        AggregationResults<AccountBalance> accounts = mongoTemplate.aggregate(
                aggregation,
                "transactions",
                AccountBalance.class
        );
        return accounts.getMappedResults();
    }
}
