package pl.adriankozlowski.budgetbackend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.adriankozlowski.budgetbackend.application.service.TransactionService;
import pl.adriankozlowski.budgetbackend.application.service.vo.DashboardPiePiece;
import pl.adriankozlowski.budgetbackend.domain.presentation.TransactionPresentation;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final TransactionService transactionService;

    @GetMapping("/pie")
    public List<DashboardPiePiece> getCategories(){
        return transactionService.getTransactionValuesByCategory();
    }
}
