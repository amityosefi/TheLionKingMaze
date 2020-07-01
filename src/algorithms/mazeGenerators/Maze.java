package algorithms.mazeGenerators;
import java.io.Serializable;
import java.util.Random;



public class Maze implements Serializable {

    private int rows;
    private int columns;
    private int[][] arr;
    private Position start;
    private Position end;
    public static final int mazeHeaderLen = 12;

    public Maze(int Integer, int Rows, int Columns) {

        rows = Rows;
        columns = Columns;
        arr = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (Integer == 0) //set the maze with walls (1) or empty (0)
                    this.arr[i][j] = 0;
                else
                    this.arr[i][j] = 1;
            }
        }
        start = new Position();
        end = new Position();
        setRandomStart();
        arr[start.getRowIndex()][start.getColumnIndex()] = 0;
    }

    public Maze(byte[] Array)
    {
        rows = (((int)Array[1]) & 0xFF) + (((int)Array[0] &0xFF) << 8);
        columns = ((int)Array[3] & 0xFF) + (((int)Array[2] & 0xFF) << 8);
        start = new Position(((int)Array[5] & 0xFF) + (((int)Array[4] & 0xFF) << 8),
                ((int)Array[7] & 0xFF) + (((int)Array[6] & 0xFF) << 8));
        end = new Position(((int)Array[9] & 0xFF) + (((int)Array[8] & 0xFF) << 8),
                ((int)Array[11] & 0xFF) + (((int)Array[10] & 0xFF) << 8));
/*
        System.out.println("Maze(array): rows= " + rows + " cols= " + columns);
        System.out.println("start: row = " + start.getRowIndex() + " col= " + start.getColumnIndex());
        System.out.println("end: row = " + end.getRowIndex() + " col= " + end.getColumnIndex());
*/
        arr = new int[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                arr[i][j] = Array[i*columns + j + mazeHeaderLen];

//        arr[start.getRowIndex()][start.getColumnIndex()] = 0;

    }

    public Position getStartPosition() { return start; }
    public Position getGoalPosition() { return end; }
    public int getRows() { return rows; }
    public int getColumns() {
        return columns;
    }
    public int getElement(int row, int column) {return arr[row][column];}
    protected void setElement(int integer, int row, int column) { arr[row][column] = integer; }

    public void print() {
        int stRow = start.getRowIndex();
        int stCol = start.getColumnIndex();
        int endRow = end.getRowIndex();
        int endCol = end.getColumnIndex();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == stRow && j == stCol)
                    System.out.print("S");
                else if (i == endRow && j == endCol)
                    System.out.print("E");
                else
                    System.out.print(String.valueOf(arr[i][j]));
            }
            System.out.println("");
        }
    }

    protected void setEnd(Position endP) {
        end.setRowIndex(endP.getRowIndex());
        end.setColIndex(endP.getColumnIndex());
    }

    public int arrayLen()
    {
        return arr.length;
    }

    private void setRandomStart() {

        Random rand = new Random();
        int Srow = rand.nextInt(rows);
        int Scol = rand.nextInt(columns);

        start.setRowIndex(Srow);
        start.setColIndex(Scol);
    }

    public byte[] toByteArray() {
        byte[] Array = new byte[rows * columns + mazeHeaderLen];
        // The first 12 bytes are Maze header
        Array[0] = (byte) ((rows & 0xff00) >> 8);
        Array[1] = (byte) (rows & 0xff);
        Array[2] = (byte) ((columns & 0xff00) >> 8);
        Array[3] = (byte) (columns & 0xff);
        Array[4] = (byte) ((start.getRowIndex() & 0xff00) >> 8);
        Array[5] = (byte) (start.getRowIndex() & 0xff);
        Array[6] = (byte) ((start.getColumnIndex() & 0xff00) >> 8);
        Array[7] = (byte) (start.getColumnIndex() & 0xff);
        Array[8] = (byte) ((end.getRowIndex() & 0xff00) >> 8);
        Array[9] = (byte) (end.getRowIndex() & 0xff);
        Array[10] = (byte) ((end.getColumnIndex() & 0xff00) >> 8);
        Array[11] = (byte) (end.getColumnIndex() & 0xff);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                Array[i*columns + j + mazeHeaderLen] = (byte) arr[i][j];

        return Array;
    }

}