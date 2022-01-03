import java.util.ArrayList;
import java.util.Arrays;

public class StateNode {
    private StateNode predecessor;
    final private Integer[] initalState;
    final private Integer[] currentState;
    final private int cost;
    final private String actionTaken;
    final private int emptyPosition;
    private int fringeSize = 0;
    private int exploredSize = 0;
    private int searchCost = 0;
    public StateNode(){
        initalState = new Integer[9];
        Arrays.fill(initalState,-1);
        currentState = initalState;
        cost = 0;
        actionTaken = "none";
        emptyPosition = 0;
    }

    public StateNode(Integer[] initState, Integer[] currState, int cost, String actionTaken, StateNode predecssor, int emptyPos){
        initalState = initState;
        currentState = currState;
        this.cost = cost;
        this.actionTaken = actionTaken;
        this.predecessor = predecssor;
        emptyPosition = emptyPos;
    }
    public void setFringeSize(int fSize){
        fringeSize = fSize;
    }
    public void setExploredSize(int eSize){
        exploredSize = eSize;
    }
    public void setSearchCost(int searchCost){
        this.searchCost = searchCost;
    }
    public int getSearchCost(){
        return searchCost;
    }
    public StateNode getPredecessor(){
        return predecessor;
    }
    public Integer[] getInitialState(){
        return initalState;
    }
    public Integer[] getCurrentState(){
        return currentState;
    }
    public int getCost(){
        return cost;
    }
    public String getAction(){
        return actionTaken;
    }
    public int getEmptyPosition(){
        return emptyPosition;
    }
    public int getFringeSize(){
        return fringeSize;
    }
    public int getExploredSize(){
        return exploredSize;
    }
    /**
     * Checks if the action will keep the empty pos in bounds of the state
     * Moving up or down requires the index to be in bounds for i+-3
     * Moving left or right requires the index to be i%3!=0 and i+1%3!=0
     */
    public boolean inBounds(String action){
        Integer[] currentBoard = currentState;
        int emptyPos = emptyPosition;
        switch(action){
            case "up":
                if(emptyPos - 3 < 0) return false;
                break;
            case "down":
                if(emptyPos + 3 >= currentBoard.length) return false;
                break;
            case "left":
                if(emptyPos == 0) return false;
                if(emptyPos%3 == 0) return false;
                break;
            case "right":
                if(emptyPos+1%3 == 0) return false;
                break;
            default:
                return false;
        }
        return true;
    }
    /**
     * Function that generates a child node given specific action; this will return the child
     * node with the empty tile moved and an increased step cost.
     */
    public StateNode generateNode(String action){
        StateNode node;
        Integer[] newState = currentState.clone();
        int newEmpty = emptyPosition;
        switch(action){
            case "up":
                if(emptyPosition - 3 >= 0) {
                    newState = swap(newState, emptyPosition, emptyPosition-3);
                    newEmpty = emptyPosition - 3;
                }
                break;
            case "down":
                if(emptyPosition + 3 <= currentState.length){
                    newState = swap(newState, emptyPosition, emptyPosition+3);
                    newEmpty = emptyPosition + 3;
                }
                break;
            case "left":
                if(emptyPosition == 0) break;
                if(emptyPosition%3 != 0){
                    newState = swap(newState, emptyPosition, emptyPosition-1);
                    newEmpty = emptyPosition - 1;
                }
                break;
            case "right":
                if((emptyPosition+1)%3 != 0){
                    newState = swap(newState, emptyPosition, emptyPosition+1);
                    newEmpty = emptyPosition + 1;
                }
                break;
            default:
        }
        node = new StateNode(initalState, newState, getCost()+1, action, this, newEmpty);
        return node;
    }
    /**
     * Function used to swap the empty tile with another existing tile
     */
    private Integer[] swap(Integer[] arr, int pos1, int pos2){
        Integer temp = arr[pos1];
        arr[pos1] = arr[pos2];
        arr[pos2] = temp;
        return arr;
    }

    /**
     * Generates all children for a specific node by activating all possible actions
     */
    public ArrayList<StateNode> expandCurrentNode(){
        ArrayList<StateNode> successorList = new ArrayList<>();
        if(inBounds("up"))
            successorList.add(generateNode("up"));
        if(inBounds("down"))
            successorList.add(generateNode("down"));
        if(inBounds("left"))
            successorList.add(generateNode("left"));
        if(inBounds("right"))
            successorList.add(generateNode("right"));
        return successorList;
    }

    /**
     * This hashcode resembles the current state of the current board using the arrays build in the hash code fucntion
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(currentState);
    }

    /**
     * If two nodes are equal then the current state of the board is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StateNode other = (StateNode) obj;
        return Arrays.deepEquals(this.currentState, other.currentState);
    }

    /**
     * Functions that prints out puzzle in the form of a matrix
     */
    @Override
    public String toString(){
        String board = "";
        for(int i = 0; i < currentState.length; ++i){
            if(i != 0 && i%3 == 0) board += "\n";
            board += currentState[i] + " ";
        }
        return board;
    }
}