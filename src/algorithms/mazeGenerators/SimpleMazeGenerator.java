package algorithms.mazeGenerators;

import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int rows, int columns) {

        Random rand = new Random();
        Maze maze = new Maze(0, rows, columns);

        // Set vertical walls
        int startCol = (maze.getStartPosition().getColumnIndex() + 1) % 2;
        for (int i = startCol; i < columns; i += 2) { // wall maze capacity is about columns/2
            int wallLength = 1 + rand.nextInt(rows - 1);
            int startIndex = rand.nextInt(rows - wallLength + 1);
            for (int j = startIndex; j < startIndex + wallLength; j++)
                maze.setElement(1, j, i); }

        //set random goal position
        int Srow = maze.getStartPosition().getRowIndex();
        int Scol = maze.getStartPosition().getColumnIndex();
        int Erow, Ecol;
        do {
            Erow = rand.nextInt(rows);
            Ecol = rand.nextInt(columns);
        } while ((maze.getElement(Erow, Ecol) != 0) || (Erow == Srow && Ecol == Scol));

        Position goalP = new Position(Erow, Ecol);
        maze.setEnd(goalP);
        return maze;
    }
}
