package View;

        import javafx.scene.input.KeyEvent;

        import java.io.IOException;
        import java.util.Observable;

public interface IView {

    public void generateMaze() throws IOException, ClassNotFoundException;
    public void solveMaze() throws IOException;
    public void showAlert(String header, String message) throws IOException;
    public void keyPressed(KeyEvent keyEvent);
    public void update(Observable o, Object arg);
    public void drawMaze();
}
