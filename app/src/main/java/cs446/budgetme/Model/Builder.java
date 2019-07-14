package cs446.budgetme.Model;

public interface Builder<E> {
    E build() throws IllegalStateException;
}
