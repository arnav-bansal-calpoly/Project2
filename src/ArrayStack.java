
public class ArrayStack<T> {

    // Stores all the items in the stack.
    private Object[] data;
    private int size;

    // Creates an empty stack with space for 10 items.
    private ArrayStack() {
        data = new Object[10];
        size = 0;
    }

    // Creates and returns a new empty stack.
    public static <T> ArrayStack<T> emptyStack() {
        return new ArrayStack<>();
    }

    // Adds a value to the top of the stack.
    // If the array is full, we make a larger array first.
    public void push(T value) {
        if (size == data.length) {
            Object[] newData = new Object[data.length * 2];

            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }

            data = newData;
        }

        data[size] = value;
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
        T result = (T) data[size];
        data[size] = null;


        return result;
    }

    // Returns the top item without removing it.
    // Throws an exception if the stack is empty.
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        return (T) data[size - 1];
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
