package cs446.budgetMe.Repository;

import cs446.budgetMe.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(String userName);

    User findByEmailAndPassword(String email, String password);

    User findByAuthToken(String authToken);
}

