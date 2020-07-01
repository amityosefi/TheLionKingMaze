package algorithms.search;

import java.util.PriorityQueue;

public class BestFirstSearch extends BreadthFirstSearch {

    public BestFirstSearch(){
        queue = new PriorityQueue<AState>((o1, o2) -> o1.getCost() - o2.getCost());
    }

    public String getName(){
        return "BestFirstSearch";
    }
}