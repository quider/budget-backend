package pl.adriankozlowski.budgetbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String id;
    private String description;
    private Direction direction;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal value;
    @Field(targetType = FieldType.TIMESTAMP)
    private Instant transactionDate;
    @Field(name = "category", targetType = FieldType.OBJECT_ID)
    private String category;
    @Field(name = "account", targetType = FieldType.OBJECT_ID)
    private String account;
    private String transactionId;
    private String accountNumber;
    private String accountName;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal saldo;
    private boolean deleted;
}
