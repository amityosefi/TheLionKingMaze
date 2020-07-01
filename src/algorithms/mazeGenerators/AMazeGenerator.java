package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{

    public abstract Maze generate(int row,int col);

    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long start_time = System.currentTimeMillis();
        generate(rows, columns);
        long end_time = System.currentTimeMillis();
        return (end_time - start_time);
    }
}
