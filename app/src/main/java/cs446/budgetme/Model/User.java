package cs446.budgetme.Model;

import java.util.ArrayList;

public class User {

    private String mName;
    private Double totalCost;
    private ArrayList<String> groupList;

    public User(String mName, Double totalCost) {
        this.mName = mName;
        this.totalCost = totalCost;
        this.groupList= new ArrayList<String>();
    }

    public String getName() {
        return mName;
    }

    public String getTotalCost() {
        return totalCost.toString();
    }

    public ArrayList<String> getGroupList(){
        return groupList;
    }

    public void setGrouList(){
        groupList.add("MYGROUP1");
        groupList.add("MYGROUP2");
    }
}
