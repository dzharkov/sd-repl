package ru.spbau.mit.repl.parser;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.spbau.mit.repl.error.Location;

public class ParserTest {
    private final ToStringVisitor toStringVisitor = new ToStringVisitor();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private void assertSuccessfulResult(String expected, String text) {
        ParserResult result = Parser.parse(text);
        Assert.assertTrue(result.isSuccessful());

        if (expected == null) {
            Assert.assertNull(result.getRoot());
        } else {
            Assert.assertEquals(expected, result.getRoot().accept(toStringVisitor));
        }
    }

    private void assertError(String expected, String text, ParserError... errors) {
        ParserResult result = Parser.parse(text);
        Assert.assertFalse(result.isSuccessful());

        if (expected == null) {
            Assert.assertNull(result.getRoot());
        } else {
            Assert.assertEquals(expected, result.getRoot().accept(toStringVisitor));
        }

        Assert.assertArrayEquals(errors, result.getErrors().toArray());
    }

    @Test
    public void testExpression() throws Exception {
        assertSuccessfulResult(null, "    ");
        assertSuccessfulResult("1", "1");
        assertSuccessfulResult("x" ,"x");

        assertSuccessfulResult("1", "(1)");
        assertSuccessfulResult("x" ,"(x)");

        assertSuccessfulResult("((((((1+((2*3)/4))-4)+9)+0)+(0*9))+7)", "1 + 2*3 /4  -4 +9 +0 +0*9 + 7");
        assertSuccessfulResult("(1+((2*(3/4))-(4+(9+(0+((0*9)+7))))))", "(1+((2*(3/4))-(4+(9+(0+((0*9)+7))))))");
    }

    @Test
    public void testAssignment() throws Exception {
        assertSuccessfulResult("abc=((((((1+((2*3)/4))-4)+9)+0)+(0*9))+7)", "abc=1 + 2*3 /4  -4 +9 +0 +0*9 + 7");
        assertSuccessfulResult("abc=(1+((2*(3/4))-(4+(9+(0+((0*9)+7))))))", "abc=(1+((2*(3/4))-(4+(9+(0+((0*9)+7))))))");
    }

    private ParserError error(int location) {
        return new ParserError(new Location(0, location), "");
    }

    @Test
    public void testExpressionError() throws Exception {
        assertError("e", "   (", error(3));
        assertError("(1+x)", "1+(x))", error(5), error(5));
        assertError("(((1+x)+e)+5)", "(1+x+)+5", error(5));
        assertError("(1+e)", "1+", error(1));
    }
}
