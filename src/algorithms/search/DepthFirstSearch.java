package algorithms.search;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;

public class DepthFirstSearch extends ASearchingAlgorithm {

    @Override
    public Solution solve(ISearchable s)
    {
        AState startNode = s.getStartState();
        AState goalNode = s.getGoalState();
        HashSet<AState> nodes = new HashSet<AState>();
        Stack<AState> stack = new Stack<AState>();

        stack.add(startNode);
        nodes.add(startNode);
        _nodesVisited ++;
        ArrayList<AState> Dpath = DSearch(s, stack, nodes, goalNode);

        Solution sol = new Solution(Dpath);
        return sol;
    }

    private ArrayList<AState> DSearch(ISearchable s, Stack<AState> stack,HashSet<AState> nodes, AState goalNode) {
        ArrayList<AState> visitedN;
        ArrayList<AState> path = new ArrayList<AState>();
        while (!stack.isEmpty()) {
            AState current = stack.pop();
            Boolean found = true;
            Boolean isPoped = true;
            while (found == true) { //found a possible way - then continue the search on neighbors
                visitedN = s.getAllPossibleStates(current);
                found = false;
                AState neighbor;
                for (int i = 0; i < visitedN.size(); i++) {
                    neighbor = visitedN.get(i);
                    if (nodes.contains(neighbor)) //if already visited the node - so it's not relevant anymore
                        continue;   // To next i in for
                    _nodesVisited ++;
                    nodes.add(neighbor);
                    if (isPoped) { //if this point should be a part of the path
                        isPoped = false;
                        stack.push(current); }
                    stack.push(neighbor);
                    if (neighbor.equals(goalNode)) {
                        setPathFromStack(stack, path); //found the goal state - build the path
                        return path; }
                    found = true;
                    current = neighbor;
                    break; }
            }
        }
        path.clear(); //didn't find a path - clear the array list
        return path;
    }

    private void setPathFromStack(Stack<AState> stack, ArrayList<AState> path)
    {
        Stack<AState> temp = new Stack<AState>();
        while (!stack.isEmpty()) {
            temp.push(stack.pop());
        }
        while (!temp.isEmpty()) {
            path.add(temp.pop());
        }
    }

    @Override
    public String getName() {
        return "DepthFirstSearch";
    }
}