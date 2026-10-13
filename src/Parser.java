// Converts mathematical expressions into syntax trees.
public class Parser {

    // Converts a postfix expression into an AST.
    // Uses a stack to store numbers and completed subtrees.
    public static AST parsePostfix(String str) {

        if (str.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        String[] tokens = str.trim().split("\s+");
        ArrayStack<AST> s = ArrayStack.emptyStack();
        for (String t : tokens) {

            if (isOperator(t)) {
                applyOperator(s, t);
            } else if (isNumber(t)) {
                double num = Double.parseDouble(t);
                s.push(new NumNode(num));
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }

        if (s.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }

        return s.pop();
    }

    // Converts an infix expression into an AST using the Shunting-yard algorithm.
    // One stack holds the expressions and the other holds operators that are waiting.
    public static AST parseInfix(String str) {

        if (str.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        String[] tokens = str.trim().split("\s+");
        ArrayStack<AST> exprs = ArrayStack.emptyStack();
        ArrayStack<String> ops = ArrayStack.emptyStack();

        for (String t : tokens) {

            if (isNumber(t)) {
                double num = Double.parseDouble(t);
                exprs.push(new NumNode(num));
            } else if (t.equals("(")) {
                ops.push(t);
            } else if (t.equals(")")) {
                closeParen(exprs, ops);
            } else if (isOperator(t)) {
                pushOperator(exprs, ops, t);
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }

        // Apply all the operators that are left.
        while (!ops.isEmpty()) {
            String op = ops.pop();

            if (op.equals("(")) {
                throw new IllegalArgumentException("mismatched open paren");
            }

            applyOperator(exprs, op);
        }

        if (exprs.isEmpty()) {
            throw new IllegalArgumentException("insufficient operands");
        }

        if (exprs.size() > 1) {
            throw new IllegalArgumentException("too many operands");
        }

        return exprs.pop();
    }

    // Returns true if the token is a supported operator.
    public static boolean isOperator(String t) {
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/") || t.equals("^");
    }

    // Returns true if the token is a number like 5, -2, or 3.75.
    // A number can start with a minus sign and can have at most one decimal point.
    public static boolean isNumber(String t) {
        int start = 0;
        if (t.charAt(0) == '-') {
            start = 1;
        }

        int digits = 0;
        int dots = 0;

        for (int i = start; i < t.length(); i++) {
            char c = t.charAt(i);

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
    public static int precedence(String op) {
        if (op.equals("^")) {
            return 3;
        } else if (op.equals("*") || op.equals("/")) {
            return 2;
        }

        return 1;
    }

    // Returns true if the operator on top of the stack should be applied
    // before the new operator is pushed.
    public static boolean shouldApplyFirst(String top, String op) {
        if (top.equals("(")) {
            return false;
        }

        if (precedence(top) > precedence(op)) {
            return true;
        }

        // ^ is right associative so it does not pop another ^.
        return precedence(top) == precedence(op) && !op.equals("^");
    }

    // Applies operators from the stack that should go first,
    // then pushes the new operator.
    public static void pushOperator(
            ArrayStack<AST> exprs,
            ArrayStack<String> ops,
            String op
    ) {

        while (!ops.isEmpty() && shouldApplyFirst(ops.peek(), op)) {
            applyOperator(exprs, ops.pop());
        }

        ops.push(op);
    }

    // Applies operators until the matching open paren is found,
    // then removes the open paren.
    public static void closeParen(
            ArrayStack<AST> exprs,
            ArrayStack<String> ops
    ) {

        while (!ops.isEmpty() && !ops.peek().equals("(")) {
            applyOperator(exprs, ops.pop());
        }

        if (ops.isEmpty()) {
            throw new IllegalArgumentException("mismatched close paren");
        }

        ops.pop();
    }

    // Applies an operator to the top two ASTs in the stack.
    public static void applyOperator(
            ArrayStack<AST> s,
            String op
    ) {

        if (s.size() < 2) {
            throw new IllegalArgumentException(
                    "insufficient operands"
            );
        }

        AST right = s.pop();
        AST left = s.pop();

        AST node = new BinopNode(op, left, right);

        s.push(node);
    }
}
