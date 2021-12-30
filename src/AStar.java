import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AStar {
    private PriorityQueue<StateNode> fringe;
    private HashSet<StateNode> exploredSet;
    private final Integer[] goal = {0,1,2,3,4,5,6,7,8};
    /**
     * A* algorithm that uses either the misplaced tile or the sum of distance heuristic.
     * The priority queue and the lambda function is used to sort costs. It will also expand children
     * of the smallest cost first and add it to the explored set, preventing duplicate states
     */
    public StateNode runAStar(StateNode initialState, boolean isH1, boolean print){
        int searchCost = 0;
        exploredSet = new HashSet<>();
        if(isH1){
            fringe = new PriorityQueue<>((final StateNode o1, final StateNode o2) -> (o1.getCost()+misplacedTiles(o1)) - (o2.getCost()+misplacedTiles(o2)));
        }else{
            fringe = new PriorityQueue<>((final StateNode o1, final StateNode o2) -> (o1.getCost()+sumOfDistance(o1)) - (o2.getCost()+sumOfDistance(o2)));
        }
        fringe.add(initialState);
        while(!fringe.isEmpty()){
            StateNode current = fringe.poll();
            exploredSet.add(current);
            if(print == true){
                System.out.println(current);
                System.out.println("Step Cost: " + current.getCost() + " - Search Cost: " + searchCost );
            }
            if(Arrays.equals(current.getCurrentState(), goal)){
                System.out.println("GOAL FOUND AT SEARCH COST OF " + searchCost );
                return current;
            }
            ArrayList<StateNode> children = current.expandCurrentNode();
            for(int i = 0; i < children.size(); ++i){
                if(!exploredSet.contains(children.get(i))){
                    searchCost++;
                    children.get(i).setSearchCost(searchCost);
                    children.get(i).setExploredSize(exploredSet.size());
                    children.get(i).setFringeSize(fringe.size());
                    fringe.add(children.get(i));
                }
            }
        }
        return null;
    }
    /**
     * A* (h1) this function will count the number of misplaced tiles and returns the total amount of misplaced tiles
     */
    public int misplacedTiles(StateNode node){
        int misplaced = 0;
        for(int i = 0; i < node.getCurrentState().length; ++ i){
            if(node.getCurrentState()[i] != i) misplaced++;
        }
        return misplaced;
    }

    /**
     * A* (h2) this function measures the sum of the distaqnce from the tile to the goal and returns a sum of the distances
     */
    public int sumOfDistance(StateNode node){
        int sum = 0;
        for(int i = 0; i < node.getCurrentState().length; ++i){
            if(node.getCurrentState()[i] == i) continue;
            if(node.getCurrentState()[i] == 0) continue;
            int row = node.getCurrentState()[i]/3;
            int col = node.getCurrentState()[i]%3;
            int goalRow = i/3;
            int goalCol = i%3;
            sum += Math.abs(col - goalCol) +  Math.abs(row - goalRow);
        }
        return sum;
    }
}