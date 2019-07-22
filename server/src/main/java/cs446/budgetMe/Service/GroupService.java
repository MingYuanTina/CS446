package cs446.budgetMe.Service;

import cs446.budgetMe.Model.Goal;
import cs446.budgetMe.Model.Group;
import cs446.budgetMe.Model.User;
import cs446.budgetMe.Repository.GroupRepository;
import cs446.budgetMe.Repository.UserRepository;
import cs446.budgetMe.Utils.BadResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepo;

	public String createGroup(Group group) {
		groupRepo.save(group);
		return group.getGroupId();
	}

	public Group findGroupByGroupName(String groupName) {
		return groupRepo.findByGroupName(groupName);
	}

	public Group deleteGroup(String groupId) throws BadResponseException {
		Group group = groupRepo.findById(groupId).orElseThrow(() -> new BadResponseException(HttpStatus.NOT_FOUND, null));
		groupRepo.deleteById(groupId);
		return group;
	}

	public void insertToUserList(String groupId, String userName) throws BadResponseException {
		Group group = groupRepo.findById(groupId).orElseThrow(() -> new BadResponseException(HttpStatus.NOT_FOUND, null));
		group.insertToUserList(userName);
		groupRepo.save(group);
	}

	public void validateGroup(String groupId) throws BadResponseException {
		groupRepo.findById(groupId).orElseThrow(() -> new BadResponseException(HttpStatus.NOT_FOUND, null));
	}

}

