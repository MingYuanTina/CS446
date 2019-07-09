package cs446.budgetme.Model;

public interface Subject {
    public void register(Observer observer);
    public void unregister(Observer observer);

    public void notifyObservers();
}
