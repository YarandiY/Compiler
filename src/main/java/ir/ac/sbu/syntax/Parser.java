package ir.ac.sbu.syntax;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

enum Action {
    ERROR, SHIFT, GOTO, PUSH_GOTO, REDUCE, ACCEPT
}

class LLCell {
    private Action action;
    private int target;
    private String function;

    public LLCell(Action action, int target, String function) {
        this.action = action;
        this.target = target;
        this.function = function;
    }

    public Action getAction() {
        return action;
    }

    public int getTarget() {
        return target;
    }

    public String getFunction() {
        return function;
    }
}


public class Parser {
    public static String TABLE_DELIMITER = ",";

    private Lexical lexical;
    private CodeGenerator codeGenerator;

    private String[] symbols;
    private LLCell[][] parseTable;
    private int startNode;
    private Deque<Integer> parseStack = new ArrayDeque<>();

    private List<String> recoveryState;

    public Parser(Lexical lexical, CodeGenerator codeGenerator, String nptPath) {
        this.lexical = lexical;
        this.codeGenerator = codeGenerator;
        this.recoveryState = new ArrayList<>();

        if (!Files.exists(Paths.get(nptPath))) {
            throw new RuntimeException("Parser table not found: " + nptPath);
        }

        try {
            Scanner in = new Scanner(new FileInputStream(nptPath));
            String[] tmpArr = in.nextLine().trim().split(" ");
            int rowSize = Integer.parseInt(tmpArr[0]);
            int colSize = Integer.parseInt(tmpArr[1]);
            startNode = Integer.parseInt(in.nextLine());
            symbols = in.nextLine().trim().split(TABLE_DELIMITER);

            parseTable = new LLCell[rowSize][colSize];
            for (int i = 0; i < rowSize; i++) {
                tmpArr = in.nextLine().trim().split(TABLE_DELIMITER);
                if (tmpArr.length != colSize) {
                    throw new RuntimeException("Invalid .npt file. File contains rows with length" +
                            " bigger than column size.");
                }

                for (int j = 0; j < colSize; j++) {
                    String[] cellParts = tmpArr[j].split(" ");
                    if (cellParts.length != 3) {
                        throw new RuntimeException("Invalid .npt file. File contains cells with 3 values.");
                    }
                    parseTable[i][j] = new LLCell(Action.values()[Integer.parseInt(cellParts[0])],
                            Integer.parseInt(cellParts[1]), cellParts[2]);
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid .npt file.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to load .npt file.", e);
        }
    }

    public void parse() {
        int tokenID = nextTokenID();
        int currentNode = startNode;
        boolean accepted = false;
        while (!accepted) {
            String tokenText = symbols[tokenID];
            LLCell cell = parseTable[currentNode][tokenID];
            switch (cell.getAction()) {
                case ERROR:
                    updateRecoveryState(currentNode, tokenText);
                    generateError("Unable to parse input");
                case SHIFT:
                    doSemantics(cell.getFunction());
                    tokenID = nextTokenID();
                    currentNode = cell.getTarget();
                    recoveryState.clear();
                    break;
                case GOTO:
                    updateRecoveryState(currentNode, tokenText);
                    doSemantics(cell.getFunction());
                    currentNode = cell.getTarget();
                    break;
                case PUSH_GOTO:
                    updateRecoveryState(currentNode, tokenText);
                    parseStack.push(currentNode);
                    currentNode = cell.getTarget();
                    break;
                case REDUCE:
                    if (parseStack.size() == 0) {
                        generateError("Unable to Reduce: token=" + tokenText + " node=" + currentNode);
                    }
                    updateRecoveryState(currentNode, tokenText);
                    int graphToken = cell.getTarget();
                    int preNode = parseStack.pop();
                    doSemantics(parseTable[preNode][graphToken].getFunction());
                    currentNode = parseTable[preNode][graphToken].getTarget();
                    break;
                case ACCEPT:
                    accepted = true;
                    break;
            }
        }
    }

    private void generateError(String message) {
        for (String state : recoveryState) {
            System.err.println(state);
        }
        throw new RuntimeException(message);
    }

    private void updateRecoveryState(int currentNode, String token) {
        List<String> availableTokens = new ArrayList<>();
        LLCell[] cellTokens = parseTable[currentNode];
        for (int i = 0; i < cellTokens.length; i++) {
            if (cellTokens[i].getAction() != Action.ERROR) {
                availableTokens.add(symbols[i]);
            }
        }
        recoveryState.add("At node " + currentNode + ": current token is " + token + " but except: " + availableTokens);
    }

    private int nextTokenID() {
        String token = lexical.nextToken();
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equalsIgnoreCase(token)) {
                return i;
            }
        }
        throw new RuntimeException("Undefined token: " + token);
    }

    private void doSemantics(String semantics) {
        if (semantics.equals("NoSem")) {
            return;
        }
        semantics = semantics.substring(1);
        String[] allFunctions = semantics.split("[;]");
        for (String function : allFunctions) {
            if (function.length() > 0) {
                codeGenerator.doSemantic(function);
            }
        }
    }
}
