package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.springframework.data.domain.Pageable;
import pl.adriankozlowski.budgetbackend.domain.model.AccountBalance;

import java.util.List;

public interface CustomAccountBalanceRepository {
    List<AccountBalance> findAllAccounts(Pageable pageable);
}
