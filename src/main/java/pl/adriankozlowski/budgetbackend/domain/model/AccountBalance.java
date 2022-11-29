package pl.adriankozlowski.budgetbackend.domain.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
public class AccountBalance {
    private ObjectId id;
    private String name;
    private BigDecimal balance;
}
