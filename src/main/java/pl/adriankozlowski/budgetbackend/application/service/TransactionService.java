package pl.adriankozlowski.budgetbackend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.*;
import pl.adriankozlowski.budgetbackend.application.cqrs.query.AllAccountsQuery;
import pl.adriankozlowski.budgetbackend.application.cqrs.query.GetAllTransactions;
import pl.adriankozlowski.budgetbackend.application.cqrs.query.GetSingleTransaction;
import pl.adriankozlowski.budgetbackend.application.service.vo.DashboardPiePiece;
import pl.adriankozlowski.budgetbackend.domain.Categorizer;
import pl.adriankozlowski.budgetbackend.domain.model.*;
import pl.adriankozlowski.budgetbackend.intrastructure.repository.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository repository;
    private final ApplicationEventPublisher publisher;
    private final AccountRepository accountRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final TransferRepository transferRepository;
    private final CategoryRepository categoryRepository;
    private final AccountService accountService;
    private final RuleRepository ruleRepository;

    public List<AccountBalance> getAllAccounts(AllAccountsQuery build) {
        return accountBalanceRepository.findAllAccounts(build.getPageable());
    }

    public Page<Transaction> getAllTransactions(GetAllTransactions query) {
        Page<Transaction> all = repository.findAllNotDeleted(query.getPageable());
        return all;
    }

    public Transaction createNewTransaction(CreateTransactionCommand command) {
        BigDecimal value = command.getValue();
        if (Direction.OUT.equals(command.getDirection())) {
            value = value.multiply(BigDecimal.valueOf(-1));
        }
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction(
                ObjectId.get().toHexString(),
                command.getDescription(),
                command.getDirection(),
                value,
                command.getTransactionDate(),
                command.getCategory(),
                command.getAccountId(),
                id.toString(),
                command.getAccountNumber(),
                null
                , null,
                false);
        Transaction savedTransaction = repository.save(transaction);

//        accountRepository.refreshAccountView();

        return savedTransaction;
    }

    public String createNewAccount(CreateNewAccount command) {
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction(
                new ObjectId().toHexString(),
                "Utworzenie nowego konta",
                switch (command.getType()) {
                    case DEBT -> Direction.OUT;
                    case LOAN, GENERAL -> Direction.IN;
                },
                command.getStartBalance(),
                Instant.now(),
                categoryRepository.getDefaultCategory().getId(),
                createAccountObject(command).getId(),
                uuid.toString(),
                null,
                null,
                null,
                false
        );
        repository.save(transaction);

        return transaction.getAccount();
    }

    private Account createAccountObject(CreateNewAccount command) {
        return Account.builder()
                .id(new ObjectId().toString())
                .name(command.getName())
                .icon(command.getIcon())
                .startBalance(command.getStartBalance())
                .modified(Instant.now())
                .type(command.getType())
                .build();
    }

    public Transfer createNewTransfer(CreateTransferCommand command) {
        Transaction sourceTransaction = new Transaction();
//        sourceTransaction.setId(UUID.randomUUID());
        sourceTransaction.setTransactionDate(command.getTransactionDate());
        sourceTransaction.setAccount(command.getAccountId());
        sourceTransaction.setCategory(command.getCategory());
        sourceTransaction.setDirection(Direction.OUT);
        sourceTransaction.setDescription(command.getDescription());
        sourceTransaction.setValue(command.getValue().negate());
        sourceTransaction.setTransactionId(UUID.randomUUID().toString());

        Transaction targetTransaction = new Transaction();
//        targetTransaction.setId(UUID.randomUUID());
        targetTransaction.setTransactionDate(command.getTransactionDate());
        targetTransaction.setAccount(command.getTargetAccountId());
        targetTransaction.setCategory(command.getCategory());
        targetTransaction.setDirection(Direction.IN);
        targetTransaction.setDescription(command.getDescription());
        targetTransaction.setValue(command.getValue());
        targetTransaction.setTransactionId(UUID.randomUUID().toString());

        repository.save(sourceTransaction);
        repository.save(targetTransaction);

        Transfer transfer = new Transfer(sourceTransaction, targetTransaction);
        return transferRepository.save(transfer);
    }

    public void updateTransaction(UpdateTransactionCommand command) {
        repository.updateCategory(command.getId(), command.getCategoryId(), command.getAccountId());
        publisher.publishEvent(command);
    }

    public void saveIngestedTransactions(List<Transaction> listOfTransactions) {
        listOfTransactions.forEach(transaction -> {
            Transaction existingTransaction = repository.findByTransactionId(transaction.getTransactionId(), transaction.getDirection(), transaction.getDescription());
            if (existingTransaction == null) {
                repository.save(transaction);
            } else {
                log.info("Transaaction {} exists", transaction.getId());
            }

        });
    }

    public Transaction getSingleTransaction(GetSingleTransaction query) {
        return repository.findById(query.getId()).orElseThrow();
    }

    public List<DashboardPiePiece> getTransactionValuesByCategory() {
//        List<TransactionPresentation> pieData = repository.getPieData();
//        BigDecimal total = pieData.stream()
//                .map(transactionPresentation -> transactionPresentation.getValue())
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        return pieData.stream()
//                .map(tp -> new DashboardPiePiece(tp.getName(), tp.getValue().divide(total, RoundingMode.DOWN)))
//                .toList();
        return null;
    }

    public void deleteTransaction(DeleteTransactionCommand build) {
        repository.softlyDelete(build.getId());
//        publisher.publishEvent(build);
    }

    public void reCategorize() {
        List<Transaction> transactions = repository.findAll()
                .stream()
                .map(transaction -> new Categorizer(ruleRepository.findAll(),
                        new ObjectId(categoryRepository.getDefaultCategory().getId()),
                        new ObjectId(accountRepository.getMainAccount().getId())
                        ).categorizeTransaction(transaction))
                .toList();

        repository.saveAll(transactions);
    }

    public AccountBalance getValuesOfTransactionFromAccount(String account) {
        List<AccountBalance> balance = repository.getBalance(new ObjectId(account));
        if (balance.size()>1) {
            throw new RuntimeException("More than one account with same id");
        } else if (  balance.isEmpty()){
          return new AccountBalance();
        }
        return balance.get(0);
    }
}
