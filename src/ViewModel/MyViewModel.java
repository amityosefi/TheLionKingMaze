package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;
    private int rowChar;
    private int colChar;
    private int rowfinish;
    private  int colfinish;

    public MyViewModel(IModel m)
    {
        model = m;
        rowChar = model.getRowChar();
        colChar = model.getColChar();
        rowfinish = model.getRowfinish();
        colfinish = model.getColfinish();
        model.assignObserver(this);
        maze = null;
    }

    public int getRowfinish() {
        return rowfinish;
    }
    public int getColfinish() {
        return colfinish;
    }

    public void setRowChar(int r) { model.setRowChar(r); }
    public void setColChar(int c) { model.setColChar(c); }

    public Maze getMaze() {
        return maze;
    }

    public int getRowChar() { return rowChar; }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(o instanceof IModel)
        {
            maze = model.getMaze();
            rowChar = model.getRowChar();
            colChar = model.getColChar();
            rowfinish = model.getRowfinish();
            colfinish = model.getColfinish();

            setChanged();
            notifyObservers();
        }
    }


    public void generateMaze(int row,int col) {
        model.generate(row,col);
        rowChar = model.getRowChar();
        colChar = model.getColChar();
        rowfinish = model.getRowfinish();
        colfinish = model.getColfinish();
    }

    public void moveCharacter(KeyCode key)
    {
        int direction = -1;

        switch (key){
            case UP: //UP
                direction = 1;
                break;
            case DOWN: //DOWN
                direction = 2;
                break;
            case LEFT: //LEFT
                direction = 3;
                break;
            case RIGHT: //RIGHT
                direction = 4;
                break;
            case NUMPAD7: //LEFT UP
                direction = 5;
                break;
            case NUMPAD9: //RIGHT UP
                direction = 6;
                break;
            case NUMPAD1: //LEFT DOWN
                direction = 7;
                break;
            case NUMPAD3: //RIGHT DOWN
                direction = 8;
                break;
            default:
                break;
        }

        model.updateCharacterLocation(direction);
    }

    public void solveMaze(Maze maze)
    {
        model.solveMaze(maze);

    }

    public Solution getSolution()
    {
        Solution solution = model.getSolution();
        return solution;
    }

    public void savemaze(File file){
        model.SaveFile(file);
    }
    public void loadmaze(File file){ model.LoadFile(file);}

    public void CloseApp() {
        model.CloseApp();
    }

}