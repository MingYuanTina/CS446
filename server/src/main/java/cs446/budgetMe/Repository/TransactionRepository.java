package cs446.budgetMe.Repository;

import cs446.budgetMe.Model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByGroupId(String groupId);
}

