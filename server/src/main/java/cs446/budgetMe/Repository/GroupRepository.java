package cs446.budgetMe.Repository;

import cs446.budgetMe.Model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
    Group findByGroupName(String groupName);
}

