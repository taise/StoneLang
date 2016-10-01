package stone;
import stone.ast.ASTree;

public class StoneException extends RuntimeException {
    public StoneException(String s) {
        super(s);
    }
    public StoneException(String s, ASTree t) {
        super(s + " " + t.location());
    }
}
