package cs446.budgetme;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

// This should maybe be updated to a singleton class with just a single map mapping Id to Name.
public class TransactionCategory implements Parcelable {
    private String mName;
    private Integer mId;

    private TransactionCategory(String name, int id) {
        mName = name;
        mId = id;
    }

    @Override
    public String toString() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public static ArrayList<TransactionCategory> getDefaults() {
        ArrayList<TransactionCategory> transactionCategories = new ArrayList<>();
        transactionCategories.add(new TransactionCategory("Groceries", 0));
        transactionCategories.add(new TransactionCategory("Entertainment", 1));
        transactionCategories.add(new TransactionCategory("Gas", 2));
        transactionCategories.add(new TransactionCategory("Self-Indulgence", 3));

        return transactionCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeInt(mId);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TransactionCategory> CREATOR = new Parcelable.Creator<TransactionCategory>() {
        public TransactionCategory createFromParcel(Parcel in) {
            return new TransactionCategory(in);
        }

        public TransactionCategory[] newArray(int size) {
            return new TransactionCategory[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private TransactionCategory(Parcel in) {
        mName = in.readString();
        mId = in.readInt();
    }
}
