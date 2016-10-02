package stone;

import stone.ast.ASTree;

public class ParserRunner {
    public static void main(String[] args) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        BasicParser bp = new BasicParser();
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
/*
    // sample code (stone)
    event = 0
    odd = 0
    i = 1
    while i < 10 {
        if i % 2 == 0 {
            even = even + i
        } else {
            odd = odd + i
        }
    }
    even + odd
*/
