package ru.spbau.mit.repl.parser;

import ru.spbau.mit.repl.ast.BinOpType;

public class BinOpUtils {
    public static final String[] operators = {"+", "-", "*", "/"};
    public static final BinOpType[] types = {
            BinOpType.PLUS, BinOpType.MINUS, BinOpType.MULTIPLY, BinOpType.DIVIDE
    };

    public static BinOpType getType(String s) {
        for (int i = 0; i < operators.length; i++) {
            if (s.equals(operators[i])) {
                return types[i];
            }
        }

        return BinOpType.UNKNOWN;
    }

    public static String toString(BinOpType type) {
        for (int i = 0; i < types.length; i++) {
            if (type == types[i]) {
                return operators[i];
            }
        }

        return "#";
    }

    public static int priority(BinOpType type) {
        switch (type) {
            case PLUS:
            case MINUS:
                return 0;
            case UNKNOWN:
                return -1;
            default:
                return 1;
        }
    }
}
