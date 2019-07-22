package cs446.budgetMe.Service;

import cs446.budgetMe.Model.Category;
import cs446.budgetMe.Model.Group;
import cs446.budgetMe.Repository.CategoryRepository;
import cs446.budgetMe.Repository.GroupRepository;
import cs446.budgetMe.Utils.BadResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepo;

	public String createCategory(Category category) {
		categoryRepo.save(category);
		return category.getCategoryId();
	}

	public List<Category> findCategoryList(String groupId) {
		return categoryRepo.findByGroupId(groupId);
	}

	public void deleteCategory(String categoryId) {
		categoryRepo.deleteById(categoryId);
	}

	public Category getCategoryById(String categoryId) throws BadResponseException {
		return categoryRepo.findById(categoryId).orElseThrow(() -> new BadResponseException(HttpStatus.NOT_FOUND, ""));
	}

}

