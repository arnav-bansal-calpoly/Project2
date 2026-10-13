// Stores a single number in the tree.
public record NumNode(double value) implements AST {

    // A number just evaluates to itself.
    @Override
    public double eval() {
        return value;
    }
}
