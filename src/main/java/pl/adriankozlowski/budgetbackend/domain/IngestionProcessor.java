package pl.adriankozlowski.budgetbackend.domain;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import pl.adriankozlowski.budgetbackend.application.service.CsvTransaction;
import pl.adriankozlowski.budgetbackend.domain.model.Account;
import pl.adriankozlowski.budgetbackend.domain.model.Direction;
import pl.adriankozlowski.budgetbackend.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
public class IngestionProcessor {

    private final ObjectId otherCategoryId;
    private final ObjectId mainAccount;

    public IngestionProcessor(ObjectId otherCategory, ObjectId mainAccount) {
        this.otherCategoryId = otherCategory;
        this.mainAccount = mainAccount;
    }

    public List<Transaction> createListOfTransactions(List<CsvTransaction> listOfCsvTransaction) {
        return listOfCsvTransaction.stream()
//                .filter(csvTransaction -> !csvTransaction.getTitle().trim().startsWith("Przelew własny"))
//                .filter(csvTransaction -> !csvTransaction.getTitle().trim().startsWith("przelew Smart Saver"))
//                .filter(csvTransaction -> !csvTransaction.getTitle().trim().startsWith("Szybki przelew"))
                .map(csvTransaction ->
                        new Transaction(new ObjectId().toHexString(),
                                csvTransaction.getTitle(),
                                getDirection(csvTransaction),
                                getValue(csvTransaction),
                                calculateTransactionDate(csvTransaction),
                                otherCategoryId.toString(),
                                getMainAccount().toString(),
                                csvTransaction.getTransactionNumber(),
                                csvTransaction.getAccount(),
                                csvTransaction.getAccountName(),
                                getSaldo(csvTransaction),
                                false)
                ).toList();

    }

    private BigDecimal getSaldo(CsvTransaction csvTransaction) {
        try {
            return new BigDecimal(csvTransaction.getSaldo().replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    private ObjectId getMainAccount() {
        return mainAccount;
    }


    private Instant calculateTransactionDate(CsvTransaction csvTransaction) {
        LocalDate localDate = LocalDate.parse(csvTransaction.getTransactionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant;
    }

    private BigDecimal getValue(CsvTransaction csvTransaction) {
        log.info(csvTransaction.toString());
        String amount = csvTransaction.getValue().isEmpty() ? csvTransaction.getBlock() : csvTransaction.getValue();
        if (amount == null) {
            log.info(csvTransaction.toString());
        }
        return new BigDecimal(amount.replace(",", "."));
    }

    public Direction getDirection(CsvTransaction csvTransaction) {
        Optional<String> or = Optional.ofNullable(csvTransaction.getValue()).or(() -> Optional.ofNullable(csvTransaction.getBlock()));
        if (or.orElseThrow().startsWith("-")) {
            return Direction.OUT;
        } else {
            return Direction.IN;
        }
    }


}
