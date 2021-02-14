package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer, Initializable {
    private MyViewModel viewModel;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public MazeSound mazeSound;
    @FXML
    private Button MuteButton;
    @FXML
    private Button SolutionButton;


    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();

    private Maze maze;
    private double mazeHeight;
    private double mazeWidth;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mazeHeight = mazeDisplayer.getHeight();
        mazeWidth = mazeDisplayer.getWidth();

    }


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }

    public void generateMaze() {

        try
        {
            int rows = Integer.parseInt(textField_mazeRows.getText());
            int cols = Integer.valueOf(textField_mazeColumns.getText());
            if (rows > 1000 || cols > 1000)
            {
                showAlert("Wrong Input", "Please enter a number that is no more than 1000");
                return;
            }
            viewModel.generateMaze(rows,cols);
            mazeDisplayer.setIsSol(false);
            mazeSound.playStartMusic();
            MuteButton.setText("Mute");
            SolutionButton.setText("Solve Maze");
            drawMaze();

        } catch (NumberFormatException ex)
        {
            showAlert("Wrong Input","Please enter numbers");
        }

    }
    private void generaHarderMaze(int rows, int cols) {

        if (rows > 996 || cols > 996)
            startOver();

        try
        {
            viewModel.generateMaze(rows,cols);
            mazeDisplayer.setIsSol(false);
            mazeSound.playStartMusic();
            MuteButton.setText("Mute");
            SolutionButton.setText("Solve Maze");
            drawMaze();

        } catch (NumberFormatException ex)
        {
            showAlert("Wrong Input","Please enter numbers");
        }

    }

    public void solveMaze() {
        if (this.maze != null)
        {
            if (SolutionButton.getText().equals("Solve Maze"))
            {
                SolutionButton.setText("Hide Solution");
                viewModel.solveMaze(this.maze);
                Solution sol = viewModel.getSolution();
                mazeDisplayer.setIsSol(true);
                mazeDisplayer.set_Solution(sol);
            }
            else if (SolutionButton.getText().equals("Hide Solution"))
            {
                SolutionButton.setText("Solve Maze");
                mazeDisplayer.setIsSol(false);
            }

            drawMaze();

        }
        else
        {
            showAlert("WARNING!","There is no maze to solve");
        }

    }

    @FXML
    private void MuteSound(ActionEvent e) {

        if (this.maze != null)
        {
            if (MuteButton.getText().equals("Unmute")) {
                mazeSound.unmuteMusic();
                MuteButton.setText("Mute");
            }
            else if (MuteButton.getText().equals("Mute"))
            {
                if (mazeSound.musisExist())
                {
                    mazeSound.muteMusic();
                    MuteButton.setText("Unmute");
                }
                else
                    showAlert("WARNING!","There is no Sound");

            }
            drawMaze();
        }
        else
        {
            showAlert("WARNING!","There is no Maze");
        }
    }

    public void showAlert(String header, String message){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setHeaderText(header);

        if (message.equals("You reached Nala! Thank you!" + '\n' + "Now try a harder maze!"))
        {

            Optional<ButtonType> option = alert.showAndWait();

            if (!option.toString().equals("Optional.empty"))
            {
                if(ButtonType.OK.equals(option.get()))
                    generaHarderMaze(maze.getRows()+5,maze.getColumns()+5);
            }
            else
            {
                startOver();
            }

            return;
        }

        alert.show();

    }

    private void startOver()
    {
        viewModel.setRowChar(maze.getStartPosition().getRowIndex());
        viewModel.setColChar(maze.getStartPosition().getColumnIndex());
        mazeSound.playStartMusic();
        mazeDisplayer.setIsSol(false);
        MuteButton.setText("Mute");
        SolutionButton.setText("Solve Maze");
        drawMaze();
    }

    public void keyPressed(KeyEvent keyEvent) {

        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();

    }


    public void ZoomScreen(ScrollEvent scrollEvent)
    {
        mazeDisplayer.zoom(scrollEvent);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null) //generate new maze
            {
                this.maze = viewModel.getMaze();
                updateParameters();
                return;
            }
            Maze maze = viewModel.getMaze();
            if (maze == this.maze) //the same maze
            {
                int rowFromViewModel = viewModel.getRowChar();
                int colFromViewModel = viewModel.getColChar();

                set_update_player_position_row(rowFromViewModel + "");
                set_update_player_position_col(colFromViewModel + "");
                this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
            }
            else //different maze - update changes
            {
                this.maze = maze;
                updateParameters();
            }
            if (mazeDisplayer.getRow_player() == maze.getGoalPosition().getRowIndex() && mazeDisplayer.getCol_player() == maze.getGoalPosition().getColumnIndex())
            {
                mazeSound.playEndMusic();
                showAlert("Congratulations!","You reached Nala! Thank you!" + '\n' + "Now try a harder maze!");
            }
        }
    }

    private void updateParameters()
    {
        mazeDisplayer.setRow_player(viewModel.getRowChar());
        mazeDisplayer.setCol_player(viewModel.getColChar());
        mazeDisplayer.setRow_finish(viewModel.getRowfinish());
        mazeDisplayer.setCol_finish(viewModel.getColfinish());
        mazeDisplayer.setHeight(mazeHeight);
        mazeDisplayer.setWidth(mazeWidth);
        drawMaze();
    }


    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }


    public void saveFile(ActionEvent actionEvent) throws InterruptedException {
        if (maze == null) {
            StringBuffer stringBuffer = new StringBuffer("");
            Alert a = new Alert(Alert.AlertType.ERROR);
            stringBuffer.append("You can't Save Non-Existing Maze");
            a.setContentText(String.valueOf(stringBuffer));
            a.show();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze Files", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null)
        {
            fileChooser.setTitle("Save as");
            viewModel.savemaze(file);
        }
    }

    public void LoadFile(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load maze");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze Files", "*.maze");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                String subS = file.getPath().substring(file.getPath().length() - 5);
                if (subS.equals(".maze"))
                {
                    mazeDisplayer.setIsSol(false);
                    mazeSound.playStartMusic();
                    SolutionButton.setText("Solve Maze");
                    MuteButton.setText("Mute");
                    viewModel.loadmaze(file);
                }

                else {
                    StringBuffer stringBuffer = new StringBuffer("");
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    stringBuffer.append("You can't Load Non-maze File.");
                    a.setContentText(String.valueOf(stringBuffer));
                    a.show();
                    return;
                }

            }
        } catch(Exception e){
            StringBuffer stringBuffer = new StringBuffer("");
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Loaded did not executed.");
            stringBuffer.append("Maze have not Loaded!");
            a.setContentText(String.valueOf(stringBuffer));
            a.show();
            return;
        }
    }
    public void Properties(ActionEvent actionEvent) throws IOException {
        String generate = Configurations.loadProperty("Generate");
        String solveMaze = Configurations.loadProperty("Search");
        String numberThreds = Configurations.loadProperty("ThreadPool");
        showAlert("Properties:","Properties:\n" +
                "The algorithm generate by: " + generate + "\n" +
                "The algorithm solve by algorithm: " + solveMaze + "\n" +
                "The number of threads are: "+numberThreds);
    }

    public void Help(ActionEvent actionEvent) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HelpStage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }catch (Exception e){
            System.out.println("There is a problem with the Help");
        }

    }

    public void About(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutStage.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();

            aboutcontroller aboutController = fxmlLoader.getController();
            aboutController.setViewModel1(viewModel);

        } catch (Exception e){
            System.out.println("There is problem with the about");
        }
    }

    public void ExitStage(ActionEvent actionEvent) {
        viewModel.CloseApp();
        Platform.exit();
    }

    public void mouseMove(MouseEvent mouseEvent){
        if (maze != null) {
            double x_Pos = mouseEvent.getX() / (mazeDisplayer.getWidth() / maze.getColumns());
            double y_Pos = mouseEvent.getY() / (mazeDisplayer.getHeight() / maze.getRows());

            if(y_Pos < viewModel.getRowChar()){
                viewModel.moveCharacter(KeyCode.UP); //UP
            }
            else if(y_Pos > viewModel.getRowChar()+1){
                viewModel.moveCharacter(KeyCode.DOWN); //DOWN
            }
            else if(x_Pos > viewModel.getColChar()+1){
                viewModel.moveCharacter(KeyCode.RIGHT); //RIGHT
            }
            else if(x_Pos < viewModel.getColChar()){
                viewModel.moveCharacter(KeyCode.LEFT); //LEFT
            }
        }
    }

    public void setResizeEvent(Scene scene) {

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                mazeDisplayer.setWidth((Double) newSceneWidth - 275);
                mazeDisplayer.draw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                mazeDisplayer.setHeight((Double) newSceneHeight - 100);
                mazeDisplayer.draw();
            }
        });
    }

    public void dontEscape(KeyEvent keyEvent) {
        keyEvent.consume();
    }
}