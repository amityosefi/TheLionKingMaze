package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import static java.lang.Integer.MAX_VALUE;

import java.util.ArrayList;


public class SearchableMaze implements ISearchable {
    private Maze _maze;
    private int _rows;
    private int _cols;
    private ArrayList<AState> _successors;
    private MazeState[][] _stateArr;

    public SearchableMaze(Maze m) {
        _maze = m;
        _successors = new ArrayList<AState>();
        _rows = m.getRows();
        _cols = m.getColumns();

        //in the _stateArr we save all the maze states of the maze's nodes
        _stateArr = new MazeState[_rows][_cols];
        for (int i = 0; i < _rows; i++){
            for (int j = 0; j < _cols; j++) {
                _stateArr[i][j] = new MazeState(i, j);
                _stateArr[i][j].setCost(MAX_VALUE);
            }
        }
    }

    public AState getStartState() {
        Position sPos = _maze.getStartPosition();
        int srow = sPos.getRowIndex();
        int scol = sPos.getColumnIndex();
        return _stateArr[srow][scol];
    }

    public AState getGoalState() {
        Position ePos = _maze.getGoalPosition();
        int erow = ePos.getRowIndex();
        int ecol = ePos.getColumnIndex();
        return _stateArr[erow][ecol];
    }


    public ArrayList<AState> getAllPossibleStates(AState s) {
        MazeState m = (MazeState) s;
        int posRow = m.getRow();
        int posCol = m.getCol();
        _successors.clear();

        if (posRow > 0) { //up, up right, up left
            upCase(posRow, posCol);
        }

        if (posCol < (_cols - 1)) { //right, right up, right down
            rightCase(posRow, posCol);
        }

        if (posRow < (_rows - 1)) { //down, down right, down left
            downCase(posRow, posCol);
        }

        if (posCol > 0) { //left, left down, left up
            leftCase(posRow, posCol);
        }

        return _successors;
    }

    private void upCase(int posRow, int posCol)
    {
        if (_maze.getElement(posRow - 1, posCol) == 0) {
            if (_stateArr[posRow - 1][posCol].getCost() >= _stateArr[posRow][posCol].getCost() + 10) {
                _stateArr[posRow - 1][posCol].setCameFrom(_stateArr[posRow][posCol]);
                _stateArr[posRow - 1][posCol].setCost(10+_stateArr[posRow][posCol].getCost());
            }
            if (!_successors.contains(_stateArr[posRow - 1][posCol]))
                _successors.add(_stateArr[posRow - 1][posCol]); //up

            if (posCol < (_cols - 1))
                if (_maze.getElement(posRow -1, posCol + 1) == 0) {
                    if (_stateArr[posRow - 1][posCol + 1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow - 1][posCol + 1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow - 1][posCol + 1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow - 1][posCol+1]))
                        _successors.add(_stateArr[posRow - 1][posCol + 1]); //up right
                }

            if (posCol > 0)
                if (_maze.getElement(posRow - 1, posCol - 1) == 0) {
                    if (_stateArr[posRow - 1][posCol-1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow - 1][posCol-1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow - 1][posCol-1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow - 1][posCol-1]))
                        _successors.add(_stateArr[posRow - 1][posCol - 1]); //up left
                }
        }
    }

    private void rightCase(int posRow, int posCol)
    {
        if (_maze.getElement(posRow, posCol + 1) == 0) {
            if (_stateArr[posRow][posCol+1].getCost() >= _stateArr[posRow][posCol].getCost() + 10) {
                _stateArr[posRow][posCol+1].setCameFrom(_stateArr[posRow][posCol]);
                _stateArr[posRow][posCol+1].setCost(10+_stateArr[posRow][posCol].getCost());
            }
            if (!_successors.contains(_stateArr[posRow][posCol + 1]))
                _successors.add(_stateArr[posRow][posCol + 1]); //right

            if (posRow > 0)
                if (_maze.getElement(posRow - 1, posCol + 1) == 0 ) {
                    if (_stateArr[posRow - 1][posCol+1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow - 1][posCol+1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow - 1][posCol+1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow - 1][posCol + 1]))
                        _successors.add(_stateArr[posRow - 1][posCol + 1]); //right up
                }

            if (posRow < (_rows - 1))
                if (_maze.getElement(posRow + 1, posCol + 1) == 0) {
                    if (_stateArr[posRow + 1][posCol+1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow + 1][posCol+1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow + 1][posCol+1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow + 1][posCol+1]))
                        _successors.add(_stateArr[posRow + 1][posCol + 1]); //right down
                }
        }
    }

    private void downCase(int posRow, int posCol)
    {
        if (_maze.getElement(posRow + 1, posCol) == 0) {
            if (_stateArr[posRow+1][posCol].getCost() >= _stateArr[posRow][posCol].getCost() + 10) {
                _stateArr[posRow+1][posCol].setCameFrom(_stateArr[posRow][posCol]);
                _stateArr[posRow+1][posCol].setCost(10+_stateArr[posRow][posCol].getCost());
            }
            if (!_successors.contains(_stateArr[posRow + 1][posCol]))
                _successors.add(_stateArr[posRow + 1][posCol]); //down


            if (posCol < _cols - 1)
                if (_maze.getElement(posRow + 1, posCol + 1) == 0) {
                    if (_stateArr[posRow + 1][posCol+1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow + 1][posCol+1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow + 1][posCol+1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow + 1][posCol+1]))
                        _successors.add(_stateArr[posRow + 1][posCol + 1]); //down right
                }

            if (posCol > 0)
                if (_maze.getElement(posRow + 1, posCol - 1) == 0) {
                    if (_stateArr[posRow + 1][posCol-1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow + 1][posCol-1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow + 1][posCol-1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow + 1][posCol - 1]))
                        _successors.add(_stateArr[posRow + 1][posCol - 1]); //down left
                }
        }
    }

    private void leftCase(int posRow, int posCol)
    {
        if (_maze.getElement(posRow, posCol - 1) == 0) {
            if (_stateArr[posRow][posCol-1].getCost() >= _stateArr[posRow][posCol].getCost() + 10) {
                _stateArr[posRow][posCol-1].setCameFrom(_stateArr[posRow][posCol]);
                _stateArr[posRow][posCol-1].setCost(10+_stateArr[posRow][posCol].getCost());
            }
            if (!_successors.contains(_stateArr[posRow][posCol-1]))
                _successors.add(_stateArr[posRow][posCol - 1]); //left

            if (posRow < (_rows - 1))
                if (_maze.getElement(posRow + 1, posCol - 1) == 0) {
                    if (_stateArr[posRow + 1][posCol-1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow + 1][posCol-1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow + 1][posCol-1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow + 1][posCol - 1]))
                        _successors.add(_stateArr[posRow + 1][posCol - 1]); //left down
                }

            if (posRow > 0)
                if (_maze.getElement(posRow - 1, posCol - 1) == 0) {
                    if (_stateArr[posRow - 1][posCol-1].getCost() >= _stateArr[posRow][posCol].getCost() + 15) {
                        _stateArr[posRow - 1][posCol-1].setCameFrom(_stateArr[posRow][posCol]);
                        _stateArr[posRow - 1][posCol-1].setCost(15+_stateArr[posRow][posCol].getCost());
                    }
                    if (!_successors.contains(_stateArr[posRow - 1][posCol - 1]))
                        _successors.add(_stateArr[posRow - 1][posCol - 1]); //left up
                }
        }
    }


}