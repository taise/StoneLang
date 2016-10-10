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

    @Reviser public static class ASTListEx extends ASTList {
        public ASTListEx(List<ASTree> list) {
            super(list);
        }

        public Object eval(Environment env) {
            throw new StoneException("cannot eval: " + toString(), this);
        }
    }

    @Reviser public static class ASTLeafEx extends ASTLeaf {
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

    @Reviser public static class NegativeEx extends NegativeExpr {
       public NegativeEx(List<ASTree> tree)  {
          super(tree);
       }

       public Object eval(Environment env) {
           Object value = ((ASTreeEx)operand()).eval(env);
           if (value instanceof Integer) {
               return new Integer(-((Integer)value).intValue());
           }
           throw new StoneException("bad type for -", this);
       }
    }

    @Reviser public static class BinaryEx extends BinaryExpr {
        public BinaryEx(List<ASTree> tree) {
            super(tree);
        }

        public Object eval(Environment env) {
            String op = operator();
            if ("=".equals(op)) {
                Object right = ((ASTreeEx)right()).eval(env);
                return computeAssign(env, right);
            }
            Object left = ((ASTreeEx)left()).eval(env);
            Object right = ((ASTreeEx)right()).eval(env);
            return computeOp(left, op, right);
        }

        public Object computeAssign(Environment env, Object right) {
            ASTree left = left();
            if (left instanceof Name) {
                env.put(((Name)left).name(), right);
                return right;
            }
            throw new StoneException("bad assignment", this);
        }

        protected Object computeOp(Object left, String op, Object right) {
            if (left instanceof Integer && right instanceof Integer) {
                return computeNumber((Integer)left, op, (Integer)right);
            }
            if (op.equals("+")) {
                return String.valueOf(left) + String.valueOf(right);
            }
            if (op.equals("==")) {
                if (left == null) {
                    return right == null ? TRUE : FALSE;
                }
                return left.equals(right) ? TRUE : FALSE;
            }
            throw new StoneException("bad type", this);
        }

        private Object computeNumber(Integer left, String op, Integer right) {
            int l = left.intValue();
            int r = right.intValue();
            if (op.equals("+")) {
               return l + r;
            }
            if (op.equals("-")) {
                return l - r;
            }
            if (op.equals("*")) {
                return l * r;
            }
            if (op.equals("/")) {
                return l / r;
            }
            if (op.equals("%")) {
                return l % r;
            }
            if (op.equals("==")) {
                return l == r ? TRUE : FALSE;
            }
            if (op.equals(">")) {
                return l > r ? TRUE : FALSE;
            }
            if (op.equals("<")) {
                return l < r ? TRUE : FALSE;
            }
            throw new StoneException("bad operator", this);
        }

        @Reviser public static class BlockEx extends BlockStatement {
            public BlockEx(List<ASTree> c) { super(c); }
            public Object eval(Environment env) {
                Object result = 0;
                for (ASTree t: this) {
                    if (!(t instanceof NullStatement))
                        result = ((ASTreeEx)t).eval(env);
                }
                return result;
            }
        }
        @Reviser public static class IfEx extends IfStatement {
            public IfEx(List<ASTree> c) { super(c); }
            public Object eval(Environment env) {
                Object c = ((ASTreeEx)condition()).eval(env);
                if (c instanceof Integer && ((Integer)c).intValue() != FALSE)
                    return ((ASTreeEx)thenBlock()).eval(env);
                else {
                    ASTree b = elseBlock();
                    if (b == null)
                        return 0;
                    else
                        return ((ASTreeEx)b).eval(env);
                }
            }
        }
        @Reviser public static class WhileEx extends WhileStatement {
            public WhileEx(List<ASTree> c) { super(c); }
            public Object eval(Environment env) {
                Object result = 0;
                for (;;) {
                    Object c = ((ASTreeEx)condition()).eval(env);
                    if (c instanceof Integer && ((Integer)c).intValue() == FALSE)
                        return result;
                    else
                        result = ((ASTreeEx)body()).eval(env);
                }
            }
        }
    }
}
