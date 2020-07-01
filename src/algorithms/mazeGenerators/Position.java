package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable {
    private int row;
    private int col;

    public Position()
    {
        this.row = 0;
        this.col = 0;
    }
    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    public String toString() { return "{" + row + ',' + col + "}"; }
    public int getRowIndex() { return row; }
    public int getColumnIndex() { return col; }
    protected void setRowIndex(int row) { this.row = row; }
    protected void setColIndex(int col) { this.col = col; }


}
