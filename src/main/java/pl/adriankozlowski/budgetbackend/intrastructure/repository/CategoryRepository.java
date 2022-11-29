package pl.adriankozlowski.budgetbackend.intrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.adriankozlowski.budgetbackend.domain.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
    @Query(fields = "{_id: 1}", value = "{def: true}")
    Category getDefaultCategory();
}
