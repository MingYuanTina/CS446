package cs446.budgetme.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import cs446.budgetme.Utils.DateUtils;

public class SpendingsDataSummary implements Subject {

    ArrayList<Observer> observerList;

    List<Transaction> mTransactions;
    List<TransactionCategory> mTransactionCategories;
    Date mStartDate; // Set to null when no date range specified
    Date mEndDate;
    HashSet<String> mChosenCategoriesIds;

    List<Goal> mGoals;

    public SpendingsDataSummary() {
        observerList = new ArrayList<>();
        mTransactionCategories = new ArrayList<>();
        mChosenCategoriesIds = new HashSet<>();
        mTransactions = new ArrayList<>();
        mGoals = new ArrayList<>();
    }

    public SpendingsDataSummary(List<Transaction> transactions) {
        this();
        mTransactions = transactions;
    }

    public SpendingsDataSummary(List<Transaction> transactions, List<Goal> goals) {
        this();
        mTransactions = transactions;
        mGoals = goals;
    }

    @Override
    public void register(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update();
        }
    }

    public void addTransaction(Transaction t) {
        mTransactions.add(t);
        notifyObservers();
    }

    public void setCategoryFilters(List<TransactionCategory> categories) {
        mChosenCategoriesIds.clear();
        for (TransactionCategory category : categories) {
            mChosenCategoriesIds.add(category.getId());
        }
        notifyObservers();
    }

    public void setDateFiltersFromDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            mStartDate = null;
            mEndDate = null;
        } else {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);
            DateUtils.setCalendarToBeginningOfDay(startCalendar);
            mStartDate = startCalendar.getTime();
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);
            DateUtils.setCalendarToBeginningOfDay(endCalendar);
            mEndDate = endCalendar.getTime();
        }

    }

    public void setDateFilters(Calendar startDate, Calendar endDate) {
        if (startDate == null || endDate == null) {
            mStartDate = null;
            mEndDate = null;
        } else {
            endDate.add(Calendar.DATE, 1);
            endDate.add(Calendar.MILLISECOND, -1);
            mStartDate = startDate.getTime();
            mEndDate = endDate.getTime();
        }
        notifyObservers();
    }

    public List<Transaction> getFilteredTransactions() {
        List<Transaction> filteredTransactions = new ArrayList<>(mTransactions);
        filteredTransactions = filterTransactionsByCategory(filteredTransactions);
        filteredTransactions = filterTransactionsByDate(filteredTransactions);

        return filteredTransactions;
    }

    private List<Transaction> filterTransactionsByCategory(List<Transaction> transactions) {
        if (mChosenCategoriesIds.isEmpty()) {
            return transactions;
        }
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction t : transactions) {
            if (mChosenCategoriesIds.contains(t.getCategoryId())) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    private List<Transaction> filterTransactionsByDate(List<Transaction> transactions) {
        if (mStartDate == null || mEndDate == null) {
            return transactions;
        }

        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.getDate().getTime() >= mStartDate.getTime() && t.getDate().getTime() <= mEndDate.getTime()) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    public List<Transaction> getTransactions() {
        return mTransactions;
    }

    public List<Goal> getGoals() {
        return mGoals;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void addGoal(Goal goal) {
        mGoals.add(goal);
        notifyObservers();
    }

    public void setTransactions(List<Transaction> transactions) {
        mTransactions = transactions;
        //sort the list according to data
        Transaction.sortTransactionsByDate(transactions);
        notifyObservers();
    }

    public void setGoals(List<Goal> goals) {
        mGoals = goals;
        notifyObservers();
    }

    public void setTransactionCategories(List<TransactionCategory> transactionCategories) {
        mTransactionCategories = transactionCategories;
    }

    public List<TransactionCategory> getTransactionCategories() {
        return mTransactionCategories;
    }
}
