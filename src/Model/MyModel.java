package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {

    private Maze maze;
    private Solution solution;
    private int rowChar;
    private int colChar;
    private int rowfinish;
    private  int colfinish;
    private Server solveSearchProblemServer;
    private Server mazeGeneratingServer;

    public MyModel()
    {
        maze = null;
        rowChar = 0;
        colChar = 0;
        rowfinish = 0;
        colfinish = 0;
        mazeGeneratingServer = new Server(5420, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5421, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();

    }

    public void setRowChar(int r) { rowChar = r; }
    public void setColChar(int c) {colChar = c;}

    @Override
    public void generate(int row, int col) {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5420, new IClientStrategy() {

                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {

                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[row * col + 12];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            client.communicateWithServer();
            this.setParameters();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void updateCharacterLocation(int direction) {

        switch(direction)
        {
            case 1: //Up
                if (rowChar > 0 && maze.getElement(rowChar-1, colChar) != 1)
                    rowChar--;
                break;
            case 2: //Down
                if (rowChar < maze.getRows()-1 && maze.getElement(rowChar + 1, colChar) != 1)
                    rowChar++;
                break;
            case 3: //Left
                if (colChar > 0 && maze.getElement(rowChar, colChar-1) != 1)
                    colChar--;
                break;
            case 4: //Right
                if (colChar < maze.getColumns()-1 && maze.getElement(rowChar, colChar+1) != 1)
                    colChar++;
                break;
            case 5: //LEFT UP
                if (colChar > 0 && rowChar > 0 && maze.getElement(rowChar-1, colChar-1) != 1)
                {
                    colChar--;
                    rowChar--;
                }
                break;
            case 6: //RIGHT UP
                if (colChar < maze.getColumns()-1 && rowChar > 0 && maze.getElement(rowChar-1, colChar+1) != 1)
                {
                    colChar++;
                    rowChar--;
                }
                break;
            case 7: //DOWN LEFT
                if (colChar> 0 && rowChar < maze.getRows()-1 && maze.getElement(rowChar+1, colChar-1) != 1)
                {
                    colChar--;
                    rowChar++;
                }
                break;
            case  8: //DOWN RIGHT
                if (colChar < maze.getColumns()-1 && rowChar < maze.getRows()-1 && maze.getElement(rowChar+1, colChar+1) != 1)
                {
                    colChar++;
                    rowChar++;
                }
                break;
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public int getRowChar() {
        return rowChar;
    }

    @Override
    public int getColChar() {
        return colChar;
    }


    public int getRowfinish() {
        return rowfinish;
    }

    public int getColfinish() {
        return colfinish;
    }


    @Override
    public void assignObserver(Observer o) {
        addObserver(o);
    }



    @Override
    public void solveMaze(Maze maze) {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5421, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution = (Solution) fromServer.readObject();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    public void SaveFile(File file){

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            byte [] arr = maze.toByteArray();
            objectOutputStream.writeObject(arr);
        }catch (IOException e){
            System.out.println("There is problem with save file");
        }
    }
    public void LoadFile(File file){
        byte [] arr = null;
        try{
            FileInputStream inputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            arr = (byte[])(objectInputStream.readObject());
        }catch (IOException | ClassNotFoundException e){
            System.out.println("There is problem with load file");
        }

        maze = new Maze(arr);
        this.setParameters();

    }
    private void setParameters(){
        rowChar = maze.getStartPosition().getRowIndex();
        colChar = maze.getStartPosition().getColumnIndex();

        rowfinish = maze.getGoalPosition().getRowIndex();
        colfinish = maze.getGoalPosition().getColumnIndex();

        setChanged();
        notifyObservers();
    }

    public void CloseApp() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }
}