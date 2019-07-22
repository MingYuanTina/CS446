package cs446.budgetMe.Controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import cs446.budgetMe.BudgetMeApplicationController;
import cs446.budgetMe.Service.GroupService;
import cs446.budgetMe.Utils.ResponseConstant;
import cs446.budgetMe.Model.Transaction;
import cs446.budgetMe.Repository.TransactionRepository;
import cs446.budgetMe.Service.TransactionService;
import cs446.budgetMe.Utils.BadResponseException;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController extends BudgetMeApplicationController {
	@Autowired
	private GroupService groupService;

	@Autowired
	private TransactionService transService;

	@Autowired
	private TransactionRepository transRepo;

	@RequestMapping(method=RequestMethod.POST, value="/pretrans/{userToken}/{groupId}/{transId}")
	public ResponseEntity checkTransactionModified(@RequestBody Transaction transaction,
												   @PathVariable String userToken,
												   @PathVariable String groupId,
												   @PathVariable String transId) {
		try {
			// Validate user identity
			authenticateUserToken(userToken);
			groupService.validateGroup(groupId);

			// Check if transaction the same
			Transaction trans = transService.validateTransaction(transaction, transId);

			if (trans == null) {
				JSONObject respondData = new JSONObject();
				respondData.put("message", "data has not been modified");
				return new ResponseEntity(respondData, HttpStatus.OK);
			}

			// Response
			return new ResponseEntity(trans, HttpStatus.OK);
		} catch (BadResponseException e){
			return new ResponseEntity(e.getStatus());
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="/trans/{userToken}/{groupId}")
	public ResponseEntity createTransaction(@RequestBody Transaction transaction,
											@PathVariable String userToken, @PathVariable String groupId){
		try {
			// Validate user identity
			authenticateUserToken(userToken);
			groupService.validateGroup(groupId);

			// Update to transaction table
			transaction.setGroupId(groupId);
			String transId = transService.insertTransaction(transaction);

			// Response
			JSONObject respondData = new JSONObject();
			respondData.put(ResponseConstant.TRANS_ID, transId);
			return new ResponseEntity(respondData, HttpStatus.OK);
		} catch (BadResponseException e){
			return new ResponseEntity(e.getStatus());
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/trans/{userToken}/{groupId}")
	public ResponseEntity getTransactions(@PathVariable String userToken, @PathVariable String groupId) {
		try {
			// Validate user identity
			authenticateUserToken(userToken);
			groupService.validateGroup(groupId);

			// Get all transaction from the group id provided
			List<Transaction> transList = transRepo.findByGroupId(groupId);

			// Response
			return new ResponseEntity(transList, HttpStatus.OK);
		} catch (BadResponseException e){
			return new ResponseEntity(e.getMessage(), e.getStatus());
		} catch (Exception e) {
			return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/trans/{userToken}/{groupId}/{transToken}")
	public ResponseEntity deleteTransaction(@PathVariable String userToken,
											@PathVariable String transToken,
											@PathVariable String groupId){
		try {
			// Validate user identity
			authenticateUserToken(userToken);
			groupService.validateGroup(groupId);

			transService.deleteTransaction(transToken);
			return new ResponseEntity(HttpStatus.OK);
		} catch (BadResponseException e){
			return new ResponseEntity(e.getStatus());
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

	}

}
