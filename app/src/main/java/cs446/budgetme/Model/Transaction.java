package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import cs446.budgetme.Utils.DateUtils;

public class Transaction implements Parcelable {
    @SerializedName("cost")
    private Double mCost;

    @SerializedName("date")
    private Date mDate;

    @SerializedName("category")
    private TransactionCategory mCategory;

    @SerializedName("description")
    private String mNote;

    @SerializedName("transId")
    private String mId;

    private Transaction(TransactionBuilder builder) {
        this.mCost = builder.mCost;
        this.mDate = builder.mDate;
        this.mCategory = builder.mCategory;
        this.mNote = builder.mNote;
        this.mId = builder.mId;
    }

    public static class TransactionBuilder implements Builder<Transaction>{
        private Date mDate;
        private Double mCost;
        private String mNote;
        private TransactionCategory mCategory;
        private String mId;

        public TransactionBuilder(Double cost, Date date, TransactionCategory category) {
            mCost = cost;
            mDate = date;
            mCategory = category;
        }

        public TransactionBuilder setNote(String note) {
            mNote = note;
            return this;
        }

        public TransactionBuilder setId(String id) {
            mId = id;
            return this;
        }
        @Override
        public Transaction build() throws IllegalStateException {
            if (mCost == null || mCost < 0) {
                throw new IllegalStateException("Invalid Cost");
            } else if (mDate == null) {
                throw new IllegalStateException("Invalid Date");
            } else if (mCategory == null) {
                throw new IllegalStateException("Invalid Category");
            }
            return new Transaction(this);
        }
    }

    public String getCategoryName() {
        return mCategory.toString();
    }

    public String getCategoryId() {
        return mCategory.getId();
    }

    public Double getCost() {
        return mCost;
    }

    public Date getDate() {
        return mDate;
    }
    public String getId() {
        return mId;
    }

    public String getNote() {
        return mNote;
    }

    public String getStringDate(){
        DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateWithoutTime = outputFormatter.format(mDate);
        return dateWithoutTime;
    }

    public void setId(String id) {
        mId = id;
    }

    /*public static List<Transaction> getFakeData() {
        List<TransactionCategory> transactionCategories = TransactionCategory.getDefaults();
        List<Transaction> transactions = new ArrayList<>();
        try {
            // Year 119 = 2019
            Transaction t1 = new TransactionBuilder(23.01, new Date(119, 5, 18), transactionCategories.get(0)).build();
            Transaction t2 = new TransactionBuilder(1.32, new Date(119, 5, 17), transactionCategories.get(1)).setNote("HI").build();
            Transaction t3 = new TransactionBuilder(4.68, new Date(119, 5, 14), transactionCategories.get(2)).build();
            Transaction t4 = new TransactionBuilder(5.79, new Date(119, 5, 13), transactionCategories.get(3)).build();
            Transaction t5 = new TransactionBuilder(8.00, new Date(119, 5, 12), transactionCategories.get(2)).build();

            transactions.add(t1);
            transactions.add(t2);
            transactions.add(t3);
            transactions.add(t4);
            transactions.add(t5);
        } catch (IllegalStateException e) {
            // Handle illegal state.
        }
        return transactions;
    }*/

    public static TreeMap<Date, List<Transaction>> getTransactionsGroupedByDay(List<Transaction> transactions) {
        TreeMap<Date, List<Transaction>> map = new TreeMap<>();
        for (Transaction t : transactions) {
            Date currentDate = t.getRoundedDateOfTransaction();
            if (map.get(currentDate) == null) {
                map.put(currentDate, new ArrayList<Transaction>());
            }
            map.get(currentDate).add(t);
        }
        return map;
    }

    private Date getRoundedDateOfTransaction() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        DateUtils.setCalendarToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static void sortTransactionsByDate(List<Transaction> transactions) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t1.getDate().compareTo(t2.getDate());
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(mCost);
        out.writeSerializable(mDate);
        out.writeParcelable(mCategory, flags);
        out.writeString(mNote);
        out.writeString(mId);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Transaction(Parcel in) {
        mCost = in.readDouble();
        mDate = (Date)in.readSerializable();
        mCategory = in.readParcelable(TransactionCategory.class.getClassLoader());
        mNote = in.readString();
        mId=in.readString();
    }
}
