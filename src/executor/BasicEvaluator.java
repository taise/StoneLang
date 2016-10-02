package executor;

import javassist.gluonj.*;
import stone.StoneException;
import stone.Token;
import stone.ast.*;

import java.util.List;


@Reviser public class BasicEvaluator {
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    @Reviser public static abstract class ASTreeEx extends ASTree {
        public abstract Object eval(Environment env);
    }

    @Reviser public static abstract class ASTListEx extends ASTList {
        public ASTListEx(List<ASTree> list) {
            super(list);
        }

        public Object eval(Environment env) {
            throw new StoneException("cannot eval: " + toString(), this);
        }
    }

    @Reviser public static abstract class ASTLeafEx extends ASTLeaf {
        public ASTLeafEx(Token token) {
            super(token);
        }

        public Object eval(Environment env) {
            throw new StoneException("cannot eval: " + toString(), this);
        }
    }

    @Reviser public static class NumberEx extends NumberLiteral {
        public NumberEx(Token token) {
            super(token);
        }

        public Object eval(Environment env) {
            return value();
        }
    }

    @Reviser public static class StringEx extends StringLiteral {
        public StringEx(Token token) {
            super(token);
        }

        public Object eval(Environment env) {
            return value();
        }
    }

    @Reviser public static class NameEx extends Name {
        public NameEx(Token token) {
            super(token);
        }

        public Object eval(Environment env) {
            Object value = env.get(name());
            if (value == null) {
                throw new StoneException("undefined name: " + name(), this);
            }
            return value;
        }

    }

}
