// Stores the operator and the left and right expressions.
public record BinopNode(String op, AST left, AST right) implements AST {

    // Evaluates both sides first and then applies the operator to them.
    @Override
    public double eval() {
        double a = left.eval();
        double b = right.eval();

        if (op.equals("+")) {
            return a + b;
        } else if (op.equals("-")) {
            return a - b;
        } else if (op.equals("*")) {
            return a * b;
        } else if (op.equals("/")) {
            return a / b;
        } else if (op.equals("^")) {
            return Math.pow(a, b);
        }

        throw new IllegalArgumentException("invalid operator");
    }
}
