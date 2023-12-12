package assignment; 
import java.util.*;

public class WebQueryEngine {
    private WebIndex index;

    private WebQueryEngine(WebIndex index) {
        this.index = index;
    }

    public static WebQueryEngine fromIndex(WebIndex index) {
        return new WebQueryEngine(index);
    }

    public Collection<Page> query(String queryString) {
        String trimmedQuery = queryString.trim();
        if (trimmedQuery.isEmpty()) {
            return index.getAllPages();
        }

        queryString = queryString.toLowerCase();
        List<String> postfixExpression = shuntingYard(queryString);
        TreeNode parseTree = buildParseTree(postfixExpression);
        return evalParseTree(parseTree);
    }

    // private List<String> shuntingYard(String infixExpression) {
    //     List<String> output = new ArrayList<>();
    //     Stack<String> operators = new Stack<>();

    //     List<String> tokens = tokenize(infixExpression);

    //     for (String token : tokens) {
    //         if (isWord(token) || isPhrase(token)) {
    //             output.add(token);
    //         } else if (isOperator(token)) {
    //             while (!operators.isEmpty() && higherPrecedence(operators.peek(), token)) {
    //                 output.add(operators.pop());
    //             }
    //             operators.push(token);
    //         } else if (token.equals("(")) {
    //             operators.push(token);
    //         } else if (token.equals(")")) {
    //             while (!operators.isEmpty() && !operators.peek().equals("(")) {
    //                 output.add(operators.pop());
    //             }
    //             operators.pop(); // Discard opening parenthesis
    //         } else if (isImplicitAndQuery(token)) {
    //             while (!operators.isEmpty() && isImplicitAndQuery(operators.peek())) {
    //                 output.add(operators.pop());
    //             }
    //             operators.push(token);
    //         }
    //     }

    //     while (!operators.isEmpty()) {
    //         output.add(operators.pop());
    //     }

    //     return output;
    // }
    // private List<String> shuntingYard(String infixExpression) {
    //     List<String> output = new ArrayList<>();
    //     Stack<String> operators = new Stack<>();
    
    //     List<String> tokens = tokenize(infixExpression);
    
    //     for (String token : tokens) {
    //         if (isWord(token) || isPhrase(token)) {
    //             output.add(token);
    //         } else if (isOperator(token)) {
    //             while (!operators.isEmpty() && higherPrecedence(operators.peek(), token)) {
    //                 output.add(operators.pop());
    //             }
    //             operators.push(token);
    //         } else if (token.equals("(")) {
    //             operators.push(token);
    //         } else if (token.equals(")")) {
    //             while (!operators.isEmpty() && !operators.peek().equals("(")) {
    //                 output.add(operators.pop());
    //             }
    //             operators.pop(); // Discard opening parenthesis
    //         } else if (isImplicitAndQuery(token)) {
    //             // Modify this block to handle phrase queries correctly
    //             while (!operators.isEmpty() && isImplicitAndQuery(operators.peek())) {
    //                 // Check if the operator at the top of the stack is a phrase
    //                 // If it is, pop it only if the current token is also a phrase
    //                 if (isPhrase(operators.peek()) && isPhrase(token)) {
    //                     output.add(operators.pop());
    //                 } else {
    //                     break;
    //                 }
    //             }
    //             operators.push(token);
    //         }
    //     }
    
    //     while (!operators.isEmpty()) {
    //         output.add(operators.pop());
    //     }
    
    //     return output;
    // }
    // private List<String> shuntingYard(String infixExpression) {
    //     List<String> output = new ArrayList<>();
    //     Stack<String> operators = new Stack<>();
    
    //     List<String> tokens = tokenize(infixExpression);
    
    //     for (String token : tokens) {
    //         if (isWord(token) || isPhrase(token)) {
    //             output.add(token);
    //         } else if (isOperator(token)) {
    //             while (!operators.isEmpty() && higherPrecedence(operators.peek(), token)) {
    //                 output.add(operators.pop());
    //             }
    //             operators.push(token);
    //         } else if (token.equals("(")) {
    //             operators.push(token);
    //         } else if (token.equals(")")) {
    //             while (!operators.isEmpty() && !operators.peek().equals("(")) {
    //                 output.add(operators.pop());
    //             }
    //             operators.pop(); // Discard opening parenthesis
    //         } else if (isImplicitAndQuery(token)) {
    //             // Handle phrase queries correctly by pushing the entire phrase as a single token
    //             if (isPhrase(token)) {
    //                 output.add(token);
    //             } else {
    //                 while (!operators.isEmpty() && isImplicitAndQuery(operators.peek())) {
    //                     output.add(operators.pop());
    //                 }
    //                 operators.push(token);
    //             }
    //         }
    //     }
    
