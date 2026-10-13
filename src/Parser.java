// Converts mathematical expressions into syntax trees.
public class Parser {

    // Converts a postfix expression into an AST.
    // Uses a stack to store numbers and completed subtrees.
    public static AST parsePostfix(String input) {

        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        String[] tokens = input.trim().split("\\s+");
        ArrayStack<AST> stack = ArrayStack.emptyStack();
        for (String token : tokens) {

            if (isOperator(token)) {
                applyOperator(stack, token);
            } else {
                try {
                    double number = Double.parseDouble(token);
                    stack.push(new NumNode(number));

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("invalid token");
                }
            }
        }

        if (stack.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }

        return stack.pop();
    }

    // Returns true if the token is a supported operator.
    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^");
    }

    // Applies an operator to the top two ASTs in the stack.
    public static void applyOperator(
            ArrayStack<AST> stack,
            String operator
    ) {

        if (stack.size() < 2) {
            throw new IllegalArgumentException(
                    "insufficient operands"
            );
        }

        AST right = stack.pop();
        AST left = stack.pop();

        AST result = new BinopNode(operator, left, right);

        stack.push(result);
    }
}
