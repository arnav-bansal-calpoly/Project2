
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ParserTest {

    @Test
    public void testSingleNumber() {
        AST result = Parser.parsePostfix("5");

        assertEquals(new NumNode(5), result);
    }

    @Test
    public void testAddition() {
        AST result = Parser.parsePostfix("2 3 +");

        AST expected = new BinopNode(
                "+",
                new NumNode(2),
                new NumNode(3)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testMultiplication() {
        AST result = Parser.parsePostfix("2 3 *");

        AST expected = new BinopNode(
                "*",
                new NumNode(2),
                new NumNode(3)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testSubtraction() {
        AST result = Parser.parsePostfix("10 3 -");

        AST expected = new BinopNode(
                "-",
                new NumNode(10),
                new NumNode(3)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testDivision() {
        AST result = Parser.parsePostfix("12 4 /");

        AST expected = new BinopNode(
                "/",
                new NumNode(12),
                new NumNode(4)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testExponent() {
        AST result = Parser.parsePostfix("2 3 ^");

        AST expected = new BinopNode(
                "^",
                new NumNode(2),
                new NumNode(3)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testMultipleOperations() {
        AST result = Parser.parsePostfix("2 3 + 4 *");

        AST addition = new BinopNode(
                "+",
                new NumNode(2),
                new NumNode(3)
        );

        AST expected = new BinopNode(
                "*",
                addition,
                new NumNode(4)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testNegativeAndDecimal() {
        AST result = Parser.parsePostfix("-2.5 3.5 +");

        AST expected = new BinopNode(
                "+",
                new NumNode(-2.5),
                new NumNode(3.5)
        );

        assertEquals(expected, result);
    }


    @Test
    public void testEmptyInput() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("")
        );

        assertEquals("empty input", error.getMessage());
    }



    @Test
    public void testNoOperators() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("2 3")
        );

        assertEquals("too many operands", error.getMessage());
    }

    @Test
    public void testIsOperator() {
        assertTrue(Parser.isOperator("+"));
        assertTrue(Parser.isOperator("-"));
        assertTrue(Parser.isOperator("*"));
        assertTrue(Parser.isOperator("/"));
        assertTrue(Parser.isOperator("^"));

        assertFalse(Parser.isOperator("a"));
        assertFalse(Parser.isOperator("2"));
        assertFalse(Parser.isOperator("("));
    }

    @Test
    public void testApplyOperator() {
        ArrayStack<AST> stack = ArrayStack.emptyStack();

        stack.push(new NumNode(10));
        stack.push(new NumNode(5));

        Parser.applyOperator(stack, "-");

        AST expected = new BinopNode(
                "-",
                new NumNode(10),
                new NumNode(5)
        );

        assertEquals(1, stack.size());
        assertEquals(expected, stack.pop());
    }


}
