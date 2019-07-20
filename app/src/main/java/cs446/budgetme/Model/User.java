package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User  implements Parcelable{

    @SerializedName("username")
    private String mName;

    @SerializedName("defaultGroupId")
    private String defaultGroupId; //current group that the user is in
    @SerializedName("groupList")
    private ArrayList<Group> groupList;

    @SerializedName("userAuthToken")
    private String userAuthToken;

    public User(String mName) {
        this.mName = mName;
        this.groupList= new ArrayList<Group>();
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Group> getGroupList(){
        return groupList;
    }

    public String getUserAuthToken(){return userAuthToken; }
    public void setGrouList(ArrayList<Group> groupList){
        this.groupList = groupList;
    }
    public String getDefaultGroupId(){
        return defaultGroupId;
    }
    public void setUserAuthToken(String userAuthToken){
        this.userAuthToken = userAuthToken;
    }

    public void setDefaultGroupId(String id){
        defaultGroupId= id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User(Parcel in) {
        mName = in.readString();
        defaultGroupId = in.readString();

        //TODO: need to change the groupList
        groupList= new ArrayList<Group>();
        in.readTypedList(groupList, Group.CREATOR);
       // groupList = in.readParcelable(Group.class.getClassLoader());
        userAuthToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeString(defaultGroupId);
        out.writeTypedList(groupList);
        out.writeString(userAuthToken);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };


}
