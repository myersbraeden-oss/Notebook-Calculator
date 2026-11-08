public class Calculator{

    public static double calculate(double num1, String operator, double num2){
        switch(operator){
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                return num1 * num2;
            case "/":
                if (num2 == 0){
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid Operator");

        }
    }



    


public static double calculateExpression(String expression) {

    String cleaned = expression.replaceAll("\\s+", "")
            .replace('x', '*')
            .replace('X', '*')
            .replace('×', '*')
            .replace('÷', '/');

    Parser parser = new Parser(cleaned);
    double result = parser.parseExpression();

    if (parser.hasRemaining()) {
        throw new IllegalArgumentException(
                "Unexpected character at position "
                + parser.position()
        );
    }

    return result;
}

private static class Parser {

    private final String input;
    private int index = 0;

    Parser(String input) {
        this.input = input;
    }

    boolean hasRemaining() {
        return index < input.length();
    }

    int position() {
        return index;
    }

    boolean match(char expected) {
        if (hasRemaining() && input.charAt(index) == expected) {
            index++;
            return true;
        }
        return false;
    }

    // Addition and subtraction
    double parseExpression() {

        double result = parseTerm();

        while (true) {
            if (match('+')) {
                result += parseTerm();
            } else if (match('-')) {
                result -= parseTerm();
            } else {
                return result;
            }
        }
    }

    // Multiplication and division
    double parseTerm() {

        double result = parseFactor();

        while (true) {
            if (match('*')) {
                result *= parseFactor();
            } else if (match('/')) {
                double divisor = parseFactor();

                if (divisor == 0) {
                    throw new ArithmeticException(
                            "Division by zero"
                    );
                }

                result /= divisor;
            } else {
                return result;
            }
        }
    }

    // Parentheses, negative numbers, and positive numbers
    double parseFactor() {

        if (match('+')) {
            return parseFactor();
        }

        if (match('-')) {
            return -parseFactor();
        }

        if (match('(')) {
            double result = parseExpression();

            if (!match(')')) {
                throw new IllegalArgumentException(
                        "Missing closing parenthesis"
                );
            }

            return result;
        }

        return parseNumber();
    }

    double parseNumber() {

        int start = index;
        boolean hasDigit = false;
        boolean hasDecimal = false;

        while (hasRemaining()) {
            char c = input.charAt(index);

            if (c >= '0' && c <= '9') {
                hasDigit = true;
                index++;
            } else if (c == '.' && !hasDecimal) {
                hasDecimal = true;
                index++;
            } else {
                break;
            }
        }

        if (!hasDigit) {
            throw new IllegalArgumentException(
                    "Expected number at position " + index
            );
        }

        return Double.parseDouble(
                input.substring(start, index)
        );
    }
}



}