// Converts mathematical expressions into syntax trees.
public class Parser {

    // Converts a postfix expression into an AST.
    // Uses a stack to store numbers and completed subtrees.
    public static AST parsePostfix(String input) {

        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        String[] tokens = input.trim().split("\s+");
        ArrayStack<AST> stack = ArrayStack.emptyStack();
        for (String token : tokens) {

            if (isOperator(token)) {
                applyOperator(stack, token);
            } else if (isNumber(token)) {
                double number = Double.parseDouble(token);
                stack.push(new NumNode(number));
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }

        if (stack.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }

        return stack.pop();
    }

    // Converts an infix expression into an AST using the Shunting-yard algorithm.
    // One stack holds the expressions and the other holds operators that are waiting.
    public static AST parseInfix(String input) {

        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        String[] tokens = input.trim().split("\s+");
        ArrayStack<AST> expressions = ArrayStack.emptyStack();
        ArrayStack<String> operators = ArrayStack.emptyStack();

        for (String token : tokens) {

            if (isNumber(token)) {
                double number = Double.parseDouble(token);
                expressions.push(new NumNode(number));
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                closeParen(expressions, operators);
            } else if (isOperator(token)) {
                pushOperator(expressions, operators, token);
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }

        // Apply all the operators that are left.
        while (!operators.isEmpty()) {
            String operator = operators.pop();

            if (operator.equals("(")) {
                throw new IllegalArgumentException("mismatched open paren");
            }

            applyOperator(expressions, operator);
        }

        if (expressions.isEmpty()) {
            throw new IllegalArgumentException("insufficient operands");
        }

        if (expressions.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }

        return expressions.pop();
    }

    // Returns true if the token is a supported operator.
    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^");
    }

    // Returns true if the token is a number like 5, -2, or 3.75.
    // A number can start with a minus sign and can have at most one decimal point.
    public static boolean isNumber(String token) {
        int start = 0;
        if (token.charAt(0) == '-') {
            start = 1;
        }

        int digits = 0;
        int dots = 0;

        for (int i = start; i < token.length(); i++) {
            char c = token.charAt(i);

            if (c >= '0' && c <= '9') {
                digits++;
            } else if (c == '.') {
                dots++;
            } else {
                return false;
            }
        }

        return digits > 0 && dots <= 1;
    }

    // Returns the precedence of an operator. Higher numbers go first.
    public static int precedence(String operator) {
        if (operator.equals("^")) {
            return 3;
        } else if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }

        return 1;
    }

    // Returns true if the operator on top of the stack should be applied
    // before the new operator is pushed.
    public static boolean shouldApplyFirst(String top, String current) {
        if (top.equals("(")) {
            return false;
        }

        if (precedence(top) > precedence(current)) {
            return true;
        }

        // ^ is right associative so it does not pop another ^.
        return precedence(top) == precedence(current) && !current.equals("^");
    }

    // Applies operators from the stack that should go first,
    // then pushes the new operator.
    public static void pushOperator(
            ArrayStack<AST> expressions,
            ArrayStack<String> operators,
            String operator
    ) {

        while (!operators.isEmpty() && shouldApplyFirst(operators.peek(), operator)) {
            applyOperator(expressions, operators.pop());
        }

        operators.push(operator);
    }

    // Applies operators until the matching open paren is found,
    // then removes the open paren.
    public static void closeParen(
            ArrayStack<AST> expressions,
            ArrayStack<String> operators
    ) {

        while (!operators.isEmpty() && !operators.peek().equals("(")) {
            applyOperator(expressions, operators.pop());
        }

        if (operators.isEmpty()) {
            throw new IllegalArgumentException("mismatched close paren");
        }

        operators.pop();
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
