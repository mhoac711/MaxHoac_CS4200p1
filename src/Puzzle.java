public class Puzzle {
    //Fields
    private Integer[] initialState = new Integer[9];
    private StateNode initialStateNode;

    final private Integer[] goalState = {0,1,2,3,4,5,6,7,8};
    //Getters and Setters
    public void setInitalState(Integer[] state){
        initialState = state;
    }
    public void setInitialStateNode(StateNode init){
        initialStateNode = init;
    }
    public StateNode getInitialStateNode(){
        return initialStateNode;
    }
    public Integer[] getInitialState(){
        return initialState;
    }
    public Integer[] getGoalState(){
        return goalState;
    }

    public boolean createPuzzle(String sPuz){
        return false;
    }
    /**
     * Checks if the puzzle is solvable by checking the number of inversions being made
     */
    public boolean checkSolvable(Integer[] board){
        int inversions = 0;
        for(int i = 0; i < board.length-1; ++i) {
            for(int j = i+1; j < board.length; ++j) {
                //Make sure not to count the 0 tile
                if(board[i] != 0 && board[j] != 0 && board[i] > board[j])
                    inversions++;
            }
        }
        return inversions%2 == 0;
    }
}