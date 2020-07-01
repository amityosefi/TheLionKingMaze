package algorithms.search;

public class MazeState extends AState{
    private int _row;
    private int _col;

    public MazeState(int row, int col) {
        super();
        _row = row;
        _col = col;
    }

    @Override
    public boolean equals(AState a) {
        MazeState aMaze = (MazeState)a;
        if ((aMaze._row == this._row) && (aMaze._col == this._col))
            return true;
        return false;
    }

    public int getRow() {
        return _row;
    }
    public int getCol() {
        return _col;
    }
    public String toString() {
        return "{" + _row + ',' + _col + "}";
    }
}
