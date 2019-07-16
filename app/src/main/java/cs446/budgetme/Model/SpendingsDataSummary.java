package cs446.budgetme.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class SpendingsDataSummary implements Subject {

    ArrayList<Observer> observerList;

    List<Transaction> mTransactions;
    Date fromDate;
    HashSet<Integer> mChosenCategoriesIds;

    List<Goal> goals; // this should not be a list, maybe a mapping from category to cost, or literally its own class

    public SpendingsDataSummary(List<Transaction> transactions) {
        observerList = new ArrayList<>();
        mChosenCategoriesIds = new HashSet<>();
        this.mTransactions = transactions;
        goals = Goal.getFakeData();

        //mGoal = Transaction.getFakeData();
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

    public List<Transaction> getFilteredTransactions() {
        if (mChosenCategoriesIds.isEmpty()) {
            return mTransactions;
        }
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction t : mTransactions) {
            if (mChosenCategoriesIds.contains(t.getCategoryId())) {
                filteredTransactions.add(t);
            }
        }
        return filteredTransactions;
    }

    private List<Transaction> getTransactions() {
        return mTransactions;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        notifyObservers();
    }
}
