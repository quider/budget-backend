package pl.adriankozlowski.budgetbackend.application.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.adriankozlowski.budgetbackend.domain.IngestionProcessor;
import pl.adriankozlowski.budgetbackend.domain.model.Transaction;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService {

    private final AccountService accountService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;

    @Scheduled(fixedRate = 10000)
    public void getFileToRead(){
        Path path = Paths.get("/Users/adrian/IdeaProjects/budget/budget-backend/ingestionFiles");
        if(Files.exists(path)){
            File file = path.getFileName().toFile();
            if(file.listFiles().length==0){
                return;
            } else {
                for (File e : Objects.requireNonNull(file.listFiles())) {
                    try {
                        List<CsvTransaction> csvTransactions = beanBuilderExample(e.toPath());
                        IngestionProcessor ingestionProcessor = new IngestionProcessor(categoryService.getDefaultCategory(), accountService.getMainAccount());
                        List<Transaction> listOfTransactions = ingestionProcessor.createListOfTransactions(csvTransactions);
                        transactionService.saveIngestedTransactions(listOfTransactions);
                        e.delete();
                    } catch (Exception exception) {
                        log.error(exception.getMessage(), exception);
                    }
                }
            }
        }
    }

    public List<CsvTransaction> beanBuilderExample(Path path) throws Exception {
        List<CsvTransaction> csvTransactions = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<CsvTransaction> cb = new CsvToBeanBuilder<CsvTransaction>(reader)
                    .withType(CsvTransaction.class)
                    .withThrowExceptions(true)
                    .build();
            csvTransactions = cb.parse();
        }
        return csvTransactions;
    }
}
