package Model;

        import algorithms.mazeGenerators.Maze;
        import algorithms.search.Solution;
        import java.io.File;
        import java.util.Observer;

public interface IModel {
    public void generate(int row, int col);
    public Maze getMaze();
    public void updateCharacterLocation(int direction);
    public int getRowChar();
    public int getColChar();
    public void assignObserver(Observer o);
    public void solveMaze(Maze maze);
    public Solution getSolution();
    public int getRowfinish();
    public int getColfinish();
    public void SaveFile(File file);
    public void LoadFile(File file);
    public void CloseApp();
    public void setRowChar(int r);
    public void setColChar(int c);
}