package algorithms.search;

public interface ISearchingAlgorithm {
    Solution solve(ISearchable s);
    String getName();
    int getNumberOfNodesEvaluated();// dont sure
}