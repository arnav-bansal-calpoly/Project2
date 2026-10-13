
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

        AST sum = new BinopNode(
                "+",
                new NumNode(2),
                new NumNode(3)
        );

        AST expected = new BinopNode(
                "*",
                sum,
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
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("")
        );

        assertEquals("empty input", e.getMessage());
    }



    @Test
    public void testNoOperators() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("2 3")
        );

        assertEquals("too many operands", e.getMessage());
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
        ArrayStack<AST> s = ArrayStack.emptyStack();

        s.push(new NumNode(10));
        s.push(new NumNode(5));

        Parser.applyOperator(s, "-");

        AST expected = new BinopNode(
                "-",
                new NumNode(10),
                new NumNode(5)
        );

        assertEquals(1, s.size());
        assertEquals(expected, s.pop());
    }

    @Test
    public void testPostfixInvalidToken() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("2 a +")
        );

        assertEquals("invalid token", e.getMessage());
    }

    @Test
    public void testPostfixInsufficientOperands() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("2 +")
        );

        assertEquals("insufficient operands", e.getMessage());
    }

    @Test
    public void testPostfixTooManyOperands() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parsePostfix("2 3 4 +")
        );

        assertEquals("too many operands", e.getMessage());
    }

    @Test
    public void testIsNumber() {
        assertTrue(Parser.isNumber("5"));
        assertTrue(Parser.isNumber("-2"));
        assertTrue(Parser.isNumber("3.75"));
        assertTrue(Parser.isNumber(".5"));

        assertFalse(Parser.isNumber("-"));
        assertFalse(Parser.isNumber("a"));
        assertFalse(Parser.isNumber("2a"));
        assertFalse(Parser.isNumber("1.2.3"));
        assertFalse(Parser.isNumber("."));
    }

    @Test
    public void testPrecedence() {
        assertEquals(3, Parser.precedence("^"));
        assertEquals(2, Parser.precedence("*"));
        assertEquals(2, Parser.precedence("/"));
        assertEquals(1, Parser.precedence("+"));
        assertEquals(1, Parser.precedence("-"));
    }

    @Test
    public void testShouldApplyFirst() {
        assertFalse(Parser.shouldApplyFirst("(", "+"));
        assertTrue(Parser.shouldApplyFirst("*", "+"));
        assertTrue(Parser.shouldApplyFirst("+", "-"));
        assertFalse(Parser.shouldApplyFirst("+", "*"));
        assertFalse(Parser.shouldApplyFirst("^", "^"));
    }

    @Test
    public void testInfixSingleNumber() {
        AST result = Parser.parseInfix("5");

        assertEquals(new NumNode(5), result);
    }

    @Test
    public void testInfixAddition() {
        AST result = Parser.parseInfix("2 + 3");

        AST expected = new BinopNode(
                "+",
                new NumNode(2),
                new NumNode(3)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixPrecedence() {
        // 2 + 3 * 4 should be 2 + (3 * 4)
        AST result = Parser.parseInfix("2 + 3 * 4");

        AST product = new BinopNode(
                "*",
                new NumNode(3),
                new NumNode(4)
        );

        AST expected = new BinopNode(
                "+",
                new NumNode(2),
                product
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixHigherPrecedenceFirst() {
        // 2 * 3 + 4 should be (2 * 3) + 4
        AST result = Parser.parseInfix("2 * 3 + 4");

        AST product = new BinopNode(
                "*",
                new NumNode(2),
                new NumNode(3)
        );

        AST expected = new BinopNode(
                "+",
                product,
                new NumNode(4)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixLeftAssociative() {
        // 10 - 4 - 3 should be (10 - 4) - 3
        AST result = Parser.parseInfix("10 - 4 - 3");

        AST diff = new BinopNode(
                "-",
                new NumNode(10),
                new NumNode(4)
        );

        AST expected = new BinopNode(
                "-",
                diff,
                new NumNode(3)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixRightAssociative() {
        // 2 ^ 3 ^ 2 should be 2 ^ (3 ^ 2)
        AST result = Parser.parseInfix("2 ^ 3 ^ 2");

        AST pow = new BinopNode(
                "^",
                new NumNode(3),
                new NumNode(2)
        );

        AST expected = new BinopNode(
                "^",
                new NumNode(2),
                pow
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixParentheses() {
        AST result = Parser.parseInfix("2 * ( 3 + 4 )");

        AST sum = new BinopNode(
                "+",
                new NumNode(3),
                new NumNode(4)
        );

        AST expected = new BinopNode(
                "*",
                new NumNode(2),
                sum
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixNestedParentheses() {
        AST result = Parser.parseInfix("( ( 1 + 2 ) * 3 ) - 4");

        AST sum = new BinopNode(
                "+",
                new NumNode(1),
                new NumNode(2)
        );

        AST product = new BinopNode(
                "*",
                sum,
                new NumNode(3)
        );

        AST expected = new BinopNode(
                "-",
                product,
                new NumNode(4)
        );

        assertEquals(expected, result);
    }

    @Test
    public void testInfixEmptyInput() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("")
        );

        assertEquals("empty input", e.getMessage());
    }

    @Test
    public void testInfixInvalidToken() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("2 + a")
        );

        assertEquals("invalid token", e.getMessage());
    }

    @Test
    public void testInfixInsufficientOperands() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("2 +")
        );

        assertEquals("insufficient operands", e.getMessage());
    }

    @Test
    public void testInfixEmptyParentheses() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("( )")
        );

        assertEquals("insufficient operands", e.getMessage());
    }

    @Test
    public void testInfixTooManyOperands() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("2 + 3 4")
        );

        assertEquals("too many operands", e.getMessage());
    }

    @Test
    public void testInfixMismatchedOpenParen() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("( 2 + 3")
        );

        assertEquals("mismatched open paren", e.getMessage());
    }

    @Test
    public void testInfixMismatchedCloseParen() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> Parser.parseInfix("2 + 3 )")
        );

        assertEquals("mismatched close paren", e.getMessage());
    }
}
