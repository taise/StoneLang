package stone.ast;

import java.util.List;

public class IfStatement extends ASTList {
    public IfStatement(List<ASTree> list) {
        super(list);
    }

    public ASTree condition() {
        return child(0);
    }

    public ASTree thenBlock() {
        return child(1);
    }

    public ASTree elseBlock() {
        return numChildren() > 2 ? child(2) : null;
    }

    public String toString() {
        return "(if " + condition()
               + " " + thenBlock()
               + " else " + elseBlock()
               + ")";
    }

}
