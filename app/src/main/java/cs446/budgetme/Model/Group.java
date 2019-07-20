package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Group implements Parcelable {
    private String groupId;
    private String groupName;
    private ArrayList<String> userList;

    @Override
    public int describeContents() {
        return 0;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName(){
        return groupName;
    }

    public Group(String groupId, String groupName, ArrayList<String> userList){
        this.groupId= groupId;
        this.groupName= groupName;
        this.userList=userList;
    }

    private Group(Parcel in) {
        groupId = in.readString();
        groupName = in.readString();
        //TODO: need to change the groupList
        userList = new ArrayList<>();
        in.readStringList(userList);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(groupId);
        out.writeString(groupName);
        out.writeStringList(userList);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
