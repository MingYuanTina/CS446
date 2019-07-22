package cs446.budgetMe.Controller;


import cs446.budgetMe.BudgetMeApplicationController;
import cs446.budgetMe.Model.Group;
import cs446.budgetMe.Model.User;
import cs446.budgetMe.Service.GroupService;
import cs446.budgetMe.Service.UserService;
import cs446.budgetMe.Utils.BadResponseException;
import cs446.budgetMe.Utils.ResponseConstant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GroupActivityController extends BudgetMeApplicationController {
	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	@RequestMapping(method=RequestMethod.POST, value="/group/{userToken}")
	public ResponseEntity createGroup(@RequestBody JSONObject inputGroup, @PathVariable String userToken) {
		try {
			// Validate user identity
			User user = authenticateUserToken(userToken);

			// When user create a new group, it sends a group name along with a list of users
			// Otherwise, it's the case of user joining a group, which sends the group name only
			String groupName = inputGroup.get(ResponseConstant.GROUP_NAME).toString();
			if (inputGroup.size() == 2){
				// Create a group and save to DB
				List<String> userList = (ArrayList) inputGroup.get(ResponseConstant.USER_LIST);
				Group group = new Group(groupName, userList);
				String groupId = groupService.createGroup(group);

				// Update group list in each user's table
				userList.forEach(userName -> { userService.insertToGroupList(userName, group); });

				// Respond with group id
				JSONObject respondData = new JSONObject();
				respondData.put(ResponseConstant.GROUP_ID, groupId);
				return new ResponseEntity(respondData, HttpStatus.CREATED);

			} else {

				// Update group list in user table
				Group group = groupService.findGroupByGroupName(groupName);
				userService.insertToGroupList(user.getUserName(), group);

				// Update the user list in group table
				groupService.insertToUserList(group.getGroupId(), user.getUserName());

				// Response
				return new ResponseEntity(HttpStatus.OK);

			}

		} catch (BadResponseException e) {
			return new ResponseEntity(e.getStatus());
		} catch (Exception e){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/group/{userToken}")
	public ResponseEntity getGroupList(@PathVariable String userToken) {
		try {
			// Validate user identity
			User user = authenticateUserToken(userToken);

			List<Group> groupList = user.getGroupList();

			// Response
			return new ResponseEntity(groupList, HttpStatus.OK);
		} catch (BadResponseException e) {
			return new ResponseEntity(e.getStatus());
		} catch (Exception e){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(method=RequestMethod.DELETE, value="/group/{userToken}/{groupId}")
	public ResponseEntity deleteGroup(@PathVariable String userToken, @PathVariable String groupId) {
		try {
			// Validate user identity
			authenticateUserToken(userToken);

			// Create a group and save to DB
			Group group = groupService.deleteGroup(groupId);

			// Update group list in each user's table
			List<String> userList = group.getUserList();
			userList.forEach(userName -> {
				try {
					userService.removeFromGroupList(userName, group);
				} catch (Throwable e) {
				}
			} );

			// Response
			return new ResponseEntity(HttpStatus.OK);
		} catch (BadResponseException e) {
			return new ResponseEntity(e.getStatus());
		} catch (Exception e){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}


}
