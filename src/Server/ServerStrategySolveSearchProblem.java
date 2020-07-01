package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.util.Arrays;
import java.util.HashMap;

import java.io.*;
import java.util.Properties;

public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private HashMap<Integer, byte[]> dictionary; //key = the file name, value = the maze represents by its byte array
    private int fileName;

    public ServerStrategySolveSearchProblem() {
        this.dictionary = new HashMap<Integer, byte[]>();
        this.fileName = 0;
    }

    public void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);

        String tempDir = System.getProperty("java.io.tmpdir"); //save the files in this path

        //System.out.println(tempDir);
        Maze maze = (Maze) (in.readObject()); //read the maze got as an input
        int crntFile = -1;

        synchronized (this) {
            crntFile = findInDictionary(maze);
            if (crntFile == -1) //maze is not in the dictionary
                createNewSolution(tempDir, out, maze);
        }

        if (crntFile != -1) //maze is in the dictionary
            useExistSolution(tempDir, out, crntFile);

        outputStream.flush();
        outputStream.close();
    }

    private int findInDictionary(Maze maze)
    {
        for (int i = 0; i < dictionary.size(); i++) {
            if (Arrays.equals(dictionary.get(i), maze.toByteArray())) {
                //if the maze exist in the dictionary - save its name, by this way use its solution (of the same file name)
                return i;
            }
        }
        return -1;
    }

    private void useExistSolution(String tempDir, ObjectOutputStream out, int crntFile) throws IOException, ClassNotFoundException
    {
        //maze is in dictionary - use its solution
        String FileName = tempDir + "sol" + (Integer.toString(crntFile) + ".txt"); //save the file name
        FileInputStream fileInputStream = new FileInputStream(FileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Solution solution = (Solution)(objectInputStream.readObject()); //read the solution from the file
        out.writeObject(solution);
    }

    private void createNewSolution(String tempDir, ObjectOutputStream out, Maze maze) throws IOException
    {
        //maze is not in dictionary - create a new solution
        SearchableMaze searchableMaze = new SearchableMaze(maze);

        String search = Configurations.loadProperty("Search");

        Solution solution;
        if(search != null) {
            if (search.equals("DepthFirstSearch"))
                solution = new DepthFirstSearch().solve(searchableMaze);
            else if (search.equals("BreadthFirstSearch"))
                solution = new BreadthFirstSearch().solve(searchableMaze);
            else //Best
                solution = new BestFirstSearch().solve(searchableMaze);
        }
        else
            solution = new BreadthFirstSearch().solve(searchableMaze);

        dictionary.put(fileName, maze.toByteArray());

        String path = tempDir + "sol" + (Integer.toString(fileName) + ".txt");
        FileOutputStream createFileName = new FileOutputStream(path); //the name of the file is filename
        ObjectOutputStream File = new ObjectOutputStream(createFileName);

        File.writeObject(solution); //add the solution to the file
        out.writeObject(solution); //send to client

        fileName++;
    }
}
