package cs446.budgetMe.Controller;


import cs446.budgetMe.BudgetMeApplicationController;
import cs446.budgetMe.Email.EmailService;
import cs446.budgetMe.Model.Group;
import cs446.budgetMe.Security.AuthenticationService;
import cs446.budgetMe.Utils.ResponseConstant;
import cs446.budgetMe.Model.User;
import cs446.budgetMe.Service.GroupService;
import cs446.budgetMe.Service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserAuthController extends BudgetMeApplicationController {
	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@RequestMapping(method=RequestMethod.POST, value="/user")
	public ResponseEntity register(@RequestBody JSONObject inputUser) {
		try {

			String userName = inputUser.get(ResponseConstant.USER_NAME).toString();
			String userEmail = inputUser.get(ResponseConstant.USER_EMAIL).toString();
			String userPassword = inputUser.get(ResponseConstant.USER_PASSWORD).toString();

			// Create a group when during registration
			String groupName = ResponseConstant.DEFAULT_GROUP_NAME;
			List<String> userList = new ArrayList<>();
			userList.add(userName);
			Group group = new Group(groupName, userList);
			groupService.createGroup(group);

			// Create an user when during registration
			AuthenticationService authService = new AuthenticationService();
			String authToken = authService.generateAuthToken(userEmail, userPassword);

			// Update group field
			String defaultGroupId = group.getGroupId();
			List<Group> groupList = new ArrayList();
			groupList.add(group);
			User user = new User(authToken, userName, userEmail, userPassword, defaultGroupId, groupList);
			String userId = userService.createUser(user);

			// Sending notification email for the user registered
			EmailService emailService = new EmailService();
			emailService.sendRegistrationEmail(userEmail);

			JSONObject respondData = new JSONObject();
			respondData.put(ResponseConstant.USER_AUTH_TOKEN, authToken);
			return new ResponseEntity(HttpStatus.ACCEPTED);

		} catch (Exception e){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/user/{userEmail}/{userPassword}")
	public ResponseEntity login(@PathVariable String userEmail,  @PathVariable String userPassword) {
		try {
			// Email and password verification
			User dbUser = userService.findByEmailAndPassword(userEmail, userPassword);
			if (dbUser != null) {
				// Generate authentication token
				String authToken = dbUser.getAuthToken();

				// Respond with user authentication token
				JSONObject respondData = new JSONObject();
				respondData.put(ResponseConstant.USER_AUTH_TOKEN, authToken);
				respondData.put(ResponseConstant.USER_NAME, dbUser.getUserName());
				respondData.put(ResponseConstant.DEFAULT_GROUP_ID, dbUser.getDefaultGroupId());
				respondData.put(ResponseConstant.GROUP_LIST, dbUser.getGroupList());
				return new ResponseEntity(respondData, HttpStatus.OK);

			} else {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

}
