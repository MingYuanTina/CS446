package cs446.budgetme.Model;

import java.util.ArrayList;

public class User {

    private String mName;
    private String defaultGroupId; //current group that the user is in
    private ArrayList<Group> groupList;

    //need to save on local
    private String userAuthToken;

    public User(String mName, Double totalCost) {
        this.mName = mName;
    //    this.totalCost = totalCost;
        this.groupList= new ArrayList<Group>();
    }

    public String getName() {
        return mName;
    }
//    public String getTotalCost() {
//        return totalCost.toString();
//    }

    public ArrayList<Group> getGroupList(){
        return groupList;
    }

    public void setGrouList(){
//        groupList.add("MYGROUP1");
//        groupList.add("MYGROUP2");
    }
}
