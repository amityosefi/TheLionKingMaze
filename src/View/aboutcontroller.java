package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class aboutcontroller {

    private MyViewModel viewModel;
    @FXML


    private void newGame(ActionEvent e){

        viewModel.generateMaze(10,10);
        ((Node) e.getSource()).getScene().getWindow().hide();//close current stage
    }

    public void setViewModel1(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
