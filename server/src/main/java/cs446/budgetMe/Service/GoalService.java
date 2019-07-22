package cs446.budgetMe.Service;

import cs446.budgetMe.Model.Category;
import cs446.budgetMe.Model.Goal;
import cs446.budgetMe.Repository.CategoryRepository;
import cs446.budgetMe.Repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {

	@Autowired
	private GoalRepository goalRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	public String createGoal(Goal goal) {
		goalRepository.save(goal);
		return goal.getGoalId();
	}

	public List<Goal> findGoalListByGroupId(String groupId) {
		return goalRepository.findByGroupId(groupId);
	}

	public void deleteGoal(String goalId) {
		goalRepository.deleteById(goalId);
	}

	public void removeFromGoal(String groupId, String categoryId) {
		List<Goal> goals = goalRepository.findByGroupId(groupId);
		if (goals != null){
			goals.forEach(goal -> {
				goal.removeFromGoal(categoryId);
				goalRepository.save(goal);
			});
		}
	}


}

