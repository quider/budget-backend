package pl.adriankozlowski.budgetbackend.domain;


import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import pl.adriankozlowski.budgetbackend.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

@Slf4j
public class Categorizer {

    private List<Rule> rules;
    private final ObjectId categoryId;
    private final ObjectId accountId;

    public Categorizer(List<Rule> rules, ObjectId categoryId, ObjectId accountId) {
        this.rules = rules;
        this.categoryId = categoryId;
        this.accountId = accountId;
    }

    public Transaction categorizeTransaction(Transaction transaction) {
        Optional<Rule> optionalRule = rules.stream().filter(rule -> rule.getAccountNumber().equals(transaction.getAccountNumber())).findFirst();
        if (transaction.getCategory() == null) {
            log.info("transaction {} has no category, settin default", transaction.getId());
            transaction.setCategory(categoryId.toString());
        }
        if (transaction.getAccount() == null) {
            log.info("transaction {} {} has no account assigned, setting default", transaction.getId().toString(), transaction.getDescription());
            transaction.setAccount(accountId.toString());
        }
        if (optionalRule.isPresent()) {
            Rule rule = optionalRule.get();
            if (!transaction.getDirection().equals(rule.direction)) {
                return transaction;
            }
            log.info("rule {} applied to transaction {}", rule.getId(), transaction.getDescription());
            ObjectId categoryId = rule.getCategoryId();
            transaction.setDeleted(rule.isDeleted());
        }
        return transaction;
    }
}