    //     while (!operators.isEmpty()) {
    //         output.add(operators.pop());
    //     }
    
    //     return output;
    // }
    // private List<String> shuntingYard(String infixExpression) {
    //     List<String> output = new ArrayList<>();
    //     Stack<String> operators = new Stack<>();
    
    //     List<String> tokens = tokenize(infixExpression);
    
    //     for (String token : tokens) {
    //         if (isWord(token) || isPhrase(token)) {
    //             output.add(token);
    //         } else if (isOperator(token)) {
    //             while (!operators.isEmpty() && higherPrecedence(operators.peek(), token)) {
    //                 output.add(operators.pop());
    //             }
    //             operators.push(token);
    //         } else if (token.equals("(")) {
    //             operators.push(token);
    //         } else if (token.equals(")")) {
    //             while (!operators.isEmpty() && !operators.peek().equals("(")) {
    //                 output.add(operators.pop());
    //             }
    //             operators.pop(); // Discard opening parenthesis
    //         } else if (isImplicitAndQuery(token)) {
    //             // Handle phrase queries correctly by pushing the entire phrase as a single token
    //             if (isPhrase(token)) {
    //                 output.add(token);
    //             } else {
    //                 while (!operators.isEmpty() && isImplicitAndQuery(operators.peek())) {
    //                     output.add(operators.pop());
    //                 }
    //                 operators.push(token);
    //             }
    //         }
    //     }
    
    //     while (!operators.isEmpty()) {
    //         output.add(operators.pop());
    //     }
    
    //     return output;
    // }
    private List<String> shuntingYard(String infixExpression) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
    
        List<String> tokens = tokenize(infixExpression);
    
        for (String token : tokens) {
            System.out.println("Op stack: " + operators);
            if (isWord(token) || isPhrase(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && higherPrecedence(operators.peek(), token) && !token.equals("(")) {

                        output.add(operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // Discard opening parenthesis
                } else {
                    // Handle error: mismatched parentheses
                    throw new IllegalArgumentException("Mismatched parentheses in the query.");
                }
            } else if (isImplicitAndQuery(token)) {
                // Handle phrase queries correctly by pushing the entire phrase as a single token
                if (isPhrase(token)) {
                    output.add(token);
                } else {
                    while (!operators.isEmpty() && isImplicitAndQuery(operators.peek())) {
                        output.add(operators.pop());
                    }
                    operators.push(token);
                }
            }
        }
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;

    }

    private List<String> tokenize(String query) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        for (char c : query.toCharArray()) {
            if (Character.isLetterOrDigit(c) || isSpecialCharacter(c)) {
                currentWord.append(c);
            } else {
                if (currentWord.length() > 0) {
                    tokens.add(currentWord.toString());
                    currentWord.setLength(0);
                }

                if (c == '&' || c == '|' || c == '!' || c == '(' || c == ')') {
                    tokens.add(String.valueOf(c));
                } else if (c == '"') {
                    int endQuoteIndex = query.indexOf('"', tokens.size());
                    if (endQuoteIndex != -1) {
                        tokens.add(query.substring(tokens.size(), endQuoteIndex + 1));
                    } else {
                        // Handle error: mismatched quotes
                        throw new IllegalArgumentException("Mismatched quotes in the query.");
                    }
                } else if (!Character.isWhitespace(c)) {
                    // Add special characters as individual tokens
                    tokens.add(String.valueOf(c));
                }
            }
        }
        // check for parenthesis and breaking
        // 

        if (currentWord.length() > 0) {
            tokens.add(currentWord.toString());
        }

        List<String> tokensCopy = List.copyOf(tokens);

        tokens.clear();

