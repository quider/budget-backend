package pl.adriankozlowski.budgetbackend.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document(collection = "transfers")
@NoArgsConstructor
public class Transfer {
    @Id
    private Long id;
    private Transaction sourceTransaction;
    private Transaction targetTransaction;
    private Instant timestamp;

    public Transfer(Transaction sourceTransaction, Transaction targetTransaction) {
        this.sourceTransaction = sourceTransaction;
        this.targetTransaction = targetTransaction;
        this.timestamp = Instant.now();
    }
}
