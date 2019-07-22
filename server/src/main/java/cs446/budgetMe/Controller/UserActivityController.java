package cs446.budgetMe.Controller;


import cs446.budgetMe.BudgetMeApplicationController;
import cs446.budgetMe.Model.Category;
import cs446.budgetMe.Model.Goal;
import cs446.budgetMe.Model.User;
import cs446.budgetMe.Security.AuthenticationService;
import cs446.budgetMe.Service.CategoryService;
import cs446.budgetMe.Service.GoalService;
import cs446.budgetMe.Service.GroupService;
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
public class UserActivityController extends BudgetMeApplicationController {
    // ****************************************** Setup category service **************************************
    @Autowired
    private GroupService groupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoalService goalService;

    @RequestMapping(method=RequestMethod.POST, value="/category/{userToken}/{groupId}")
    public ResponseEntity createCategory(@RequestBody Category category,
                                         @PathVariable String userToken,
                                         @PathVariable String groupId) {
        try {
            // Validate user identity and group identity
            User user = authenticateUserToken(userToken);
            groupService.validateGroup(groupId);

            // Create a category and save it to DB
            category.setGroupId(groupId);
            String categoryId = categoryService.createCategory(category);

            // Respond with category token
            JSONObject respondData = new JSONObject();
            respondData.put(ResponseConstant.CATEGORY_ID, categoryId);
            return new ResponseEntity(respondData, HttpStatus.CREATED);
        } catch (BadResponseException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method=RequestMethod.GET, value="/category/{userToken}/{groupId}")
    public ResponseEntity getCategoryList(@PathVariable String userToken, @PathVariable String groupId) {
        try {
            // Validate user identity and group identity
            authenticateUserToken(userToken);
            groupService.validateGroup(groupId);

            List<Category> categoryList = categoryService.findCategoryList(groupId);

            // Respond with user authentication token
            List<JSONObject> respondList = new ArrayList<>();
            for (int i = 0; i < categoryList.size(); i++){
                JSONObject respondData = new JSONObject();
                respondData.put(ResponseConstant.CATEGORY_ID, categoryList.get(i).getCategoryId());
                respondData.put(ResponseConstant.CATEGORY_NAME, categoryList.get(i).getCategoryName());
                respondList.add(respondData);
            }

            // Response
            return new ResponseEntity(respondList, HttpStatus.CREATED);
        } catch (BadResponseException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/category/{userToken}/{groupId}/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable String userToken,
                                         @PathVariable String groupId,
                                         @PathVariable String categoryId) {
        try {
            // Validate user identity and group identity
            authenticateUserToken(userToken);
            groupService.validateGroup(groupId);

            // Update category list in the goal table
            goalService.removeFromGoal(groupId, categoryId);

            // Respond with user authentication token
            categoryService.deleteCategory(categoryId);

            // Response
            return new ResponseEntity(null, HttpStatus.OK);
        } catch (BadResponseException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ****************************************** Setup goal service **************************************
    @RequestMapping(method=RequestMethod.POST, value="/goal/{userToken}/{groupId}")
    public ResponseEntity createGoal(@RequestBody Goal goal, @PathVariable String userToken, @PathVariable String groupId) {
        try {
            // Validate user identity and group identity
            authenticateUserToken(userToken);
            groupService.validateGroup(groupId);

            // Update goal table
            goal.setGroupId(groupId);
            String goalId = goalService.createGoal(goal);

            // Respond with user authentication token
            JSONObject respondData = new JSONObject();
            respondData.put(ResponseConstant.GOAL_ID, goalId);
            return new ResponseEntity(respondData, HttpStatus.CREATED);
        } catch (BadResponseException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method=RequestMethod.GET, value="/goal/{userToken}/{groupId}")
    public ResponseEntity getGoals(@PathVariable String userToken, @PathVariable String groupId) {
        try {
            // Validate user identity
            authenticateUserToken(userToken);
            groupService.validateGroup(groupId);

            // Update goal table
            List<Goal> goalList = goalService.findGoalListByGroupId(groupId);

            // Respond with user authentication token
            return new ResponseEntity(goalList, HttpStatus.CREATED);
        } catch (BadResponseException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/goal/{userToken}/{groupId}/{goalId}")
    public ResponseEntity deleteGoal(@PathVariable String userToken,
                                     @PathVariable String groupId,
                                     @PathVariable String goalId) {
        try {
            // Validate user identity
            authenticateUserToken(userToken);
            groupService.validateGroup(groupId);

            // Update goal table
            goalService.deleteGoal(goalId);

            // Respond with user authentication token
            return new ResponseEntity(null, HttpStatus.OK);
        } catch (BadResponseException e) {
            return new ResponseEntity(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

    }

}
