
public class ArrayStack<T> {

    // Stores all the items in the stack.
    private Object[] values;
    private int size;

    // Creates an empty stack with space for 10 items.
    private ArrayStack() {
        values = new Object[10];
        size = 0;
    }

    // Creates and returns a new empty stack.
    public static <T> ArrayStack<T> emptyStack() {
        return new ArrayStack<>();
    }

    // Adds a value to the top of the stack.
    // If the array is full, we make a larger array first.
    public void push(T value) {
        if (size == values.length) {
            Object[] bigger = new Object[values.length * 2];

            for (int i = 0; i < values.length; i++) {
                bigger[i] = values[i];
            }

            values = bigger;
        }

        values[size] = value;
        size++;
    }

    // Removes and returns the top item.
    // Throws an exception if the stack is empty.
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        size--;
        T value = (T) values[size];
        values[size] = null;


        return value;
    }

    // Returns the top item without removing it.
    // Throws an exception if the stack is empty.
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        return (T) values[size - 1];
    }

    // Returns true if the stack has no items.
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the number of items in the stack.
    public int size() {
        return size;
    }
}
