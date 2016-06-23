import java.util.*;

public class Helper {
    public static String cardValToStr(int val) {
        if (val >= 2 && val <= 10) {
            return Integer.toString(val);
        }
        else if (val == 1) {
            return "A";
        }
        else if (val == 11) {
            return "J";
        }
        else if (val == 12) {
            return "Q";
        }
        else if (val == 13) {
            return "K";
        }
        else {
            throw new IllegalArgumentException("Card value " + val + " is invalid.");
        }
    }

    private static boolean isInteger(String expr) {
        try {
            Integer.parseInt(expr);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    private static ArrayList<Integer> parseColumn(String cards) throws IllegalArgumentException {
        ArrayList<String> listOfCards = new ArrayList<String>(Arrays.asList(cards.split(" ")));
        ArrayList<Integer> column = new ArrayList<Integer>(listOfCards.size());

        for (String card : listOfCards) {
            if (isInteger(card)) {
                int cardVal = Integer.parseInt(card);
                if (cardVal < 2 || cardVal > 10) {
                    throw new IllegalArgumentException("Invalid card: " + card);
                }
                column.add(cardVal);
            }
            else {
                if (card.equalsIgnoreCase("A")) {
                    column.add(1);
                }
                else if (card.equalsIgnoreCase("J")) {
                    column.add(11);
                }
                else if (card.equalsIgnoreCase("Q")) {
                    column.add(12);
                }
                else if (card.equalsIgnoreCase("K")) {
                    column.add(13);
                }
                else {
                    throw new IllegalArgumentException("Invalid card: " + card);
                }
            }
        }

        return column;
    }

    public static void main(String[] args) {
        GameState initialState;

        ArrayList<ArrayList<Integer>> board;
        int numCols = -1;
        int tempCols;

        Scanner in = new Scanner(System.in).useDelimiter("[\\r\\n]+");

        boolean passesCols = false;

        while (!passesCols) {
            System.out.print("Enter the number of columns of cards: ");
            try {
                if (!in.hasNextInt()) {
                    in.next();
                    throw new IllegalArgumentException("The number of columns must be a positive "
                        + "integer.");
                }
                else {
                    tempCols = in.nextInt();
                    if (tempCols < 1) {
                        throw new IllegalArgumentException("The number of columns must be a "
                            + "positive integer.");
                    }
                }
                numCols = tempCols;
                passesCols = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        in.nextLine();
        board = new ArrayList<ArrayList<Integer>>(numCols);

        for (int i = 1; i <= numCols; i++) {
            System.out.println("Enter the cards, space-separated, for column " + Integer.toString(i)
                + ". Order them from the top (i.e. playable) card to the bottom (i.e. most"
                + " covered) card.");

            String curCol = in.nextLine();

            try {
                board.add(Helper.parseColumn(curCol));
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Valid card values are A 2-10 Q J K");
                i -= 1;
            }
        }

        ArrayList<ArrayList<GameState>> statesByDepth = new ArrayList<>();
        HashMap<Long, GameState> statesByID = new HashMap<>();

        ArrayList<GameState> workList = new ArrayList<>();


        initialState = new GameState(board);
        System.out.println("Current state:");
        initialState.printBoard();

        System.out.println("Solving...");

        workList.add(initialState);
        statesByID.put(0L, initialState);

        int curDepth = 0;
        while (workList.size() > 0) {
            ArrayList<GameState> newWorkList = new ArrayList<>();
            statesByDepth.add(curDepth, new ArrayList<GameState>());
            for (GameState next : workList) {
                statesByDepth.get(curDepth).add(next);
                ArrayList<Integer> colsToTry = next.playableCols();
                for (Integer col : colsToTry) {
                    GameState nextState = new GameState(next, col);
                    newWorkList.add(nextState);
                    statesByID.put(nextState.stateID, nextState);
                }
            }
            curDepth = curDepth + 1;
            workList = new ArrayList<>(newWorkList);
        }

        System.out.println("Solved. Max number of cards playable: " +
            Integer.toString(curDepth - 1));
        System.out.println("Cards to play:");
        GameState curState = statesByDepth.get(statesByDepth.size() - 1).get(0);
        long curID = curState.stateID;

        String output = "";
        while (curID > 0) {
            String thisOutput = cardValToStr(curState.cardPlayed) + " in col " +
                (curState.colPlayed + 1);
            curID = curState.cameFromState;
            curState = statesByID.get(curID);
            if (output == "") {
                thisOutput += ".";
            }
            else {
                thisOutput += ", ";
            }
            output = thisOutput + output;
        }
        System.out.print("Play card " + output);
    }
}
