package pl.adriankozlowski.budgetbackend.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.adriankozlowski.budgetbackend.domain.model.Account;
import pl.adriankozlowski.budgetbackend.intrastructure.repository.AccountRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public ObjectId getMainAccount(){
        return new ObjectId(accountRepository.getMainAccount().getId());
    }

//    @Cacheable("accounts")
    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }
}
