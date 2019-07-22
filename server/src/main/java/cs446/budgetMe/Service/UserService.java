package cs446.budgetMe.Service;

import cs446.budgetMe.Model.Group;
import cs446.budgetMe.Model.User;
import cs446.budgetMe.Repository.UserRepository;
import cs446.budgetMe.Utils.BadResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public User getUser(String userId) { return userRepository.findById(userId).orElse(null);  }

	public User findByAuthToken(String authToken) {
		return userRepository.findByAuthToken(authToken);
	}

	public String createUser(User user) {
		userRepository.save(user);
		return user.getUserId();
	}

	public void insertToGroupList(String userName, Group group) {
		User user = userRepository.findByUserName(userName);
		user.insertToGroupList(group);
		userRepository.save(user);
	}

	public void removeFromGroupList(String userName, Group group) throws BadResponseException {
		User user = userRepository.findByUserName(userName);
		if (user == null) {
			throw new BadResponseException(HttpStatus.NOT_FOUND, null);
		}
		user.removeFromGroupList(group);
		userRepository.save(user);
	}

	public User findByEmailAndPassword(String email, String password){
		return userRepository.findByEmailAndPassword(email, password);
	}

}

