package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Goal implements Parcelable {
    private Date mStartDate;
    private Date mEndDate;
    private List<TransactionCategory> applicableCategories;
    private Double mTotalSpendings;
    private Double mCurrentSpendings;

    public Goal(Date startDate, Date endDate, List<TransactionCategory> categories, Double totalSpendings, Double currentSpendings) {
        mStartDate = startDate;
        mEndDate = endDate;
        applicableCategories = categories;
        mTotalSpendings = totalSpendings;
        mCurrentSpendings = currentSpendings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mStartDate);
        out.writeSerializable(mEndDate);
        out.writeTypedList(applicableCategories);
        out.writeDouble(mTotalSpendings);
        out.writeDouble(mCurrentSpendings);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Goal> CREATOR = new Parcelable.Creator<Goal>() {
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Goal(Parcel in) {
        mStartDate = (Date)in.readSerializable();
        mEndDate = (Date)in.readSerializable();
        applicableCategories = new ArrayList<>();
        in.readTypedList(applicableCategories, TransactionCategory.CREATOR);
        mTotalSpendings = in.readDouble();
        mCurrentSpendings = in.readDouble();
    }

    public boolean affectedBy(Transaction transaction) {
        if (applicableCategories.isEmpty()) return true;
        for (TransactionCategory category : applicableCategories) {
            if (transaction.getCategoryId() == category.getId()) {
                return true;
            }
        }
        return false;
    }

    public static List<Goal> getFakeData() {
        List<TransactionCategory> transactionCategories = TransactionCategory.getDefaults();
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i<3; i++) {
            List<TransactionCategory> thisList = new ArrayList<TransactionCategory>(transactionCategories);
            thisList.remove(i);
            goals.add(new Goal(new Date(119, 5, 8), new Date(119, 6, 8), thisList, 300.0, 0.0));
        }
        return goals;
    }
}
