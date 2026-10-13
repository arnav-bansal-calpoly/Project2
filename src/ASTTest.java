
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ASTTest {

    @Test
    public void testNumNodeEval() {
        AST n = new NumNode(7.5);

        assertEquals(7.5, n.eval());
    }

    @Test
    public void testAddEval() {
        AST n = new BinopNode("+", new NumNode(2), new NumNode(3));

        assertEquals(5, n.eval());
    }

    @Test
    public void testSubtractEval() {
        AST n = new BinopNode("-", new NumNode(10), new NumNode(4));

        assertEquals(6, n.eval());
    }

    @Test
    public void testMultiplyEval() {
        AST n = new BinopNode("*", new NumNode(6), new NumNode(7));

        assertEquals(42, n.eval());
    }

    @Test
    public void testDivideEval() {
        AST n = new BinopNode("/", new NumNode(6), new NumNode(4));

        assertEquals(1.5, n.eval());
    }

    @Test
    public void testPowerEval() {
        AST n = new BinopNode("^", new NumNode(2), new NumNode(10));

        assertEquals(1024, n.eval());
    }

    @Test
    public void testInvalidOperatorEval() {
        AST n = new BinopNode("%", new NumNode(2), new NumNode(3));

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> n.eval()
        );

        assertEquals("invalid operator", e.getMessage());
    }

    @Test
    public void testNestedEval() {
        // 2 * (3 + 4)
        AST sum = new BinopNode("+", new NumNode(3), new NumNode(4));
        AST n = new BinopNode("*", new NumNode(2), sum);

        assertEquals(14, n.eval());
    }

    @Test
    public void testParseInfixEval() {
        assertEquals(14, Parser.parseInfix("2 * ( 3 + 4 )").eval());
        assertEquals(128, Parser.parseInfix("2 * 4 ^ 3").eval());
        assertEquals(512, Parser.parseInfix("2 ^ 3 ^ 2").eval());
        assertEquals(1.5, Parser.parseInfix("2 * 3 / 4").eval());
        assertEquals(3, Parser.parseInfix("10 - 4 - 3").eval());
    }

    @Test
    public void testParsePostfixEval() {
        assertEquals(20, Parser.parsePostfix("2 3 + 4 *").eval());
        assertEquals(1, Parser.parsePostfix("5 2 - 3 /").eval());
    }
}
