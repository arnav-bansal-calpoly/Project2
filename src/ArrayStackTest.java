
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArrayStackTest {

    @Test
    public void testEmptyStack() {
        ArrayStack<Integer> s = ArrayStack.emptyStack();

        assertTrue(s.isEmpty());
        assertEquals(0, s.size());
    }

    @Test
    public void testPush() {
        ArrayStack<Integer> s = ArrayStack.emptyStack();

        s.push(10);
        assertEquals(1, s.size());
        assertEquals(10, s.peek());

        s.push(20);
        assertEquals(2, s.size());
        assertEquals(20, s.peek());

        assertFalse(s.isEmpty());
    }

    @Test
    public void testPop() {
        ArrayStack<Integer> s = ArrayStack.emptyStack();

        s.push(10);
        s.push(20);
        s.push(30);

        assertEquals(30, s.pop());
        assertEquals(20, s.pop());
        assertEquals(10, s.pop());

        assertTrue(s.isEmpty());
        assertEquals(0, s.size());
    }

    @Test
    public void testPeek() {
        ArrayStack<String> s = ArrayStack.emptyStack();

        s.push("hello");
        s.push("world");

        assertEquals("world", s.peek());
        assertEquals("world", s.peek());
        assertEquals(2, s.size());
    }

    @Test
    public void testPopEmpty() {
        ArrayStack<Integer> s = ArrayStack.emptyStack();

        assertThrows(IndexOutOfBoundsException.class,
                () -> s.pop());
    }

    @Test
    public void testPeekEmpty() {
        ArrayStack<Integer> s = ArrayStack.emptyStack();

        assertThrows(IndexOutOfBoundsException.class,
                () -> s.peek());
    }

    @Test
    public void testResize() {
        ArrayStack<Integer> s = ArrayStack.emptyStack();

        for (int i = 0; i < 25; i++) {
            s.push(i);
        }

        assertEquals(25, s.size());

        for (int i = 24; i >= 0; i--) {
            assertEquals(i, s.pop());
        }

        assertTrue(s.isEmpty());
    }




}
