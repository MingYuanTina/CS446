package cs446.budgetMe.Repository;

import cs446.budgetMe.Model.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, String> {
    List<Goal> findByGroupId(String groupId);
}

