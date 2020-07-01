package algorithms.mazeGenerators;

import java.util.Random;
import java.util.Stack;

public class MyMazeGenerator extends AMazeGenerator {
    private int maxStackLen;


    public Maze generate(int rows, int columns) {

          Maze maze = new Maze(1, rows, columns);

        if ((rows < 4) || (columns < 4))
        {
            //this is a simple maze - generate it by simple generator
            IMazeGenerator gen = new SimpleMazeGenerator();
            maze = gen.generate(rows, columns);
            return maze;
        }

        Random rand = new Random();
        Position startP = maze.getStartPosition();
        Stack<Position> stack = new Stack<Position>();
        stack.push(startP);

        maxStackLen = 0;
        Position goalP = new Position();
        Position pos = new Position();

        while (!stack.empty()) {
            buildMaze(maze, rows, columns, stack, rand, pos, goalP);
        }

        maze.setEnd(goalP);
        return maze;
    }

    private void buildMaze(Maze maze, int rows, int columns, Stack<Position> stack, Random rand, Position pos, Position goalP)
    {
        boolean found;
        Position tmp = stack.pop();
        pos.setRowIndex(tmp.getRowIndex());
        pos.setColIndex(tmp.getColumnIndex());

        do {
            int num = rand.nextInt(4); //choose between 4 options - right, left, up, down
            found = false;

            int r = pos.getRowIndex();
            int c = pos.getColumnIndex();

            for (int i = 0; i < 4; i++) {
                found = chooseDirection(maze, rows, columns, pos, r, c, num);
                if (found == true) {
                    stack.push(new Position(pos.getRowIndex(), pos.getColumnIndex()));
                    int stackLen = stack.size();
                    if (stackLen > maxStackLen) {
                        maxStackLen = stackLen;
                        goalP.setRowIndex(pos.getRowIndex());
                        goalP.setColIndex(pos.getColumnIndex());
                    }
                    break; // //if found a possible direction, then break from for (which presents the 4 options)
                }
                else {
                    num = (num + 1) % 4; //if can't take the specific choice, try other - different than the last
                }
            }
        }  while (found == true);
    }

    private Boolean chooseDirection (Maze maze, int rows, int columns, Position pos, int r, int c, int num)
    {
        Boolean success = false;
        switch (num)
        {
            case 0: //go right
            {
                success = goRight(columns, maze, pos, r, c);
                break;
            }
            case 1: //go left
            {
                success = goLeft(maze, pos, r, c);
                break;
            }
            case 2: //go down
            {
                success = goDown(rows, maze, pos, r, c);
                break;
            }
            case 3: //go up
            {
                success = goUp(maze, pos, r, c);
                break;
            }
        }
        return success;
    }
    private boolean goLeft(Maze maze, Position pos, int r, int c)
    {
        if ((c-2) < 0)
            return false;

        if (maze.getElement(r, c-2) == 0)
            return false;

        // It is legal to go left. Row stays the same
        maze.setElement(0, r, c - 1);
        maze.setElement(0, r, c - 2);
        pos.setColIndex(c - 2);
        return true;
    }

    private boolean goRight(int columns, Maze maze, Position pos, int r, int c)
    {
        if ((c + 2) >= columns)
            return false;

        if (maze.getElement(r, c+2) == 0)
            return false;

        // It is legal to go right. Row stays the same
        maze.setElement(0, r, c + 1);
        maze.setElement(0, r, c + 2);
        pos.setColIndex(c + 2);
        return true;
    }

    private boolean goDown(int rows, Maze maze, Position pos, int r, int c)
    {
        if ((r + 2) >= rows)
            return false;

        if (maze.getElement(r+2, c) == 0)
            return false;

        // It is legal to go down. Column stays the same
        maze.setElement(0, r + 1, c);
        maze.setElement(0, r + 2, c);
        pos.setRowIndex(r + 2);
        return true;
    }

    private boolean goUp(Maze maze, Position pos, int r, int c)
    {
        if ((r -2) < 0)
            return false;

        if (maze.getElement(r-2, c) == 0)
            return false;

        // It is legal to go up. Column stays the same
        maze.setElement(0, r - 1, c);
        maze.setElement(0, r - 2, c);
        pos.setRowIndex(r - 2);
        return true;
    }
}
