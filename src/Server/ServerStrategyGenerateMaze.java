package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.util.Properties;
import Server.Configurations;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    public void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);

        int[] Array = (int[])(in.readObject()); //read the array which gets as an input
        AMazeGenerator mazeGenerator;
        String generate = Configurations.loadProperty("Generate");
        if(generate != null) {

            if (generate.equals("MyMazeGenerator"))
                mazeGenerator = new MyMazeGenerator();
            else if (generate.equals("SimpleMazeGenerator"))
                mazeGenerator = new SimpleMazeGenerator();
            else //EmptyMazeGenerator
                mazeGenerator = new EmptyMazeGenerator();
        }
        else
            mazeGenerator = new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(Array[0], Array[1]); //Array[0] = rows, Array[1] = columns

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //initialize the byte array as an ByteArrayOutputStream
        outputStream = new MyCompressorOutputStream(byteArrayOutputStream); //initialize the compressor
        outputStream.write(maze.toByteArray()); //compress
        out.writeObject(byteArrayOutputStream.toByteArray()); //send to client

        outputStream.flush();
        outputStream.close();
    }
}