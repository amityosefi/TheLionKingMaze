package algorithms.search;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {
    protected int _nodesVisited;

    public ASearchingAlgorithm(){
        _nodesVisited = 0;
    }
    public int getNumberOfNodesEvaluated() { return _nodesVisited; }
}