
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ASTTest {

    @Test
    public void testNumNodeEval() {
        AST node = new NumNode(7.5);

        assertEquals(7.5, node.eval());
    }

    @Test
    public void testAddEval() {
        AST node = new BinopNode("+", new NumNode(2), new NumNode(3));

        assertEquals(5, node.eval());
    }

    @Test
    public void testSubtractEval() {
        AST node = new BinopNode("-", new NumNode(10), new NumNode(4));

        assertEquals(6, node.eval());
    }

    @Test
    public void testMultiplyEval() {
        AST node = new BinopNode("*", new NumNode(6), new NumNode(7));

        assertEquals(42, node.eval());
    }

    @Test
    public void testDivideEval() {
        AST node = new BinopNode("/", new NumNode(6), new NumNode(4));

        assertEquals(1.5, node.eval());
    }

    @Test
    public void testPowerEval() {
        AST node = new BinopNode("^", new NumNode(2), new NumNode(10));

        assertEquals(1024, node.eval());
    }

    @Test
    public void testInvalidOperatorEval() {
        AST node = new BinopNode("%", new NumNode(2), new NumNode(3));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> node.eval()
        );

        assertEquals("invalid operator", error.getMessage());
    }

    @Test
    public void testNestedEval() {
        // 2 * (3 + 4)
        AST addition = new BinopNode("+", new NumNode(3), new NumNode(4));
        AST node = new BinopNode("*", new NumNode(2), addition);

        assertEquals(14, node.eval());
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
