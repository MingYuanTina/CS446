package cs446.budgetme;

public class TransactionCategory {
    private String name;

    public TransactionCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
