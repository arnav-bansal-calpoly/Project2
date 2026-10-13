
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayStackTest {

    @Test
    public void testEmptyStack() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    public void testPush() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();

        stack.push(10);
        assertEquals(1, stack.size());
        assertEquals(10, stack.peek());

        stack.push(20);
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());

        assertFalse(stack.isEmpty());
    }

    @Test
    public void testPop() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    public void testPeek() {
        ArrayStack<String> stack = ArrayStack.emptyStack();

        stack.push("hello");
        stack.push("world");

        assertEquals("world", stack.peek());
        assertEquals("world", stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    public void testPopEmpty() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();

        assertThrows(IndexOutOfBoundsException.class,
                () -> stack.pop());
    }

    @Test
    public void testPeekEmpty() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();

        assertThrows(IndexOutOfBoundsException.class,
                () -> stack.peek());
    }

    @Test
    public void testResize() {
        ArrayStack<Integer> stack = ArrayStack.emptyStack();

        for (int i = 0; i < 25; i++) {
            stack.push(i);
        }

        assertEquals(25, stack.size());

        for (int i = 24; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }

        assertTrue(stack.isEmpty());
    }




}