        for (String token : tokensCopy) {
            if (token.startsWith("(") && token.endsWith(")")) {
                while (token.startsWith("(")) {
                    tokens.add("(");
                    token = token.substring(1);
                }

                int numParen = 0;
                while (token.endsWith(")")) {
                    numParen++;
                    token = token.substring(0, token.length() - 1);
                }
                tokens.add(token);
                for (int i = 0; i < numParen; i++) {
                    tokens.add(")");
                }

            } else if (token.startsWith("(")) {
                while (token.startsWith("(")) {
                    tokens.add("(");
                    token = token.substring(1);
                }
                tokens.add(token);
            } else if (token.endsWith(")")) {
                int numParen = 0;
                while (token.endsWith(")")) {
                    numParen++;
                    token = token.substring(0, token.length() - 1);
                }
                tokens.add(token);
                for (int i = 0; i < numParen; i++) {
                    tokens.add(")");
                }
            } else {
                tokens.add(token);
            }
        }


        tokensCopy = List.copyOf(tokens);

        tokens.clear();

        boolean phraseFound = false;

        StringBuilder sb = new StringBuilder();

        for (String token : tokensCopy) {
            if (token.startsWith("\"") && token.endsWith("\"") && !token.equals("\"")) {
                tokens.add(token);
            } else if (token.equals("\"")) {
                if (phraseFound) {
                    phraseFound = false;

                    sb.append(token);
                    tokens.add(sb.toString());
                } else {
                    phraseFound = true;

                    sb = new StringBuilder();

                    sb.append(token);
                    sb.append(" ");
                }
            } else if (token.startsWith("\"")) {
                sb = new StringBuilder();

                phraseFound = true;

                sb.append(token);
                sb.append(" ");
            } else if (token.endsWith("\"")) {
                sb.append(token);
                tokens.add(sb.toString());

                phraseFound = false;
            } else {
                if (phraseFound) {
                    sb.append(token);
                    sb.append(" ");
                } else {
                    tokens.add(token);
                }
            }
        }


//        for (String token : tokensCopy) {
//
//            if (token.startsWith("\"") && token.endsWith("\"") && token.length() != 1) {
//                tokens.add(token);
//            } else if (token.startsWith("\"")) {
//                sb.append(token);
//                sb.append(" ");
//                phraseFound = !phraseFound;
//            } else if (token.endsWith("\"")) {
//                sb.append(token);
//                tokens.add(sb.toString());
//                sb = new StringBuilder();
//            } else {
//                if (phraseFound) {
//                    sb.append(token);
//                    sb.append(" ");
//                } else {
//                    //not dealing with phrase
//                    tokens.add(token);
//                }
//            }
//        }

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.trim().isEmpty()) {
                tokens.remove(i);
            }
        }

        tokensCopy = List.copyOf(tokens);

        tokens.clear();

        boolean lastTokenOperator = true;
        String lastToken = "";

        for (String token : tokensCopy) {
            if (isOperator(token) || token.equals("(")) {
                lastTokenOperator = true;
                if (lastToken.equals(")") && !isOperator(token)) {
                    tokens.add("&");
                }
                if (isWord(lastToken) && token.equals("(")) {
                    tokens.add("&");
                }else if (isPhrase(lastToken) && token.equals("(")) {
                    tokens.add("&");
                }
                tokens.add(token);
            } else {
                if (!lastTokenOperator) {
                    if ((!token.equals(")"))) {
                        tokens.add("&");
                        tokens.add(token);
                    } else {
                        tokens.add(token);
                    }
                    lastTokenOperator = false;
                    // current not operator and last not operator
                } else {
                    //current not operator and last is operator
                    tokens.add(token);
                    lastTokenOperator = false;
                }
            }

            lastToken = token;
        }

        System.out.println(query + " tokens: " + tokens);
    
        return tokens;
    }



    private boolean isSeparator(char c) {
        return Character.isWhitespace(c) || c == '(' || c == ')';
    }
    
    private boolean isSpecialCharacter(char c) {
        return !Character.isLetterOrDigit(c) && !Character.isWhitespace(c);
    }
    
    private TreeNode buildParseTree(List<String> postfixExpression) {
        Stack<TreeNode> nodeStack = new Stack<>();
        System.out.println("Postfix Expression: " + postfixExpression);


        for (String token : postfixExpression) {
            if (isWord(token) || isPhrase(token)) {
                nodeStack.push(new WordNode(token));
            } else if (isOperator(token)) {
                OperatorNode operatorNode = new OperatorNode(token);
                operatorNode.right = nodeStack.pop();
                operatorNode.left = nodeStack.pop();
                nodeStack.push(operatorNode);
            }
        }

        // Final result on the stack
        return nodeStack.isEmpty() ? null : nodeStack.pop();
    }

    //TODO: Fix negation by keeping exclamation point in word and processing in evalparsetree

    private Collection<Page> evalParseTree(TreeNode parseTree) {
        if (parseTree instanceof WordNode) {
            return parseWordImpl(((WordNode) parseTree).word);
        } else if (parseTree instanceof OperatorNode) {
            OperatorNode operatorNode = (OperatorNode) parseTree;
            Collection<Page> leftResults = evalParseTree(operatorNode.left);
            Collection<Page> rightResults = evalParseTree(operatorNode.right);
            return evalOp(operatorNode.operator, leftResults, rightResults);
        } else if (parseTree instanceof PhraseNode) {
            return index.findPhrase(((PhraseNode) parseTree).phrase);
        }

        return new LinkedList<>();
    }

    private Collection<Page> parseWordImpl(String word) {
        if (word.startsWith("\"") && word.endsWith("\"")) {
            word = word.substring(1, word.length() - 1);
            return index.findPhrase(word);
        }
        else if (word.startsWith("!")) {
            Set<Page> wordPages = index.findWord(word.substring(1));
            return negateRes(wordPages);
        } else {
            return index.findWord(word);
        }
    }

    private boolean isWord(String token) {
        if (token.isEmpty()) {
            return false;
        }

        // Might cause problems in the future
        if (token.startsWith("!")) {
            return true;
        }
    
        char firstChar = token.charAt(0);
        char lastChar = token.charAt(token.length() - 1);
    
        if (!Character.isLetterOrDigit(firstChar) || (!Character.isLetterOrDigit(lastChar))){
            return false;
        }
    
        for (int i = 1; i < token.length() - 1; i++) {
            char currentChar = token.charAt(i);
            if (!Character.isLetterOrDigit(currentChar) && !isSpecialCharacter(currentChar)) {
                return false;
            }
        }
    
        return true;
    }
    

    private boolean isPhrase(String token) {
        return token.startsWith("\"") && token.endsWith("\"");
    }

    private boolean isOperator(String token) {
        return token.equals("&") || token.equals("|") || token.equals("!");
    }

    private boolean isPhraseQuery(String token) {
        return isPhrase(token) && token.length() >= 2;
    }

    private boolean higherPrecedence(String op1, String op2) {
        if (op1.equals("!")) {
            return true;
        } else if ((op1.equals("&") && op2.equals("|")) || (op1.equals("|") && op2.equals("&"))) {
            return op1.equals("&");
        } else if (op1.equals("&") || op1.equals("|")) {
            return true;
        } else {
            return false;
        }
    }

    private Collection<Page> evalOp(String operator, Collection<Page> leftResults, Collection<Page> rightResults) {
        switch (operator) {
            case "&":
                return intersectRes(leftResults, rightResults);
            case "|":
                return unionRes(leftResults, rightResults);
            case "!":
                return negateRes(leftResults);
            default:
                return new LinkedList<>();
        }
    }

    private Collection<Page> phraseRes(Collection<Page> set1, Collection<Page> set2) {
        Set<Page> result = new HashSet<>();

        for (Page page1 : set1) {
            if (set2.contains(page1)) {
                result.add(page1);
            }
        }

        return result;
    }

    private Collection<Page> intersectRes(Collection<Page> set1, Collection<Page> set2) {
        Set<Page> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    private Collection<Page> unionRes(Collection<Page> set1, Collection<Page> set2) {
        Set<Page> union = new HashSet<>(set1);
        union.addAll(set2);
        return union;
    }

    private Collection<Page> negateRes(Collection<Page> set) {
        Set<Page> allPages = new HashSet<>(index.getAllPages());
        allPages.removeAll(set);
        return allPages;
    }

    private boolean isImplicitAndQuery(String token) {
        return isWord(token) || isPhraseQuery(token);
    }

    private abstract class TreeNode {
    }

    private class WordNode extends TreeNode {
        String word;

        boolean negation = false;

        WordNode(String word) {
            this.word = word;
        }
    }

    private class OperatorNode extends TreeNode {
        String operator;
        TreeNode left;
        TreeNode right;

        OperatorNode(String operator) {
            this.operator = operator;
        }
    }

    private class PhraseNode extends TreeNode {
        String phrase;

        PhraseNode(String phrase) {
            this.phrase = phrase;
        }
    }
} 