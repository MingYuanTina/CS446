package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Goal implements Parcelable {
    private Double mLimit;
    private Date mStartDate;
    private Date mEndDate;
    private List<TransactionCategory> mCategories;
    private String mNote;

    private Goal(GoalBuilder builder) {
        this.mLimit = builder.mLimit;
        this.mStartDate = builder.mStartDate;
        this.mEndDate = builder.mEndDate;
        this.mCategories = builder.mCategories;
        this.mNote = builder.mNote;
    }

    public static class GoalBuilder implements Builder<Goal> {
        private Double mLimit;
        private Date mStartDate;
        private Date mEndDate;
        private List<TransactionCategory> mCategories;
        private String mNote;

        public GoalBuilder(Double limit, Date startDate, Date endDate) {
            mLimit = limit;
            mStartDate = startDate;
            mEndDate = endDate;
        }

        // TODO Perhaps can make mCategories optional.

        public GoalBuilder setNote(String note) {
            mNote = note;
            return this;
        }

        public GoalBuilder setCategories(List<TransactionCategory> categories) {
            mCategories = categories;
            return this;
        }

        @Override
        public Goal build() throws IllegalStateException {
            if (mLimit < 0) {
                throw new IllegalStateException("Invalid limit");
            } else if (mStartDate == null) {
                throw new IllegalStateException("Invalid start date");
            } else if (mEndDate == null) {
                throw new IllegalStateException("Invalid end date");
            }
            return new Goal(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(mLimit);
        out.writeSerializable(mStartDate);
        out.writeSerializable(mEndDate);
        out.writeTypedList(mCategories);
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
        mLimit = in.readDouble();
        mStartDate = (Date)in.readSerializable();
        mEndDate = (Date)in.readSerializable();
        mCategories = new ArrayList<>();
        in.readTypedList(mCategories, TransactionCategory.CREATOR);
    }

    public boolean affectedBy(Transaction transaction) {
        if (mCategories.isEmpty()) return true;
        for (TransactionCategory category : mCategories) {
            if (transaction.getCategoryId() == category.getId()) {
                return true;
            }
        }
        return false;
    }

    public static List<Goal> getFakeData() {
        List<TransactionCategory> transactionCategories = TransactionCategory.getDefaults();
        List<Goal> goals = new ArrayList<>();
        try {
            for (int i = 0; i<3; i++) {
                List<TransactionCategory> thisList = new ArrayList<TransactionCategory>(transactionCategories);
                thisList.remove(i);
                goals.add(new GoalBuilder(300.0, new Date(119, 5, 8), new Date(119, 6, 8)).setCategories(thisList).build());
            }
        } catch (IllegalStateException e) {
            // Handle illegal state.
        }

        return goals;
    }
}
