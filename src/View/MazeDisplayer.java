<<<<<<< HEAD
package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution solution;

    private int row_player;
    private int col_player;

    private int row_finish;
    private int col_finish;

    private boolean isSol = false;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameFinish = new SimpleStringProperty();
    StringProperty imageFileNameSol = new SimpleStringProperty();


    public void setIsSol(boolean b)
    {
        isSol = b;
    }
    public boolean getIsSol() { return isSol;}

    public int getRow_finish() {
        return row_finish;
    }

    public void set_Solution(Solution s) {
        solution = s;
    }

    public Solution get_Solution()
    {
        return solution;
    }

    public int getCol_finish() {
        return col_finish;
    }

    public void setImageFileNameSol(String imageFileNameSol) {
        this.imageFileNameSol.set(imageFileNameSol);
    }

    public String getImageFileNameSol() {
        return imageFileNameSol.get();
    }

    public void setRow_finish(int row_finish) {
        this.row_finish = row_finish;
    }

    public void setCol_finish(int col_finish) {
        this.col_finish = col_finish;
    }

    public void setRow_player(int row_player) {
        this.row_player = row_player;
    }

    public void setCol_player(int col_player) {
        this.col_player = col_player;
    }

    public String getImageFileNameFinish() {
        return imageFileNameFinish.get();
    }

    public void setImageFileNameFinish(String imageFileNameFinish) { this.imageFileNameFinish.set(imageFileNameFinish); }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }


    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void zoom(ScrollEvent scrollEvent)
    {
        if (scrollEvent.isControlDown())
        {
            if (scrollEvent.getDeltaY() < 0)
            {
                if (getHeight() < 70 || getWidth() < 70)
                    return;

                setHeight(getHeight() / 1.05);
                setWidth(getWidth() / 1.05);
            }
            if (scrollEvent.getDeltaY() > 0)
            {
                if (getHeight() > 7000 || getWidth() > 7000)
                    return;

                setHeight(getHeight() * 1.05);
                setWidth(getWidth() * 1.05);
            }
            draw();
        }
    }

    public void moveScreen(MouseEvent mouseEvent)
    {
        if (mouseEvent.isDragDetect())
        {
            setLayoutY(mouseEvent.getY());
            setLayoutX(mouseEvent.getX());
            draw();
        }

    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
        draw();
    }

    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        draw();
    }

    public void draw()
    {
        if(maze == null)
            return;

        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / (maze.getRows());
        double cellWidth = canvasWidth / (maze.getColumns());

        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
      //  graphicsContext.setFill(Color.YELLOW);

        drawWall(graphicsContext, cellHeight, cellWidth);

        if (isSol == true)
            drawSolution(graphicsContext, cellHeight, cellWidth);

        drawPlayer(graphicsContext, cellHeight, cellWidth);
        drawGoalImage(graphicsContext, cellHeight, cellWidth);
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        Image solImage = null;
        graphicsContext.setFill(Color.DARKGREY);

        try {
            solImage = new Image(new FileInputStream(getImageFileNameSol()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image solution....");
        }
        ArrayList<AState> solPath = solution.getSolutionPath();

        for (int i = 0; i < solPath.size(); i++) {

            AState aState = solPath.get(i);
            MazeState mazeState = (MazeState) aState;
            int sol_row = mazeState.getRow();
            int sol_col = mazeState.getCol();

            double h_solution = sol_row * cellHeight;
            double w_solution = sol_col * cellWidth;

            if (solImage == null){
                graphicsContext.fillRect(w_solution, h_solution, cellWidth, cellHeight);
            }else{
                graphicsContext.drawImage(solImage,w_solution,h_solution,cellWidth,cellHeight);
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double h_player = getRow_player() * cellHeight;
        double w_player = getCol_player() * cellWidth;

        Image playerImage = null;
        graphicsContext.setFill(Color.BLUE);

        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image....");
        }

        if (playerImage == null){
            graphicsContext.fillRect(w_player, h_player, cellWidth, cellHeight);
        }else{
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
        }
    }

    private void drawGoalImage(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double h_goal = getRow_finish() * cellHeight;
        double w_goal = getCol_finish() * cellWidth;

        Image goalImage = null;
        graphicsContext.setFill(Color.PAPAYAWHIP);

        try {
            goalImage = new Image(new FileInputStream(getImageFileNameFinish()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no goal image....");
        }

        if (goalImage == null){
            graphicsContext.fillRect(w_goal, h_goal, cellWidth, cellHeight);
        }else{
            graphicsContext.drawImage(goalImage, w_goal,h_goal,cellWidth,cellHeight);
        }
    }

    private void drawWall(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double w_wall,h_wall;
        Image wallImage = null;
        graphicsContext.setFill(Color.YELLOW);

        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image....");
        }

        int row = maze.getRows();
        int col = maze.getColumns();

        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                if(maze.getElement(i, j) == 1) // Wall
                {
                    h_wall = i * cellHeight;
                    w_wall = j * cellWidth;
                    if (wallImage == null){
                        graphicsContext.fillRect(w_wall, h_wall, cellWidth, cellHeight);
                    }else{
                        graphicsContext.drawImage(wallImage,w_wall,h_wall,cellWidth,cellHeight);
                    }
                }
            }
        }
    }
=======
package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Solution solution;

    private int row_player;
    private int col_player;

    private int row_finish;
    private int col_finish;

    private boolean isSol = false;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameFinish = new SimpleStringProperty();
    StringProperty imageFileNameSol = new SimpleStringProperty();


    public void setIsSol(boolean b)
    {
        isSol = b;
    }
    public boolean getIsSol() { return isSol;}

    public int getRow_finish() {
        return row_finish;
    }

    public void set_Solution(Solution s) {
        solution = s;
    }

    public Solution get_Solution()
    {
        return solution;
    }

    public int getCol_finish() {
        return col_finish;
    }

    public void setImageFileNameSol(String imageFileNameSol) {
        this.imageFileNameSol.set(imageFileNameSol);
    }

    public String getImageFileNameSol() {
        return imageFileNameSol.get();
    }

    public void setRow_finish(int row_finish) {
        this.row_finish = row_finish;
    }

    public void setCol_finish(int col_finish) {
        this.col_finish = col_finish;
    }

    public void setRow_player(int row_player) {
        this.row_player = row_player;
    }

    public void setCol_player(int col_player) {
        this.col_player = col_player;
    }

    public String getImageFileNameFinish() {
        return imageFileNameFinish.get();
    }

    public void setImageFileNameFinish(String imageFileNameFinish) { this.imageFileNameFinish.set(imageFileNameFinish); }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }


    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void zoom(ScrollEvent scrollEvent)
    {
        if (scrollEvent.isControlDown())
        {
            if (scrollEvent.getDeltaY() < 0)
            {
                if (getHeight() < 70 || getWidth() < 70)
                    return;

                setHeight(getHeight() / 1.05);
                setWidth(getWidth() / 1.05);
            }
            if (scrollEvent.getDeltaY() > 0)
            {
                if (getHeight() > 7000 || getWidth() > 7000)
                    return;

                setHeight(getHeight() * 1.05);
                setWidth(getWidth() * 1.05);
            }
            draw();
        }
    }

    public void moveScreen(MouseEvent mouseEvent)
    {
        if (mouseEvent.isDragDetect())
        {
            setLayoutY(mouseEvent.getY());
            setLayoutX(mouseEvent.getX());
            draw();
        }

    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
        draw();
    }

    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        draw();
    }

    public void draw()
    {
        if(maze == null)
            return;

        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / (maze.getRows());
        double cellWidth = canvasWidth / (maze.getColumns());

        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
      //  graphicsContext.setFill(Color.YELLOW);

        drawWall(graphicsContext, cellHeight, cellWidth);

        if (isSol == true)
            drawSolution(graphicsContext, cellHeight, cellWidth);

        drawPlayer(graphicsContext, cellHeight, cellWidth);
        drawGoalImage(graphicsContext, cellHeight, cellWidth);
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        Image solImage = null;
        graphicsContext.setFill(Color.DARKGREY);

        try {
            solImage = new Image(new FileInputStream(getImageFileNameSol()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image solution....");
        }
        ArrayList<AState> solPath = solution.getSolutionPath();

        for (int i = 0; i < solPath.size(); i++) {

            AState aState = solPath.get(i);
            MazeState mazeState = (MazeState) aState;
            int sol_row = mazeState.getRow();
            int sol_col = mazeState.getCol();

            double h_solution = sol_row * cellHeight;
            double w_solution = sol_col * cellWidth;

            if (solImage == null){
                graphicsContext.fillRect(w_solution, h_solution, cellWidth, cellHeight);
            }else{
                graphicsContext.drawImage(solImage,w_solution,h_solution,cellWidth,cellHeight);
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double h_player = getRow_player() * cellHeight;
        double w_player = getCol_player() * cellWidth;

        Image playerImage = null;
        graphicsContext.setFill(Color.BLUE);

        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image....");
        }

        if (playerImage == null){
            graphicsContext.fillRect(w_player, h_player, cellWidth, cellHeight);
        }else{
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
        }
    }

    private void drawGoalImage(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double h_goal = getRow_finish() * cellHeight;
        double w_goal = getCol_finish() * cellWidth;

        Image goalImage = null;
        graphicsContext.setFill(Color.PAPAYAWHIP);

        try {
            goalImage = new Image(new FileInputStream(getImageFileNameFinish()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no goal image....");
        }

        if (goalImage == null){
            graphicsContext.fillRect(w_goal, h_goal, cellWidth, cellHeight);
        }else{
            graphicsContext.drawImage(goalImage, w_goal,h_goal,cellWidth,cellHeight);
        }
    }

    private void drawWall(GraphicsContext graphicsContext, double cellHeight, double cellWidth)
    {
        double w_wall,h_wall;
        Image wallImage = null;
        graphicsContext.setFill(Color.YELLOW);

        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image....");
        }

        int row = maze.getRows();
        int col = maze.getColumns();

        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                if(maze.getElement(i, j) == 1) // Wall
                {
                    h_wall = i * cellHeight;
                    w_wall = j * cellWidth;
                    if (wallImage == null){
                        graphicsContext.fillRect(w_wall, h_wall, cellWidth, cellHeight);
                    }else{
                        graphicsContext.drawImage(wallImage,w_wall,h_wall,cellWidth,cellHeight);
                    }
                }
            }
        }
    }
>>>>>>> 61bbfae54443a08292d12e13556d9d38a6627a8d
}