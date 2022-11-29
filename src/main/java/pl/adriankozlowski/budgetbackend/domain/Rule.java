package pl.adriankozlowski.budgetbackend.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.adriankozlowski.budgetbackend.domain.model.Direction;

@Data
@Document
public class Rule {

    @Id
    private String id;
    String accountNumber;
    ObjectId categoryId;
    boolean deleted;
    Direction direction;
}
