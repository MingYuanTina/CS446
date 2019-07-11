package cs446.budgetme.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class SpendingsDataSummary implements Subject {

    ArrayList<Observer> observerList;

    List<Transaction> transactions;
    Date fromDate;
    HashSet<TransactionCategory> excludeList;

    List<Goal> goals; // this should not be a list, maybe a mapping from category to cost, or literally its own class

    public SpendingsDataSummary(List<Transaction> transactions) {
        observerList = new ArrayList<>();
        excludeList = new HashSet<>();
        this.transactions = transactions;
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
        transactions.add(t);
        notifyObservers();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
        notifyObservers();
    }
}
