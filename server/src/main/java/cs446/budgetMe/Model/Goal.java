package cs446.budgetMe.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection="goal")
public class Goal {

	@Id
    private String goalId;
    private Double targetAmount;
    private Date startDate;
    private Date endDate;
    private List<Category> categoryList;
    private String groupId;
    private String description;

    public Goal() {}

    @JsonCreator
    public Goal(@JsonProperty("startDate") String startDate, @JsonProperty("endDate") String endDate,
                @JsonProperty("targetAmount") Double targetAmount, @JsonProperty("description") String description,
                @JsonProperty("categoryList") List<Category> categoryList) throws Exception {
        SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy");
        this.startDate = dateFormat.parse(startDate);
        this.endDate = dateFormat.parse(endDate);
        this.targetAmount = targetAmount;
        this.description = description;
        this.categoryList = new ArrayList<>();
        this.categoryList.addAll(categoryList);
    }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getGoalId() { return this.goalId; }

    public String getGroupId() { return this.groupId; }

    public Date getStartDate() { return this.startDate; }

    public Date getEndDate() { return this.endDate; }

    public Double getTargetAmount() { return this.targetAmount; }

    public String getDescription() { return this.description; }

    public List<Category> getCategoryList() { return this.categoryList; }

    public void removeFromGoal(String categoryId) {
        for (int i = 0; i < this.categoryList.size(); i++) {
            if (this.categoryList.get(i).getCategoryId().equals(categoryId)) {
                this.categoryList.remove(i);
            }
        }
    }

}
