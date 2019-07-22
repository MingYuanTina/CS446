package cs446.budgetMe.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minidev.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Document(collection="user")
public class User {
	@Id
    private String userId;

	private String authToken;
	private String userName;
    private String email;
    private String password;
    private String defaultGroupId;
    private List<Group> groupList;

    public User() { }

    public User(String authToken, String userName, String email, String password, String defaultGroupId,
                List<Group> groupList) {
        this.authToken = authToken;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.defaultGroupId = defaultGroupId;
        this.groupList = new ArrayList<>();
        this.groupList.addAll(groupList);
    }

    public void insertToGroupList(Group group) {
        this.groupList.add(group);
    }

    public void removeFromGroupList(Group inputGroup) {
        String groupId = inputGroup.getGroupId();
        for (int i = 0; i < this.groupList.size(); i++) {
            if (this.groupList.get(i).getGroupId().equals(groupId)){
                this.groupList.remove(i);
            }
        }
    }

    public String getUserId(){ return this.userId; }

    public String getAuthToken() { return this.authToken; }

    public String getUserName(){ return this.userName; }

    public String getEmail(){ return this.email; }

    public String getPassword(){ return this.password; }

    public String getDefaultGroupId() { return this.defaultGroupId; }

    public List<Group> getGroupList() { return this.groupList; }
}
