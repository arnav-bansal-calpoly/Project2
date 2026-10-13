// Stores the operator and the left and right expressions.
public record BinopNode(String operator, AST left, AST right) implements AST {

}