package pl.adriankozlowski.budgetbackend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.CreateNewCategory;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.DeleteTransactionCommand;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.UpdateTransactionCommand;
import pl.adriankozlowski.budgetbackend.domain.Categorizer;
import pl.adriankozlowski.budgetbackend.domain.Rule;
import pl.adriankozlowski.budgetbackend.domain.model.Category;
import pl.adriankozlowski.budgetbackend.domain.model.Transaction;
import pl.adriankozlowski.budgetbackend.intrastructure.repository.AccountRepository;
import pl.adriankozlowski.budgetbackend.intrastructure.repository.CategoryRepository;
import pl.adriankozlowski.budgetbackend.intrastructure.repository.RuleRepository;
import pl.adriankozlowski.budgetbackend.intrastructure.repository.TransactionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final RuleRepository ruleRepository;
    private final AccountRepository accountRepository;
    private List<Category> categories;

    @Cacheable("categories")
    public List<Category> getAllCategories() {
        categories = categoryRepository.findAll();
        return categories;
    }

    @CacheEvict("categories")
    public Category createNewCategory(CreateNewCategory command) {
        Category category = new Category();
        category.setName(command.getName());
        category.setCreated(Instant.now());
        category.setModified(Instant.now());
        categoryRepository.save(category);
        return category;
    }

    @EventListener
    @Async
    public void createCategoryRule(UpdateTransactionCommand cmd){
        Transaction trn = transactionRepository.findById(cmd.getId()).get();
        Rule rule = new Rule();
        rule.setAccountNumber(trn.getAccountNumber());
        rule.setCategoryId(new ObjectId(cmd.getCategoryId()));
        rule.setDeleted(trn.isDeleted());
        ruleRepository.save(rule);
    }

    @EventListener
    @Async
    public void createCategoryRule(DeleteTransactionCommand cmd){
        Transaction trn = transactionRepository.findById(cmd.getId()).get();
        Rule rule = new Rule();
        rule.setAccountNumber(trn.getAccountNumber());
        rule.setDeleted(trn.isDeleted());
        rule.setDirection(trn.getDirection());
        ruleRepository.save(rule);
        Categorizer categorizer = new Categorizer(ruleRepository.findAll(),
                new ObjectId(categoryRepository.getDefaultCategory().getId()),
                new ObjectId(accountRepository.getMainAccount().getId()));
        categorizer.categorizeTransaction(trn);
        transactionRepository.save(trn);
    }

    public ObjectId getDefaultCategory() {
        return new ObjectId(categoryRepository.getDefaultCategory().getId());
    }
}
