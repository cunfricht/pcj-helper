import java.util.ArrayList;

public class GameState {
    public static long lastID;
    public final long stateID;
    public final long cameFromState;
    public final int cardPlayed; // 0 represents wild - either joker or start of game
    public final int colPlayed; // column the card was played in
    public final ArrayList<ArrayList<Integer>> board;

    /**
     * Creates a new GameState, given the current board. This constructor should only be used
     * to initialize the first board.
     *
     * @param board the cards on the board, from left-to-right and top-of-stack to bottom
     */
    GameState(ArrayList<ArrayList<Integer>> board) {
        GameState.lastID = 0;
        this.stateID = GameState.lastID;
        this.cameFromState = -1;
        this.cardPlayed = 0;
        this.colPlayed = -1;
        this.board = board;
    }

    /**
     * Creates a new GameState, advancing from the specified GameState
     *
     * @param prevState the GameState that this GameState came from
     * @param colPlayed the column containing the card played to arrive at this GameState
     */
    GameState(GameState prevState, int colPlayed) {
        this.stateID = ++GameState.lastID;
        this.cameFromState = prevState.stateID;
        this.cardPlayed = prevState.getTopCardInCol(colPlayed);
        this.colPlayed = colPlayed;
        this.board = prevState.playCard(colPlayed);
    }

    /**
     * Gets the value of the top-most card in the given column.
     *
     * @param col the column, 0-based from left to right
     * @return the value of the top-most card
     */
    private int getTopCardInCol(int col) {
        if (this.board.get(col).size() > 0) {
            return this.board.get(col).get(0);
        }
        else {
            throw new IllegalStateException("Column " + Integer.toString(col) + " is empty.");
        }
    }


    private ArrayList<ArrayList<Integer>> playCard(int colPlayed) {
        if (this.canPlayCard(colPlayed)) {
            ArrayList<ArrayList<Integer>> newBoard =
                new ArrayList<ArrayList<Integer>>(this.board.size());
            for (int i = 0; i < this.board.size(); i++){
                ArrayList<Integer> curCol = this.board.get(i);
                ArrayList<Integer> newCol;
                if (curCol.size() > 0) {
                    newCol = new ArrayList<Integer>
                        (curCol.subList(1, curCol.size()));
                    if (i != colPlayed) {
                        newCol.add(0, curCol.get(0));
                    }
                }
                else {
                    newCol = new ArrayList<>();
                }
                newBoard.add(newCol);
            }
            return newBoard;
        }
        else {
            throw new IllegalStateException("Cannot play card in column " +
                Integer.toString(colPlayed) + ".");
        }
    }

    private boolean canPlayCard(int col) {
        if (this.cardPlayed == 0) {
            return true;
        }
        else {
            int cardVal = this.getTopCardInCol(col);
            int valDiff = Math.abs(this.cardPlayed - cardVal);
            if (valDiff == 1 || valDiff == 12) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public ArrayList<Integer> playableCols() {
        ArrayList<Integer> playableCols = new ArrayList<>();
        for (int i = 0; i < this.board.size(); i++) {
            ArrayList<Integer> curCol = this.board.get(i);
            if (curCol.size() > 0 && this.canPlayCard(i)) {
                playableCols.add(i);
            }
        }
        return playableCols;
    }

    public void printBoard() {
        ArrayList<ArrayList<String>> boardAsStrings = new ArrayList<ArrayList<String>>();
        int maxColLength = 0;
        for (ArrayList<Integer> col : this.board) {
            ArrayList<String> cards = new ArrayList<String>(col.size());
            for (int cardVal : col) {
                String cardAsString = Helper.cardValToStr(cardVal) + " ";
                if (cardAsString.length() == 2) {
                    cardAsString += " ";
                }

                cards.add(cardAsString);
            }
            if (cards.size() > maxColLength) {
                maxColLength = cards.size();
            }
            boardAsStrings.add(cards);
        }

        for (int row = maxColLength - 1; row >= 0; row--) {
            for (int col = 0; col < boardAsStrings.size(); col++) {
                if (boardAsStrings.get(col).size() > row) {
                    System.out.print(boardAsStrings.get(col).get(row));
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }
}
