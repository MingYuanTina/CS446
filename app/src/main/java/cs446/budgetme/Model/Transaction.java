package cs446.budgetme.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction implements Parcelable {
    private Date mDate;
    private Double mCost;
    private String mNote;
    private TransactionCategory mCategory;

    public Transaction(Date date, Double cost, String note, TransactionCategory category) {
        mDate = date;
        mCost = cost;
        mNote = note;
        mCategory = category;
    }

    public String getCategoryName() {
        return mCategory.toString();
    }

    public int getCategoryId() {
        return mCategory.getId();
    }

    public Double getCost() {
        return mCost;
    }

    public String getDate(){
        DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateWithoutTime = outputFormatter.format(mDate);
        return dateWithoutTime;
    }

    public static List<Transaction> getFakeData() {
        List<TransactionCategory> transactionCategories = TransactionCategory.getDefaults();
        Transaction t1 = new Transaction(new Date(2019, 5, 18), 23.01, "", transactionCategories.get(0));
        Transaction t2 = new Transaction(new Date(2019, 5, 17), 1.32, "", transactionCategories.get(1));
        Transaction t3 = new Transaction(new Date(2019, 5, 14), 4.68, "", transactionCategories.get(2));
        Transaction t4 = new Transaction(new Date(2019, 5, 13), 5.79, "", transactionCategories.get(3));
        Transaction t5 = new Transaction(new Date(2019, 5, 12), 8.00, "", transactionCategories.get(2));
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        transactions.add(t5);
        return transactions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mDate);
        out.writeDouble(mCost);
        out.writeParcelable(mCategory, flags);
        out.writeString(mNote);
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
        mDate = (Date)in.readSerializable();
        mCost = in.readDouble();
        mCategory = in.readParcelable(TransactionCategory.class.getClassLoader());
        mNote = in.readString();
    }
}
