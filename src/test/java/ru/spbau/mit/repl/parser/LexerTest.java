package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.error.Location;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class LexerTest {
    private Location buildLocation(int column) {
        return new Location(0, column);
    }

    @Test
    public void testTokenize() throws Exception {
        Token[] expected = new Token[]{
                new Token(TokenType.BINARY_OPERATOR, "+", buildLocation(1), buildLocation(1)),
                new Token(TokenType.LITERAL, "1122", buildLocation(3), buildLocation(6)),
                new Token(TokenType.IDENTIFIER, "abc", buildLocation(10), buildLocation(12)),
                new Token(TokenType.IDENTIFIER, "d_877", buildLocation(14), buildLocation(18)),
                new Token(TokenType.BINARY_OPERATOR, "+", buildLocation(20), buildLocation(20)),
                new Token(TokenType.LEFT_PARENTHESIS, "(", buildLocation(23), buildLocation(23)),
                new Token(TokenType.BINARY_OPERATOR, "-", buildLocation(24), buildLocation(24)),
                new Token(TokenType.BINARY_OPERATOR, "/", buildLocation(27), buildLocation(27)),
                new Token(TokenType.BINARY_OPERATOR, "*", buildLocation(29), buildLocation(29)),
                new Token(TokenType.RIGHT_PARENTHESIS, ")", buildLocation(31), buildLocation(31)),
                new Token(TokenType.ASSIGNMENT, "=", buildLocation(34), buildLocation(34))
        };

        Lexer lexer = new Lexer(" + 1122   abc d_877 +  (-  / * )  =  ");
        List<Token> result = lexer.tokenize();

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testTokenizeError() throws Exception {
        thrown.expect(LexerException.class);
        thrown.expectMessage("Unexpected char '#' at line=0, column=9");
        Lexer lexer = new Lexer(" + 1122  #--- abc d_877 +  (-  / * )  =  ");
        lexer.tokenize();
    }
}
