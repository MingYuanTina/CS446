package cs446.budgetMe;

import cs446.budgetMe.Model.User;
import cs446.budgetMe.Service.UserService;
import cs446.budgetMe.Utils.BadResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BudgetMeApplicationController {
    @Autowired
    private UserService userService;

    public User authenticateUserToken(String userToken) throws BadResponseException {
        User user = userService.findByAuthToken(userToken);
        if (user == null) {
            throw new BadResponseException(HttpStatus.UNAUTHORIZED, "Invalid user token");
        }
        return user;
    }
}
