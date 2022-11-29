package pl.adriankozlowski.budgetbackend.web.account;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.adriankozlowski.budgetbackend.application.cqrs.command.CreateNewAccount;
import pl.adriankozlowski.budgetbackend.application.cqrs.query.AllAccountsQuery;
import pl.adriankozlowski.budgetbackend.application.service.AccountService;
import pl.adriankozlowski.budgetbackend.application.service.TransactionService;
import pl.adriankozlowski.budgetbackend.domain.model.Account;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    @GetMapping("/balances")
    public Page<AccountBalance> getAllAccountsBalances(Pageable pageable, HttpServletResponse response) {
        List<AccountBalance> handle = accountService.getAccounts().stream().map(account -> {

        AccountBalance ab = transactionService.getValuesOfTransactionFromAccount(account.getId());
        return ab;
        }).toList();
        return new PageImpl<>(handle,pageable,handle.size());
    }

    @GetMapping
    public Page<Account> getAllAccounts(Pageable pageable, HttpServletResponse response) {
        List<Account> handle = accountService.getAccounts();
        return new PageImpl<>(handle,pageable,handle.size());
    }

    @PostMapping
    public Map createNewAccount(@RequestBody NewAccountRequest request) {
        String id = transactionService.createNewAccount(CreateNewAccount.builder()
                .startBalance(request.getStartBalance())
                .name(request.getName()).build());
        HashMap<String, String> result = new HashMap<>();
        result.put("id", id);
        return result;
    }

    @DeleteMapping("/{id}")
    public void closeAccount(@PathVariable("id") String accountId) {

    }
}
