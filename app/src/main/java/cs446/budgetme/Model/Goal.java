package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs446.budgetme.R;

public class Goal implements Parcelable {
    //need id for Goal for deletion
    @SerializedName("goalId")
    private String mId;
    @SerializedName("targetAmount")
    private Double mLimit;
    @SerializedName("startDate")
    private Date mStartDate;
    @SerializedName("endDate")
    private Date mEndDate;
    @SerializedName("categoryList")
    private List<TransactionCategory> mCategories;
    @SerializedName("description")
    private String mNote;

    private Goal(GoalBuilder builder) {
        this.mLimit = builder.mLimit;
        this.mStartDate = builder.mStartDate;
        this.mEndDate = builder.mEndDate;
        this.mCategories = builder.mCategories;
        this.mNote = builder.mNote;
        this.mId = builder.mId;
    }

    public static class GoalBuilder implements Builder<Goal> {
        private Double mLimit;
        private Date mStartDate;
        private Date mEndDate;
        private List<TransactionCategory> mCategories;
        private String mNote;
        private String mId;

        public GoalBuilder(Double limit, Date startDate, Date endDate) {
            mLimit = limit;
            mStartDate = startDate;
            mEndDate = endDate;
        }

        public GoalBuilder setNote(String note) {
            mNote = note;
            return this;
        }

        public GoalBuilder setCategories(List<TransactionCategory> categories) {
            mCategories = categories;
            return this;
        }

        public GoalBuilder setId(String id) {
            mId = id;
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

    public String getId() {
        return mId;
    }

    public Double getLimit() {
        return mLimit;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public List<TransactionCategory> getCategories() {
        return mCategories;
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

    public String getTitleString() {
        if (mNote != null && mNote.length() > 0) {
            return mNote;
        } else {
            String categoriesString;
            if (mCategories.isEmpty()) {
                categoriesString = "All Categories";
            } else {
                List<String> text = new ArrayList<>();
                for (int i = 0; i < mCategories.size(); i++) {
                    text.add(mCategories.get(i).toString());
                }
                categoriesString = TextUtils.join(", ", text);
            }
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
            return categoriesString + ", " + mLimit + ", " + outputFormatter.format(mStartDate) + " to " + outputFormatter.format(mEndDate);
        }
    }
}
