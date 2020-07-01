package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm {
    protected Queue queue;

    public BreadthFirstSearch() {
        queue = new LinkedList();
    }

    public Solution solve(ISearchable s)
    {
        queue.clear();
        AState startNode = s.getStartState();
        AState goalNode = s.getGoalState();
        ArrayList<AState> path = new ArrayList<AState>();
        HashSet<AState> nodes = new HashSet<AState>();

        startNode.setCost(0);
        queue.add(startNode);
        _nodesVisited++;
        path.add(startNode);
        nodes.add(startNode);
        ArrayList<AState> Bpath = BSearch(s, path, nodes, goalNode);

        Solution sol = new Solution(Bpath);
        return sol;
    }

    private ArrayList<AState> BSearch(ISearchable s, ArrayList<AState> path, HashSet<AState> nodes, AState goalNode)
    {
        ArrayList<AState> visitedN;
        while (!queue.isEmpty()) {
            AState current = (AState) queue.remove();
            visitedN = s.getAllPossibleStates(current);
            AState neighbor;
            for (int i = 0; i < visitedN.size(); i++) {
                neighbor = visitedN.get(i);
                if(nodes.contains(neighbor)) //if already visited the node - so it's not relevant anymore
                    continue; // To next i in for
                _nodesVisited++;
                nodes.add(neighbor);
                queue.add( neighbor);
                if (neighbor.equals(goalNode)) {
                    setPathFromArray(neighbor, path);
                    return path;
                }
            }
        }
        path.clear(); //didn't find a path - clear the array list
        return path;
    }

    private void setPathFromArray(AState neighbor, ArrayList<AState> path)
    {
        ArrayList<AState> temp = new ArrayList<AState>();
        while (neighbor.getCameFrom() != null) {
            temp.add(neighbor);
            neighbor = neighbor.camefrom; }
        int j=0;
        while (j<temp.size()) {
            path.add((AState) temp.remove(temp.size() - 1));
        }
    }

    public String getName(){
        return "BreadthFirstSearch";
    }
}