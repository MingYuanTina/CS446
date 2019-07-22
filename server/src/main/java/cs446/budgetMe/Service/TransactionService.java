package cs446.budgetMe.Service;

import cs446.budgetMe.Model.Transaction;
import cs446.budgetMe.Repository.TransactionRepository;
import cs446.budgetMe.Utils.BadResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transRepo;


	public Transaction getTransaction(String transId){
		return transRepo.findById(transId).orElse(null);
	}

	public String insertTransaction(Transaction trans){
		transRepo.save(trans);
		return trans.getTransId();
	}

	public void deleteTransaction(String transId){
		transRepo.deleteById(transId);
	}

	public Transaction validateTransaction(Transaction transaction, String transId) throws BadResponseException {
		Transaction dbTrans = transRepo.findById(transId).orElse(null);
		if (dbTrans == null){
			throw new BadResponseException(HttpStatus.NOT_FOUND, null);
		}

		if (transaction.isEqual(dbTrans)){
			return null;
		} else {
			return dbTrans;
		}

	}



}

