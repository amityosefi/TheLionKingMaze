import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import View.aboutcontroller;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze Game");
        Scene scene = new Scene(root, 900, 750);
        primaryStage.setScene(scene);
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);

        MyViewController myViewController = fxmlLoader.getController();
        myViewController.setViewModel(viewModel);

        viewModel.addObserver(myViewController);
        myViewController.setResizeEvent(scene);
        primaryStage.setOnCloseRequest(e->viewModel.CloseApp());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
