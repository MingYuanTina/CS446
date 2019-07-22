package cs446.budgetMe.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="group")
public class Group {

	@Id
    private String groupId;
    private String groupName;
    private List<String> userList;

    public Group() {}

    @JsonCreator
    public Group(@JsonProperty("groupName") String groupName) {
        this.groupName = groupName;
    }


    @JsonCreator
    public Group(@JsonProperty("groupName") String groupName,
                 @JsonProperty("userList") List<String> userList) {
        this.groupName = groupName;
        this.userList = new ArrayList<>();
        this.userList.addAll(userList);
    }

    public void insertToUserList(String userName) { this.userList.add(userName); }

    public String getGroupId() { return this.groupId; }

    public String getGroupName() { return this.groupName; }

    public List<String> getUserList() { return this.userList; }
}
