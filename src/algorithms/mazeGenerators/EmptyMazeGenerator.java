package algorithms.mazeGenerators;

import java.util.Random;

public class EmptyMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int rows, int columns) {

        Maze maze = new Maze(0, rows, columns);
        Random rand = new Random();

        //set random goal position
        int Srow = maze.getStartPosition().getRowIndex();
        int Scol = maze.getStartPosition().getColumnIndex();
        int Erow, Ecol;
        do {
            Erow = rand.nextInt(rows);
            Ecol = rand.nextInt(columns);
        } while (Erow == Srow && Ecol == Scol);
        Position goalP = new Position(Erow, Ecol);
        maze.setEnd(goalP);
        return maze;

    }
}