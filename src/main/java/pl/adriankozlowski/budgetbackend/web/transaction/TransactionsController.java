package pl.adriankozlowski.budgetbackend.web.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.CreateTransactionCommand;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.CreateTransferCommand;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.DeleteTransactionCommand;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.UpdateTransactionCommand;
import pl.adriankozlowski.budgetbackend.application.cqrs.query.GetAllTransactions;
import pl.adriankozlowski.budgetbackend.application.cqrs.query.GetSingleTransaction;
import pl.adriankozlowski.budgetbackend.application.service.CategoryService;
import pl.adriankozlowski.budgetbackend.application.service.TransactionService;
import pl.adriankozlowski.budgetbackend.domain.model.Transaction;
import pl.adriankozlowski.budgetbackend.domain.model.Transfer;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionsController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final ApplicationContext spring;

    @GetMapping
    public Page<Transaction> getTransactions(Pageable pageable, HttpServletResponse response) {
        Page<Transaction> allTransactions = transactionService.getAllTransactions(GetAllTransactions.builder()
                .pageable(pageable)
                .build());
        response.setHeader("x-total-count", String.valueOf(allTransactions.getTotalElements()));

        return allTransactions;
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable String id) {
        return transactionService.getSingleTransaction(GetSingleTransaction.builder().id(id).build());
    }

    @PutMapping("/{id}")
    public TransactionRequest updateTransaction(@PathVariable String id, @RequestBody TransactionRequest request) {
        transactionService.updateTransaction(UpdateTransactionCommand.builder().id(id)
                .categoryId(request.getCategory().getId())
                .accountId(request.getAccount().getId())
                .build());
        request.setId(id);
        return request;
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable("id") String id){
        transactionService.deleteTransaction(DeleteTransactionCommand.builder().id(id)
                .build());
    }

    @GetMapping("/recategorize")
    public void reCategorize(){
        transactionService.reCategorize();
    }

    @PostMapping
    public Transaction createNewTransaction(@RequestBody TransactionRequest request) {

        String transactionDate = request.getTransactionDate();
        LocalDate localDate = LocalDate.parse(transactionDate, DateTimeFormatter.ISO_DATE_TIME);
        if (request.getIsTransfer() != null && request.getIsTransfer()) {
            Transfer newTransfer = transactionService.createNewTransfer(CreateTransferCommand.builder()
                    .transactionDate(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    .description(request.getDescription())
                    .targetAccountId(request.getTargetAccount().getId())
                    .accountId(request.getAccount().getId())
                    .value(request.getValue())
                    .direction(request.getDirection())
                    .category(request.getCategory().getId())
                    .build());
            return newTransfer.getTargetTransaction();
        } else {
            Transaction transaction = transactionService.createNewTransaction(CreateTransactionCommand.builder()
                    .transactionDate(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    .description(request.getDescription())
                    .accountId(request.getAccount().getId())
                    .value(request.getValue())
                    .direction(request.getDirection())
                    .category(request.getCategory().getId())
                    .build());
            return transaction;
        }
    }
}
