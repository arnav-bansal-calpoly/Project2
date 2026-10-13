// Stores the operator and the left and right expressions.
public record BinopNode(String operator, AST left, AST right) implements AST {

    // Evaluates both sides first and then applies the operator to them.
    @Override
    public double eval() {
        double leftValue = left.eval();
        double rightValue = right.eval();

        if (operator.equals("+")) {
            return leftValue + rightValue;
        } else if (operator.equals("-")) {
            return leftValue - rightValue;
        } else if (operator.equals("*")) {
            return leftValue * rightValue;
        } else if (operator.equals("/")) {
            return leftValue / rightValue;
        } else if (operator.equals("^")) {
            return Math.pow(leftValue, rightValue);
        }

        throw new IllegalArgumentException("invalid operator");
    }
}
