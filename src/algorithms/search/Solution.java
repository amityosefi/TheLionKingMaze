package algorithms.search;
import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
    private ArrayList<AState> _path;

    public Solution(ArrayList<AState> path) {
        this._path = path;
    }
    public ArrayList<AState> getSolutionPath(){
        return _path;
    }
    public String toString() {
        String solStr = "";
        for (int i = 0; i < _path.size(); i++)
        {
            if (i == _path.size() - 1)
                solStr += _path.get(i);
            else
                solStr += _path.get(i) + "\n";
        }
        return solStr;
    }
}