public class InputPuzzle extends Puzzle {
    /**
     * Creates a puzzle from a string of numbers
     */
    public boolean createPuzzle(String sPuzzle){
        Integer[] puzzle = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int emptyPos = -1;
        if(sPuzzle.length() < 9){
            System.out.println("The puzzle must contain 9 numbers!");
            return false;
        }
        for(int i = 0; i < puzzle.length; ++i){
            Integer val;
            try{
                val = Integer.parseInt(Character.toString(sPuzzle.charAt(i)));
            }catch(NumberFormatException e){
                System.out.println("The string contained an incorrect character, please try again");
                return false;
            }
            if(val == 0) emptyPos = i;
            if(val < 0){
                System.out.println("The number must be in between 0 and 8");
                return false;
            }else if(val > 8){
                System.out.println("The number must be in between 0 and 8");
                return false;
            }
            if(arrIndexOf(puzzle,val) == -1)
                puzzle[i] = val;
            else{
                System.out.println("The string has duplicate numbers");
                return false;
            }
        }
        boolean canSolve = checkSolvable(puzzle);
        if(canSolve == false) System.out.println("This puzzle cannot be solved");
        else{
            System.out.println("Empty Pos: " + emptyPos);
            setInitalState(puzzle);
            setInitialStateNode(new StateNode(puzzle, puzzle,0,"noop",null,emptyPos));
        }
        return canSolve;
    }
    /**
     * Function for createPuzzle, used to find the index of `searchFor` within the array `arr`
     */
    private int arrIndexOf(Integer[] arr, Integer searchFor){
        for(int i = 0; i < arr.length; ++i){
            if(arr[i].equals(searchFor)) return i;
        }
        return -1;
    }
}