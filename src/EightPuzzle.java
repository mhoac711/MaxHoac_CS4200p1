import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EightPuzzle {
    public static void main(String[] args) {
        boolean goodInput = false;
        Integer userInput = 1;
        String start = "8-Puzzle Problem\n";
        start += "Please select an option:\n";
        start += "1) Random Puzzle\n2) User-Defined Puzzle\n3) Exit";
        while(!goodInput){
            System.out.println(start);
            Scanner scanner = new Scanner(System.in);
            String badInput = scanner.nextLine();
            try{
                userInput = Integer.parseInt(badInput);
                if(userInput > -1 && userInput <=4) break;
                else
                    System.out.println("Input is not a valid option");
            }catch(NumberFormatException e) {
                System.out.println("Input is not a number. Please try again.");
            }
        }
        EightPuzzle test = new EightPuzzle();
        switch (userInput) {
            case 1:
                test.randomPuzzle();
                break;
            case 2:
                test.userInputPuzzle();
                break;
            case 4:
                System.exit(0);
            default:
                break;
        }
    }
    private AStar aStar = new AStar();
    public void randomPuzzle(){
        Map<Integer,ArrayList<SearchData>> runtimeData = new TreeMap<>();
        int timesRun = -1;
        System.out.println("How many times do you want to run this?");
        Scanner scanner = new Scanner(System.in);
        while(timesRun < 1){
            String num = scanner.nextLine();
            try{
                timesRun = Integer.parseInt(num);
            }catch(NumberFormatException e){
                System.out.println("That was not a number.");
                timesRun = -1;
            }
            if(timesRun < 1) System.out.println("How many times do you want to run this? (Must be greater than 0)");
        }
        File random = new File(timesRun + "_Random_Test_Cases.txt");
        BufferedWriter bw = null;
        try {
            random.createNewFile();
            bw = new BufferedWriter(new FileWriter(random));
        } catch (IOException ex) {

        }
        for(int i = 0; i < timesRun; ++i){
            Puzzle puzzle = new RandomPuzzle();
            try {
                bw.write(puzzle.getInitialStateNode().toString().replace(" ","").replace("\n",""));
                bw.newLine();
            } catch (IOException ex) {
            }
            SearchData compute = solve(puzzle.getInitialStateNode());
            if(!runtimeData.containsKey(compute.depth)){
                runtimeData.put(compute.depth, new ArrayList<>());
            }
            runtimeData.get(compute.depth).add(compute);
        }
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(EightPuzzle.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("d  | Total Cases | H1 Search Cost | H1 Time | H2 Search Cost | H2 Time");
        runtimeData.entrySet().stream().forEach((entry) -> {
            int h1AvgCost=0,h1AvgTime=0,h2AvgCost=0,h2AvgTime=0,total=entry.getValue().size();
            for(int i = 0; i < entry.getValue().size(); ++i){
                SearchData data = entry.getValue().get(i);
                h1AvgCost += data.searchCostH1;
                h1AvgTime += data.totalTimeH1;
                h2AvgCost += data.searchCostH2;
                h2AvgTime += data.totalTimeH2;
            }

            System.out.println(entry.getKey() +  " |\t " + total + " \t\t|\t " + (h1AvgCost/total) + "\t\t|\t " + (h1AvgTime/total)
                    + " ms |\t " + (h2AvgCost/total) + "\t\t|\t " + (h2AvgTime/total) + " ms");
        });
    }
    public void userInputPuzzle(){
        Puzzle puzzle = new InputPuzzle();
        boolean madePuzzle = false;
        while(!madePuzzle){
            System.out.println("Enter a new puzzle:");
            Scanner scanner = new Scanner(System.in);
            String sPuzzle = "";
            String r1 = scanner.nextLine().replace(" ", "");
            if(r1.length() == 9){
                sPuzzle = r1;
            }else{
                String r2 = scanner.nextLine().replace(" ", "");
                String r3 = scanner.nextLine().replace(" ", "");
                sPuzzle = r1.replace("\n", "") + r2.replace("\n", "") + r3.replace("\n", "");
            }
            madePuzzle = puzzle.createPuzzle(sPuzzle);
        }
        StateNode init = puzzle.getInitialStateNode();
        SearchData compute = solve(init);
        System.out.println("d  | Total Cases | H1 Search Cost | H1 Time | H2 Search Cost | H2 Time");
        System.out.println(compute.depth + " |\t "  + 1 + " \t\t|\t" + compute.searchCostH1 + " \t\t|\t " + compute.totalTimeH1 + " ms |\t " + compute.searchCostH2 + " \t\t|\t " + compute.totalTimeH2 + "ms") ;
    }


    private SearchData solve(StateNode init){
        System.out.println(init);
        System.out.println("SOLVING PUZZLE WITH H1");
        long start1 = System.currentTimeMillis();
        StateNode goalNode1 = aStar.runAStar(init, true, true);
        long end1 = System.currentTimeMillis();
        long total1 = end1 - start1;
        System.out.println("SOLVING PUZZLE WITH H2");
        long start2 = System.currentTimeMillis();
        StateNode goalNode2 = aStar.runAStar(init, false, true);
        long end2 = System.currentTimeMillis();
        long total2 = end2 - start2;
        System.out.println("FINISHED!");
        System.out.println("\nSolved Using H1\nDepth: " + goalNode1.getCost() + "\nSearch Cost: " + goalNode1.getSearchCost() + "\nTotal Time: " + total1 + " ms");
        System.out.println("\nSolved Using H2\nDepth: " + goalNode2.getCost() + "\nSearch Cost: " + goalNode2.getSearchCost() + "1\nTotal Time: " + total2 + " ms"+"\n");
        if(goalNode1.getCost() != goalNode2.getCost()){
            System.out.println(goalNode1.getCost() + " != " + goalNode2.getCost());
            System.out.println("The depths calculated from the heuristics are not the same!");
            System.exit(0);
        }
        return new SearchData(goalNode1.getCost(),goalNode1.getSearchCost(),total1,goalNode2.getSearchCost(), total2);
    }

    /**
     * This will store the data from the puzzle and will be used to calculate averages
     */
    private class SearchData {
        public int depth;
        public int searchCostH1;
        public long totalTimeH1;
        public int searchCostH2;
        public long totalTimeH2;
        public SearchData(int d, int sCostH1, long tTimeH1, int sCostH2, long tTimeH2){
            depth = d;
            searchCostH1 = sCostH1;
            totalTimeH1 = tTimeH1;
            searchCostH2 = sCostH2;
            totalTimeH2 = tTimeH2;
        }
    }
}
