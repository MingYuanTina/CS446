package cs446.budgetMe.Repository;

import cs446.budgetMe.Model.Category;
import cs446.budgetMe.Model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByGroupId(String groupId);
}

