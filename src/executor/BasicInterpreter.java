package executor;

import stone.BasicParser;
import stone.Lexer;
import stone.ParseException;
import stone.Token;
import stone.ast.ASTree;
import stone.ast.NullStatement;

public class BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new BasicParser(), new BasicEnv());
    }

    private static void run(BasicParser basicParser, BasicEnv basicEnv)
            throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        while (lexer.peek(0) != Token.EOF) {
            ASTree t = basicParser.parse(lexer);
            if (!(t instanceof NullStatement)) {
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(basicEnv);
                System.out.println("=> " + r);
            }
        }
    }
}
