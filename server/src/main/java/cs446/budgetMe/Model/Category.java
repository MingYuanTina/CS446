package cs446.budgetMe.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection="category")
public class Category {

	@Id
    private String categoryId;
    private String categoryName;
    private String groupId;

    public Category() {}

    @JsonCreator
    public Category(@JsonProperty("categoryName") String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryName(){ return this.categoryName; }

    public String getCategoryId(){ return this.categoryId; }

    public String getGroupId(){ return this.groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

}
